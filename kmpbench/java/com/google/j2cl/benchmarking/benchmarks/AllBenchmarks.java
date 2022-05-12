package com.google.j2cl.benchmarking.benchmarks;

import com.google.j2cl.benchmarking.benchmarks.simple.Fibonacci;
import com.google.j2cl.benchmarking.framework.AbstractBenchmark;
import java.util.LinkedHashMap;
import java.util.Map;

/** Provides a map of all benchmarks */
public final class AllBenchmarks {

  public static final Map<String, AbstractBenchmark> map = new LinkedHashMap<>();

  static {
    map.put("Fibonacci", new Fibonacci());
  }

  private AllBenchmarks() {}
}
