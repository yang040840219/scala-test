package com.akka

import akka.actor.{ActorLogging, ActorRef, Actor}

/**
 * Created by yxl on 15/10/19.
 */
class MapActor(reduceActor:ActorRef) extends Actor with ActorLogging {


  override def receive: Receive = {

    case msg:String => {
        val msgArray = msg.split(",")
        val words = msgArray.map( x => {
             Word(x.trim,1)
        }).toList
        reduceActor ! words
    }

    case result:Result => {
         reduceActor ! result
    }

  }
}
