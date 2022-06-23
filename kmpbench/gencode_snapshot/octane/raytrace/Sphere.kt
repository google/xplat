// Generated from "com/google/j2cl/benchmarks/octane/raytrace/Sphere.java"
package com.google.j2cl.benchmarks.octane.raytrace

import javaemul.lang.*
import kotlin.jvm.*

open class Sphere: com.google.j2cl.benchmarks.octane.raytrace.Shape {
 @kotlin.jvm.JvmField val radius: kotlin.Double

 @kotlin.jvm.JvmField val position: com.google.j2cl.benchmarks.octane.raytrace.Vector?

 @kotlin.jvm.JvmField val material: com.google.j2cl.benchmarks.octane.raytrace.BaseMaterial?

 constructor(pos: com.google.j2cl.benchmarks.octane.raytrace.Vector?, radius: kotlin.Double, material: com.google.j2cl.benchmarks.octane.raytrace.BaseMaterial?) {
  this.radius = radius
  this.position = pos
  this.material = material
 }

 open override fun intersect(ray: com.google.j2cl.benchmarks.octane.raytrace.Ray?): com.google.j2cl.benchmarks.octane.raytrace.IntersectionInfo? {
  val info: com.google.j2cl.benchmarks.octane.raytrace.IntersectionInfo? = com.google.j2cl.benchmarks.octane.raytrace.IntersectionInfo()
  info!!.setShape(this)
  val dst: com.google.j2cl.benchmarks.octane.raytrace.Vector? = com.google.j2cl.benchmarks.octane.raytrace.Vector.subtract(ray!!.getPosition(), this.position)
  val b: kotlin.Double = dst!!.dot(ray!!.getDirection())
  val c: kotlin.Double = dst!!.dot(dst) - this.radius * this.radius
  val d: kotlin.Double = b * b - c
  if (d > 0.0) {
   info!!.isHit(true)
   info!!.setDistance(- b - java.lang.Math.sqrt(d))
   info!!.setPosition(com.google.j2cl.benchmarks.octane.raytrace.Vector.add(ray!!.getPosition(), com.google.j2cl.benchmarks.octane.raytrace.Vector.multiplyScalar(ray!!.getDirection(), info!!.getDistance())))
   info!!.setNormal(com.google.j2cl.benchmarks.octane.raytrace.Vector.subtract(info!!.getPosition(), this.position)!!.normalize())
   info!!.setColor(this.material!!.getColor(0.0, 0.0))
  } else {
   info!!.isHit(false)
  }
  return info
 }

 open override fun getMaterial(): com.google.j2cl.benchmarks.octane.raytrace.BaseMaterial? {
  return this.material
 }

 open override fun getPosition(): com.google.j2cl.benchmarks.octane.raytrace.Vector? {
  return this.position
 }
}
