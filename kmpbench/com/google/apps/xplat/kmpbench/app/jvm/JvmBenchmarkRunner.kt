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
package com.google.apps.xplat.kmpbench.app.jvm

import com.google.apps.xplat.kmpbench.combination.BenchmarkManager
import com.google.apps.xplat.kmpbench.java.benchmarking.framework.BenchmarkExecutor
import com.google.apps.xplat.kmpbench.java.benchmarks.AllBenchmarks

fun main() {
  val javaBenchmarks =
    AllBenchmarks.map.mapValues { entry ->
      { BenchmarkExecutor.execute(entry.value!!).averageThroughput }
    }
  val manager = BenchmarkManager(javaBenchmarks)

  for (benchmark in manager.benchmarks) {
    val name = benchmark.name
    val kotlinResult = benchmark.kotlinBenchmark()
    val javaResult = benchmark.javaBenchmark()
    println("Benchmark $name Kotlin result: $kotlinResult; Java result: $javaResult")
  }
}
