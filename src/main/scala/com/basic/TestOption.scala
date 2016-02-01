package com.basic


case class Student(
                 id: Int,
                 firstName: String,
                 lastName: String,
                 age: Int,
                 gender: Option[String]
                 )



object TestOption {


  private val users = Map(1 -> Student(1, "John", "Doe", 32, Some("male")),
    2 -> Student(2, "Johanna", "Doe", 30, None))

  def findById(id: Int): Option[Student] = users.get(id)

  def findAll = users.values


  /**
   * Option[A] 是一个类型为 A 的可选值的容器：
   * 如果值存在， Option[A] 就是一个 Some[A] ，如果不存在， Option[A] 就是对象 None
   * @param args
   */
  def main(args: Array[String]){

    // findById  student
    val student = findById(1)
    if (student.isDefined) {
      println(student.get.firstName)
    }


    val user = Student(2, "Johanna", "Doe", 30, None)
    println("Gender: " + user.gender.getOrElse("not specified"))

    // 不同 getOrElse
    user.gender match {
      case Some(gender) => println("Gender: " + gender)
      case None => println("Gender: not specified")
    }

    // 如果返回的 Student 为 None  则不会执行 foreach 中的方法
    findById(3).foreach(user => println(user.firstName))

  }

}