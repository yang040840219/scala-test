package com.kafka

import kafka.utils.{ZkUtils, ZKStringSerializer}
import org.I0Itec.zkclient.ZkClient

import scala.collection.{Seq, mutable}

/**
 * Created by yxl on 15/10/29.
 */
object KafkaPartition {

  /**
   * 获取所有 topic
   * @param zookeeper
   * @return
   */
  def listTopics(zookeeper:String):Seq[String] ={
    val zkClient = new ZkClient(zookeeper, 30000, 30000, ZKStringSerializer)
    val allTopics = ZkUtils.getAllTopics(zkClient).sorted
    allTopics
  }

  /**
   * 获取topic 所有 partitions
   * @param zookeeper
   * @param topic
   * @return
   */
  def listPartition(zookeeper:String,topic:String): mutable.Map[String, Seq[Int]] ={
    val zkClient = new ZkClient(zookeeper, 30000, 30000, ZKStringSerializer)
    val topicsAndPartition = ZkUtils.getPartitionsForTopics(zkClient,List(topic))
    topicsAndPartition
  }



  def main(args:Array[String]): Unit ={

    val zookeeper = "localhost:2181"
    val groupId = "group_2"
    val topic = "page_visits"

    listPartition(zookeeper,topic)


  }

}
