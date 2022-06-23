// Generated from "com/google/j2cl/benchmarks/octane/raytrace/IntersectionInfo.java"
package com.google.j2cl.benchmarks.octane.raytrace

import javaemul.lang.*
import kotlin.jvm.*

open class IntersectionInfo {
 @kotlin.jvm.JvmField var isHit: kotlin.Boolean = false

 @kotlin.jvm.JvmField var shape: com.google.j2cl.benchmarks.octane.raytrace.Shape? = null

 @kotlin.jvm.JvmField var position: com.google.j2cl.benchmarks.octane.raytrace.Vector? = null

 @kotlin.jvm.JvmField var normal: com.google.j2cl.benchmarks.octane.raytrace.Vector? = null

 @kotlin.jvm.JvmField var color: com.google.j2cl.benchmarks.octane.raytrace.Color? = null

 @kotlin.jvm.JvmField var distance: kotlin.Double = 0.0

 constructor() {
  this.color = com.google.j2cl.benchmarks.octane.raytrace.Color(0.0, 0.0, 0.0)
 }

 open fun isHit(b: kotlin.Boolean) {
  this.isHit = b
 }

 open fun isHit(): kotlin.Boolean {
  return this.isHit
 }

 open fun setDistance(distance: kotlin.Double) {
  this.distance = distance
 }

 open fun setShape(shape: com.google.j2cl.benchmarks.octane.raytrace.Shape?) {
  this.shape = shape
 }

 open fun getDistance(): kotlin.Double {
  return this.distance
 }

 open fun setPosition(position: com.google.j2cl.benchmarks.octane.raytrace.Vector?) {
  this.position = position
 }

 open fun getPosition(): com.google.j2cl.benchmarks.octane.raytrace.Vector? {
  return this.position
 }

 open fun setNormal(normal: com.google.j2cl.benchmarks.octane.raytrace.Vector?) {
  this.normal = normal
 }

 open fun setColor(color: com.google.j2cl.benchmarks.octane.raytrace.Color?) {
  this.color = color
 }

 open override fun toString(): kotlin.String {
  return "Intersection [" + this.position + "]"
 }

 open fun getColor(): com.google.j2cl.benchmarks.octane.raytrace.Color? {
  return this.color
 }

 open fun getShape(): com.google.j2cl.benchmarks.octane.raytrace.Shape? {
  return this.shape
 }

 open fun getNormal(): com.google.j2cl.benchmarks.octane.raytrace.Vector? {
  return this.normal
 }
}
