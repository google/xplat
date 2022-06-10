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
import kotlin.test.Test
import kotlin.test.assertTrue
import third_party.java_src.xplat.kmpbench.com.google.apps.xplat.kmpbench.app.ios_PerfgateLogger.PerfgateLogger

class BenchmarkTest {

  // Benchmark fibonacci Kotlin result: 0.030929525699216615; Java result: 0.12469562731280748
  // Kotlin performance must be at least expectedPerformance * javaPerformance to pass test.
  val expectedPerformance = 0.2
  val benchmarkManager = createIosBenchmarkManager()
  val disabled = setOf<String>()
  val logger = PerfgateLogger()

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

      logger.logPerformance(name, kotlinResult, javaResult)

      val message =
        "Benchmark $name (ops/s) Kotlin: $kotlinResult; Java: $javaResult; Relative: $relative\n"
      println(message)
      results.append(message)
    }

    assertTrue(ok, results.toString())
  }
}
