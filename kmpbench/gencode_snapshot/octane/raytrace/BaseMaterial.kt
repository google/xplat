// Generated from "com/google/j2cl/benchmarks/octane/raytrace/BaseMaterial.java"
package com.google.j2cl.benchmarks.octane.raytrace

import javaemul.lang.*
import kotlin.jvm.*

abstract class BaseMaterial {
 @kotlin.jvm.JvmField var gloss: kotlin.Double = 2.0

 @kotlin.jvm.JvmField var transparency: kotlin.Double = 0.0

 @kotlin.jvm.JvmField var reflection: kotlin.Double = 0.0

 @kotlin.jvm.JvmField var hasTexture: kotlin.Boolean = false

 abstract fun getColor(u: kotlin.Double, v: kotlin.Double): com.google.j2cl.benchmarks.octane.raytrace.Color?

 open fun wrapUp(t: kotlin.Double): kotlin.Double {
  var t_1: kotlin.Double = t
  t_1 = t_1 % 2.0
  if (t_1 < (- 1).toDouble()) {
   t_1 = t_1 + 2.0
  } else if (t_1 >= 1.0) {
   t_1 = t_1 - 2.0
  }
  return t_1
 }

 open override fun toString(): kotlin.String {
  return "Material [gloss=" + this.gloss + ", transparency=" + this.transparency + ", hasTexture=" + this.hasTexture + "]"
 }
}
