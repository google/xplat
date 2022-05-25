package com.google.apps.xplat.kmpbench.app.ios

import com.google.apps.xplat.kmpbench.combination.BenchmarkManager
import third_party.java_src.j2objc.jre_emul_jre_core.JavaUtilMap_EntryProtocol
import third_party.java_src.xplat.kmpbench.java_benchmarks_j2objc.ComGoogleJ2clBenchmarkingFrameworkAbstractBenchmark
import third_party.java_src.xplat.kmpbench.java_benchmarks_j2objc.ComGoogleJ2clBenchmarkingFrameworkBenchmarkExecutor
import third_party.java_src.xplat.kmpbench.java_benchmarks_j2objc.ComGoogleJ2clBenchmarksAllBenchmarks_get_map

fun createIosBenchmarkManager(): BenchmarkManager {
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