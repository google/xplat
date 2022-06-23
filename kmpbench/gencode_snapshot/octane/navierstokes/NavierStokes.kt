// Generated from "com/google/j2cl/benchmarks/octane/navierstokes/NavierStokes.java"
package com.google.j2cl.benchmarks.octane.navierstokes

import javaemul.lang.*
import kotlin.jvm.*

open class NavierStokes: com.google.j2cl.benchmarks.octane.navierstokes.FluidField.UICallback, com.google.j2cl.benchmarks.octane.navierstokes.FluidField.DisplayFunction {
 @kotlin.jvm.JvmField var solver: com.google.j2cl.benchmarks.octane.navierstokes.FluidField? = null

 @kotlin.jvm.JvmField var nsFrameCounter: kotlin.Int = 0

 @kotlin.jvm.JvmField var framesTillAddingPoints: kotlin.Int = 0

 @kotlin.jvm.JvmField var framesBetweenAddingPoints: kotlin.Int = 0

 open fun runNavierStokes() {
  this.solver!!.update()
  this.nsFrameCounter = this.nsFrameCounter + 1
  if (this.nsFrameCounter === 15) {
   this.checkResult(this.solver!!.getDens())
  }
 }

 open fun checkResult(dens: kotlin.DoubleArray?) {
  var result: kotlin.Int = 0
  var i: kotlin.Int = 7000
  LOOP@ while (i < 7100) {
   LOOP_CONTINUE@ do {
    result = result + (dens!![i] * 10.0).toInt()
   } while (false)
   i = i + 1
  }
  if (result !== 77) {
   throw kotlin.AssertionError("checksum failed " + result)
  }
 }

 open fun setup() {
  this.nsFrameCounter = 0
  this.framesTillAddingPoints = 0
  this.framesBetweenAddingPoints = 5
  this.solver = com.google.j2cl.benchmarks.octane.navierstokes.FluidField()
  this.solver!!.setResolution(128, 128)
  this.solver!!.setIterations(20)
  this.solver!!.setUICallback(this)
  this.solver!!.setDisplayFunction(this)
  this.solver!!.reset()
 }

 open fun tearDown() {
  this.solver = null
 }

 open fun addPoints(field: com.google.j2cl.benchmarks.octane.navierstokes.Field?) {
  val n: kotlin.Int = 64
  var i: kotlin.Int = 1
  LOOP@ while (i <= n) {
   LOOP_CONTINUE@ do {
    field!!.setVelocity(i, i, n.toDouble(), n.toDouble())
    field!!.setDensity(i, i, 5.0)
    field!!.setVelocity(i, n - i, (- n).toDouble(), (- n).toDouble())
    field!!.setDensity(i, n - i, 20.0)
    field!!.setVelocity(128 - i, n + i, (- n).toDouble(), (- n).toDouble())
    field!!.setDensity(128 - i, n + i, 30.0)
   } while (false)
   i = i + 1
  }
 }

 open override fun prepareFrame(field: com.google.j2cl.benchmarks.octane.navierstokes.Field?) {
  if (this.framesTillAddingPoints === 0) {
   this.addPoints(field)
   this.framesTillAddingPoints = this.framesBetweenAddingPoints
   this.framesBetweenAddingPoints = this.framesBetweenAddingPoints + 1
  } else {
   this.framesTillAddingPoints = this.framesTillAddingPoints - 1
  }
 }

 open override fun call(f: com.google.j2cl.benchmarks.octane.navierstokes.Field?) {}
}
