// Generated from "com/google/j2cl/benchmarks/octane/richards/WorkerTask.java"
package com.google.j2cl.benchmarks.octane.richards

import javaemul.lang.*
import kotlin.jvm.*

open class WorkerTask: com.google.j2cl.benchmarks.octane.richards.Task {
 @kotlin.jvm.JvmField val scheduler: com.google.j2cl.benchmarks.octane.richards.Scheduler?

 @kotlin.jvm.JvmField var v1: kotlin.Int = 0

 @kotlin.jvm.JvmField var v2: kotlin.Int = 0

 constructor(scheduler: com.google.j2cl.benchmarks.octane.richards.Scheduler?, v1: kotlin.Int, v2: kotlin.Int) {
  this.scheduler = scheduler
  this.v1 = v1
  this.v2 = v2
 }

 open override fun run(packet: com.google.j2cl.benchmarks.octane.richards.Packet?): com.google.j2cl.benchmarks.octane.richards.TaskControlBlock? {
  if (packet === null) {
   return this.scheduler!!.suspendCurrent()
  } else {
   if (this.v1 === com.google.j2cl.benchmarks.octane.richards.Scheduler.ID_HANDLER_A) {
    this.v1 = com.google.j2cl.benchmarks.octane.richards.Scheduler.ID_HANDLER_B
   } else {
    this.v1 = com.google.j2cl.benchmarks.octane.richards.Scheduler.ID_HANDLER_A
   }
   packet!!.id = this.v1
   packet!!.a1 = 0
   var i: kotlin.Int = 0
   LOOP@ while (i < com.google.j2cl.benchmarks.octane.richards.Packet.DATA_SIZE) {
    LOOP_CONTINUE@ do {
     this.v2 = this.v2 + 1
     if (this.v2 > 26) {
      this.v2 = 1
     }
     packet!!.a2!![i] = this.v2
    } while (false)
    i = i + 1
   }
   return this.scheduler!!.queue(packet)
  }
 }

 open override fun toString(): kotlin.String {
  return "WorkerTask"
 }
}
