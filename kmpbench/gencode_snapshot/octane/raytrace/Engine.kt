// Generated from "com/google/j2cl/benchmarks/octane/raytrace/Engine.java"
package com.google.j2cl.benchmarks.octane.raytrace

import javaemul.lang.*
import kotlin.jvm.*

open class Engine {
 @kotlin.jvm.JvmField val options: com.google.j2cl.benchmarks.octane.raytrace.Engine.Options?

 constructor(options: com.google.j2cl.benchmarks.octane.raytrace.Engine.Options?) {
  this.options = options
  this.options!!.canvasHeight = this.options!!.canvasHeight / this.options!!.pixelHeight
  this.options!!.canvasWidth = this.options!!.canvasWidth / this.options!!.pixelWidth
 }

 open fun setPixel(x: kotlin.Double, y: kotlin.Double, color: com.google.j2cl.benchmarks.octane.raytrace.Color?) {
  if (x === y) {
   com.google.j2cl.benchmarks.octane.raytrace.RayTrace.checkNumber = (com.google.j2cl.benchmarks.octane.raytrace.RayTrace.checkNumber.toDouble() + color!!.brightness()).toInt()
  }
 }

 open fun renderScene(scene: com.google.j2cl.benchmarks.octane.raytrace.Scene?) {
  val canvasHeight: kotlin.Double = this.options!!.canvasHeight.toDouble()
  val canvasWidth: kotlin.Double = this.options!!.canvasWidth.toDouble()
  var y: kotlin.Int = 0
  LOOP@ while (y.toDouble() < canvasHeight) {
   LOOP_CONTINUE@ do {
    var x: kotlin.Int = 0
    LOOP_1@ while (x.toDouble() < canvasWidth) {
     LOOP_CONTINUE_1@ do {
      val yp: kotlin.Double = y.toDouble() * 1.0 / canvasHeight * 2.0 - 1.0
      val xp: kotlin.Double = x.toDouble() * 1.0 / canvasWidth * 2.0 - 1.0
      val ray: com.google.j2cl.benchmarks.octane.raytrace.Ray? = scene!!.getCamera()!!.getRay(xp, yp)
      val color: com.google.j2cl.benchmarks.octane.raytrace.Color? = this.getPixelColor(ray, scene)
      this.setPixel(x.toDouble(), y.toDouble(), color)
     } while (false)
     x = x + 1
    }
   } while (false)
   y = y + 1
  }
 }

 open fun getPixelColor(ray: com.google.j2cl.benchmarks.octane.raytrace.Ray?, scene: com.google.j2cl.benchmarks.octane.raytrace.Scene?): com.google.j2cl.benchmarks.octane.raytrace.Color? {
  val info: com.google.j2cl.benchmarks.octane.raytrace.IntersectionInfo? = this.testIntersection(ray, scene, null)
  if (info!!.isHit()) {
   return this.rayTrace(info, ray, scene, 0.0)
  }
  return scene!!.getBackground()!!.getColor()
 }

 open fun testIntersection(ray: com.google.j2cl.benchmarks.octane.raytrace.Ray?, scene: com.google.j2cl.benchmarks.octane.raytrace.Scene?, exclude: com.google.j2cl.benchmarks.octane.raytrace.Shape?): com.google.j2cl.benchmarks.octane.raytrace.IntersectionInfo? {
  var best: com.google.j2cl.benchmarks.octane.raytrace.IntersectionInfo? = com.google.j2cl.benchmarks.octane.raytrace.IntersectionInfo()
  best!!.setDistance(2000.0)
  var i: kotlin.Int = 0
  LOOP@ while (i < scene!!.getShapes()!!.size) {
   LOOP_CONTINUE@ do {
    val shape: com.google.j2cl.benchmarks.octane.raytrace.Shape? = scene!!.getShape(i)
    if (shape !== exclude) {
     val info: com.google.j2cl.benchmarks.octane.raytrace.IntersectionInfo? = shape!!.intersect(ray)
     if (info!!.isHit() && info!!.getDistance() >= 0.0 && info!!.getDistance() < best!!.getDistance()) {
      best = info
     }
    }
   } while (false)
   i = i + 1
  }
  return best
 }

 open fun getReflectionRay(p: com.google.j2cl.benchmarks.octane.raytrace.Vector?, n: com.google.j2cl.benchmarks.octane.raytrace.Vector?, v: com.google.j2cl.benchmarks.octane.raytrace.Vector?): com.google.j2cl.benchmarks.octane.raytrace.Ray? {
  val c1: kotlin.Double = - n!!.dot(v)
  val r1: com.google.j2cl.benchmarks.octane.raytrace.Vector? = com.google.j2cl.benchmarks.octane.raytrace.Vector.add(com.google.j2cl.benchmarks.octane.raytrace.Vector.multiplyScalar(n, 2.0 * c1), v)
  return com.google.j2cl.benchmarks.octane.raytrace.Ray(p, r1)
 }

 open fun rayTrace(info: com.google.j2cl.benchmarks.octane.raytrace.IntersectionInfo?, ray: com.google.j2cl.benchmarks.octane.raytrace.Ray?, scene: com.google.j2cl.benchmarks.octane.raytrace.Scene?, depth: kotlin.Double): com.google.j2cl.benchmarks.octane.raytrace.Color? {
  var color: com.google.j2cl.benchmarks.octane.raytrace.Color? = com.google.j2cl.benchmarks.octane.raytrace.Color.multiplyScalar(info!!.getColor(), scene!!.getBackground()!!.getAmbience())
  val oldColor: com.google.j2cl.benchmarks.octane.raytrace.Color? = color
  val shininess: kotlin.Double = java.lang.Math.pow(10.0, info!!.getShape()!!.getMaterial()!!.gloss + 1.0)
  var i: kotlin.Int = 0
  LOOP@ while (i < scene!!.getLights()!!.size) {
   LOOP_CONTINUE@ do {
    val light: com.google.j2cl.benchmarks.octane.raytrace.Light? = scene!!.getLight(i)
    val v: com.google.j2cl.benchmarks.octane.raytrace.Vector? = com.google.j2cl.benchmarks.octane.raytrace.Vector.subtract(light!!.getPosition(), info!!.getPosition())!!.normalize()
    if (this.options!!.renderDiffuse) {
     val l: kotlin.Double = v!!.dot(info!!.getNormal())
     if (l > 0.0) {
      color = com.google.j2cl.benchmarks.octane.raytrace.Color.add(color, com.google.j2cl.benchmarks.octane.raytrace.Color.multiply(info!!.getColor(), com.google.j2cl.benchmarks.octane.raytrace.Color.multiplyScalar(light!!.getColor(), l)))
     }
    }
    if (depth <= this.options!!.rayDepth.toDouble()) {
     if (this.options!!.renderReflections && info!!.getShape()!!.getMaterial()!!.reflection > 0.0) {
      val reflectionRay: com.google.j2cl.benchmarks.octane.raytrace.Ray? = this.getReflectionRay(info!!.getPosition(), info!!.getNormal(), ray!!.getDirection())
      val refl: com.google.j2cl.benchmarks.octane.raytrace.IntersectionInfo? = this.testIntersection(reflectionRay, scene, info!!.getShape())
      if (refl!!.isHit() && refl!!.getDistance() > 0.0) {
       refl!!.setColor(this.rayTrace(refl, reflectionRay, scene, depth + 1.0))
      } else {
       refl!!.setColor(scene!!.getBackground()!!.getColor())
      }
      color = com.google.j2cl.benchmarks.octane.raytrace.Color.blend(color, refl!!.getColor(), info!!.getShape()!!.getMaterial()!!.reflection)
     }
    }
    var shadowInfo: com.google.j2cl.benchmarks.octane.raytrace.IntersectionInfo? = com.google.j2cl.benchmarks.octane.raytrace.IntersectionInfo()
    if (this.options!!.renderShadows) {
     val shadowRay: com.google.j2cl.benchmarks.octane.raytrace.Ray? = com.google.j2cl.benchmarks.octane.raytrace.Ray(info!!.getPosition(), v)
     shadowInfo = this.testIntersection(shadowRay, scene, info!!.getShape())
     if (shadowInfo!!.isHit() && shadowInfo!!.getShape() !== info!!.getShape()) {
      val vA: com.google.j2cl.benchmarks.octane.raytrace.Color? = com.google.j2cl.benchmarks.octane.raytrace.Color.multiplyScalar(color, 0.5)
      val dB: kotlin.Double = 0.5 * java.lang.Math.sqrt(shadowInfo!!.getShape()!!.getMaterial()!!.transparency)
      color = com.google.j2cl.benchmarks.octane.raytrace.Color.addScalar(vA, dB)
     }
    }
    if (this.options!!.renderHighlights && !shadowInfo!!.isHit() && info!!.getShape()!!.getMaterial()!!.gloss > 0.0) {
     val lv: com.google.j2cl.benchmarks.octane.raytrace.Vector? = com.google.j2cl.benchmarks.octane.raytrace.Vector.subtract(info!!.getShape()!!.getPosition(), light!!.getPosition())!!.normalize()
     val e: com.google.j2cl.benchmarks.octane.raytrace.Vector? = com.google.j2cl.benchmarks.octane.raytrace.Vector.subtract(scene!!.getCamera()!!.getPosition(), info!!.getShape()!!.getPosition())!!.normalize()
     val h: com.google.j2cl.benchmarks.octane.raytrace.Vector? = com.google.j2cl.benchmarks.octane.raytrace.Vector.subtract(e, lv)!!.normalize()
     val glossWeight: kotlin.Double = java.lang.Math.pow(java.lang.Math.max(info!!.getNormal()!!.dot(h), 0.0), shininess)
     color = com.google.j2cl.benchmarks.octane.raytrace.Color.add(com.google.j2cl.benchmarks.octane.raytrace.Color.multiplyScalar(light!!.getColor(), glossWeight), color)
    }
   } while (false)
   i = i + 1
  }
  color!!.limit()
  return color
 }

 open class Options {
  @kotlin.jvm.JvmField var canvasHeight: kotlin.Int = 100

  @kotlin.jvm.JvmField var canvasWidth: kotlin.Int = 100

  @kotlin.jvm.JvmField var pixelWidth: kotlin.Int = 2

  @kotlin.jvm.JvmField var pixelHeight: kotlin.Int = 2

  @kotlin.jvm.JvmField var renderDiffuse: kotlin.Boolean = false

  @kotlin.jvm.JvmField var renderShadows: kotlin.Boolean = false

  @kotlin.jvm.JvmField var renderHighlights: kotlin.Boolean = false

  @kotlin.jvm.JvmField var renderReflections: kotlin.Boolean = false

  @kotlin.jvm.JvmField var rayDepth: kotlin.Int = 2
 }
}
