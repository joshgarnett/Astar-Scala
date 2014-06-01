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

import akka.actor.Actor
import akka.actor.Actor._
import akka.routing._

class AstarPool extends Actor
    with DefaultActorPool
    with BoundedCapacityStrategy
    with MailboxPressureCapacitor
    with SmallestMailboxSelector
    with Filter
    with RunningMeanBackoff
    with BasicRampup {
  /**
   * Any messages received are pushed to the Pool routing
   */
  def receive = _route

  /**
   * The minimum number of actors to start with in the pool
   */
  def lowerBound = 10

  /**
   * The maximum number of actors to ramp to in the pool
   */
  def upperBound = 20

  /**
   * Threshold to evaluate if the actor is considered to be busy.
   * In this case the actor is considered busy if it is already processing
   * a single message.
   */
  def pressureThreshold = 1

  /**
   * This instructs the selector to only return unique actors from the pool.
   * Since our selectionCount is set to 1, this value is ignored
   */
  def partialFill = true

  /**
   * This defines how many actors the message should be routed to.  In our
   * case we only want to route to a single actor.
   */
  def selectionCount = 1

  /**
   * When pressure exceeds current capacity, increase the number of actors in the pool
   * by some factor (rampupRate) of the current pool size.
   */
  def rampupRate = 0.1

  /**
   * The number of delegates to cull from the pool is determined by some scaling factor
   * (the backoffRate) multiplied by the difference in capacity and pressure.
   */
  def backoffRate = 0.50

  /**
   * When the pressure ratio falls under some predefined amount (backoffThreshold),
   * decrease the number of actors in the pool by some factor of the current pool size.
   */
  def backoffThreshold = 0.50

  /**
   * This method returns an instance of the Astar Actor
   */
  def instance = actorOf[Astar]
}