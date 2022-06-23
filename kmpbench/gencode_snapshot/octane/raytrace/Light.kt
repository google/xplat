// Generated from "com/google/j2cl/benchmarks/octane/raytrace/Light.java"
package com.google.j2cl.benchmarks.octane.raytrace

import javaemul.lang.*
import kotlin.jvm.*

open class Light {
 @kotlin.jvm.JvmField val position: com.google.j2cl.benchmarks.octane.raytrace.Vector?

 @kotlin.jvm.JvmField val color: com.google.j2cl.benchmarks.octane.raytrace.Color?

 constructor(pos: com.google.j2cl.benchmarks.octane.raytrace.Vector?, color: com.google.j2cl.benchmarks.octane.raytrace.Color?) {
  this.position = pos
  this.color = color
 }

 open override fun toString(): kotlin.String {
  return "Light [" + this.position!!.getX() + "," + this.position!!.getY() + "," + this.position!!.getZ() + "]"
 }

 open fun getPosition(): com.google.j2cl.benchmarks.octane.raytrace.Vector? {
  return this.position
 }

 open fun getColor(): com.google.j2cl.benchmarks.octane.raytrace.Color? {
  return this.color
 }
}
