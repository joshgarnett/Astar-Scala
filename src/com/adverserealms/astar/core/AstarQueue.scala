package com.adverserealms.astar.core

import scala.collection.mutable.ListBuffer

class AstarQueue(cap:Int) {
	protected lazy val heap = new Array[DataTile](cap)
	private var size:Int = 0;
	private val capacity = cap;
	
	private def bubbleUp(i:Int, tile:DataTile) = {
	  var index = i
	  var parent = (index-1)/2
	  while ((index > 0) && (heap(parent).getH > tile.getH)){
	    heap(index) = heap(parent)
	    index = parent
	    parent = (index-1)/2
	  }
	  heap(index) = tile
	}
	
	private def trickleDown(i:Int, tile:DataTile) = {
	  var index = i
	  var child = (index*2) + 1
	  while (child < size) {
	    if (((child + 1) < size) && (heap(child).getH > heap(child + 1).getH)) {
	      child += 1
	    }
	    heap(index) = heap(child)
	    index = child
	    child = (index*2) + 1
	  }
	  bubbleUp(index, tile)
	}
	
	def clear() = {
	  size = 0;
	}
	
	def pop() : DataTile = {
	  val node = heap(0);
	  size -= 1;
	  trickleDown(0, heap(size));
	  node
	}
	
	def push(tile: DataTile) = {
	  size += 1;
	  bubbleUp(size-1, tile)
	}
	
	def modify(tile: DataTile) : Unit = {
	  for (n <- 0 until size){
	    if (heap(n) == tile){
	      bubbleUp(n, tile)
	      return;
	    }
	  }
	}
	
	def isEmpty() : Boolean = {
	  size == 0
	}
	
	def getCapacity() : Int = {
	  capacity
	}
}