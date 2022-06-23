// Generated from "com/google/j2cl/benchmarks/octane/richards/HandlerTask.java"
package com.google.j2cl.benchmarks.octane.richards

import javaemul.lang.*
import kotlin.jvm.*

open class HandlerTask: com.google.j2cl.benchmarks.octane.richards.Task {
 @kotlin.jvm.JvmField val scheduler: com.google.j2cl.benchmarks.octane.richards.Scheduler?

 @kotlin.jvm.JvmField var v1: com.google.j2cl.benchmarks.octane.richards.Packet? = null

 @kotlin.jvm.JvmField var v2: com.google.j2cl.benchmarks.octane.richards.Packet? = null

 constructor(scheduler: com.google.j2cl.benchmarks.octane.richards.Scheduler?) {
  this.scheduler = scheduler
  this.v1 = null
  this.v2 = null
 }

 open override fun run(packet: com.google.j2cl.benchmarks.octane.richards.Packet?): com.google.j2cl.benchmarks.octane.richards.TaskControlBlock? {
  if (packet !== null) {
   if (packet!!.kind === com.google.j2cl.benchmarks.octane.richards.Scheduler.KIND_WORK) {
    this.v1 = packet!!.addTo(this.v1)
   } else {
    this.v2 = packet!!.addTo(this.v2)
   }
  }
  if (this.v1 !== null) {
   val count: kotlin.Int = this.v1!!.a1
   val v: com.google.j2cl.benchmarks.octane.richards.Packet?
   if (count < com.google.j2cl.benchmarks.octane.richards.Packet.DATA_SIZE) {
    if (this.v2 !== null) {
     v = this.v2
     this.v2 = this.v2!!.link
     v!!.a1 = this.v1!!.a2!![count]
     this.v1!!.a1 = count + 1
     return this.scheduler!!.queue(v)
    }
   } else {
    v = this.v1
    this.v1 = this.v1!!.link
    return this.scheduler!!.queue(v)
   }
  }
  return this.scheduler!!.suspendCurrent()
 }

 open override fun toString(): kotlin.String {
  return "HandlerTask"
 }
}
