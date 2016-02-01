package com.metrics

import java.util.concurrent.TimeUnit

import com.codahale.metrics.health.HealthCheck.Result
import com.codahale.metrics.health.{HealthCheck, HealthCheckRegistry}
import com.log.Logging

import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

/**
 * Created by yxl on 15/11/2.
 */


class DatabaseHealthCheck(size:Int) extends HealthCheck with Logging{
  override def check(): Result = {
    if(size%2 == 0){
      logInfo("health")
      return HealthCheck.Result.healthy();
    }else{
      logInfo("unhealth")
      return HealthCheck.Result.unhealthy("list size error")
    }

  }
}


/**
 * 检测的，用起来比较麻烦
 */
object HealthDemo extends Logging{

  def main(args:Array[String]): Unit ={

    val healthChecks = new HealthCheckRegistry();

    val listBuffer = new ListBuffer[Int]()

    healthChecks.register("requests",new DatabaseHealthCheck(listBuffer.size))

    for (entry <- healthChecks.runHealthChecks().entrySet()) {
      if (entry.getValue().isHealthy()) {
        println(entry.getKey() + ": OK");
      } else {
        println(entry.getKey() + ": FAIL");
      }
    }

  }

}
