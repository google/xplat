// Generated from "com/google/j2cl/benchmarks/octane/raytrace/Plane.java"
package com.google.j2cl.benchmarks.octane.raytrace

import javaemul.lang.*
import kotlin.jvm.*

open class Plane: com.google.j2cl.benchmarks.octane.raytrace.Shape {
 @kotlin.jvm.JvmField val position: com.google.j2cl.benchmarks.octane.raytrace.Vector?

 @kotlin.jvm.JvmField val d: kotlin.Double

 @kotlin.jvm.JvmField val material: com.google.j2cl.benchmarks.octane.raytrace.BaseMaterial?

 constructor(pos: com.google.j2cl.benchmarks.octane.raytrace.Vector?, d: kotlin.Double, material: com.google.j2cl.benchmarks.octane.raytrace.BaseMaterial?) {
  this.position = pos
  this.d = d
  this.material = material
 }

 open override fun intersect(ray: com.google.j2cl.benchmarks.octane.raytrace.Ray?): com.google.j2cl.benchmarks.octane.raytrace.IntersectionInfo? {
  val info: com.google.j2cl.benchmarks.octane.raytrace.IntersectionInfo? = com.google.j2cl.benchmarks.octane.raytrace.IntersectionInfo()
  val vd: kotlin.Double = this.position!!.dot(ray!!.getDirection())
  if (vd === 0.0) {
   return info
  }
  val t: kotlin.Double = - (this.position!!.dot(ray!!.getPosition()) + this.d) / vd
  if (t <= 0.0) {
   return info
  }
  info!!.setShape(this)
  info!!.isHit(true)
  info!!.setPosition(com.google.j2cl.benchmarks.octane.raytrace.Vector.add(ray!!.getPosition(), com.google.j2cl.benchmarks.octane.raytrace.Vector.multiplyScalar(ray!!.getDirection(), t)))
  info!!.setNormal(this.position)
  info!!.setDistance(t)
  if (this.material!!.hasTexture) {
   val vU: com.google.j2cl.benchmarks.octane.raytrace.Vector? = com.google.j2cl.benchmarks.octane.raytrace.Vector(this.position!!.getY(), this.position!!.getZ(), - this.position!!.getX())
   val vV: com.google.j2cl.benchmarks.octane.raytrace.Vector? = vU!!.cross(this.position)
   val u: kotlin.Double = info!!.getPosition()!!.dot(vU)
   val v: kotlin.Double = info!!.getPosition()!!.dot(vV)
   info!!.setColor(this.material!!.getColor(u, v))
  } else {
   info!!.setColor(this.material!!.getColor(0.0, 0.0))
  }
  return info
 }

 open override fun toString(): kotlin.String {
  return "Plane [" + this.position + ", d=" + this.d + "]"
 }

 open override fun getMaterial(): com.google.j2cl.benchmarks.octane.raytrace.BaseMaterial? {
  return this.material
 }

 open override fun getPosition(): com.google.j2cl.benchmarks.octane.raytrace.Vector? {
  return this.position
 }
}
