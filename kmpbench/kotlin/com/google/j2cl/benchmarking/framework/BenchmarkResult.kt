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

// This is a Kotlin port of the corresponding J2cl Java class in
// google3/third_party/java_src/j2cl/benchmarking/java/com/google/j2cl/benchmarking/framework/

/** A benchmark result contains all information for an executed benchmark. */
data class BenchmarkResult
private constructor(
  private val throughputs: DoubleArray,
) {
  private val averageThroughput = throughputs.sum() / throughputs.size
  // Keeping explicit get methods here instead of exposing the property to match the Java API.

  /** Average number of benchmark run per millisecond. */
  fun getAverageThroughput() = averageThroughput

  /** Throughputs measured from all iterations. */
  fun getThroughputs() = throughputs

  companion object {
    fun from(throughputs: DoubleArray) = BenchmarkResult(throughputs)
  }
}
