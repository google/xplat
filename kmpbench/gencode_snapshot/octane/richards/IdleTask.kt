// Generated from "com/google/j2cl/benchmarks/octane/richards/IdleTask.java"
package com.google.j2cl.benchmarks.octane.richards

import javaemul.lang.*
import kotlin.jvm.*

open class IdleTask: com.google.j2cl.benchmarks.octane.richards.Task {
 @kotlin.jvm.JvmField val scheduler: com.google.j2cl.benchmarks.octane.richards.Scheduler?

 @kotlin.jvm.JvmField var v1: kotlin.Int = 0

 @kotlin.jvm.JvmField var count: kotlin.Int = 0

 constructor(scheduler: com.google.j2cl.benchmarks.octane.richards.Scheduler?, v1: kotlin.Int, count: kotlin.Int) {
  this.scheduler = scheduler
  this.v1 = v1
  this.count = count
 }

 open override fun run(packet: com.google.j2cl.benchmarks.octane.richards.Packet?): com.google.j2cl.benchmarks.octane.richards.TaskControlBlock? {
  this.count = this.count - 1
  if (this.count === 0) {
   return this.scheduler!!.holdCurrent()
  }
  if (this.v1.and(1) === 0) {
   this.v1 = this.v1.shr(1)
   return this.scheduler!!.release(com.google.j2cl.benchmarks.octane.richards.Scheduler.ID_DEVICE_A)
  } else {
   this.v1 = this.v1.shr(1).xor(53256)
   return this.scheduler!!.release(com.google.j2cl.benchmarks.octane.richards.Scheduler.ID_DEVICE_B)
  }
 }

 open override fun toString(): kotlin.String {
  return "IdleTask"
 }
}
