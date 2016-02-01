package com.akka

import java.util.concurrent.TimeUnit

import akka.actor._

import akka.pattern.ask
import akka.util.Timeout

/**
 * Created by yxl on 15/10/19.
 */
class MasterActor extends Actor with ActorLogging {


  val reduceActor:ActorRef = context.actorOf(Props[ReduceActor],"reduceActor")
  val mapActor:ActorRef = context.actorOf(Props(new MapActor(reduceActor)),"mapActor")


  override def receive: Receive  = {

    case msg:String => {
         mapActor ! msg
         sender() ! "success"
    }
    case result:Result =>{
        mapActor ! result
    }

    case _ => {
      log.error("wrong msg")
    }
  }
}


object MasterActor {

    def main(args:Array[String]): Unit ={

      val system = ActorSystem("AkkaSystem")

      val masterActor = system.actorOf(Props[MasterActor], name = "masterActor")

      masterActor ! "Hello,World"

      masterActor ! "Hello"

      TimeUnit.SECONDS.sleep(5)

      masterActor ! Result("finish")

      TimeUnit.SECONDS.sleep(5)

      implicit val timeout = Timeout(5,TimeUnit.SECONDS)

      masterActor.ask("hello")

      system.shutdown()

    }

}
