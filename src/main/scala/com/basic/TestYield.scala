package com.basic

/**
 * Created by yxl on 15/11/16.
 */
object TestYield {

   def main(args:Array[String]): Unit ={


     val list = List("aaa","aaa","cccd","ddd")


     /**
      * yield 会记录每次for 循环的值, for循环结束后，会返回对应的被循环的类型，如果循环的是List
      * 则返回值是list ， map 返回map
      * 可以使用if 条件判断（守卫），过滤返回的值
      */
     val lineLengths =
       for {
         line <- list
         trimmedLine = line.trim
         if(trimmedLine.length > 3)
       } yield line + "：合计" + trimmedLine.length + "个字。"


     println(lineLengths)


     def nextPowerOf2(n: Int): Int = {
       val highBit = Integer.highestOneBit(n)
       println("highBit:" + highBit)
       if (highBit == n) n else highBit << 1
     }


     println(nextPowerOf2(127))


   }


}
