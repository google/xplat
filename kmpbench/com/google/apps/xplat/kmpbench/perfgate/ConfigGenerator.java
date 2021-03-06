/*
 * Copyright 2021 Google Inc.
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
package com.google.apps.xplat.kmpbench.perfgate;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.common.collect.ImmutableList;
import com.google.j2cl.benchmarks.AllBenchmarks;
import com.google.testing.performance.perfgate.spec.proto.PerfgateProto.AggregateChartConfiguration;
import com.google.testing.performance.perfgate.spec.proto.PerfgateProto.BenchmarkInfo;
import com.google.testing.performance.perfgate.spec.proto.PerfgateProto.DashboardAggregateChartInput;
import com.google.testing.performance.perfgate.spec.proto.PerfgateProto.DashboardAggregateChartInput.StandardDeviationStyle;
import com.google.testing.performance.perfgate.spec.proto.PerfgateProto.DataFilter;
import com.google.testing.performance.perfgate.spec.proto.PerfgateProto.DataFilter.DataType;
import com.google.testing.performance.perfgate.spec.proto.PerfgateProto.RunOrder;
import com.google.testing.performance.perfgate.spec.proto.PerfgateProto.ValueInfo;
import java.io.IOException;

/**
 * Generate perfgate config file for J2Cl benchmarks.
 *
 * <p>To push any change of the config to perfgate, run:
 *
 * <pre>
 *   $ blaze build //third_party/java_src/xplat/kmpbench/com/google/apps/xplat/kmpbench/perfgate:benchmark_config
 *   $ /google/data/ro/teams/perfgate/perfgate update_benchmark \
 *       blaze-genfiles/third_party/java_src/xplat/kmpbench/com/google/apps/xplat/kmpbench/perfgate/benchmark.config
 * </pre>
 */
public final class ConfigGenerator {

  public static final String PERFGATE_BENCHMARK_ID = "6643097937903616";
  public static final ImmutableList<String> PLATFORMS = ImmutableList.of("j2objc", "kt");

  public static void main(String[] args) throws IOException {
    System.out.println("# This file is autogenerated, please do not edit it yourself.");
    System.out.println("# Please modify:\n");
    System.out.println(
        "# //third_party/java_src/xplat/kmpbench/"
            + "com/google/apps/xplat/kmpbench/perfgate/ConfigGenerator.java.\n\n");
    System.out.write(generatePerfgateConfig().toString().getBytes(UTF_8));
  }

  private static BenchmarkInfo generatePerfgateConfig() {
    return BenchmarkInfo.newBuilder()
        .setBenchmarkKey(PERFGATE_BENCHMARK_ID)
        .setBenchmarkName("kmpbench")
        .setProjectName("xplat")
        .addAllOwnerList(ImmutableList.of("haustein@google.com", "xplat@prod.google.com"))
        .setInputValueInfo(
            ValueInfo.newBuilder()
                .setValueKey("t")
                .setLabel("throughput")
                .setType(ValueInfo.Type.NUMERIC)
                .build())
        .addAllMetricInfoList(
            PLATFORMS.stream()
                .map(p -> ValueInfo.newBuilder().setValueKey(p).setLabel("throughput_" + p).build())
                .collect(toImmutableList()))
        .addAllAggregateChartConfigurationList(generateAggregateCharts())
        .setDefaultAggregateChartConfiguration("raytrace")
        .build();
  }

  private static ImmutableList<AggregateChartConfiguration> generateAggregateCharts() {
    return AllBenchmarks.map.keySet().stream()
        .map(ConfigGenerator::generateAggregateChart)
        .collect(toImmutableList());
  }

  private static AggregateChartConfiguration generateAggregateChart(String benchmarkName) {
    return AggregateChartConfiguration.newBuilder()
        .setName(benchmarkName)
        .setLabel(benchmarkName)
        .setChartInput(
            DashboardAggregateChartInput.newBuilder()
                .setRunOrder(RunOrder.BUILD_ID)
                .addAllTags(
                    ImmutableList.of(
                        "benchmark=" + benchmarkName,
                        "chamber_execution_mode=cbuild",
                        "chamber=execution"))
                .addAllValueSelections(getMetricSelections())
                .setStandardDeviationStyle(StandardDeviationStyle.ERROR_BANDS)
                .setMaxRuns(3000)
                .setHighlightSeriesOnHover(true)
                .setXSpread(true))
        .build();
  }

  private static ImmutableList<DataFilter> getMetricSelections() {
    ImmutableList.Builder<DataFilter> listBuilder = ImmutableList.builder();
    for (String platform : PLATFORMS) {
      listBuilder.add(
          DataFilter.newBuilder()
              .setValueKey(platform)
              .setDataType(DataType.METRIC_AGGREGATE_MEAN)
              .build());
      listBuilder.add(
          DataFilter.newBuilder()
              .setValueKey(platform)
              .setDataType(DataType.METRIC_AGGREGATE_STDDEV)
              .build());
    }
    return listBuilder.build();
  }

  private ConfigGenerator() {}
}
