package com.akka

import java.util.concurrent.ConcurrentHashMap


import akka.actor.{ActorLogging, Actor}


/**
 * Created by yxl on 15/10/19.
 */
class ReduceActor extends Actor with ActorLogging {

  val wordValue = new ConcurrentHashMap[String,Int]()


  override def preStart(): Unit = super.preStart()

  override def receive: Receive = {

    case words:List[Word] => {
        log.info(words.toString())
        words.map(x => {
            val key = x.key
            val count = x.count
            if(wordValue.containsKey(key)){
               val old = wordValue.get(key)
               wordValue.put(key, (old + count))
            }else{
               wordValue.put(key,count)
            }
        })
    }

    case result:Result =>{
        println(wordValue)
    }

  }

  override def postStop(): Unit = {
     log.info("停止 reduce actor")
  }
}
