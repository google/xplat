package com.google.j2cl.benchmarks

import com.google.j2cl.benchmarks.octane.RayTraceBenchmark
import com.google.j2cl.benchmarks.simple.Fibonacci

object AllBenchmarks {
  val map = mapOf("fibonacci" to Fibonacci(), "raytrace" to RayTraceBenchmark())
}
