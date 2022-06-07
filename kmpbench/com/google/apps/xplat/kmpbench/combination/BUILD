# Functionlity to simplify handling of pairs of Java and Kotlin benchmarks.

load("//tools/build_defs/kotlin:rules.bzl", "kt_jvm_library")
load("//tools/build_defs/kotlin/release/rules/native:native_rules.bzl", "kt_native_library")

package(
    default_visibility = ["//third_party/java_src/xplat/kmpbench:__subpackages__"],
    licenses = ["notice"],
)

kt_jvm_library(
    name = "combination",
    srcs = glob(["*.kt"]),
    deps = [
        "//third_party/java_src/xplat/kmpbench/kotlin/com/google/j2cl/benchmarking/framework:framework-jvm",
        "//third_party/java_src/xplat/kmpbench/kotlin/com/google/j2cl/benchmarks:benchmarks-jvm",
    ],
)

kt_native_library(
    name = "combination-native",
    srcs = glob(["*.kt"]),
    deps = [
        "//third_party/java_src/xplat/kmpbench/kotlin/com/google/j2cl/benchmarking/framework:framework-native",
        "//third_party/java_src/xplat/kmpbench/kotlin/com/google/j2cl/benchmarks:benchmarks-native",
        "//third_party/java_src/xplat/kmpbench/kotlin/com/google/j2cl/benchmarks/simple:simple-native",
    ],
)
