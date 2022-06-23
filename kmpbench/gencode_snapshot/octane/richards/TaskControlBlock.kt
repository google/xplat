// Generated from "com/google/j2cl/benchmarks/octane/richards/TaskControlBlock.java"
package com.google.j2cl.benchmarks.octane.richards

import javaemul.lang.*
import kotlin.jvm.*

open class TaskControlBlock: com.google.j2cl.benchmarks.octane.richards.Task {
 @kotlin.jvm.JvmField var link: com.google.j2cl.benchmarks.octane.richards.TaskControlBlock? = null

 @kotlin.jvm.JvmField var id: kotlin.Int = 0

 @kotlin.jvm.JvmField var priority: kotlin.Int = 0

 @kotlin.jvm.JvmField var queue: com.google.j2cl.benchmarks.octane.richards.Packet? = null

 @kotlin.jvm.JvmField val task: com.google.j2cl.benchmarks.octane.richards.Task?

 @kotlin.jvm.JvmField var state: kotlin.Int = 0

 constructor(link: com.google.j2cl.benchmarks.octane.richards.TaskControlBlock?, id: kotlin.Int, priority: kotlin.Int, queue: com.google.j2cl.benchmarks.octane.richards.Packet?, task: com.google.j2cl.benchmarks.octane.richards.Task?) {
  this.link = link
  this.id = id
  this.priority = priority
  this.queue = queue
  this.task = task
  if (queue === null) {
   this.state = com.google.j2cl.benchmarks.octane.richards.TaskControlBlock.STATE_SUSPENDED
  } else {
   this.state = com.google.j2cl.benchmarks.octane.richards.TaskControlBlock.STATE_SUSPENDED_RUNNABLE
  }
 }

 open fun setRunning() {
  this.state = com.google.j2cl.benchmarks.octane.richards.TaskControlBlock.STATE_RUNNING
 }

 open fun markAsNotHeld() {
  this.state = this.state.and(com.google.j2cl.benchmarks.octane.richards.TaskControlBlock.STATE_NOT_HELD)
 }

 open fun markAsHeld() {
  this.state = this.state.or(com.google.j2cl.benchmarks.octane.richards.TaskControlBlock.STATE_HELD)
 }

 open fun isHeldOrSuspended(): kotlin.Boolean {
  return this.state.and(com.google.j2cl.benchmarks.octane.richards.TaskControlBlock.STATE_HELD) !== 0 || this.state === com.google.j2cl.benchmarks.octane.richards.TaskControlBlock.STATE_SUSPENDED
 }

 open fun markAsSuspended() {
  this.state = this.state.or(com.google.j2cl.benchmarks.octane.richards.TaskControlBlock.STATE_SUSPENDED)
 }

 open fun markAsRunnable() {
  this.state = this.state.or(com.google.j2cl.benchmarks.octane.richards.TaskControlBlock.STATE_RUNNABLE)
 }

 open fun run(): com.google.j2cl.benchmarks.octane.richards.TaskControlBlock? {
  val packet: com.google.j2cl.benchmarks.octane.richards.Packet?
  if (this.state === com.google.j2cl.benchmarks.octane.richards.TaskControlBlock.STATE_SUSPENDED_RUNNABLE) {
   packet = this.queue
   this.queue = packet!!.link
   if (this.queue === null) {
    this.state = com.google.j2cl.benchmarks.octane.richards.TaskControlBlock.STATE_RUNNING
   } else {
    this.state = com.google.j2cl.benchmarks.octane.richards.TaskControlBlock.STATE_RUNNABLE
   }
  } else {
   packet = null
  }
  return this.task!!.run(packet)
 }

 open override fun run(packet: com.google.j2cl.benchmarks.octane.richards.Packet?): com.google.j2cl.benchmarks.octane.richards.TaskControlBlock? {
  return this.run()
 }

 open fun checkPriorityAdd(task: com.google.j2cl.benchmarks.octane.richards.TaskControlBlock?, packet: com.google.j2cl.benchmarks.octane.richards.Packet?): com.google.j2cl.benchmarks.octane.richards.TaskControlBlock? {
  if (this.queue === null) {
   this.queue = packet
   this.markAsRunnable()
   if (this.priority > task!!.priority) {
    return this
   }
  } else {
   this.queue = packet!!.addTo(this.queue)
  }
  return task
 }

 open override fun toString(): kotlin.String {
  return "tcb { " + this.task + "@" + this.state + " }"
 }

 companion object {
  @kotlin.jvm.JvmField val STATE_RUNNING: kotlin.Int = 0

  @kotlin.jvm.JvmField val STATE_RUNNABLE: kotlin.Int = 1

  @kotlin.jvm.JvmField val STATE_SUSPENDED: kotlin.Int = 2

  @kotlin.jvm.JvmField val STATE_HELD: kotlin.Int = 4

  @kotlin.jvm.JvmField val STATE_SUSPENDED_RUNNABLE: kotlin.Int = 3

  @kotlin.jvm.JvmField val STATE_NOT_HELD: kotlin.Int = -5
 }
}
