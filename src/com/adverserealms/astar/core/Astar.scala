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

/**
 * The main search algorithm and core class of the Astar library
 */
class Astar(safeMode:Int = Astar.NORMAL_CHECK) extends Actor {

  def receive = {
    case req:PathRequest =>
      //do something
    case _ =>
      
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
   * If Astar.safeMode is seto to NO_CHECK, nothing will be checked
   * at the start of the search.
   */
  val NO_CHECK:Int = 1
  
  /**
   * This should be called at the start of the application
   */
  def initialize() = {
    val actor = actorOf[Astar]
    actor.start
  }
  
  /**
   * This should be called at the end of the application
   */
  def cleanup() = {
    val actor = actorOf[Astar]
    actor.stop
  }
}