package com.google.apps.xplat.kmpbench.app.ios

import com.google.apps.xplat.kmpbench.combination.BenchmarkManager
import kotlin.test.Test
import kotlin.test.assertTrue

class BenchmarkTest {

  // Benchmark fibonacci Kotlin result: 0.030929525699216615; Java result: 0.12469562731280748
  // Kotlin performance must be at least expectedPerformance * javaPerformance to pass test.
  val expectedPerformance = 0.2
  val benchmarkManager = createIosBenchmarkManager()
  val disabled = setOf<String>()

  @Test
  fun testBenchmarks() {
    var ok = true
    val results = StringBuilder()

    for (benchmark in benchmarkManager.benchmarks) {
      val name = benchmark.name
      if (disabled.contains(name)) {
        continue
      }
      val kotlinResult = benchmark.kotlinBenchmark()
      val javaResult = benchmark.javaBenchmark()
      val relative = kotlinResult / javaResult

      if (kotlinResult == BenchmarkManager.UNAVAILABLE_RESULT ||
          javaResult == BenchmarkManager.UNAVAILABLE_RESULT
      ) {
        ok = false
      } else {
        ok = ok && relative >= expectedPerformance
      }

      val message = 
          "Benchmark $name (ops/s) Kotlin: $kotlinResult; Java: $javaResult; Relative: $relative\n"
      println(message)
      results.append(message)
    }

    assertTrue(ok, results.toString())
  }
}
