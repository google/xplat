// Generated from "com/google/j2cl/benchmarks/octane/raytrace/RayTrace.java"
package com.google.j2cl.benchmarks.octane.raytrace

import javaemul.lang.*
import kotlin.jvm.*

class RayTrace {
 constructor()

 companion object {
  @kotlin.jvm.JvmField var checkNumber: kotlin.Int = 0

  @kotlin.jvm.JvmStatic
  fun renderScene() {
   val scene: com.google.j2cl.benchmarks.octane.raytrace.Scene? = com.google.j2cl.benchmarks.octane.raytrace.Scene()
   scene!!.setCamera(com.google.j2cl.benchmarks.octane.raytrace.Camera(com.google.j2cl.benchmarks.octane.raytrace.Vector(0.0, 0.0, (- 15).toDouble()), com.google.j2cl.benchmarks.octane.raytrace.Vector(- 0.2, 0.0, 5.0), com.google.j2cl.benchmarks.octane.raytrace.Vector(0.0, 1.0, 0.0)))
   scene!!.setBackground(com.google.j2cl.benchmarks.octane.raytrace.Background(com.google.j2cl.benchmarks.octane.raytrace.Color(0.5, 0.5, 0.5), 0.4))
   val sphere: com.google.j2cl.benchmarks.octane.raytrace.Sphere? = com.google.j2cl.benchmarks.octane.raytrace.Sphere(com.google.j2cl.benchmarks.octane.raytrace.Vector(- 1.5, 1.5, 2.0), 1.5, com.google.j2cl.benchmarks.octane.raytrace.MaterialSolid(com.google.j2cl.benchmarks.octane.raytrace.Color(0.0, 0.5, 0.5), 0.3, 0.0, 2.0))
   val sphere1: com.google.j2cl.benchmarks.octane.raytrace.Sphere? = com.google.j2cl.benchmarks.octane.raytrace.Sphere(com.google.j2cl.benchmarks.octane.raytrace.Vector(1.0, 0.25, 1.0), 0.5, com.google.j2cl.benchmarks.octane.raytrace.MaterialSolid(com.google.j2cl.benchmarks.octane.raytrace.Color(0.9, 0.9, 0.9), 0.1, 0.0, 1.5))
   val plane: com.google.j2cl.benchmarks.octane.raytrace.Plane? = com.google.j2cl.benchmarks.octane.raytrace.Plane(com.google.j2cl.benchmarks.octane.raytrace.Vector(0.1, 0.9, - 0.5).normalize(), 1.2, com.google.j2cl.benchmarks.octane.raytrace.Chessboard(com.google.j2cl.benchmarks.octane.raytrace.Color(1.0, 1.0, 1.0), com.google.j2cl.benchmarks.octane.raytrace.Color(0.0, 0.0, 0.0), 0.2, 0.0, 1.0, 0.7))
   scene!!.setShapes(plane, sphere, sphere1)
   val light: com.google.j2cl.benchmarks.octane.raytrace.Light? = com.google.j2cl.benchmarks.octane.raytrace.Light(com.google.j2cl.benchmarks.octane.raytrace.Vector(5.0, 10.0, (- 1).toDouble()), com.google.j2cl.benchmarks.octane.raytrace.Color(0.8, 0.8, 0.8))
   val light1: com.google.j2cl.benchmarks.octane.raytrace.Light? = com.google.j2cl.benchmarks.octane.raytrace.Light(com.google.j2cl.benchmarks.octane.raytrace.Vector((- 3).toDouble(), 5.0, (- 15).toDouble()), com.google.j2cl.benchmarks.octane.raytrace.Color(0.8, 0.8, 0.8))
   scene!!.setLights(light, light1)
   val options: com.google.j2cl.benchmarks.octane.raytrace.Engine.Options? = com.google.j2cl.benchmarks.octane.raytrace.Engine.Options()
   options!!.canvasWidth = 100
   options!!.canvasHeight = 100
   options!!.pixelWidth = 5
   options!!.pixelHeight = 5
   options!!.renderDiffuse = true
   options!!.renderShadows = true
   options!!.renderHighlights = true
   options!!.renderReflections = true
   options!!.rayDepth = 2
   com.google.j2cl.benchmarks.octane.raytrace.RayTrace.checkNumber = 0
   com.google.j2cl.benchmarks.octane.raytrace.Engine(options).renderScene(scene)
   if (com.google.j2cl.benchmarks.octane.raytrace.RayTrace.checkNumber !== 2321) {
    throw kotlin.AssertionError("Scene rendered incorrectly")
   }
  }
 }
}
