package com.basic

import java.util.concurrent.TimeUnit

/**
 * Created by yxl on 15/12/16.
 */

object Execute{


  def execute1(body:()=>Unit): Unit ={
       body()
  }


  def execute2(body:Double=>Unit): Unit ={
     body(2)
  }


  def execute3(body:Double=>Boolean):Boolean ={
     body(2)
  }

  def execute4(callback:(Long) => Unit):Unit = {
      var index = 0
      while(true){
         TimeUnit.SECONDS.sleep(1)
         callback(System.currentTimeMillis())
         if(index==10){
            return
         }
        index = index + 1
      }
  }

}



object TestFunction extends App {

   // 定义函数
   def fun1():Unit ={
      println("fun1")
   }
   Execute.execute1(fun1)

   // 匿名函数
   Execute.execute1(()=> {
      println("anonymous")
   })


   // 带参数
   val fun2 = (x:Double) => println("x:" + x)
   Execute.execute2(fun2)


   // 带参数返回值
   val fun3 = (x:Double)=> x>0
   val flag = Execute.execute3(fun3)
   println(flag)


   Execute.execute4(longTime => println("当前时间：" + longTime))


}
