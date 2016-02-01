package com.zookeeper

import java.util.concurrent.TimeUnit

import kafka.utils.ZKStringSerializer
import org.I0Itec.zkclient.ZkClient

import scala.util.Random

/**
 * Created by yxl on 15/10/30.
 */
object ZookeepChange {


  def main(args:Array[String]): Unit ={


    val zookeeper = "localhost:2181"

    val zkClient = new ZkClient(zookeeper, 30000, 30000, ZKStringSerializer)

    val basePath = "/test"


    val random = new Random()



    while(true){

      TimeUnit.SECONDS.sleep(10)

      zkClient.writeData(basePath,random.nextInt().toString)


    }


  }

}
