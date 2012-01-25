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

package com.adverserealms.astar

import java.util.concurrent.TimeUnit
import org.junit.Assert._
import org.junit._
import com.adverserealms.astar.basic2d._
import com.adverserealms.astar.core._
import akka.actor.Actor._
import akka.actor._
import akka.util.Duration
import com.adverserealms.astar.basic2d.analyzers.WalkableAnalyzer

class AstarSquareGridTest {
  
  private var astarPool:ActorRef = null
  
  @Before
  def setUp = {
    astarPool = actorOf[AstarPool].start
  }
  
  @After
  def tearDown = {
    astarPool.stop()
  }
  
  @Test
  def squareGridTest() = {
    val map = new MockSquareGridMap
    
    val start = map.getTile(0,0)
    val end = map.getTile(3,3)

    var response:Any = null
    val f = astarPool ? AstarPathRequest(start, end, map, List(new WalkableAnalyzer(false)), (value:Any) => {
      response = value
    })
    
    //wait up to a second for a response
    f.await(Duration.create(1, TimeUnit.SECONDS))
    
    assertNotNull("Response is not null", response)
    
    assertTrue("Listener received a AstarPathResponse", response.isInstanceOf[AstarPathResponse])
    
    val pathResponse = response.asInstanceOf[AstarPathResponse]
    
    assertTrue("Path was found", pathResponse.found)
    
    assertEquals("Path length is 5", 5, pathResponse.path.getPath.size)
    
    println("squareGridTest: " + pathResponse.path.toString())
  }
  
  @Test
  def largeSquareGridTest() = {
    val map = new MockLargeSquareGridMap
    
    val start = map.getTile(0,0)
    val end = map.getTile(7,7)

    var response:Any = null
    val f = astarPool ? AstarPathRequest(start, end, map, List(new WalkableAnalyzer(false)), (value:Any) => {
      response = value
    })
    
    //wait up to a second for a response
    f.await(Duration.create(1, TimeUnit.SECONDS))
    
    assertNotNull("Response is not null", response)
    
    assertTrue("Listener received a AstarPathResponse", response.isInstanceOf[AstarPathResponse])
    
    val pathResponse = response.asInstanceOf[AstarPathResponse]
    
    assertTrue("Path was found", pathResponse.found)
    
    assertEquals("Path length is 12", 12, pathResponse.path.getPath.size)
    
    println("largeSquareGridTest: " + pathResponse.path.toString())
  }
}