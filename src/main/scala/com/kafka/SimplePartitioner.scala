package com.kafka


import kafka.producer.Partitioner
import kafka.utils.VerifiableProperties

/**
 * Created by yxl on 15/10/28.
 */
class SimplePartitioner(props: VerifiableProperties = null) extends Partitioner {

  override def partition(key: Any, numPartitions: Int): Int = {
    var partition = 0;
    val stringKey = key.toString ;
    partition = stringKey.toInt % numPartitions ;
    return partition;
  }

}
