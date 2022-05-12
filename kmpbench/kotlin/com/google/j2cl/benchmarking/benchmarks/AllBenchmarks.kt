package com.google.j2cl.benchmarking.benchmarks

import com.google.j2cl.benchmarking.benchmarks.simple.Fibonacci

object AllBenchmarks {
  val map = mapOf("Fibonacci" to Fibonacci())
}
