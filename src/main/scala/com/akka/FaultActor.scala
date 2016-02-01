package com.akka

import akka.actor._
import akka.event.LoggingReceive

/**
 * Created by yxl on 16/1/28.
 */


class Supervisor extends Actor with ActorLogging {
  import akka.actor.OneForOneStrategy
  import akka.actor.SupervisorStrategy._
  import scala.concurrent.duration._

  // 定义对子Actor 出现异常情况的处理策略
  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
      case _: ArithmeticException      => Resume // 不重启直接恢复子Actor状态
      case _: NullPointerException     => Restart // 重启子Actor不能恢复状态
      case _: IllegalArgumentException => Stop   // 停止子Actor
      case _: Exception                => Escalate  // 父Actor 和 子Actor 重启
    }

  def receive = LoggingReceive {
    case p: Props => {
      val child = context.actorOf(p)
      context.watch(child)
      println(child.path)
    }

    case Terminated(actorRef) => {
         log.info("Terminated:" + actorRef)
    }
  }

  @throws[Exception](classOf[Exception])
  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
      log.info("supervisor:" + "preRestart")
  }

  @throws[Exception](classOf[Exception])
  override def postRestart(reason: Throwable): Unit = {
     log.info("supervisor:" + "postRestart")
  }

  @throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    log.info("supervisor:" + "postStop")
  }
}

class Child extends Actor with ActorLogging {
  var state = 0
  def receive =  LoggingReceive {
    case ex: Exception => throw ex
    case x: Int        => state = x
    case "get"         => log.info("child:" + state)
  }

  @throws[Exception](classOf[Exception])
  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
      log.info("child:" + "preRestart")
  }

  @throws[Exception](classOf[Exception])
  override def postRestart(reason: Throwable): Unit = {
     log.info("child:" + "postRestart")
  }

  @throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
     log.info("child:" + "postStop")
  }
}

object FaultActor {


  def main(args:Array[String]): Unit ={

    val system = ActorSystem("FaultActor")
    val supervisor = system.actorOf(Props[Supervisor], "supervisor")

    supervisor ! Props[Child]

    val child = system.actorFor("akka://FaultActor/user/supervisor/$a")

    child ! 42


//    child ! "get"
//
//    child ! new ArithmeticException
//
//    child ! "get"

    child ! "get"

    child ! new NullPointerException

    child ! "get"

//      child ! "get"
//
//      child ! new IllegalArgumentException
//
//      child ! "get"


//    child ! "get"
//
//    child ! new Exception("crash")
//
//    child ! "get"



  }

}
