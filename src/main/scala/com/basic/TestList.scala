package com.basic

/**
 * Created by yxl on 15/7/13.
 */
object TestList {
  def main(args: Array[String]): Unit = {

    val parts = Array[String]("flag > 10 and flag < 20", "flag > 25 and flag < 30")
    parts.zipWithIndex.map(x => {x match {case(item,index) => println(item + " " + index)}})

    println("----")

    parts.zipWithIndex.map({case(item,index) => println(item + " " + index)})

    println("----")

    val testList = List("1:a","2:b","3:c","4:d")
    val testMap = testList.map( item => {
        val array = item.split(":")
        (array.apply(0),array.apply(1))
    })

    println(testMap) // 根据类型推导，返回List元组

    val (indexes,values) = testMap.unzip  // 拆分成两个List

    println(indexes)

    println(values)

    println("---zipWithIndex---")

    val contents = List("a","b","c","d")
    val s = contents.zipWithIndex
    println(s)


    println("---zipWithIndex----")

    val checkNumber:PartialFunction[Int,Int] = {
       case  number if(number > 0) => number
    }

    val numbers = List(1,2,3,-1,-2)

    // 在 collect 内部调用 isDefinedAt 方法
    numbers.collect(checkNumber).foreach(println(_))


    print("-------scanLeft-------")

    val array = Array[Int](1,2,3,4,5,6,7,8,9,10)

    array.scanLeft(0)(_ + _).map(x => println(x))




  }
}
