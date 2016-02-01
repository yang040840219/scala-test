package com.akka

import akka.actor._
import akka.actor.SupervisorStrategy._
import com.akka.Counter.UseStorage
import com.akka.CounterService._
import com.akka.Storage.{Store, Entry, Get, StorageException}
import com.akka.Worker.{Progress, Do}
import scala.concurrent.duration._
import akka.util.Timeout
import akka.event.LoggingReceive
import akka.pattern.{ ask, pipe }
import com.typesafe.config.ConfigFactory

object Worker {
    case object Start
    case object Do
    case class Progress(percent:Double)
}


object CounterService {
  case class Increment(n:Int)
  case object GetCurrentCount
  case class CurrentCount(key:String,count:Long)
  class ServiceUnavailable(msg:String) extends RuntimeException(msg)

  private case object Reconnect
}

object Storage{
  case class Store(entry:Entry)
  case class Get(key:String)
  case class Entry(key:String,value:Long)
  class StorageException(msg:String) extends RuntimeException(msg)

}

object Counter{
   case class UseStorage(storage:Option[ActorRef])
}


/**
 * 模拟存储
 */
object DummyDB {

  private var db = Map[String,Long]()

  def save(key:String,value:Long): Unit = synchronized {
     if(value >= 11 && value <=14){
        throw new StorageException("Simulated store failure " + value)
     }
     db +=(key -> value)
  }

  def load(key:String):Option[Long] = synchronized {
      db.get(key)
  }

}

class Storage extends Actor with ActorLogging{

   val db = DummyDB

   override def receive = LoggingReceive{
     case Store(Entry(key,count)) => db.save(key,count)
     case Get(key) => sender ! Entry(key,db.load(key).getOrElse(0L))
   }

}


class Counter(key:String,initialValue:Long) extends Actor with ActorLogging{

   var count = initialValue

   var storage: Option[ActorRef] = None

   override def receive = LoggingReceive{

     case UseStorage(s) => {
        storage = s
        storeCount()
     }

     case Increment(n) => {
        count += n
        storeCount()
     }

     case GetCurrentCount => {
        sender ! CurrentCount(key,count)
     }

   }


   def storeCount(): Unit ={
        storage foreach  { _ ! Store(Entry(key,count))}
   }

}

class CounterService extends Actor with ActorLogging{

  // 重启
  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(maxNrOfRetries = 3,
  withinTimeRange = 5 seconds ){
    case _: StorageException => Restart
  }

  val key = self.path.name

  var storage : Option[ActorRef] = None
  var counter : Option[ActorRef] = None
  var backlog  = IndexedSeq.empty[(ActorRef,Any)]

  val MaxBacklog = 10000

  import context.dispatcher

  override def preStart(): Unit ={
     initStorage()
  }

  def initStorage(): Unit ={
     log.info("初始化 initStorage ")
     storage = Some(context.watch(context.actorOf(Props[Storage],name="storage")))
     counter foreach { _ ! UseStorage(storage)}
     storage.get ! Get(key)
  }

  def receive = LoggingReceive {

    case Entry(k,v)  if k == key && counter == None => {
      val c = context.actorOf(Props(classOf[Counter],k,v))
      counter = Some(c)
      c ! UseStorage(storage)
      for((replyTo,msg) <- backlog) {
         c.tell(msg,sender = replyTo)
      }
      backlog = IndexedSeq.empty
    }

    case msg @ Increment(n) => forwardOrPlaceInBacklog(msg)

    case msg @ GetCurrentCount => forwardOrPlaceInBacklog(msg)

    // 只能收到 watch 过的 storage 信息
    case Terminated(actorRef) if Some(actorRef) == storage => {
       storage = None
       counter foreach { _ ! UseStorage(None)}
       context.system.scheduler.scheduleOnce(10 seconds , self, Reconnect)
    }

    case Reconnect => {
        initStorage()
    }

  }

  def forwardOrPlaceInBacklog(msg:Any): Unit ={

    counter match {
      case Some(c) => c.forward(msg) // 转发
      case None => {
         if(backlog.size >= MaxBacklog){
            throw new ServiceUnavailable("CounterService not available, lack of initial value")
         }
         backlog :+= (sender() -> msg)
      }
    }
  }
}


class Worker extends Actor with ActorLogging {

  implicit val askTimeout = Timeout(5)


  // 停止 child
  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(){
    case _: ServiceUnavailable => Stop
  }

  var progressListener: Option[ActorRef] = None

  val counterService = context.actorOf(Props[CounterService],name="counterService")

  val totalCount = 100

  import context.dispatcher

  override def receive: Receive = LoggingReceive{

    case Start if progressListener.isEmpty => {
        progressListener = Some(sender)
        context.system.scheduler.schedule(Duration.Zero,1 second,self,Do)
    }

    case Do => {
        counterService ! Increment(1)
        counterService ! Increment(1)
        counterService ! Increment(1)

        counterService ? GetCurrentCount map{
          case CurrentCount(_,count) => Progress(100 * count / totalCount)
        } pipeTo progressListener.get
    }

  }

}

class Listener extends Actor with ActorLogging {

    context.setReceiveTimeout(15 seconds)

    def receive = LoggingReceive {

      case Progress(percent) => {
          log.info("Current percent : {} %",percent)
          if(percent >= 100){
              log.info("That's all, shutting down")
              context.system.shutdown()
          }
      }

      case ReceiveTimeout => {
          log.error("Shutting down due to unavailable service")
          context.system.shutdown()
      }

    }


}


object FaultTolerance {

  def main(args:Array[String]): Unit ={

    val config = ConfigFactory.parseString(
      """
         akka.loglevel = "DEBUG"
         akka.actor.debug {
             receive = on
             lifecycle = on
         }
      """.stripMargin)

    val system = ActorSystem("FaultTolerance",config)

    val worker = system.actorOf(Props[Worker], name="worker")

    val listener = system.actorOf(Props[Listener],name="listener")

    worker.tell(Start,sender = listener)

  }


}
