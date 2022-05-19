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

import kotlin.system.measureNanoTime

// This is a Kotlin port of the corresponding J2cl Java class in
// google3/third_party/java_src/j2cl/benchmarking/java/com/google/j2cl/benchmarking/framework/

/** The BenchmarkExecutor executes benchmarks and measures their performance. */
object BenchmarkExecutor {
  fun execute(benchmark: AbstractBenchmark): BenchmarkResult {
    // Run the benchmark with 10 warm-up rounds of 1 second each and then with 20 measurement rounds
    // of 1 second each.
    return execute(benchmark, 30, 10, 1000)
  }

  fun prepareForRunOnce(benchmark: AbstractBenchmark) {
    // Warm up the benchmark with 10 rounds.
    execute(benchmark, 11, 10, 1000)

    // Prepare the benchmark for another run. Note that it was teared down after the execute.
    benchmark.setupOneTime()
    benchmark.setup()
  }

  /**
   * Runs a benchmark with a certain amount of rounds defined by `timePerIterations`. Each round has
   * a duration of `timePerIterations` milliseconds. The measurements will be taken once a number of
   * warmup rounds defined by `warmupIterations` is executed.
   */
  fun execute(
    benchmark: AbstractBenchmark,
    totalIterations: Int,
    warmupIterations: Int,
    timePerIterations: Long,
  ): BenchmarkResult {
    require(totalIterations > warmupIterations)
    val measurementIterations = totalIterations - warmupIterations
    log("# Running ${benchmark::class.simpleName}")
    log("# Warmup: $warmupIterations iterations, $timePerIterations ms each")
    log("# Measurement: $measurementIterations iterations, $timePerIterations ms each")
    benchmark.setupOneTime()
    val throughputs = DoubleArray(measurementIterations)
    for (iteration in 0 until totalIterations) {
      val isWarmup = iteration < warmupIterations
      var iterationTime = 0.0
      var runs = 0
      Platform.forceGc()
      while (iterationTime < timePerIterations) {
        benchmark.setup()
        var result: Any? = null
        val executionTime =
          measureNanoTime {
            // We use the results to avoid V8 or the JVM to figure out whole benchmark doesn't have
            // side effects, consider it as death code and not even execute it.
            result = benchmark.run()
          } / 1000000.0
        benchmark.tearDown()
        iterationTime += executionTime
        runs++
        useResult(result)
      }
      val iterationThroughput = runs / iterationTime
      if (!isWarmup) {
        throughputs[iteration - warmupIterations] = iterationThroughput
      }
      val number = if (isWarmup) iteration else iteration - warmupIterations
      log("${if (isWarmup) "# Warmup " else ""}Iteration $number: $iterationThroughput ops/ms")
    }
    benchmark.tearDownOneTime()
    val benchmarkResult: BenchmarkResult = BenchmarkResult.from(throughputs)
    log("Throughput: ${benchmarkResult.getAverageThroughput()} ops/ms")
    return benchmarkResult
  }

  private fun log(s: String) {
    println(s)
  }

  // TODO(b/230841155): For native, this causes a warning that the variable can't be changed after
  //   initialization.
  private fun useResult(o: Any?) {
    Global.__benchmarking_result = o
  }

  private object Global {
    init {
      CollectionUtilizer.dependOnAllCollections()
    }
    var __benchmarking_result: Any? = null
  }
}
