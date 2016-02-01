package com.akka

import akka.actor.{ExtendedActorSystem, ActorSystem}
import com.typesafe.config.ConfigFactory

/**
 * Created by yxl on 15/11/6.
 */


object MyActorSystem {

     def createActorSystem():(ActorSystem,Int) ={
       val config = ConfigFactory.load("remote.conf")
       val actorSystem = ActorSystem("myAkkaSystem",config)
       val provider = actorSystem.asInstanceOf[ExtendedActorSystem].provider
       val boundPort = provider.getDefaultAddress.port.get
       (actorSystem, boundPort)
     }


    def main(args:Array[String]): Unit ={

      val (actorSystem,port) = createActorSystem()

      println(actorSystem)
      println(port)

      actorSystem.shutdown()

    }


}
