package com.basic

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Try, Failure, Success, Random}
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by yxl on 15/11/4.
 */


// some exceptions for things that might go wrong in the individual steps
// (we'll need some of them later, use the others when experimenting with the code):
case class GrindingException(msg: String) extends Exception(msg)
case class FrothingException(msg: String) extends Exception(msg)
case class WaterBoilingException(msg: String) extends Exception(msg)
case class BrewingException(msg: String) extends Exception(msg)


class TestFuture {

  type CoffeeBeans = String
  type GroundCoffee = String
  case class Water(temperature: Int)
  type Milk = String
  type FrothedMilk = String
  type Espresso = String
  type Cappuccino = String


  def grind(beans: CoffeeBeans): Future[GroundCoffee] = Future {
    println("start grinding...")
    Thread.sleep(Random.nextInt(5000))
    if (beans == "baked beans") throw GrindingException("are you joking?")
    println("finished grinding...")
    s"ground coffee of $beans"
  }

  def heatWater(water: Water): Future[Water] = Future {
    println("heating the water now")
    Thread.sleep(Random.nextInt(2000))
    println("hot, it's hot!")
    water.copy(temperature = 85)
  }

  def frothMilk(milk: Milk): Future[FrothedMilk] = Future {
    println("milk frothing system engaged!")
    Thread.sleep(Random.nextInt(2000))
    println("shutting down milk frothing system")
    s"frothed $milk"
  }

  def brew(coffee: GroundCoffee, heatedWater: Water): Future[Espresso] = Future {
    println("happy brewing :)")
    Thread.sleep(Random.nextInt(2000))
    println("it's brewed!")
    "espresso"
  }


  def testWaitGrind(): Unit ={
       val future = grind("arabica beans")
       val result: Try[GroundCoffee] = Await.ready(future,Duration.create(2,TimeUnit.SECONDS)).value.get
       result match {
          case Success(t) => println(t)
          case Failure(e) => println(e)
        }
  }


  def tempreatureOkay: Future[Boolean] = heatWater(Water(25)) map { water =>
    println("we're in the future!")
    (80 to 85).contains(water.temperature)
  }

}


object Test{


  def main(args:Array[String]): Unit ={

    val test = new TestFuture()

    // test.testWaitGrind()


  }



}
