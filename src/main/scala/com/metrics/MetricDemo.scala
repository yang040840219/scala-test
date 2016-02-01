package com.metrics

import java.util.Random
import java.util.concurrent.TimeUnit

import com.codahale.metrics.jvm.{MemoryUsageGaugeSet, GarbageCollectorMetricSet}
import com.codahale.metrics.{Gauge, ConsoleReporter, MetricRegistry}
import com.log.Logging
import org.elasticsearch.metrics.ElasticsearchReporter

import scala.collection.mutable.ListBuffer

/**
 * Created by yxl on 15/10/31.
 */

object MetricDemo extends Logging {

  /**
   * Gauge是最简单的度量类型，只有一个简单的返回值，他用来记录一些对象或者事物的瞬时值
   * @param metrics
   */
  def testGauge(metrics:MetricRegistry): Unit ={

    logInfo("测试 testGauge")

     val listBuffer = new ListBuffer[Int]()

     val gauge = new Gauge[Integer](){
       override def getValue: Integer = {
            return listBuffer.size
       }
     }

    metrics.register(MetricRegistry.name( "jobs", "size"),gauge)


    for(i <- 1 to 100){
      listBuffer.+=(i)
      TimeUnit.SECONDS.sleep(1)
    }


  }

  /**
   * Meter是一种只能自增的计数器，通常用来度量一系列事件发生的比率。
   * 他提供了平均速率，以及指数平滑平均速率，以及采样后的1分钟，5分钟，15分钟速率
   * @param metrics
   */
  def testMeter(metrics:MetricRegistry): Unit ={
    val requests = metrics.meter("requests")
    for(i <- 1 to 100){
      TimeUnit.SECONDS.sleep(3)
      requests.mark() // 自增
    }
  }

  /**
   * Counter是一个简单64位的计数器，它可以增加和减少
   * @param metrics
   */
  def testCounter(metrics:MetricRegistry): Unit ={
    val requests = metrics.counter("requests")
    val random = new Random();
    for(i <- 1 to 100){
      val current = random.nextInt(100)
      if(current %2 == 0){
         requests.inc()
      }else{
         requests.dec()
      }
      TimeUnit.SECONDS.sleep(1)
    }
  }

  /**
   * Histrogram是用来度量流数据中Value的分布情况，
   * Histrogram可以计算最大/小值、平均值，方差，分位数（如中位数，或者95th分位数），
   * 如75%,90%,98%,99%的数据在哪个范围内
   * @param metrics
   */
  def testHistograms(metrics:MetricRegistry): Unit ={
    val requests = metrics.histogram("requests")

    val random = new Random();
    for(i <- 1 to 100){
      val current = random.nextInt(100)
      requests.update(current)
      TimeUnit.SECONDS.sleep(1)
    }
  }

  /**
   *Timer是Histogram跟Meter的一个组合，比如要统计当前请求的速率和处理时间
   * @param metrics
   */
  def testTimer(metrics:MetricRegistry): Unit ={
    val responses = metrics.timer("response");
    val context = responses.time();
    for(i <- 1 to 10){
       TimeUnit.SECONDS.sleep(1)
    }
    context.stop()

  }

  /**
   * 内部的对内存和gc的监控
   * @param metrics
   */
  def testJVM(metrics:MetricRegistry): Unit ={
    metrics.registerAll(new GarbageCollectorMetricSet)
    metrics.registerAll(new MemoryUsageGaugeSet)
  }

  def main(args:Array[String]): Unit ={

    val metrics = new MetricRegistry()

//    val reporter = ConsoleReporter.forRegistry(metrics)
//      .convertRatesTo(TimeUnit.SECONDS)
//      .convertDurationsTo(TimeUnit.MILLISECONDS)
//      .build();
//    reporter.start(5, TimeUnit.SECONDS);


    val reporter = ElasticsearchReporter.forRegistry(metrics)
      .hosts("localhost:9200")
      .build();
    reporter.start(5, TimeUnit.SECONDS);


    val httpServer = new HttpMetricsServer("localhost",8080,metrics)
    httpServer.start()


    //testMeter(metrics)

    //testCounter(metrics)

    //testGauge(metrics)

    testHistograms(metrics)

    //testTimer(metrics)

//    testJVM(metrics)

    TimeUnit.SECONDS.sleep(1000)



  }

}
