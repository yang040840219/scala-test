package com.zookeeper

import java.util
import java.util.concurrent.TimeUnit

import com.log.Logging
import kafka.utils.ZKStringSerializer
import org.I0Itec.zkclient.{IZkDataListener, IZkChildListener, ZkClient}

import scala.collection.JavaConversions._
import scala.collection.mutable

/**
 * Created by yxl on 15/12/15.
 *
 * 1.客户端调用create()方法创建名为 LOCK_ROOT/lock- 的节点，需要注意的是，这里节点的创建类型需要设置为EPHEMERAL_SEQUENTIAL。

 * 2.客户端调用 tryLock 获取到所有子节点path之后，如果发现自己在步骤1中创建的节点序号最小，那么就认为这个客户端获得了锁。

 * 3.如果在步骤2中发现自己并非所有子节点中最小的，说明自己还没有获取到锁。此时客户端需要找到比自己小的那个节点，同时注册事件监听。

 * 4.当被关注的节点被移除了，客户端会收到相应的通知。这个时候客户端需要再次调用 tryLock法来获取锁
 *
 */
class ZookeeperLock  extends Logging {

  private val LOCK_ROOT = "/locknode"

  private val LOCK_CHILD = "lock-"


  /**
   * 初始化一个 persist node
   */
  def init(): Unit ={
       val zkClient = ZookeeperLock.getZKClient()
       zkClient.createPersistent(LOCK_ROOT)
  }


  /**
   * 在 persist node 下创建 ephemeral node
   * @return
   */
  def create():String ={
     val  zkClient = ZookeeperLock.getZKClient()
     val path = zkClient.createEphemeralSequential(LOCK_ROOT + "/" + LOCK_CHILD ,"")
     logInfo("create ephemeral sequential node :" + path)
     path
  }


  /**
   * 尝试获取锁
   * 对比自己的path 中的 id 是否为当前  ephemeral sequential 中 最小的
   * @param myPath
   * @return
   */
  def tryLock(myPath:String):Boolean ={
    val myId = myPath.split("-").apply(1).toLong
    val zkClient = ZookeeperLock.getZKClient()
    val children = zkClient.getChildren(LOCK_ROOT)
    var isMin = true ;
    for(path <- children){
        val id = path.split("-").apply(1).toLong
        if(myId > id){ // 取最小的 myId
            isMin = false
        }
    }

    if(isMin){
       logInfo(myPath + " get lock")
       return true
    }else{
      logInfo(myPath + " not lock")
      return false;
    }

  }

  /**
   * 监控目录的变化，有变化时尝试再次获取锁
   * @param myPath
   */
  def listenNode(myPath:String): Unit ={
    val zkClient = ZookeeperLock.getZKClient()
    zkClient.subscribeChildChanges(LOCK_ROOT, new IZkChildListener(){
      override def handleChildChange(parentPath: String, currentChilds: util.List[String]): Unit = {
          tryLock(myPath)
      }
    })
  }

  /**
   * 监控 ephemeral sequential 中 最小的 ephemeral node 变化
   * 每次都是监控最小的有个问题，最小的ephemeral node 变化后，只是影响了下一个，
   * 其他的不影响，因此只需监听 上一个 ephemeral node 就可以
   * @param myPath
   */
  def listenMinNode(myPath:String): Unit ={
    val zkClient = ZookeeperLock.getZKClient()
    val children = zkClient.getChildren(LOCK_ROOT)
    val idToPath = new mutable.HashMap[Long,String]()
    val myId = myPath.split("-").apply(1).toLong
    for(path <- children){
      val id = path.split("-").apply(1).toLong
      if(id < myId){ // 只需选择比自己小的Id
        idToPath.put(id,path)
      }
    }

    val minPath = idToPath.toSeq.sortBy((-1) * _._1).head._2
    logInfo(myPath + "  监听前一个 ephemeral node :" + minPath)
    zkClient.subscribeDataChanges(LOCK_ROOT + "/" + minPath,new IZkDataListener {
      override def handleDataChange(dataPath: String, data: scala.Any): Unit = {
      }

      override def handleDataDeleted(dataPath: String): Unit = {
          val isLock = tryLock(myPath) // 原来的锁节点已删除重新获取锁
          // 移除原有的subscribe
          zkClient.unsubscribeDataChanges(LOCK_ROOT + "/" + minPath,new IZkDataListener {
            override def handleDataChange(dataPath: String, data: scala.Any): Unit = {}
            override def handleDataDeleted(dataPath: String): Unit = {}
          })
          if(!isLock){
             listenMinNode(myPath) // 添加对新的锁 node subscribe
          }
      }
    })

  }

  /**
   * 释放锁
   * @param myPath
   */
   def releaseLock(myPath:String):Unit = {
     val zkClient = ZookeeperLock.getZKClient()
     zkClient.delete(myPath)
     zkClient.close()
   }

}


object ZookeeperLock {

  val zookeeper = "localhost:2181"

  val zkClient = new ZkClient(zookeeper, 30000, 30000, ZKStringSerializer)

  def getZKClient(): ZkClient ={
        zkClient
  }

}

/**
 * 测试 需要运行多个 client
 */
object TestLockClient extends App {

   val zookeeperLock = new ZookeeperLock() // zkClient 是不同的

   //zookeeperLock.init()

   val path = zookeeperLock.create()

   val lock = zookeeperLock.tryLock(path)
   if(!lock){
        // 未获取到锁，一直等待
        zookeeperLock.listenMinNode(path)
        TimeUnit.SECONDS.sleep(200)
   }else{
     // 模拟释放锁
     TimeUnit.SECONDS.sleep(20)
     zookeeperLock.releaseLock(path)

   }


}
