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

package com.google.apps.xplat.kmpbench.combination

import com.google.j2cl.benchmarking.framework.BenchmarkExecutor
import com.google.j2cl.benchmarks.AllBenchmarks

/** Manages a list of paired java / kotlin benchmarks */
class BenchmarkManager(javaBenchmarks: Map<String, () -> Double>) {

  val benchmarks: List<CombinedBenchmark>

  init {
    val allKeys = AllBenchmarks.map.keys + javaBenchmarks.keys
    val result = mutableListOf<CombinedBenchmark>()

    for (key in allKeys) {
      val rawKotlinBenchmark = AllBenchmarks.map.get(key)
      val kotlinBenchmark =
        if (rawKotlinBenchmark == null) UNAVAILABLE_BENCHMARK
        else {
          { BenchmarkExecutor.execute(rawKotlinBenchmark).getAverageThroughput() }
        }
      val javaBenchmark = javaBenchmarks[key] ?: UNAVAILABLE_BENCHMARK
      result.add(CombinedBenchmark(key, kotlinBenchmark, javaBenchmark))
    }
    benchmarks = result.toList()
  }

  companion object {
    const val UNAVAILABLE_RESULT = -1.0
    val UNAVAILABLE_BENCHMARK: () -> Double = { UNAVAILABLE_RESULT }
  }
}
