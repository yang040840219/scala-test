package com.akka

import akka.actor._

import scala.util.Random

/**
 * Created by yxl on 16/1/21.
 */


case class QuoteRepositoryRequest
case class QuoteRequest
case class QuoteRepositoryResponse(quote:String)

class QuoteRepositoryActor extends Actor with ActorLogging {

  val quotes = List(
    "Moderation is for cowards",
    "Anything worth doing is worth overdoing",
    "The trouble is you think you have time",
    "You never gonna know if you never even try")

  var repoRequestCount = 1

  override def receive: Receive = {
    case QuoteRepositoryRequest =>  {
       if(repoRequestCount > 3){
          self ! PoisonPill
       }else{
         val quoteResponse = QuoteRepositoryResponse(quotes(Random.nextInt(quotes.size)))
         repoRequestCount = repoRequestCount + 1
         sender ! quoteResponse
       }
    }
  }
}

class TeacherActorWatcher extends Actor with ActorLogging {

  val quoteRepositoryActor=context.actorOf(Props[QuoteRepositoryActor], "quoteRepositoryActor")
  context.watch(quoteRepositoryActor)

  override  def receive:Receive = {
      case QuoteRequest => {
        quoteRepositoryActor ! QuoteRepositoryRequest
      }
      case Terminated(terminatedActorRef)=>{
        log.error(s"Child Actor {$terminatedActorRef} Terminated")
      }
  }

}


object DeathWatchActor {


}
