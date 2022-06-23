// Generated from "com/google/j2cl/benchmarks/octane/raytrace/Color.java"
package com.google.j2cl.benchmarks.octane.raytrace

import javaemul.lang.*
import kotlin.jvm.*

open class Color {
 @kotlin.jvm.JvmField var red: kotlin.Double = 0.0

 @kotlin.jvm.JvmField var green: kotlin.Double = 0.0

 @kotlin.jvm.JvmField var blue: kotlin.Double = 0.0

 constructor(r: kotlin.Double, g: kotlin.Double, b: kotlin.Double) {
  this.red = r
  this.green = g
  this.blue = b
 }

 open fun limit() {
  this.red = if (this.red > 0.0) java.lang.Math.min(this.red, 1.0) else 0.0
  this.green = if (this.green > 0.0) java.lang.Math.min(this.green, 1.0) else 0.0
  this.blue = if (this.blue > 0.0) java.lang.Math.min(this.blue, 1.0) else 0.0
 }

 open fun brightness(): kotlin.Double {
  val r: kotlin.Int = java.lang.Math.floor(this.red * 255.0).toInt()
  val g: kotlin.Int = java.lang.Math.floor(this.green * 255.0).toInt()
  val b: kotlin.Int = java.lang.Math.floor(this.blue * 255.0).toInt()
  return (r * 77 + g * 150 + b * 29).shr(8).toDouble()
 }

 open override fun toString(): kotlin.String {
  val r: kotlin.Double = java.lang.Math.floor(this.red * 255.0)
  val g: kotlin.Double = java.lang.Math.floor(this.green * 255.0)
  val b: kotlin.Double = java.lang.Math.floor(this.blue * 255.0)
  return "rgb(" + r + "," + g + "," + b + ")"
 }

 companion object {
  @kotlin.jvm.JvmStatic
  fun add(c1: com.google.j2cl.benchmarks.octane.raytrace.Color?, c2: com.google.j2cl.benchmarks.octane.raytrace.Color?): com.google.j2cl.benchmarks.octane.raytrace.Color? {
   val result: com.google.j2cl.benchmarks.octane.raytrace.Color? = com.google.j2cl.benchmarks.octane.raytrace.Color(0.0, 0.0, 0.0)
   result!!.red = c1!!.red + c2!!.red
   result!!.green = c1!!.green + c2!!.green
   result!!.blue = c1!!.blue + c2!!.blue
   return result
  }

  @kotlin.jvm.JvmStatic
  fun addScalar(c1: com.google.j2cl.benchmarks.octane.raytrace.Color?, s: kotlin.Double): com.google.j2cl.benchmarks.octane.raytrace.Color? {
   val result: com.google.j2cl.benchmarks.octane.raytrace.Color? = com.google.j2cl.benchmarks.octane.raytrace.Color(0.0, 0.0, 0.0)
   result!!.red = c1!!.red + s
   result!!.green = c1!!.green + s
   result!!.blue = c1!!.blue + s
   result!!.limit()
   return result
  }

  @kotlin.jvm.JvmStatic
  fun multiply(c1: com.google.j2cl.benchmarks.octane.raytrace.Color?, c2: com.google.j2cl.benchmarks.octane.raytrace.Color?): com.google.j2cl.benchmarks.octane.raytrace.Color? {
   val result: com.google.j2cl.benchmarks.octane.raytrace.Color? = com.google.j2cl.benchmarks.octane.raytrace.Color(0.0, 0.0, 0.0)
   result!!.red = c1!!.red * c2!!.red
   result!!.green = c1!!.green * c2!!.green
   result!!.blue = c1!!.blue * c2!!.blue
   return result
  }

  @kotlin.jvm.JvmStatic
  fun multiplyScalar(c1: com.google.j2cl.benchmarks.octane.raytrace.Color?, f: kotlin.Double): com.google.j2cl.benchmarks.octane.raytrace.Color? {
   val result: com.google.j2cl.benchmarks.octane.raytrace.Color? = com.google.j2cl.benchmarks.octane.raytrace.Color(0.0, 0.0, 0.0)
   result!!.red = c1!!.red * f
   result!!.green = c1!!.green * f
   result!!.blue = c1!!.blue * f
   return result
  }

  @kotlin.jvm.JvmStatic
  fun blend(c1: com.google.j2cl.benchmarks.octane.raytrace.Color?, c2: com.google.j2cl.benchmarks.octane.raytrace.Color?, w: kotlin.Double): com.google.j2cl.benchmarks.octane.raytrace.Color? {
   return com.google.j2cl.benchmarks.octane.raytrace.Color.add(com.google.j2cl.benchmarks.octane.raytrace.Color.multiplyScalar(c1, 1.0 - w), com.google.j2cl.benchmarks.octane.raytrace.Color.multiplyScalar(c2, w))
  }
 }
}
