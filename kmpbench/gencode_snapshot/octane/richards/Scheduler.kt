// Generated from "com/google/j2cl/benchmarks/octane/richards/Scheduler.java"
package com.google.j2cl.benchmarks.octane.richards

import javaemul.lang.*
import kotlin.jvm.*

open class Scheduler {
 @kotlin.jvm.JvmField var queueCount: kotlin.Int = 0

 @kotlin.jvm.JvmField var holdCount: kotlin.Int = 0

 @kotlin.jvm.JvmField var list: com.google.j2cl.benchmarks.octane.richards.TaskControlBlock? = null

 @kotlin.jvm.JvmField var currentTcb: com.google.j2cl.benchmarks.octane.richards.TaskControlBlock? = null

 @kotlin.jvm.JvmField var currentId: kotlin.Int = 0

 @kotlin.jvm.JvmField val blocks: kotlin.Array<com.google.j2cl.benchmarks.octane.richards.TaskControlBlock?>?

 constructor() {
  this.queueCount = 0
  this.holdCount = 0
  this.blocks = kotlin.arrayOfNulls<com.google.j2cl.benchmarks.octane.richards.TaskControlBlock>(com.google.j2cl.benchmarks.octane.richards.Scheduler.NUMBER_OF_IDS)
  this.list = null
  this.currentTcb = null
  this.currentId = - 1
 }

 open fun addIdleTask(id: kotlin.Int, priority: kotlin.Int, queue: com.google.j2cl.benchmarks.octane.richards.Packet?, count: kotlin.Int) {
  this.addRunningTask(id, priority, queue, com.google.j2cl.benchmarks.octane.richards.IdleTask(this, 1, count))
 }

 open fun addWorkerTask(id: kotlin.Int, priority: kotlin.Int, queue: com.google.j2cl.benchmarks.octane.richards.Packet?) {
  this.addTask(id, priority, queue, com.google.j2cl.benchmarks.octane.richards.WorkerTask(this, com.google.j2cl.benchmarks.octane.richards.Scheduler.ID_HANDLER_A, 0))
 }

 open fun addHandlerTask(id: kotlin.Int, priority: kotlin.Int, queue: com.google.j2cl.benchmarks.octane.richards.Packet?) {
  this.addTask(id, priority, queue, com.google.j2cl.benchmarks.octane.richards.HandlerTask(this))
 }

 open fun addDeviceTask(id: kotlin.Int, priority: kotlin.Int, queue: com.google.j2cl.benchmarks.octane.richards.Packet?) {
  this.addTask(id, priority, queue, com.google.j2cl.benchmarks.octane.richards.DeviceTask(this))
 }

 open fun addRunningTask(id: kotlin.Int, priority: kotlin.Int, queue: com.google.j2cl.benchmarks.octane.richards.Packet?, task: com.google.j2cl.benchmarks.octane.richards.Task?) {
  this.addTask(id, priority, queue, task)
  this.currentTcb!!.setRunning()
 }

 fun addTask(id: kotlin.Int, priority: kotlin.Int, queue: com.google.j2cl.benchmarks.octane.richards.Packet?, task: com.google.j2cl.benchmarks.octane.richards.Task?) {
  this.currentTcb = com.google.j2cl.benchmarks.octane.richards.TaskControlBlock(this.list, id, priority, queue, task)
  this.list = this.currentTcb
  this.blocks!![id] = this.currentTcb
 }

 open fun schedule() {
  this.currentTcb = this.list
  LOOP@ while (this.currentTcb !== null) {
   if (this.currentTcb!!.isHeldOrSuspended()) {
    this.currentTcb = this.currentTcb!!.link
   } else {
    this.currentId = this.currentTcb!!.id
    this.currentTcb = this.currentTcb!!.run()
   }
  }
 }

 open fun release(id: kotlin.Int): com.google.j2cl.benchmarks.octane.richards.TaskControlBlock? {
  val tcb: com.google.j2cl.benchmarks.octane.richards.TaskControlBlock? = this.blocks!![id]
  if (tcb === null) {
   return tcb
  }
  tcb!!.markAsNotHeld()
  if (tcb!!.priority > this.currentTcb!!.priority) {
   return tcb
  } else {
   return this.currentTcb
  }
 }

 open fun holdCurrent(): com.google.j2cl.benchmarks.octane.richards.TaskControlBlock? {
  this.holdCount = this.holdCount + 1
  this.currentTcb!!.markAsHeld()
  return this.currentTcb!!.link
 }

 open fun suspendCurrent(): com.google.j2cl.benchmarks.octane.richards.TaskControlBlock? {
  this.currentTcb!!.markAsSuspended()
  return this.currentTcb
 }

 open fun queue(packet: com.google.j2cl.benchmarks.octane.richards.Packet?): com.google.j2cl.benchmarks.octane.richards.TaskControlBlock? {
  val t: com.google.j2cl.benchmarks.octane.richards.TaskControlBlock? = this.blocks!![packet!!.id]
  if (t === null) {
   return t
  }
  this.queueCount = this.queueCount + 1
  packet!!.link = null
  packet!!.id = this.currentId
  return t!!.checkPriorityAdd(this.currentTcb, packet)
 }

 companion object {
  @kotlin.jvm.JvmField val ID_IDLE: kotlin.Int = 0

  @kotlin.jvm.JvmField val ID_WORKER: kotlin.Int = 1

  @kotlin.jvm.JvmField val ID_HANDLER_A: kotlin.Int = 2

  @kotlin.jvm.JvmField val ID_HANDLER_B: kotlin.Int = 3

  @kotlin.jvm.JvmField val ID_DEVICE_A: kotlin.Int = 4

  @kotlin.jvm.JvmField val ID_DEVICE_B: kotlin.Int = 5

  @kotlin.jvm.JvmField val NUMBER_OF_IDS: kotlin.Int = 6

  @kotlin.jvm.JvmField val KIND_DEVICE: kotlin.Int = 0

  @kotlin.jvm.JvmField val KIND_WORK: kotlin.Int = 1
 }
}
