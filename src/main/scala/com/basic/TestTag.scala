package com.basic

import scala.reflect.ClassTag

/**
 * Created by yxl on 15/7/11.
 */
object TestTag {

  // jvm 在运行时必须指定类型（基本类型）

  // 创建一个泛型的数组 Array[T] = new Array[T]() 不是 Array[T] = new Array[String]
  // Manifest 可以理解为存储运行时的类型，隐式的
  def arrayMake[T: Manifest](first: T, second: T) = {
    val r = new Array[T](2)
    r(0) = first
    r(1) = second
    r
  }

  // 用来保存运行时的类型, 在编译时无法确定的类型,同样有个 隐式值
  // TypeTag 保存的信息要比 ClassTag 多
  def mkArray[T: ClassTag](elemes: T*) = Array(elemes: _*)


  def main(args: Array[String]) {

    // 通过 "spark" 可以推断出 T 为 String 类型
    arrayMake("spark", "hello").foreach(println(_))


    // val array = Array[String]("1",2)
    mkArray("1", "2", 3.2).foreach(x => println(x.isInstanceOf[String]))


  }


}
