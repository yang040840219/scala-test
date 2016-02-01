package com.basic

/**
 * Created by yxl on 15/9/27.
 */


class Foo {
  // a method that takes a function and a string, and passes the string into
  // the function, and then executes the function
  def exec(f: (String) => Unit, name: String) {
    f(name)
  }
}


object ClosureExample extends App {
  var hello = "Hello"

  def sayHello(name: String) {
    println(s"$hello, $name")
  }

  // execute sayHello from the exec method foo
  val foo = new Foo
  foo.exec(sayHello, "Al")
  // change the local variable 'hello', then execute sayHello from
  // the exec method of foo, and see what happens
  hello = "Hola"
  foo.exec(sayHello, "Lorenzo")


  println("------------ another example ------------")

  var votingAge = 18
  val isOfVotingAge = (age: Int) => age >= votingAge

  def printResult(f: Int => Boolean, x: Int) {
    println(f(x))
  }

  printResult(isOfVotingAge, 12) //votingAge 的定义范围，在 printResult中不能修改


  println("------------ another example ------------")


  // 返回值是一个函数
  def greeting(language: String) = (name: String) => {
    language match {
      case "english" => "Hello, " + name
      case "spanish" => "Buenos dias, " + name
    }
  }

  val hellos = greeting("english")

  println(hellos("Al"))


  println("------------ another example ------------")


  val divide = new PartialFunction[Int, Int] {
    def apply(x: Int) = 42 / x
    def isDefinedAt(x: Int) = x != 0
  }

  println(divide.isDefinedAt(0))

  val divide2: PartialFunction[Int, Int] = {
    case d: Int if d != 0 => 42 / d
  }

  val v = if(divide2.isDefinedAt(0)) divide2(3)

  println(v == ())


  println("------------ another example ------------")





}


