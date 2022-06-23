// Generated from "com/google/j2cl/benchmarks/octane/raytrace/Scene.java"
package com.google.j2cl.benchmarks.octane.raytrace

import javaemul.lang.*
import kotlin.jvm.*

open class Scene {
 @kotlin.jvm.JvmField var camera: com.google.j2cl.benchmarks.octane.raytrace.Camera? = null

 @kotlin.jvm.JvmField var shapes: kotlin.Array<com.google.j2cl.benchmarks.octane.raytrace.Shape?>? = null

 @kotlin.jvm.JvmField var lights: kotlin.Array<com.google.j2cl.benchmarks.octane.raytrace.Light?>? = null

 @kotlin.jvm.JvmField var background: com.google.j2cl.benchmarks.octane.raytrace.Background? = null

 constructor() {
  this.camera = com.google.j2cl.benchmarks.octane.raytrace.Camera(com.google.j2cl.benchmarks.octane.raytrace.Vector(0.0, 0.0, (- 5).toDouble()), com.google.j2cl.benchmarks.octane.raytrace.Vector(0.0, 0.0, 1.0), com.google.j2cl.benchmarks.octane.raytrace.Vector(0.0, 1.0, 0.0))
  this.shapes = kotlin.arrayOfNulls<com.google.j2cl.benchmarks.octane.raytrace.Shape>(0)
  this.lights = kotlin.arrayOfNulls<com.google.j2cl.benchmarks.octane.raytrace.Light>(0)
  this.background = com.google.j2cl.benchmarks.octane.raytrace.Background(com.google.j2cl.benchmarks.octane.raytrace.Color(0.0, 0.0, 0.5), 0.2)
 }

 open fun getCamera(): com.google.j2cl.benchmarks.octane.raytrace.Camera? {
  return this.camera
 }

 open fun getBackground(): com.google.j2cl.benchmarks.octane.raytrace.Background? {
  return this.background
 }

 open fun getShapes(): kotlin.Array<com.google.j2cl.benchmarks.octane.raytrace.Shape?>? {
  return this.shapes
 }

 open fun getShape(i: kotlin.Int): com.google.j2cl.benchmarks.octane.raytrace.Shape? {
  return this.shapes!![i]
 }

 open fun getLights(): kotlin.Array<com.google.j2cl.benchmarks.octane.raytrace.Light?>? {
  return this.lights
 }

 open fun getLight(i: kotlin.Int): com.google.j2cl.benchmarks.octane.raytrace.Light? {
  return this.lights!![i]
 }

 open fun setCamera(camera: com.google.j2cl.benchmarks.octane.raytrace.Camera?) {
  this.camera = camera
 }

 open fun setBackground(background: com.google.j2cl.benchmarks.octane.raytrace.Background?) {
  this.background = background
 }

 open fun setShapes(vararg shapes: com.google.j2cl.benchmarks.octane.raytrace.Shape?) {
  this.shapes = shapes as kotlin.Array<com.google.j2cl.benchmarks.octane.raytrace.Shape?>?
 }

 open fun setLights(vararg lights: com.google.j2cl.benchmarks.octane.raytrace.Light?) {
  this.lights = lights as kotlin.Array<com.google.j2cl.benchmarks.octane.raytrace.Light?>?
 }
}
