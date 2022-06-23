// Generated from "com/google/j2cl/benchmarks/octane/richards/Packet.java"
package com.google.j2cl.benchmarks.octane.richards

import javaemul.lang.*
import kotlin.jvm.*

open class Packet {
 @kotlin.jvm.JvmField var link: com.google.j2cl.benchmarks.octane.richards.Packet? = null

 @kotlin.jvm.JvmField var id: kotlin.Int = 0

 @kotlin.jvm.JvmField var a1: kotlin.Int = 0

 @kotlin.jvm.JvmField var a2: kotlin.IntArray? = null

 @kotlin.jvm.JvmField var kind: kotlin.Int = 0

 constructor(link: com.google.j2cl.benchmarks.octane.richards.Packet?, id: kotlin.Int, kind: kotlin.Int) {
  this.link = link
  this.id = id
  this.kind = kind
  this.a1 = 0
  this.a2 = kotlin.IntArray(com.google.j2cl.benchmarks.octane.richards.Packet.DATA_SIZE)
 }

 open fun addTo(queue: com.google.j2cl.benchmarks.octane.richards.Packet?): com.google.j2cl.benchmarks.octane.richards.Packet? {
  this.link = null
  if (queue === null) {
   return this
  }
  var peek: com.google.j2cl.benchmarks.octane.richards.Packet?
  var next: com.google.j2cl.benchmarks.octane.richards.Packet? = queue
  LOOP@ while (kotlin.run {
   peek = next!!.link
   peek
  } !== null) {
   next = peek
  }
  next!!.link = this
  return queue
 }

 open override fun toString(): kotlin.String {
  return "Packet"
 }

 companion object {
  @kotlin.jvm.JvmField val DATA_SIZE: kotlin.Int = 4
 }
}
