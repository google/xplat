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

package com.google.j2cl.benchmarks;

import com.google.j2cl.benchmarking.framework.AbstractBenchmark;
import com.google.j2cl.benchmarks.octane.DeltaBlueBenchmark;
import com.google.j2cl.benchmarks.octane.NavierStokesBenchmark;
import com.google.j2cl.benchmarks.octane.RayTraceBenchmark;
import com.google.j2cl.benchmarks.octane.RichardsBenchmark;
import com.google.j2cl.benchmarks.simple.Fibonacci;
import java.util.LinkedHashMap;
import java.util.Map;

/** Provides a map of all benchmarks */
public final class AllBenchmarks {

  public static final Map<String, AbstractBenchmark> map = new LinkedHashMap<>();

  static {
    map.put("deltablue", new DeltaBlueBenchmark());
    map.put("fibonacci", new Fibonacci());
    map.put("navierstokes", new NavierStokesBenchmark());
    map.put("raytrace", new RayTraceBenchmark());
    map.put("richards", new RichardsBenchmark());
  }

  private AllBenchmarks() {}
}
