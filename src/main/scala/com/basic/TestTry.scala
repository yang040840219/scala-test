package com.basic

import java.io.FileNotFoundException
import java.net.{MalformedURLException, URL}


import scala.io.Source
import scala.util.{Failure, Success, Try}

/**
 * Created by yxl on 15/11/5.
 */
object TestTry {

  def main(args:Array[String]): Unit ={

    /**
     * Try[A] 则表示一种计算： 这种计算在成功的情况下，返回类型为 A 的值，在出错的情况下，返回 Throwable 。
     * 这种可以容纳错误的容器可以很轻易的在并发执行的程序之间传递。
     * @param url
     * @return
     */
    def parseURL(url: String): Try[URL] = Try(new URL(url))


    parseURL("123") match {
      case url: Success[URL] => println(url)   // url的类型 Success
      case ex:Failure[URL] => println(ex)
    }


    // unapply 方法
    parseURL("123") match {
      case Success(url) => println(url) // url 的类型 URL
      case Failure(ex) => println(ex.printStackTrace())
    }


    def getURLContent(url: String): Try[Iterator[String]] =
      for {
        url <- parseURL(url)
        source = Source.fromURL(url)
      } yield source.getLines()


    // 如果想在失败的情况下执行某种动作, 匹配Try 中的异常
    val content = getURLContent("garbage").recover({
      case e: FileNotFoundException => Iterator("Requested page does not exist")
      case e: MalformedURLException => Iterator("Please make sure to enter a valid URL")
      case _ => Iterator("An unexpected error has occurred. We are so sorry!")
    })

    // 可以安全调用
    println(content.get)




  }



}
