package com.adverserealms.astar.core

import akka.actor.Actor
import akka.actor.Actor._

class AstarProxy(listener: (Any) => Unit, safeMode:Int) extends Actor {
  
  /**
   * Instantiate an Astar actor instance for each path request
   * 
   * TODO: To conserve resources and improve performance we may 
   * want to eventually migrate this to an Actor Pool
   */
  private lazy val astar = actorOf(new Astar(safeMode)).start
  
  private var request:AstarPathRequest = null
  
  def receive = {
    case req:AstarPathRequest =>
      //forward the request to the Astar Actor Instance
      request = req
      astar ! req
    case response:AstarPathResponse =>
      //forward the response to the listener and stop the actors
      notifyListener(response)
      stopActors
    case error:AstarPathError =>
      //forward the error to the listener and stop the actors
      notifyListener(error)
      stopActors
    case _ =>
      //forward some generic error to the listener and stop the actors
      notifyListener(AstarPathError("Invalid message sent to AstarProxy", request))
      stopActors
  }
  
  private def notifyListener(value:Any) = {
    listener(value)
  }
  
  private def stopActors() = {
    self.stop
    astar.stop
  }
}