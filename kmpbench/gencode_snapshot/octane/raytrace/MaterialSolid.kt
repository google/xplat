// Generated from "com/google/j2cl/benchmarks/octane/raytrace/MaterialSolid.java"
package com.google.j2cl.benchmarks.octane.raytrace

import javaemul.lang.*
import kotlin.jvm.*

open class MaterialSolid: com.google.j2cl.benchmarks.octane.raytrace.BaseMaterial {
 @kotlin.jvm.JvmField val color: com.google.j2cl.benchmarks.octane.raytrace.Color?

 constructor(color: com.google.j2cl.benchmarks.octane.raytrace.Color?, reflection: kotlin.Double, transparency: kotlin.Double, gloss: kotlin.Double) {
  this.color = color
  this.reflection = reflection
  this.transparency = transparency
  this.gloss = gloss
  this.hasTexture = false
 }

 open override fun getColor(u: kotlin.Double, v: kotlin.Double): com.google.j2cl.benchmarks.octane.raytrace.Color? {
  return this.color
 }

 open override fun toString(): kotlin.String {
  return "SolidMaterial [gloss=" + this.gloss + ", transparency=" + this.transparency + ", hasTexture=" + this.hasTexture + "]"
 }
}
