/*
Copyright (c) 2012 Joshua Garnett

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.adverserealms.astar.core

import akka.actor._
import akka.actor.Actor._
import scala.collection.mutable.HashMap
import org.slf4j.{Logger, LoggerFactory}

/**
 * The main search algorithm and core class of the Astar library
 */
class Astar extends Actor {
  
  protected lazy val log = LoggerFactory.getLogger(getClass())

  private val dataTiles = new HashMap[AstarTile, DataTile]()
  
  def receive = {
    case req:AstarPathRequest =>
      getPath(req)
    case _ =>
      log.error("Invalid message sent to Astar actor")
  }
  
  private def getPath(request:AstarPathRequest) = {
    log.debug("getPath")
    
    //clear any values in the dataTiles HashMap
    dataTiles.clear()
    
    notifyListener(request, AstarPathError("Not implemented", request))
  }
  
  private def notifyListener(request:AstarPathRequest, value:Any) = {
    request.listener(value)
  }
}

object Astar {
  
  private lazy val astarPool = actorOf[AstarPool].start
  
  /**
   * If Astar.safeMode is set to NORMAL_CHECK, the end tile will be
   * validated with the analyzer.analyzeTile call.  Only the end
   * tile is checked.  It is assumed you start from a valid position.
   */
  val NORMAL_CHECK:Int = 0
  
  /**
   * If Astar.safeMode is set to NO_CHECK, nothing will be checked
   * at the start of the search.
   */
  val NO_CHECK:Int = 1
  
  /**
   * Static method for finding an Astar path.  It will automatically
   * instantiate actors needed for asynchronous processing and will
   * clean them up.
   */
  def getPath(path:AstarPathRequest) = {
    astarPool ! path
  }
}