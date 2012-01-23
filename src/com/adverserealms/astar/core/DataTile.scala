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