// Generated from "com/google/j2cl/benchmarks/octane/richards/Richards.java"
package com.google.j2cl.benchmarks.octane.richards

import javaemul.lang.*
import kotlin.jvm.*

open class Richards {
 open fun runRichards() {
  val scheduler: com.google.j2cl.benchmarks.octane.richards.Scheduler? = com.google.j2cl.benchmarks.octane.richards.Scheduler()
  scheduler!!.addIdleTask(com.google.j2cl.benchmarks.octane.richards.Scheduler.ID_IDLE, 0, null, com.google.j2cl.benchmarks.octane.richards.Richards.COUNT)
  var queue: com.google.j2cl.benchmarks.octane.richards.Packet? = com.google.j2cl.benchmarks.octane.richards.Packet(null, com.google.j2cl.benchmarks.octane.richards.Scheduler.ID_WORKER, com.google.j2cl.benchmarks.octane.richards.Scheduler.KIND_WORK)
  queue = com.google.j2cl.benchmarks.octane.richards.Packet(queue, com.google.j2cl.benchmarks.octane.richards.Scheduler.ID_WORKER, com.google.j2cl.benchmarks.octane.richards.Scheduler.KIND_WORK)
  scheduler!!.addWorkerTask(com.google.j2cl.benchmarks.octane.richards.Scheduler.ID_WORKER, 1000, queue)
  queue = com.google.j2cl.benchmarks.octane.richards.Packet(null, com.google.j2cl.benchmarks.octane.richards.Scheduler.ID_DEVICE_A, com.google.j2cl.benchmarks.octane.richards.Scheduler.KIND_DEVICE)
  queue = com.google.j2cl.benchmarks.octane.richards.Packet(queue, com.google.j2cl.benchmarks.octane.richards.Scheduler.ID_DEVICE_A, com.google.j2cl.benchmarks.octane.richards.Scheduler.KIND_DEVICE)
  queue = com.google.j2cl.benchmarks.octane.richards.Packet(queue, com.google.j2cl.benchmarks.octane.richards.Scheduler.ID_DEVICE_A, com.google.j2cl.benchmarks.octane.richards.Scheduler.KIND_DEVICE)
  scheduler!!.addHandlerTask(com.google.j2cl.benchmarks.octane.richards.Scheduler.ID_HANDLER_A, 2000, queue)
  queue = com.google.j2cl.benchmarks.octane.richards.Packet(null, com.google.j2cl.benchmarks.octane.richards.Scheduler.ID_DEVICE_B, com.google.j2cl.benchmarks.octane.richards.Scheduler.KIND_DEVICE)
  queue = com.google.j2cl.benchmarks.octane.richards.Packet(queue, com.google.j2cl.benchmarks.octane.richards.Scheduler.ID_DEVICE_B, com.google.j2cl.benchmarks.octane.richards.Scheduler.KIND_DEVICE)
  queue = com.google.j2cl.benchmarks.octane.richards.Packet(queue, com.google.j2cl.benchmarks.octane.richards.Scheduler.ID_DEVICE_B, com.google.j2cl.benchmarks.octane.richards.Scheduler.KIND_DEVICE)
  scheduler!!.addHandlerTask(com.google.j2cl.benchmarks.octane.richards.Scheduler.ID_HANDLER_B, 3000, queue)
  scheduler!!.addDeviceTask(com.google.j2cl.benchmarks.octane.richards.Scheduler.ID_DEVICE_A, 4000, null)
  scheduler!!.addDeviceTask(com.google.j2cl.benchmarks.octane.richards.Scheduler.ID_DEVICE_B, 5000, null)
  scheduler!!.schedule()
  if (scheduler!!.queueCount !== com.google.j2cl.benchmarks.octane.richards.Richards.EXPECTED_QUEUE_COUNT || scheduler!!.holdCount !== com.google.j2cl.benchmarks.octane.richards.Richards.EXPECTED_HOLD_COUNT) {
   val msg: kotlin.String? = "Error during execution: queueCount = " + scheduler!!.queueCount + ", holdCount = " + scheduler!!.holdCount + "."
   throw kotlin.AssertionError(msg)
  }
 }

 companion object {
  @kotlin.jvm.JvmField val COUNT: kotlin.Int = 1000

  @kotlin.jvm.JvmField val EXPECTED_QUEUE_COUNT: kotlin.Int = 2322

  @kotlin.jvm.JvmField val EXPECTED_HOLD_COUNT: kotlin.Int = 928
 }
}
