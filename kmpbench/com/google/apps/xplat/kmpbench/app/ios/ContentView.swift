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
import SwiftUI

/// Root view for Demo App.
public struct ContentView: View {
  let benchmarks = IosBenchmarkManagerKt.createIosBenchmarkManager().benchmarks
  let resultWidth = 50.0

  @State var javaResults: [Double]
  @State var kotlinResults: [Double]
  @State var currentBenchmark = -1
  @State var lastBenchmark = -1

  public init() {
    _javaResults = State(initialValue: [Double](repeating: -1.0, count: benchmarks.count))
    _kotlinResults = State(initialValue: [Double](repeating: -1.0, count: benchmarks.count))
  }

  public var body: some View {
    List {
      HStack {
        Button("Run all") {
          Task {
            await runBenchmarks(from: 0, to: benchmarks.count - 1)
          }
        }.disabled(currentBenchmark != -1)
        Spacer()
        Text("J").frame(width: resultWidth, alignment: .trailing)
        Text("Kt").frame(width: resultWidth, alignment: .trailing)
        Text("Kt/J").frame(width: resultWidth, alignment: .trailing)
      }
      ForEach(benchmarks.indices) { i in
        HStack {
          Button(benchmarks[i].name) {
            Task {
              await runBenchmarks(from: i, to: i)
            }
          }.disabled(currentBenchmark != -1)
          Spacer()
          Text(format(value: javaResults[i])).frame(width: resultWidth, alignment: .trailing)
          Text(format(value: kotlinResults[i])).frame(width: resultWidth, alignment: .trailing)
          if currentBenchmark == i {
            ProgressView().frame(width: resultWidth, alignment: .trailing)
          } else {
            Text(format(value: kotlinResults[i] / javaResults[i])).frame(
              width: resultWidth, alignment: .trailing)
          }
        }
      }
    }
  }

  func format(value: Double) -> String {
    return value <= 0.0 ? "n/a" : String(format: "%.2f", value)
  }

  @MainActor
  func runBenchmarks(from: Int, to: Int) async {
    currentBenchmark = from
    while currentBenchmark <= to {
      let index = currentBenchmark
      let benchmark = benchmarks[index]

      try? await Task.sleep(nanoseconds: 500_000_000)
      javaResults[index] = run(benchmark: benchmark, kotlin: false)

      try? await Task.sleep(nanoseconds: 500_000_000)
      kotlinResults[index] = run(benchmark: benchmark, kotlin: true)

      currentBenchmark += 1
    }
    currentBenchmark = -1
  }

  // Ideally, this would be actually async -- but the app crashes when the benchmarks are not
  // run on the main thread.
  func run(benchmark: CombinedBenchmark, kotlin: Bool) -> Double {
    let result = kotlin ? benchmark.kotlinBenchmark() : benchmark.javaBenchmark()
    return Double(truncating: result)
  }
}
