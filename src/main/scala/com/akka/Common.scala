package com.akka

/**
 * Created by yxl on 15/7/9.
 */
object Common {

}

case object Start

case class Message(msg: String)


case class Word(key:String ,count:Int)

case class Result(key:String)

