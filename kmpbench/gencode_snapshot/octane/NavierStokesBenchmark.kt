// Generated from "com/google/j2cl/benchmarks/octane/NavierStokesBenchmark.java"
package com.google.j2cl.benchmarks.octane

import javaemul.lang.*
import kotlin.jvm.*

open class NavierStokesBenchmark: com.google.j2cl.benchmarking.framework.AbstractBenchmark() {
 @kotlin.jvm.JvmField var navierStokes: com.google.j2cl.benchmarks.octane.navierstokes.NavierStokes? = null

 open override fun run(): kotlin.Any? {
  this.navierStokes!!.runNavierStokes()
  return null
 }

 open override fun setupOneTime() {
  this.navierStokes = com.google.j2cl.benchmarks.octane.navierstokes.NavierStokes()
  this.navierStokes!!.setup()
 }

 open override fun tearDownOneTime() {
  this.navierStokes!!.tearDown()
 }
}
