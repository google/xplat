package com.google.j2cl.benchmarks;

import com.google.j2cl.benchmarking.framework.AbstractBenchmark;
import com.google.j2cl.benchmarks.octane.RayTraceBenchmark;
import com.google.j2cl.benchmarks.simple.Fibonacci;
import java.util.LinkedHashMap;
import java.util.Map;

/** Provides a map of all benchmarks */
public final class AllBenchmarks {

  public static final Map<String, AbstractBenchmark> map = new LinkedHashMap<>();

  static {
    map.put("fibonacci", new Fibonacci());
    map.put("raytrace", new RayTraceBenchmark());
  }

  private AllBenchmarks() {}
}
