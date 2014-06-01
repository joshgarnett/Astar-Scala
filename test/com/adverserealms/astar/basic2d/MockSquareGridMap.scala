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

package com.adverserealms.astar.basic2d

import scala.collection.immutable.List
import scala.collection.mutable.ListBuffer
import com.adverserealms.astar.core._
import com.adverserealms.astar.basic2d._
import org.slf4j.{ Logger, LoggerFactory }

class MockSquareGridMap extends AstarMap {

  protected lazy val log = LoggerFactory.getLogger(getClass())

  private val MAP_WIDTH = 4

  private val MAP_HEIGHT: Int = 4

  private val tiles: List[MockSquareTile] = populateMockTiles()

  private val diagonalMultiplier = 1.4d

  private val normalMultiplier = 1.0d

  private val defaultCost = 1.0d

  /**
   * Map: x's are not walkable
   *
   * 0000
   * 0xx0
   * 0xx0
   * 0000
   */
  def populateMockTiles(): List[MockSquareTile] = {
    val tiles = new ListBuffer[MockSquareTile]

    for (y <- 0 until MAP_HEIGHT) {
      for (x <- 0 until MAP_WIDTH) {
        val tile = new MockSquareTile(new Point(x, y))

        if (x == 1 && y == 1) {
          tile.setWalkable(false)
        }
        if (x == 2 && y == 1) {
          tile.setWalkable(false)
        }
        if (x == 1 && y == 2) {
          tile.setWalkable(false)
        }
        if (x == 2 && y == 2) {
          tile.setWalkable(false)
        }

        tiles += tile
      }
    }

    tiles.toList
  }

  def getNeighbors(tile: AstarTile): List[AstarTile] = {
    val neighbors = new ListBuffer[AstarTile]

    val position = tile.asInstanceOf[PositionTile].getPosition()

    val x = position.getX
    val y = position.getY

    //up, left
    if (getTile(x - 1, y - 1) != null) {
      neighbors += getTile(x - 1, y - 1)
    }
    //up
    if (getTile(x, y - 1) != null) {
      neighbors += getTile(x, y - 1)
    }
    //up, right
    if (getTile(x + 1, y - 1) != null) {
      neighbors += getTile(x + 1, y - 1)
    }
    //left
    if (getTile(x - 1, y) != null) {
      neighbors += getTile(x - 1, y)
    }
    //right
    if (getTile(x + 1, y) != null) {
      neighbors += getTile(x + 1, y)
    }
    //down, left
    if (getTile(x - 1, y + 1) != null) {
      neighbors += getTile(x - 1, y + 1)
    }
    //down
    if (getTile(x, y + 1) != null) {
      neighbors += getTile(x, y + 1)
    }
    //down, right
    if (getTile(x + 1, y + 1) != null) {
      neighbors += getTile(x + 1, y + 1)
    }

    neighbors.toList
  }

  def getTile(x: Int, y: Int): MockSquareTile = {
    if (x < 0 || x >= MAP_WIDTH) {
      null
    }
    else if (y < 0 || y >= MAP_HEIGHT) {
      null
    }
    else {
      tiles(x + (y * MAP_WIDTH))
    }
  }

  def getHeuristic(tile: AstarTile, req: AstarPathRequest): Float = {
    val start = tile.asInstanceOf[PositionTile].getPosition()
    val end = req.end.asInstanceOf[PositionTile].getPosition()

    //using a diagonal distance heuristic
    val distance: Point = getXYDistanceBetweenPoints(start, end);

    var h = scala.math.max(distance.getX, distance.getY)

    h
  }

  def getXYDistanceBetweenPoints(start: Point, end: Point): Point = {
    new Point(getAxisDistance(start.getX, end.getX), getAxisDistance(start.getY, end.getY))
  }

  private def getAxisDistance(start: Int, end: Int): Int = {
    scala.math.abs(start - end)
  }

  def getDistance(start: AstarTile, end: AstarTile): Float = {
    val startP = start.asInstanceOf[PositionTile].getPosition()
    val endP = end.asInstanceOf[PositionTile].getPosition()

    if (startP.getX != endP.getX && startP.getY != endP.getY) {
      //diagonal move
      1.4f
    }
    else {
      1.0f
    }
  }

}