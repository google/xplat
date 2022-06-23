// Generated from "com/google/j2cl/benchmarks/octane/raytrace/Ray.java"
package com.google.j2cl.benchmarks.octane.raytrace

import javaemul.lang.*
import kotlin.jvm.*

open class Ray {
 @kotlin.jvm.JvmField val position: com.google.j2cl.benchmarks.octane.raytrace.Vector?

 @kotlin.jvm.JvmField val direction: com.google.j2cl.benchmarks.octane.raytrace.Vector?

 constructor(pos: com.google.j2cl.benchmarks.octane.raytrace.Vector?, dir: com.google.j2cl.benchmarks.octane.raytrace.Vector?) {
  this.position = pos
  this.direction = dir
 }

 open override fun toString(): kotlin.String {
  return "Ray [" + this.position + "," + this.direction + "]"
 }

 open fun getPosition(): com.google.j2cl.benchmarks.octane.raytrace.Vector? {
  return this.position
 }

 open fun getDirection(): com.google.j2cl.benchmarks.octane.raytrace.Vector? {
  return this.direction
 }
}
