/*
 * Copyright 2022 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

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
