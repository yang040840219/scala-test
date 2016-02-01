package com.basic

class User

class FreeUser(val name:String) extends User

class PremiumUser(val name:String) extends User


//Scala 会隐式的调用提取器的 unapply 方法
object FreeUser {

   // 伴生对象的性质
  def apply(name:String)= new FreeUser(name)

  def unapply(user: FreeUser): Option[String] = Some(user.name)
}
object PremiumUser {
  def unapply(user: PremiumUser): Option[String] = Some(user.name)
}


object Extractor {

  def main(args: Array[String]){

    def match_array(arr : Any) = arr match {
	  case Array(0) => println("Array:" + "0")
	  case Array(x, y) => println("Array:" + x + " " +y)
	  case Array(0, _*) => println("Array:" + "0 ...")
	  case _ => println("something else")
	}

    match_array(Array(0))
    match_array(Array(0,1))
    match_array(Array(0,1,2,3,4,5))

    val pattern = "([0-9]+) ([a-z]+)".r
		"20150628 hadoop" match {
		  case pattern(num, item) => println(num + " : " + item)
		}


    // 模式匹配可以解构各种数据结构，包括 列表 、 流 ，cass class 或者是 半生对象实现 unapply() 方法的类
    val user: User = new PremiumUser("Daniel")
    val result = user match {
      case FreeUser(name) => "Hello" + name
      case PremiumUser(name) => "Welcome back, dear" + name
    }
    println(result)


    val freeUser = FreeUser("abc")

  }

}