package com.basic

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.{CountDownLatch, TimeUnit, ExecutorService, Executors}

/**
 * Created by yxl on 15/12/14.
 */

class TestAccount(var balance: Double) extends Runnable {

   val id = TestAccount.newUniqueNumber() // 调用伴生对象的方法

  val uid = TestAccount.counter.incrementAndGet()

  def deposit(amount: Double) {
    balance += amount
  }

  def printBalance(): Unit = {
    println("id:" + id + "  balance:" + balance)
  }


  override def run(): Unit = {
      println("Thread:" + Thread.currentThread().getName + "  id:" + id  + "  uid:" + uid)
  }
}

class TestUnsafeCreateAccount(countDownLatch:CountDownLatch) extends Runnable{

  override  def run(): Unit = {
    val account = new TestAccount(1)
    println("id:" + account.id)
    countDownLatch.countDown();
  }

}

class TestUnsafeAccount() extends Runnable{

   override  def run():Unit = {
       TestAccount.newUniqueNumber()
   }

}

class TestSafeAccount extends Runnable{
  override  def run():Unit = {
      var sum = 0 ;
      for( i <- TestAccount.lastNumber to 10){
         sum = sum + i
      }
      if(sum != 55){
          println("sum error")
          sys.exit(0)
      }

  }
}

object TestAccount {
  //伴生对象
   var lastNumber = 0

  def  newUniqueNumber() = {

    //this.synchronized{
      lastNumber += 1
      TimeUnit.MICROSECONDS.sleep(100)
      lastNumber
    //}

  }

  val counter = new AtomicInteger(0)

  def apply(balance: Double) = new TestAccount(balance)

  def staticAccount() = println("staticAccount")

}


object TestMain {


  def main(args: Array[String]): Unit = {

//    val account1 = TestAccount(1) // 默认可以调用伴生对象的 apply 方法
//    account1.printBalance()
//
//    val account2 = TestAccount(2)
//    account2.printBalance()
//
//    TestAccount.staticAccount() // 直接调用

    val pool: ExecutorService = Executors.newFixedThreadPool(50)

    val number = 20

    val countDownLatch = new CountDownLatch(number);

    for (i <- 1 to number) {
       // pool.execute(new TestAccount(1))  // 不涉及到线程安全，id 在创建对象的时候获取
          pool.execute(new TestUnsafeCreateAccount(countDownLatch)) // 多个线程同时创建 TestAccount 对象 newUniqueNumber 非线程安全
      //  pool.execute(new TestUnsafeAccount())   // 相当于调用类的静态方法,lastNumber值有修改，非线程安全
      // pool.execute(new TestSafeAccount()) // 访问 lastNumber 的 副本,不会改变 lastNumber 的值
    }


     countDownLatch.await() // 等多线程运行完成


    println(TestAccount.lastNumber)

    pool.shutdown()





  }

}
