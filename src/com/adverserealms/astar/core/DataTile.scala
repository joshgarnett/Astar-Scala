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

class DataTile(target:AstarTile) {
  
  private val STANDARD_COST:Double = 1
  
  /**
   * Cost from start along best known path.
   */
  private var g:Double = 0
  
  /**
   * Heuristic Cost Estimate
   */
  private var h:Double = 0
  
  /**
   * Estimated total cost from start to goal through y
   */
  private var f:Double = 0
  
  private var open:Boolean = false
  private var closed:Boolean = false
  private var parent:AstarTile = null
  private var multiplier:Double = 1
  
  def getTarget = target
  
  def getG = g
  def setG(value:Double) = {
    g = value + getCost
    f = h + g
  }
  
  def getH = h
  def setH(value:Double) = {
    h = value
    f = h + g
  }
  
  def getF = f
  
  def getOpen = open
  def setOpen(value:Boolean) = { open = value }
  
  def getClosed = closed
  def setClosed(value:Boolean) = { closed = value }
  
  def getParent = parent
  def setParent(value:AstarTile) = { parent = value }
  
  /**
   * Sets the distance from this tile to its parent
   */
  def setDistance(distance:Double) = {
    multiplier = distance
  }
  
  def getCost() : Double = {
    if(target.isInstanceOf[CostTile]) {
      target.asInstanceOf[CostTile].getCost() * multiplier
    }
    else {
      STANDARD_COST * multiplier
    }
  }
  
  def calculateUpdateF(parentCost:Double) : Double = {
    getCost + parentCost + h
  }
}