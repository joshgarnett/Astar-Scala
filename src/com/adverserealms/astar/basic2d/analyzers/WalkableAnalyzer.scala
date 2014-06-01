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

package com.adverserealms.astar.basic2d.analyzers

import scala.collection.mutable.ArrayBuffer

import com.adverserealms.astar.basic2d.WalkableTile
import com.adverserealms.astar.core.Analyzer
import com.adverserealms.astar.core.AstarTile
import com.adverserealms.astar.core.AstarPathRequest
import com.adverserealms.astar.core.DataTile

/**
 * The WalkableAnalyzer eliminates tiles that aren't walkable. If ignoreEnd is
 * true, the end node (PathRequest.isTarget(tile)) doesn't have to be walkable
 */
class WalkableAnalyzer(ignoreEnd: Boolean) extends Analyzer {

  def analyzeTile(mainTile: DataTile, request: AstarPathRequest): Boolean = {
    if (ignoreEnd && mainTile.getTarget == request.end) {
      true
    }
    else {
      mainTile.getTarget.asInstanceOf[WalkableTile].getWalkable
    }
  }

  def analyze(mainTile: AstarTile, neighbors: ArrayBuffer[DataTile], request: AstarPathRequest): ArrayBuffer[DataTile] = {
    val walkableNeighbors = new ArrayBuffer[DataTile]

    neighbors.foreach { tile =>
      if (tile.getTarget.asInstanceOf[WalkableTile].getWalkable || (ignoreEnd && tile == request.end)) {
        walkableNeighbors += tile
      }
    }

    walkableNeighbors
  }
}