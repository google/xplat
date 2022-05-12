package com.google.j2cl.benchmarking.benchmarks.simple

import com.google.j2cl.benchmarking.framework.AbstractBenchmark

/** Trivial sample benchmark implemented in Kotlin. */
class Fibonacci() : AbstractBenchmark() {

  override fun run(): Int = fib(TEST_VALUE)

  companion object {
    const val TEST_VALUE = 32

    /**
     * Calculates the n-th number in the fibonacci sequence in an intentionally inefficient way,
     * providing a trivial case for the benchmark framework.
     */
    fun fib(n: Int): Int =
      when (n) {
        0 -> 0
        1 -> 1
        else -> {
          require(n >= 0)
          fib(n - 1) + fib(n - 2)
        }
      }
  }
}
