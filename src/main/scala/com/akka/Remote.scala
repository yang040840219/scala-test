package com.akka

import akka.actor.{Actor, Props, ActorSystem}
import com.typesafe.config.ConfigFactory

/**
 * Created by yxl on 15/7/9.
 */

object Remote extends App  {
  val config = ConfigFactory.load("remote.conf")
  val system = ActorSystem("actorSystem",config) // 相当于 Actor 的 容器
  val remoteActor = system.actorOf(Props[RemoteActor], name = "RemoteActor")
  //remoteActor ! Message("The RemoteActor is alive")

  println(remoteActor.path)
}

class RemoteActor extends Actor {
  def receive = {
    case Message(msg) =>
      println(s"RemoteActor received message '$msg'")
      sender ! Message("Hello from the RemoteActor")
    case _ =>
      println("RemoteActor got something unexpected.")

  }
}

