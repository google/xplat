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

import BenchmarkManager
import XCTest

class BenchmarkTest: XCTestCase {

  // Benchmark fibonacci Kotlin result: 0.030929525699216615; Java result: 0.12469562731280748
  // Kotlin performance must be at least expectedPerformance * javaPerformance to pass test.
  let expectedPerformance = 0.2
  let benchmarkManager = IosBenchmarkManagerKt.createIosBenchmarkManager()

  func testBenchmarks() {
    for benchmark in benchmarkManager.benchmarks {
      let name = benchmark.name

      let kotlinResult = benchmark.kotlinBenchmark().doubleValue
      let javaResult = benchmark.javaBenchmark().doubleValue
      let relative = kotlinResult / javaResult
      var ok = true

      if kotlinResult == BenchmarkManager.companion.UNAVAILABLE_RESULT
        || javaResult == BenchmarkManager.companion.UNAVAILABLE_RESULT
      {
        ok = false
      } else {
        ok = ok && relative >= expectedPerformance
      }

      let message =
        "Benchmark \(name) (ops/s) Kotlin: \(kotlinResult); Java: \(javaResult); Relative: \(relative)"
      print(message)
      XCTAssertTrue(ok, message)
    }
  }
}
