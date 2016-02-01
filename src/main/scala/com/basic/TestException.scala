package com.basic

import java.io.IOException

import scala.util.control.NonFatal

/**
 * Created by yxl on 16/1/20.
 */
object TestException {

   def main(args:Array[String]): Unit ={


     def tryOrIOException[T](block: => T): T = {
       try {
         block
       } catch {
         case e: IOException => throw e
         case NonFatal(t) => throw new IOException(t)
       }
     }


     def printHello(): Unit ={
         println("hello")
     }


     tryOrIOException(printHello())




   }

}
