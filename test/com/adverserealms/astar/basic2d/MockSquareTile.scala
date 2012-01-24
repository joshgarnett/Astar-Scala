package com.adverserealms.astar.basic2d

import com.adverserealms.astar.core._
import com.adverserealms.astar.basic2d._

class MockSquareTile(point:Point) extends PositionTile with AstarTile with CostTile with WalkableTile {

  private var walkable = true
  
  def setWalkable(value:Boolean) = { walkable = value }
  
  def getWalkable(): Boolean = {
    walkable
  }

  def getPosition(): Point = { 
    point
  }
  
  def getCost() : Double = {
    1.0d
  }

}