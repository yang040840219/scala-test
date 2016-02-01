package com.actor


import java.util.concurrent.TimeUnit

import scala.actors._
import scala.actors.Actor._

/**
 * Created by yxl on 16/1/15.
 */
object Silly {

  def act(): Unit ={
     for(i <- 1 to 5){
        println("a")
     }
  }

  val printMessage:PartialFunction[Any,Unit] = {
    case msg => println("receive msg:" + msg)
    case _ => println("None")
  }

  val echoActor = actor ({
    while(true){
       receive (printMessage)
    }
  })

  val reactActor = actor {
     loop(react(printMessage))
  }

  val unblockActor = actor {

     def emoteLater(): Unit ={
        val mainActor = self
        actor { // 创建子的actor , 等待2s
           TimeUnit.SECONDS.sleep(2)
           mainActor ! "Emoting"
        }
     }

    var emoted = 0

    loop({
       react({
         case "Emote" =>{ // 父的 actor 不阻塞
           println(" I'm Emoting")

           while(emoted < 5){
             emoted += 1
              emoteLater()
           }
         }

         case msg => println("receive :" + msg)
       })
    })

  }

  def main(args:Array[String]): Unit ={

    //   SillyActor.act()

    echoActor ! "1"

    unblockActor ! "Emote"

   }

}
