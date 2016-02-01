package com.packagea

/**
 * Created by yxl on 15/11/6.
 */
private[packagea] class AkkaRpcEnv extends RpcEnv {

   def fun2():Unit = {

   }

}

object AkkaRpcEnv{

     val a = "123"

     def fun1(): Unit ={

     }
}

private[packagea] object test{


  def main(args:Array[String]): Unit ={

    new AkkaRpcEnv().fun2()


  }

}
