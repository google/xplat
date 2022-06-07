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

package com.google.j2cl.benchmarks.simple;

import com.google.j2cl.benchmarking.framework.AbstractBenchmark;

/** Trivial sample benchmark implemented in Java. */
public class Fibonacci extends AbstractBenchmark {

  static final int TEST_VALUE = 32;

  /**
   * Calculates the n-th number in the fibonacci sequence in an intentionally inefficient way,
   * providing a trivial case for the benchmark framework.
   */
  static int fib(int n) {
    switch (n) {
      case 0:
        return 0;
      case 1:
        return 1;
      default:
        if (n < 0) {
          throw new IllegalArgumentException();
        }
        return fib(n - 1) + fib(n - 2);
    }
  }

  @Override
  public Object run() {
    return fib(TEST_VALUE);
  }
}
