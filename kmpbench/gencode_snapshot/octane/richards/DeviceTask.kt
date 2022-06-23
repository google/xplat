// Generated from "com/google/j2cl/benchmarks/octane/richards/DeviceTask.java"
package com.google.j2cl.benchmarks.octane.richards

import javaemul.lang.*
import kotlin.jvm.*

open class DeviceTask: com.google.j2cl.benchmarks.octane.richards.Task {
 @kotlin.jvm.JvmField val scheduler: com.google.j2cl.benchmarks.octane.richards.Scheduler?

 @kotlin.jvm.JvmField var v1: com.google.j2cl.benchmarks.octane.richards.Packet? = null

 constructor(scheduler: com.google.j2cl.benchmarks.octane.richards.Scheduler?) {
  this.scheduler = scheduler
  this.v1 = null
 }

 open override fun run(packet: com.google.j2cl.benchmarks.octane.richards.Packet?): com.google.j2cl.benchmarks.octane.richards.TaskControlBlock? {
  if (packet === null) {
   if (this.v1 === null) {
    return this.scheduler!!.suspendCurrent()
   }
   val v: com.google.j2cl.benchmarks.octane.richards.Packet? = this.v1
   this.v1 = null
   return this.scheduler!!.queue(v)
  } else {
   this.v1 = packet
   return this.scheduler!!.holdCurrent()
  }
 }

 open override fun toString(): kotlin.String {
  return "DeviceTask"
 }
}
