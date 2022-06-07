load("//tools/build_defs/kotlin:rules.bzl", "kt_jvm_library")
load("//tools/build_defs/kotlin/release/rules/native:native_rules.bzl", "kt_native_library")

package(
    default_visibility = ["//third_party/java_src/xplat/kmpbench:__subpackages__"],
    licenses = ["notice"],
)

kt_jvm_library(
    name = "benchmarks-jvm",
    srcs = glob(["*.kt"]),
    deps = [
        "//third_party/java_src/xplat/kmpbench/java:benchmarks-j2kt-jvm",
        "//third_party/java_src/xplat/kmpbench/kotlin/com/google/j2cl/benchmarks/simple:simple-jvm",
    ],
)

kt_native_library(
    name = "benchmarks-native",
    srcs = glob(["*.kt"]),
    deps = [
        "//third_party/java_src/xplat/kmpbench/java:benchmarks-j2kt-native",
        "//third_party/java_src/xplat/kmpbench/kotlin/com/google/j2cl/benchmarks/simple:simple-native",
    ],
)
