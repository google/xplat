// Generated from "com/google/j2cl/benchmarks/octane/raytrace/Background.java"
package com.google.j2cl.benchmarks.octane.raytrace

import javaemul.lang.*
import kotlin.jvm.*

open class Background {
 @kotlin.jvm.JvmField val color: com.google.j2cl.benchmarks.octane.raytrace.Color?

 @kotlin.jvm.JvmField val ambience: kotlin.Double

 constructor(color: com.google.j2cl.benchmarks.octane.raytrace.Color?, ambience: kotlin.Double) {
  this.color = color
  this.ambience = ambience
 }

 open fun getColor(): com.google.j2cl.benchmarks.octane.raytrace.Color? {
  return this.color
 }

 open fun getAmbience(): kotlin.Double {
  return this.ambience
 }
}
