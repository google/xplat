// Generated from "com/google/j2cl/benchmarks/octane/RichardsBenchmark.java"
package com.google.j2cl.benchmarks.octane

import javaemul.lang.*
import kotlin.jvm.*

open class RichardsBenchmark: com.google.j2cl.benchmarking.framework.AbstractBenchmark() {
 @kotlin.jvm.JvmField var richards: com.google.j2cl.benchmarks.octane.richards.Richards? = null

 open override fun setupOneTime() {
  this.richards = com.google.j2cl.benchmarks.octane.richards.Richards()
 }

 open override fun run(): kotlin.Any? {
  this.richards!!.runRichards()
  return null
 }

 open override fun tearDownOneTime() {
  this.richards = null
 }
}
