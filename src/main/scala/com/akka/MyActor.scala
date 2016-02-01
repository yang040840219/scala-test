package com.akka

import java.util.concurrent.TimeUnit
import akka.pattern.gracefulStop
import akka.actor._
import akka.event.Logging
import akka.util.Timeout

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._


/**
 * Created by yxl on 16/1/20.
 */


class MyActor extends Actor {

  val log = Logging(context.system,this)


  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
     log.info(" preStart myActor ")
     super.preStart()
  }


  override def receive: Receive = {

    case "start" => {
      log.info("start")
      sender ! "ok"
    }

    case "stop" => {
       self ! PoisonPill
    }

    case  _  => log.info("unkown")  ;  // 必须要有一个缺省的case 否则会有一个 akka.actor.UnhandledMessage(message, sender, recipient) 被发布到 Actor系统（ActorSystem）

  }

  @throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    log.info(" postStop myActor ")
    super.postStop()
  }
}

class WatchActor extends Actor {

  val log = Logging(context.system,this)

  implicit val resolveTimeout = Timeout(5 seconds)
  // 启动就先注册一个(手动)
  val path = "akka://MySystem/user/firstActor/myactor"
  //val myActorRef = context.actorFor(path)
  val actorRef = Await.result(context.actorSelection(path).resolveOne(),5 seconds)
  context.watch(actorRef)

  override def receive: Receive = {

    // 通过发消息的方式注册监听
    case "register" => {
       log.info("register:" + sender)
       context.watch(sender)
    }

    //
    case Terminated(child) => {
       log.info("Terminated :" + child)
    }
  }


}

class FirstActor extends Actor {

  val log = Logging(context.system,this)

  var myActor : ActorRef = null

  override def receive: Receive = {
    case "start" =>  {
        log.info("start  myActor ")
        // 使用上下文时当前的actor将成为所创建的子actor的监管者
        // 启动后地址： akka://MySystem/user/firstActor/myactor
        myActor = context.actorOf(Props[MyActor], name = "myactor")
        myActor ! "start"
    }

    case "stop" => {
      log.info("stop myActor ")
      myActor ! "stop"
    }

    case "ok" => {
       context.watch(myActor)
       log.info("actor ok")
    }

    case "watch" =>{
       log.info("start watch actor")
       val watchActor = context.actorOf(Props[WatchActor],name="watchActor")
    }

    case Terminated(child) => { // watch 的 Actor 停止之后发出的消息
      log.info("Terminated :" + child)
      context.unwatch(child)
    }

    case _ =>  log.info("unknow")
  }
}

object MyActor {


   def main(args:Array[String]): Unit ={

     val system = ActorSystem("MySystem")

     // 由 ActorSystem 创建 顶级Actor , 由系统内部 Actor 管理 /user 后续创建的Actor 都是其子Actor
     // 通过 FirstActor 启动
     val firstActor = system.actorOf(Props[FirstActor], name="firstActor")

     firstActor ! "start"

     TimeUnit.SECONDS.sleep(5)

     firstActor ! "watch"

     TimeUnit.SECONDS.sleep(5)

     firstActor ! "stop"


     TimeUnit.SECONDS.sleep(5)
     // 停止 Actor
     try {
       val stopped: Future[Boolean] = gracefulStop(firstActor,5.seconds)
       Await.result(stopped,5.seconds)
       // actor被成功终止
     } catch {
       case e: Exception => println(e) // actor没有在5秒内终止
     }

    system.shutdown()

   }

}
