// Generated from "com/google/j2cl/benchmarks/octane/raytrace/Chessboard.java"
package com.google.j2cl.benchmarks.octane.raytrace

import javaemul.lang.*
import kotlin.jvm.*

open class Chessboard: com.google.j2cl.benchmarks.octane.raytrace.BaseMaterial {
 @kotlin.jvm.JvmField val density: kotlin.Double

 @kotlin.jvm.JvmField val colorOdd: com.google.j2cl.benchmarks.octane.raytrace.Color?

 @kotlin.jvm.JvmField val colorEven: com.google.j2cl.benchmarks.octane.raytrace.Color?

 constructor(colorEven: com.google.j2cl.benchmarks.octane.raytrace.Color?, colorOdd: com.google.j2cl.benchmarks.octane.raytrace.Color?, reflection: kotlin.Double, transparency: kotlin.Double, gloss: kotlin.Double, density: kotlin.Double) {
  this.colorEven = colorEven
  this.colorOdd = colorOdd
  this.reflection = reflection
  this.transparency = transparency
  this.gloss = gloss
  this.density = density
  this.hasTexture = true
 }

 open override fun getColor(u: kotlin.Double, v: kotlin.Double): com.google.j2cl.benchmarks.octane.raytrace.Color? {
  val t: kotlin.Double = this.wrapUp(u * this.density) * this.wrapUp(v * this.density)
  if (t < 0.0) {
   return this.colorEven
  }
  return this.colorOdd
 }

 open override fun toString(): kotlin.String {
  return "ChessMaterial [gloss=" + this.gloss + ", transparency=" + this.transparency + ", hasTexture=" + this.hasTexture + "]"
 }
}
