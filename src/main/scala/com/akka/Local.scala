package com.akka

import akka.actor.{Actor, Props, ActorSystem}
import com.typesafe.config.ConfigFactory

/**
 * Created by yxl on 15/7/9.
 */

object Local extends App {
  val config = ConfigFactory.load("local.conf")
  val system = ActorSystem("actorSystem",config)
  val localActor = system.actorOf(Props[LocalActor], name = "LocalActor")  // the local actor
  localActor ! Start                                                       // start the action

}

class LocalActor extends Actor {

  // create the remote actor
  val remote = context.actorSelection("akka.tcp://actorSystem@127.0.0.1:5150/user/RemoteActor")
  var counter = 0

  def receive = {
    case Start =>
      remote ! Message("Hello from the LocalActor")
    case Message(msg) =>
      println(s"LocalActor received message: '$msg'")
      if (counter < 5) {
        sender ! Message("Hello back to you")
        counter += 1
      }
    case _ =>
      println("LocalActor got something unexpected.")
  }

}


