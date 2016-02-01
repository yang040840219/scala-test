package com.kafka

import java.util.concurrent.TimeUnit

import kafka.producer.{KeyedMessage, ProducerConfig, Producer}
import java.util.{Date, Random, Properties}

/**
 * Created by yxl on 15/10/28.
 */

class KafkaProducer(topic: String) {

  def sendMessage(msg: String, part: String): Unit = {

    val data = part match {
      case null | "" => new KeyedMessage[String, String](topic, msg)
      case _ => new KeyedMessage[String, String](topic, part, msg);
    }

    KafkaProducer.producer.send(data)

  }

  def closeProducer(): Unit = {
    KafkaProducer.producer.close()
  }

}


object KafkaProducer {
  val props = new Properties()
  props.put("metadata.broker.list", "localhost:9092,localhost:9093,localhost:9094");
  props.put("serializer.class", "kafka.serializer.StringEncoder");
  props.put("partitioner.class", "com.kafka.SimplePartitioner");
  props.put("request.required.acks", "1");
  props.put("default.replication.factor", "2")
  props.put("num.partitions", "3")

  val config = new ProducerConfig(props);
  val producer = new Producer[String, String](config);
}

object TestKafkaProducer {

  def main(args: Array[String]): Unit = {

    val topic = "page_visits"
    val producer = new KafkaProducer(topic)

    println(producer)

    for (i <- 1 to 1000000) {
      val rnd = new Random();
      val runtime = new Date().getTime();
      val randomInt = rnd.nextInt(255);
      val ip = "192.168.2." + randomInt
      val msg = runtime + ",www.example.com," + ip;
      println(msg)
      producer.sendMessage(msg, randomInt.toString);
      TimeUnit.SECONDS.sleep(1)
    }

    producer.closeProducer()

  }

}


