package com.basic

import scala.collection.immutable.{ListMap, HashMap}

/**
 * Created by yxl on 15/12/15.
 */
object TestMap extends App {


   val data = HashMap(2->"c",1->"d",4->"a",3->"b")

   println(data)

   val a = ListMap(data.toSeq.sortBy(_._2) : _*)

   println(a)


}
