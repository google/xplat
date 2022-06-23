// Generated from "com/google/j2cl/benchmarks/octane/RayTraceBenchmark.java"
package com.google.j2cl.benchmarks.octane

import javaemul.lang.*
import kotlin.jvm.*

open class RayTraceBenchmark: com.google.j2cl.benchmarking.framework.AbstractBenchmark() {
 open override fun run(): kotlin.Any? {
  com.google.j2cl.benchmarks.octane.raytrace.RayTrace.renderScene()
  return null
 }
}
