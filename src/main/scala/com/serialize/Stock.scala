package com.serialize

import java.io.{FileInputStream, ObjectInputStream, ObjectOutputStream, FileOutputStream}

/**
 * Created by yxl on 15/11/4.
 */


/**
 *
 * @param map
 */
class Stock(map:Map[String,String]) extends Serializable {

     def getValue():Map[String,String] ={
          map
     }

}


object SerializeTest{

    def main(args:Array[String]): Unit ={

      val map = Map("a"-> "1","b" -> "2")
      val stock = new Stock(map)

      val path = "/Users/yxl/data/serialize/stock"

      val oos = new ObjectOutputStream(new FileOutputStream(path))
      oos.writeObject(stock)
      oos.close

      val ois = new ObjectInputStream(new FileInputStream(path))

      val serializeObject = ois.readObject().asInstanceOf[Stock]

      val value = serializeObject.getValue().get("a")

      println(value.get)

    }


}
