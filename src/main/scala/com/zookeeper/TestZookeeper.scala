package com.zookeeper

import kafka.utils.ZKStringSerializer
import org.I0Itec.zkclient.{IZkDataListener, ZkClient}


/**
 * Created by yxl on 15/10/30.
 */

object TestZookeeper {


  /**
   * org.I0Itec.zkclient 对zookeeper底层的API 进行封装 测试例子
   * @param args
   */

  def main(args:Array[String]): Unit ={

    val zookeeper = "localhost:2181"

    val zkClient = new ZkClient(zookeeper, 30000, 30000, ZKStringSerializer)

    val basePath = "/test"

   // zkClient.createPersistent("/test")

   // zkClient.writeData(basePath,"123")

    zkClient.watchForData(basePath)



    zkClient.subscribeDataChanges(basePath,new IZkDataListener {
      override def handleDataChange(p1: String, p2: scala.Any): Unit = {
           println(p1 + "   " + zkClient.readData(basePath))
      }

      override def handleDataDeleted(p1: String): Unit = {
          println("delete data")
      }
    })





  }

}
