package com.adverserealms.astar.core

class DataTile(target:AstarTile) {

  /**
   * g
   */
  private var heuristicValue : Double = 0
  
  /**
   * h
   */
  private var totalCostToThisTile :Double = 0
  
  private var multiplier : Double = 1
  
  private var standardCost : Double = 1
  
  /**
   * f
   */
  def getTotalCost() : Double = {
    return heuristicValue + totalCostToThisTile
  }
  
  def getCost() : Double = {
    if(target.isInstanceOf[CostTile]) {
      target.asInstanceOf[CostTile].getCost() * multiplier
    }
    else {
      standardCost * multiplier
    }
  }
}