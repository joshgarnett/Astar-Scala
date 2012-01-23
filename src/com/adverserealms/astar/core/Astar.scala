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

/**
 * The main search algorithm and core class of the Astar library
 */
class Astar(safeMode:Int = Astar.NORMAL_CHECK) extends Actor {

  private val dataTiles = new HashMap[AstarTile, DataTile]()
  
  def receive = {
    case req:AstarPathRequest =>
      getPath(req)
    case _ =>
      self.channel ! AstarPathError("Invalid message sent to Astar", null)
      
  }
  
  private def getPath(request:AstarPathRequest) = {
    //clear any values in the dataTiles HashMap
    dataTiles.clear()
  }
}

object Astar {
  
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
  def getPath(path:AstarPathRequest, listener: (Any) => Unit, safeMode:Int = Astar.NORMAL_CHECK) = {
    val proxy = actorOf(new AstarProxy(listener, safeMode)).start
    proxy ! path
  }
}