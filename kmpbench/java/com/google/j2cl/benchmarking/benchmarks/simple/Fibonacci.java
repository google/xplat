package com.google.j2cl.benchmarking.benchmarks.simple;

import com.google.j2cl.benchmarking.framework.AbstractBenchmark;

/** Trivial sample benchmark implemented in Java. */
public class Fibonacci extends AbstractBenchmark {

  static final int TEST_VALUE = 32;

  /**
   * Calculates the n-th number in the fibonacci sequence in an intentionally inefficient way,
   * providing a trivial case for the benchmark framework.
   */
  static int fib(int n) {
    if (n < 0) {
      throw new IllegalArgumentException();
    }
    if (n <= 1) {
      return n;
    }
    return fib(n - 1) + fib(n - 2);
  }

  @Override
  public Object run() {
    return fib(TEST_VALUE);
  }
}
