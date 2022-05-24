package com.google.apps.xplat.kmpbench.app.ios

import com.google.apps.xplat.kmpbench.combination.BenchmarkManager
import kotlin.test.Test
import kotlin.test.assertTrue
import third_party.java_src.j2objc.jre_emul_jre_core.JavaUtilMap_EntryProtocol
import third_party.java_src.xplat.kmpbench.java_benchmarks_j2objc.ComGoogleJ2clBenchmarkingFrameworkAbstractBenchmark
import third_party.java_src.xplat.kmpbench.java_benchmarks_j2objc.ComGoogleJ2clBenchmarkingFrameworkBenchmarkExecutor
import third_party.java_src.xplat.kmpbench.java_benchmarks_j2objc.ComGoogleJ2clBenchmarksAllBenchmarks_get_map

class BenchmarkTest {

  // Benchmark fibonacci Kotlin result: 0.030929525699216615; Java result: 0.12469562731280748
  // Kotlin performance must be at least expectedPerformance * javaPerformance to pass test.
  val expectedPerformance = 0.2
  val benchmarkManager = buildIosBenchmarkManager()
  val disabled = setOf("raytrace") // Crashes before Kotlin 1.6

  fun buildIosBenchmarkManager(): BenchmarkManager {
    val javaMap = mutableMapOf<String, () -> Double>()
    val iterator = ComGoogleJ2clBenchmarksAllBenchmarks_get_map()!!.entrySet().iterator()
    while (iterator.hasNext()) {
      val entry = iterator.next() as JavaUtilMap_EntryProtocol
      val javaBenchmark = entry.getValue() as ComGoogleJ2clBenchmarkingFrameworkAbstractBenchmark
      javaMap[entry.getKey() as String] = {
        ComGoogleJ2clBenchmarkingFrameworkBenchmarkExecutor
          .executeWithComGoogleJ2clBenchmarkingFrameworkAbstractBenchmark(javaBenchmark)!!
          .getAverageThroughput()
      }
    }
    return BenchmarkManager(javaMap)
  }

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
