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

/** A clock abstraction that is used in benchmarks */
fun interface Clock {

  /**
   * The current time in millisecons. Valid for comparisons between now values, there are no
   * guarantees about the base of these values.
   */
  fun now(): Double

  companion object {
    val DEFAULT = Clock { System.nanoTime() / 1000000.0 }
  }
}
