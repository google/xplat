package com.google.apps.xplat.kmpbench.app.ios

import com.google.apps.xplat.kmpbench.combination.BenchmarkManager
import kotlin.test.Test
import kotlin.test.assertTrue

class BenchmarkTest {

  // Benchmark fibonacci Kotlin result: 0.030929525699216615; Java result: 0.12469562731280748
  // Kotlin performance must be at least expectedPerformance * javaPerformance to pass test.
  val expectedPerformance = 0.2
  val benchmarkManager = createIosBenchmarkManager()
  val disabled = setOf("raytrace") // Crashes before Kotlin 1.6

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

      if (kotlinResult == BenchmarkManager.UNAVAILABLE_RESULT ||
          javaResult == BenchmarkManager.UNAVAILABLE_RESULT
      ) {
        ok = false
      } else {
        ok = ok && kotlinResult > javaResult * expectedPerformance
      }

      val message = "Benchmark $name Kotlin result: $kotlinResult; Java result: $javaResult\n"
      println(message)
      results.append(message)
    }

    assertTrue(ok, results.toString())
  }
}
