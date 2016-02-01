package com.basic

/**
 * Created by yxl on 15/7/11.
 */


//class Pair[T](val first:T,val second:T)

// T 的上界是 Comparable, T 必须是 Comparable的实现类
class Pair[T <: Comparable[T]](val first: T, val second: T) {
  def bigger(): T = {
    if (first.compareTo(second) > 0) {
      first
    } else {
      second
    }
  }

  def replaceFirst[R <: T](newFirst: R) = {
    new Pair[T](newFirst, second)
  }

}

// T可以不是Comparable的子类,但是隐式转换必须有 <%
class PairNotPerfect[T <% Comparable[T]](val first:T,val second:T){
  def bigger(): T = {
    if (first.compareTo(second) > 0) {
      first
    } else {
      second
    }
  }
}

// Ordered 相当于 Comparable的增强版,定义了更多的操作符
class PairPerfect[T <% Ordered[T]](val first:T,val second:T){
   def bigger():T = {
       if(first > second){
          first
       }else{
         second
       }
   }
}


// Ordering 泛型有隐式值 ordered , 可以理解为运行时有 ordered 在 编译时自动传入的
class PairOrdering[T : Ordering](val first:T,val second:T){
  def bigger()(implicit ordered:Ordering[T]):T = { // ordered 为 隐式参数
    if(ordered.compare(first,second)>0){
       first
    }else{
      second
    }

  }
}


object TestBounds {
  def main(args: Array[String]): Unit = {

    val pair = new Pair("spark", "hello")
    println(pair.bigger())

    val pairNotPerfect = new PairNotPerfect(2,3) // Integer 有隐式转换
    println(pairNotPerfect.bigger())

    val pairOrdering = new PairOrdering("spark","hello")
    println(pairOrdering.bigger())

  }
}
