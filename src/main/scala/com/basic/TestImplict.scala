package com.basic

/**
 * Created by yxl on 15/7/15.
 */

class Arrow[T <:Int](val first:T) {// 泛型
   def -> (second:T) = {
      first + second
   }


}

class Prompt(val pre:String)

object JoesPre{
  implicit val p = new Prompt("-->")

  implicit class IntExtend(val x:Int) {
      def toImplictString(a:String):String = {
           x.toString() + a
    }
  }

}

case class OrderedClass(n:Int) extends Ordered[OrderedClass] {
  def compare(that: OrderedClass) =  this.n - that.n
}




object TestImplict {

  // predef 有很多隐式转换

  implicit def double2Int(x:Double) = x.toInt

  implicit def string2Double(x:String) = x.toDouble

  implicit def intToArrow[T <:Int]( x:T) = new Arrow(x)

  def main(args:Array[String]): Unit ={

     val a:Int = 3.2  // 使用隐式转换 double2Int ，编译的时候 convert(a)
     println(a) // 3

     // 隐式转换只尝试一次 不会 convert(convert(x))
     // val b:Int = "3.2"   不会  String -> Double -> Int

     val first = 1
     val second = 99
     val result = first -> second   // first 会隐式转换为  Arrow 调用 -> 方法
     println(result)



    // 隐式参数, 多个隐式参数 写一个 implicit 就可以了
    val prompt = new Prompt("->")

    import JoesPre._
    def greet(name:String)(implicit prompt:Prompt): Unit ={
      println("Hello " + name + " " + prompt.pre)
    }

    val b = 2
    println(b.toImplictString("s"))

    //greet("greet")(prompt)

    greet("greet") //声明 隐式 p 之后 不用显示的传递



    // 找到 List 中的最大值， 不可以处理 Int
    def maxListUpBonund_one[T <: Ordered[T]](elements:List[T]) :T =
      elements match {
        case List() => throw new Exception("empty list")
        case List(element) => element
        case element :: rest => {
          val max = maxListUpBonund_one(rest)
          if(element > max){
            element
          }else{
            max
          }
        }
      }

    //println(maxListUpBonund_two(List(1,2,3)))
    println(maxListUpBonund_one(List(OrderedClass(1),OrderedClass(2))))


    // maxListUpBonund_one 也可以使用 <% 这样可以处理 Int 了，有隐式转换
    // maxListUpBonund_two 有个隐式的 order 可以把 element 转换为 Ordered scala 库提供
    def maxListUpBonund_two[T](elements:List[T])(implicit order:T => Ordered[T]):T = {
      elements match {
        case List() => throw new Exception("empty list")
        case List(element) => element
        case element :: rest => {
          val max = maxListUpBonund_two(rest) //(order)
          if (order(element) > max) {
            element
          } else {
            max
          }
        }
      }
    }

    println(maxListUpBonund_two(List(1,2,3)))











  }


}
