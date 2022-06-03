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
