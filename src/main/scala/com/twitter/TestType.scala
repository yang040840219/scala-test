package com.twitter

import scala.collection.mutable
import scala.collection.mutable.{HashSet, HashMap, ArrayBuffer}

/**
 * Created by yxl on 16/1/5.
 */
object TestType extends App {

   def  toList[A](a:A) = List[A](a)

   // 因为所有的类型变量只有在调用上下文中才被固定
   //def foo[A, B](f: A => List[A], b: B) = f(b) // 编译错误  f 需要的是A类型

  val  test = false
//  val thing: Int =
//    if (test)
//      42                             // : Int
//    else
//      throw new Exception("Whoops!") // : Nothing
//
//
//  print(thing)

   val a:Array[_<:AnyVal] = Array(123)

  // def getInt(s:String) =  if(test) s.toInt else  throw new RuntimeException("hello")



  object WeekDay extends Enumeration {
    type WeekDay = Value
    val Mon, Tue, Wed, Thu, Fri, Sat, Sun = Value
  }
  import WeekDay._

  def isWorkingDay(d: WeekDay) = ! (d == Sat || d == Sun)

  // println(WeekDay.Fri < WeekDay.Mon)





  //println("a=%s b".format(2))

  val executorsByHost = new HashMap[String, HashSet[String]]

  executorsByHost.put("linux2", new HashSet[String]())

  def hasExecutorsAliveOnHost(host: String): Boolean =  {
    executorsByHost.contains(host)
  }

  val pendingTasksForHost = new HashMap[String, ArrayBuffer[Int]]

  pendingTasksForHost.put("linux1", new ArrayBuffer[Int]())
  pendingTasksForHost.put("linux2", new ArrayBuffer[Int]())

  println(pendingTasksForHost.keySet.exists(hasExecutorsAliveOnHost(_)))

}
