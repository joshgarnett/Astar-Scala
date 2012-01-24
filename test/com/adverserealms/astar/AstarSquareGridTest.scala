package com.adverserealms.astar

import java.util.concurrent.TimeUnit
import org.junit.Assert._
import org.junit._
import com.adverserealms.astar.basic2d.MockSquareGridMap
import com.adverserealms.astar.core._
import akka.actor.Actor._
import akka.actor._
import akka.util.Duration

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
  def sample() = {
    val map = new MockSquareGridMap
    
    val start = map.getTile(0,0)
    val end = map.getTile(3,3)

    var response:Any = null
    val f = astarPool ? AstarPathRequest(start, end, map, (value:Any) => {
      response = value
    })
    
    //wait up to a second for a response
    f.await(Duration.create(1, TimeUnit.SECONDS))
    
    assertNotNull("Response is not null", response)
    
    assertTrue("reponse is an error", response.isInstanceOf[AstarPathResponse])
    
    //add other checks to the path
  }
}