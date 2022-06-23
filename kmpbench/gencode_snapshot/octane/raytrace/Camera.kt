// Generated from "com/google/j2cl/benchmarks/octane/raytrace/Camera.java"
package com.google.j2cl.benchmarks.octane.raytrace

import javaemul.lang.*
import kotlin.jvm.*

open class Camera {
 @kotlin.jvm.JvmField val position: com.google.j2cl.benchmarks.octane.raytrace.Vector?

 @kotlin.jvm.JvmField val up: com.google.j2cl.benchmarks.octane.raytrace.Vector?

 @kotlin.jvm.JvmField val equator: com.google.j2cl.benchmarks.octane.raytrace.Vector?

 @kotlin.jvm.JvmField val screen: com.google.j2cl.benchmarks.octane.raytrace.Vector?

 constructor(pos: com.google.j2cl.benchmarks.octane.raytrace.Vector?, lookAt: com.google.j2cl.benchmarks.octane.raytrace.Vector?, up: com.google.j2cl.benchmarks.octane.raytrace.Vector?) {
  this.position = pos
  this.up = up
  this.equator = lookAt!!.normalize()!!.cross(up)
  this.screen = com.google.j2cl.benchmarks.octane.raytrace.Vector.add(this.position, lookAt)
 }

 open fun getRay(vx: kotlin.Double, vy: kotlin.Double): com.google.j2cl.benchmarks.octane.raytrace.Ray? {
  var pos: com.google.j2cl.benchmarks.octane.raytrace.Vector? = com.google.j2cl.benchmarks.octane.raytrace.Vector.subtract(this.screen, com.google.j2cl.benchmarks.octane.raytrace.Vector.subtract(com.google.j2cl.benchmarks.octane.raytrace.Vector.multiplyScalar(this.equator, vx), com.google.j2cl.benchmarks.octane.raytrace.Vector.multiplyScalar(this.up, vy)))
  pos = com.google.j2cl.benchmarks.octane.raytrace.Vector(pos!!.getX(), - pos!!.getY(), pos!!.getZ())
  val dir: com.google.j2cl.benchmarks.octane.raytrace.Vector? = com.google.j2cl.benchmarks.octane.raytrace.Vector.subtract(pos, this.position)
  return com.google.j2cl.benchmarks.octane.raytrace.Ray(pos, dir!!.normalize())
 }

 open override fun toString(): kotlin.String {
  return "Ray []"
 }

 open fun getPosition(): com.google.j2cl.benchmarks.octane.raytrace.Vector? {
  return this.position
 }
}
