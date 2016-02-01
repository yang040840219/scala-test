package com.basic

import scala.collection.mutable.ListBuffer


/**
 * Created by yxl on 15/7/21.
 */


case class A(p1:String)


object TestVal {
    def main(args:Array[String]){

      val flag = true
      //val env = { if(flag) { println("flag") ; "test " + flag}else{ "test " + flag } }

      //println(env)


//      val checkpointData:Option[A] = None
//      def fun:Option[String] = checkpointData.map(x => x.p1)
//      val x = fun.map(x => x+ "b").getOrElse({
//         "123"
//      })
//      println(x)

//      val listBuffer = new ListBuffer[(Int,String)]()
//      listBuffer.append((1,"a"))
//       listBuffer.+=((2,"b"))
//
//      val arrayBuffer = new ArrayBuffer[(Int,String)]()
//
//      arrayBuffer.+=((1,"a"))


      val list = List(1,2,3,4,5)

      var resultList = List[Int]()
      list.foreach( x => {
          resultList = x::resultList
      })

      print(resultList)

    }
}
