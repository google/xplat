load("//tools/build_defs/kotlin:rules.bzl", "kt_jvm_library")

package(
    default_visibility = ["//third_party/java_src/xplat/kmpbench:__subpackages__"],
    licenses = ["notice"],
)

kt_jvm_library(
    name = "benchmark_runner_library_jvm",
    srcs = glob(["*.kt"]),
    deps = [
        "//third_party/java_src/xplat/kmpbench/com/google/apps/xplat/kmpbench/combination",
        "//third_party/java_src/xplat/kmpbench/java",
    ],
)

java_binary(
    name = "benchmark_runner_jvm",
    main_class = "com.google.apps.xplat.kmpbench.app.jvm.JvmBenchmarkRunnerKt",
    runtime_deps = [
        ":benchmark_runner_library_jvm",
    ],
)
