package com.kafka

import java.util.Properties
import java.util.concurrent.Executors

import kafka.consumer.{ConsumerConfig, KafkaStream, ConsumerConnector}
import kafka.utils.Logging

/**
 * Created by yxl on 15/10/28.
 */
class KafkaConsumer(zookeeper: String, groupId: String, topic: String) {

  import KafkaConsumer._

  val topicMap: Map[String,Int] = Map(topic -> 3) // topic -> numberOfThread/numberOfPartition

  val consumer = getConsumer(zookeeper, groupId, topic)


  val consumerStreamsMap  = consumer.createMessageStreams(topicMap)


  def run(): Unit = {
    val streamList = consumerStreamsMap.get(topic).get

    val executor = Executors.newFixedThreadPool(topicMap.getOrElse(topic,1));

    var threadNumber = 0;
    streamList.foreach(stream => { // MessageStream
          threadNumber = threadNumber + 1
          executor.submit(new HandleMsg(stream,threadNumber))
    })
  }

}

/**
 * 处理 message
 * @param stream
 * @param threadNumber
 */
private class HandleMsg(val stream: KafkaStream[Array[Byte], Array[Byte]], val threadNumber: Int) extends Logging with Runnable {
   override def run() {
    val it = stream.iterator()
    while (it.hasNext()) {
      val msg = new String(it.next().message());
      logger.info(System.currentTimeMillis() + ",Thread " + threadNumber + ": " + msg);
    }
  }
}


object KafkaConsumer{

  def createConsumerConfig(zookeeper: String, groupId: String): ConsumerConfig = {
    val props = new Properties()
    props.put("zookeeper.connect", zookeeper)
    props.put("group.id", groupId)
    props.put("zookeeper.session.timeout.ms", "500")
    props.put("auto.commit.intervals.ms", "1000")
    new ConsumerConfig(props)
  }

  def getConsumer(zookeeper: String, groupId: String, topic: String): ConsumerConnector = {
    kafka.consumer.Consumer.create(createConsumerConfig(zookeeper, groupId))
  }

  def closeConsumer(consumer:ConsumerConnector): Unit ={
      consumer.shutdown()
  }

}

object TestKafkaConsumer{

  def main(args:Array[String]): Unit ={
    val zookeeper = "localhost:2181"
    val groupId = "group_2"
    val topic = "page_visits"
    val kafkaConsumer = new KafkaConsumer(zookeeper,groupId,topic)

    kafkaConsumer.run()
  }


}

