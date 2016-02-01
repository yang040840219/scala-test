package com.basic

/**
 * Created by yxl on 16/1/19.
 */
object TestZip {

  def main(args:Array[String]): Unit ={

    val numbers = List(0, 1, 2, 3, 4)

    val series = List(0, -1, -2, -3)

    /**
     * zip函数将传进来的两个参数中相应位置上的元素组成一个pair数组。如果其中一个参数元素比较长，那么多余的参数会被删掉
     */
    val zip = numbers.zip(series)

    println("zip:" + zip)

    /**
     * zipAll 函数和上面的zip函数类似，但是如果其中一个元素个数比较少，那么将用默认的元素填充
     */
    val zipAll = numbers.zipAll(series,-1,-1)

    println("zipAll:" + zipAll)


    /**
     * zipWithIndex函数将元素和其所在的下表组成一个pair
     * (数组值，数组下标)
     */
    val zipWithIndex =  series.zipWithIndex

    println("zipWithIndex:" + zipWithIndex)






  }

}
