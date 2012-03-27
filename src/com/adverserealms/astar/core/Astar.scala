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

import java.util.PriorityQueue
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.HashMap
import org.slf4j.LoggerFactory
import akka.actor.Actor._
import akka.actor.Actor
import akka.dispatch.CompletableFuture

/**
 * The main search algorithm and core class of the Astar library
 */
class Astar extends Actor {
  
  protected lazy val log = LoggerFactory.getLogger(getClass())

  /**
   * Used for mapping the DataTiles to the tiles given by the user
   */
  private val dataTiles = new HashMap[AstarTile, DataTile]()
 
  /**
   * The heap stores all the tiles in the open list
   */
  private val heap = new PriorityQueue[DataTile]()
  
  /**
   * The PathRequest that is being processed
   */
  private var currentRequest:AstarPathRequest = null
  
  def receive = {
    case req:AstarPathRequest =>
      getPath(req)
    case _ =>
      log.error("Invalid message sent to Astar actor")
  }

  /**
   * Processes the PathRequest
   */
  private def getPath(request: AstarPathRequest) = {
    //clear any values in the dataTiles HashMap and the heap
    dataTiles.clear()
    heap.clear()

    //check if the endpoint is valid
    var validPath = true
    if (request.safeMode == Astar.NORMAL_CHECK) {
      for (analyzer <- request.analyzers) {
        if (!analyzer.analyzeTile(getDataTile(request.end), request)) {
          validPath = false
        }
      }
    }
    
    if(!validPath) {
      notifyListener(request, AstarPathError("The end tile is not a valid tile", request))
    }
    else {
      //set the request that is being processed
      currentRequest = request
      
      val start = getDataTile(request.start)
      var end = getDataTile(request.end)
      
      //open the starting tile and add it to the heap
      openTile(start, 0, null)
      
      var lastBestTile:DataTile = start;
      var lastBestH:Float = start.getH;
          
      while(!heap.isEmpty && lastBestTile.getTarget != end.getTarget) {
        val current = heap.poll()
        
        //check if the destination has been reached
        if(current.getTarget == end.getTarget) {
          lastBestTile = current
        }
        else {
          //close current tile
          current.setClosed()
          
          //get surrounding neighbors
          val neighbors = getNeighbors(current)
        
          //now inspect the neighbors
          for(neighbor <- neighbors) {
            if(!neighbor.isOpen) {
              //add back to the heap
              neighbor.setDistance(currentRequest.map.getDistance(current.getTarget, neighbor.getTarget))
              openTile(neighbor, current.getG, current)
            }
            else {
              val newF = neighbor.calculateUpdateF(current.getG)
              if(newF < neighbor.getF) {
                neighbor.setDistance(currentRequest.map.getDistance(current.getTarget, neighbor.getTarget))
                
                //remove and re-add the neighbor to adjust priority
                heap.remove(neighbor)
                openTile(neighbor, current.getG, current)
              }
            }
            
            if (neighbor.getH < lastBestH) {
              lastBestTile = neighbor
              lastBestH = neighbor.getH	
            }
          }
        }
      }
      
      var partialPath = ((lastBestTile != end) && request.safeMode == Astar.PARTIAL_CHECK)
      
      val path:AstarPath = buildPath(start, lastBestTile)
      
      notifyListener(request, AstarPathResponse(partialPath, request, path, path.getPath.size))
      
      //cleanup
      dataTiles.clear()
      heap.clear()
    }
  }
  
  /**
   * Builds the path starting from the end and working its way back.
   */
  private def buildPath(start:DataTile, end:DataTile) : AstarPath = {
    val path = new ListBuffer[AstarTile]
    
    var tile = end
    
    while(tile != start) {
      path.prepend(tile.getTarget)
      tile = tile.getParent
    }
    
    new AstarPath(end.getF, path.toList) 
  }
  
  /**
   * Returns all the neighbors of the specified tile. Each neighbor is passed through
   * the list of analyzers specified by the request.
   */
  private def getNeighbors(dataTile:DataTile) : List[DataTile] = {
    var neighbors = getStandardNeighbors(dataTile)
    
    //pass the list through each of the analyzers
    for(analyzer <- currentRequest.analyzers) {
      neighbors = analyzer.analyze(dataTile.getTarget, neighbors, currentRequest)
    }
    
    neighbors
  }
  
  /**
   * Returns the standard neighbors of the tile, according to the source map's 
   * getNeighbors method.
   */
  private def getStandardNeighbors(dataTile:DataTile) : List[DataTile] = {
    //the map determines which tiles are neighbors of the given tile
    val potentialNeighbors = currentRequest.map.getNeighbors(dataTile.getTarget)
    
    //leave out all the tiles that are already closed
    val neighbors = new ListBuffer[DataTile]
    
    for(tile <- potentialNeighbors) {
      val t = getDataTile(tile)
      if(t != null && t.isOpen) {
        neighbors += t
      }
    }
    
    neighbors.toList
  }
  
  /**
   * Gets the DataTile at the given location in the map.
   */
  private def getDataTile(tile:AstarTile) : DataTile = {
    if(dataTiles.contains(tile)) {
      dataTiles.get(tile).get
    }
    else {
      val dataTile = new DataTile(tile)
      dataTiles.put(tile, dataTile)
      dataTile
    }
  }
  
  /**
   * Opens a tile. The tile's position is set, it's added to the open list, the G and H 
   * are set and the parent is set. Afterwards, the tile is added to the heap.
   */
  private def openTile(tile:DataTile, g:Float, parent:DataTile)  = {
    tile.setOpen()
    tile.setG(g)
    tile.setH(currentRequest.map.getHeuristic(tile.getTarget, currentRequest))
    tile.setParent(parent)
    heap.add(tile)
  }
  
  /**
   * Sends a response to the listener callback specified by the request.
   */
  private def notifyListener(request:AstarPathRequest, value:Any) = {
    request.listener(value)
    
    try {
      //This is primarily for alerting futures that the request is complete
      self.senderFuture match {
        case Some(future) =>
          self.channel ! value
        case None =>
      }
    }
    catch { case _ => }
  }
}

object Astar {
  
  private val astarPool = actorOf[AstarPool].start
  
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
   * If Astar.safeMode is set to PARTIAL_CHECK, it will not check
   * the if the end point is valid but may do other checks and will
   * return a path even if the end point is not reached
   */
  val PARTIAL_CHECK:Int = 2;
  
  /**
   * Static method for finding an Astar path.  It will automatically
   * instantiate actors needed for asynchronous processing and will
   * clean them up.
   */
  def getPath(path:AstarPathRequest) = {
    astarPool ! path
  }
}