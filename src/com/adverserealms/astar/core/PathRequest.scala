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

class PathRequest(start:AstarTile, end:AstarTile, map:Map, priority:Int = 10) {

  /**
   * Returns the start point of this request
   */
  def getStart = start
  
  /**
   * returns the end point of this request
   */
  def getEnd = end
  
  /**
   * Returns the map of this request
   */
  def getMap = map
  
  /**
   * Returns the priority of this request
   */
  def getPriority = priority
  
  /**
   * Returns true if the given tile is a target tile. Returns false otherwise. 
   * A* will stop searching if it find this tile as the best tile.
   */
  def isTarget(tile:AstarTile) : Boolean = {
    tile == end
  }
}