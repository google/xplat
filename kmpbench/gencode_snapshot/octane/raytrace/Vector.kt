// Generated from "com/google/j2cl/benchmarks/octane/raytrace/Vector.java"
package com.google.j2cl.benchmarks.octane.raytrace

import javaemul.lang.*
import kotlin.jvm.*

open class Vector {
 @kotlin.jvm.JvmField val x: kotlin.Double

 @kotlin.jvm.JvmField val y: kotlin.Double

 @kotlin.jvm.JvmField val z: kotlin.Double

 constructor(x: kotlin.Double, y: kotlin.Double, z: kotlin.Double) {
  this.x = x
  this.y = y
  this.z = z
 }

 open fun normalize(): com.google.j2cl.benchmarks.octane.raytrace.Vector? {
  val m: kotlin.Double = this.magnitude()
  return com.google.j2cl.benchmarks.octane.raytrace.Vector(this.x / m, this.y / m, this.z / m)
 }

 open fun magnitude(): kotlin.Double {
  return java.lang.Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z)
 }

 open fun cross(w: com.google.j2cl.benchmarks.octane.raytrace.Vector?): com.google.j2cl.benchmarks.octane.raytrace.Vector? {
  return com.google.j2cl.benchmarks.octane.raytrace.Vector(- this.z * w!!.y + this.y * w!!.z, this.z * w!!.x - this.x * w!!.z, - this.y * w!!.x + this.x * w!!.y)
 }

 open fun dot(w: com.google.j2cl.benchmarks.octane.raytrace.Vector?): kotlin.Double {
  return this.x * w!!.x + this.y * w!!.y + this.z * w!!.z
 }

 open override fun toString(): kotlin.String {
  return "Vector [" + this.x + "," + this.y + "," + this.z + "]"
 }

 open fun getX(): kotlin.Double {
  return this.x
 }

 open fun getY(): kotlin.Double {
  return this.y
 }

 open fun getZ(): kotlin.Double {
  return this.z
 }

 companion object {
  @kotlin.jvm.JvmStatic
  fun add(v: com.google.j2cl.benchmarks.octane.raytrace.Vector?, w: com.google.j2cl.benchmarks.octane.raytrace.Vector?): com.google.j2cl.benchmarks.octane.raytrace.Vector? {
   return com.google.j2cl.benchmarks.octane.raytrace.Vector(w!!.x + v!!.x, w!!.y + v!!.y, w!!.z + v!!.z)
  }

  @kotlin.jvm.JvmStatic
  fun subtract(v: com.google.j2cl.benchmarks.octane.raytrace.Vector?, w: com.google.j2cl.benchmarks.octane.raytrace.Vector?): com.google.j2cl.benchmarks.octane.raytrace.Vector? {
   return com.google.j2cl.benchmarks.octane.raytrace.Vector(v!!.x - w!!.x, v!!.y - w!!.y, v!!.z - w!!.z)
  }

  @kotlin.jvm.JvmStatic
  fun multiplyScalar(v: com.google.j2cl.benchmarks.octane.raytrace.Vector?, w: kotlin.Double): com.google.j2cl.benchmarks.octane.raytrace.Vector? {
   return com.google.j2cl.benchmarks.octane.raytrace.Vector(v!!.x * w, v!!.y * w, v!!.z * w)
  }
 }
}
