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

/**
 * The Analyzer class describes searching constraints for the Astar class.
 */
trait Analyzer {

  /**
   * Used to validate a single tile. This method is used to see if the 
   * start/end tile is a valid tile.
   * 
   * @param mainTile The tile that is being analyzed
   * @param req The path request that is currently being executed
   * 
   * @return A boolean indicating whether or not the tile is valid
   */
  def analyzeTile(mainTile:DataTile, req:AstarPathRequest) : Boolean
  
  /**
   * Eliminates neighbors from the given array and returns the neighbors that were valid.
   * 
   * @param mainTile The tile who's neighbors are being analyzed
   * @param neighbors A list consisting of all the neighbors of the mainTile
   * @param request The PathRequest that is currently being executed
   * 
   * @return A list consisting of all the neighbors that passed this analyzer
   */
  def analyze(mainTile:AstarTile, neighbors:List[DataTile], request:AstarPathRequest) : List[DataTile]
}