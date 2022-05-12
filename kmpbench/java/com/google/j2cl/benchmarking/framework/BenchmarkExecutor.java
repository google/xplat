/*
 * Copyright 2014 Google Inc.
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
package com.google.j2cl.benchmarking.framework;

import static com.google.common.base.Strings.lenientFormat;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.util.logging.Logger;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/** The BenchmarkExecutor executes benchmarks and measures their performance. */
public final class BenchmarkExecutor {
  public static BenchmarkResult execute(AbstractBenchmark benchmark) {
    // Run the benchmark with 10 warm-up rounds of 1 second each and then with 20 measurement rounds
    // of 1 second each.
    return execute(benchmark, Clock.DEFAULT, 30, 10, 1000);
  }

  public static void prepareForRunOnce(AbstractBenchmark benchmark) {
    // Warm up the benchmark with 10 rounds.
    execute(benchmark, Clock.DEFAULT, 11, 10, 1000);

    // Prepare the benchmark for another run. Note that it was teared down after the execute.
    benchmark.setupOneTime();
    benchmark.setup();
  }

  /**
   * Runs a benchmark with a certain amount of rounds defined by <code>timePerIterations</code>.
   * Each round has a duration of <code>timePerIterations</code> milliseconds. The measurements will
   * be taken once a number of warmup rounds defined by <code>warmupIterations</code> is executed.
   */
  @VisibleForTesting
  static BenchmarkResult execute(
      AbstractBenchmark benchmark,
      Clock clock,
      int totalIterations,
      int warmupIterations,
      long timePerIterations) {
    Preconditions.checkArgument(totalIterations > warmupIterations);
    int measurementIterations = totalIterations - warmupIterations;

    log(lenientFormat("# Running %s", benchmark.getClass().getSimpleName()));
    log(lenientFormat("# Warmup: %s iterations, %s ms each", warmupIterations, timePerIterations));
    log(
        lenientFormat(
            "# Measurement: %s iterations, %s ms each", measurementIterations, timePerIterations));

    benchmark.setupOneTime();
    double[] throughputs = new double[measurementIterations];

    for (int iteration = 0; iteration < totalIterations; iteration++) {
      boolean isWarmup = iteration < warmupIterations;
      double iterationTime = 0;
      int runs = 0;
      Platform.forceGc();

      while (iterationTime < timePerIterations) {
        benchmark.setup();
        double before = clock.now();
        Object result = benchmark.run();
        double executionTime = clock.now() - before;
        benchmark.tearDown();

        iterationTime += executionTime;
        runs++;
        // We use the results to avoid V8 or the JVM to figure out whole benchmark doesn't have
        // side effects, consider it as death code and not even execute it.
        useResult(result);
      }

      double iterationThroughput = runs / iterationTime;

      if (!isWarmup) {
        throughputs[iteration - warmupIterations] = iterationThroughput;
      }
      log(
          lenientFormat(
              "%sIteration %s: %s ops/ms",
              (isWarmup ? "# Warmup " : ""),
              (isWarmup ? iteration : iteration - warmupIterations),
              iterationThroughput));
    }

    benchmark.tearDownOneTime();

    BenchmarkResult benchmarkResult = BenchmarkResult.from(throughputs);
    log(lenientFormat("Throughput: %s ops/ms", benchmarkResult.getAverageThroughput()));
    return benchmarkResult;
  }

  private static void log(String s) {
    Logger.getGlobal().info(s);
  }

  private static void useResult(Object o) {
    Global.__benchmarking_result = o;
  }

  @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "goog.global")
  private static class Global {
    public static Object __benchmarking_result;
  }

  private BenchmarkExecutor() {}
}
