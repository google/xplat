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

package com.google.j2cl.benchmarking.framework

import com.google.common.annotations.VisibleForTesting
import com.google.common.base.Strings.lenientFormat
import java.util.logging.Logger

// This is a Kotlin port of the corresponding J2cl Java class in
// google3/third_party/java_src/j2cl/benchmarking/java/com/google/j2cl/benchmarking/framework/

/** The BenchmarkExecutor executes benchmarks and measures their performance. */
object BenchmarkExecutor {
  fun execute(benchmark: AbstractBenchmark): BenchmarkResult {
    // Run the benchmark with 10 warm-up rounds of 1 second each and then with 20 measurement rounds
    // of 1 second each.
    return execute(benchmark, Clock.DEFAULT, 30, 10, 1000)
  }

  fun prepareForRunOnce(benchmark: AbstractBenchmark) {
    // Warm up the benchmark with 10 rounds.
    execute(benchmark, Clock.DEFAULT, 11, 10, 1000)

    // Prepare the benchmark for another run. Note that it was teared down after the execute.
    benchmark.setupOneTime()
    benchmark.setup()
  }

  /**
   * Runs a benchmark with a certain amount of rounds defined by `timePerIterations`. Each round has
   * a duration of `timePerIterations` milliseconds. The measurements will be taken once a number of
   * warmup rounds defined by `warmupIterations` is executed.
   */
  @VisibleForTesting
  fun execute(
    benchmark: AbstractBenchmark,
    clock: Clock,
    totalIterations: Int,
    warmupIterations: Int,
    timePerIterations: Long,
  ): BenchmarkResult {
    require(totalIterations > warmupIterations)
    val measurementIterations = totalIterations - warmupIterations
    log(lenientFormat("# Running %s", benchmark.javaClass.simpleName))
    log(lenientFormat("# Warmup: %s iterations, %s ms each", warmupIterations, timePerIterations))
    log(
      lenientFormat(
        "# Measurement: %s iterations, %s ms each",
        measurementIterations,
        timePerIterations
      )
    )
    benchmark.setupOneTime()
    val throughputs = DoubleArray(measurementIterations)
    for (iteration in 0 until totalIterations) {
      val isWarmup = iteration < warmupIterations
      var iterationTime = 0.0
      var runs = 0
      Platform.forceGc()
      while (iterationTime < timePerIterations) {
        benchmark.setup()
        val before: Double = clock.now()
        val result = benchmark.run()
        val executionTime: Double = clock.now() - before
        benchmark.tearDown()
        iterationTime += executionTime
        runs++
        // We use the results to avoid V8 or the JVM to figure out whole benchmark doesn't have
        // side effects, consider it as death code and not even execute it.
        useResult(result)
      }
      val iterationThroughput = runs / iterationTime
      if (!isWarmup) {
        throughputs[iteration - warmupIterations] = iterationThroughput
      }
      log(
        lenientFormat(
          "%sIteration %s: %s ops/ms",
          if (isWarmup) "# Warmup " else "",
          if (isWarmup) iteration else iteration - warmupIterations,
          iterationThroughput
        )
      )
    }
    benchmark.tearDownOneTime()
    val benchmarkResult: BenchmarkResult = BenchmarkResult.from(throughputs)
    log(lenientFormat("Throughput: %s ops/ms", benchmarkResult.getAverageThroughput()))
    return benchmarkResult
  }

  private fun log(s: String) {
    Logger.getGlobal().info(s)
  }

  private fun useResult(o: Any) {
    Global.__benchmarking_result = o
  }

  private object Global {
    var __benchmarking_result: Any? = null
  }
}
