load("//tools/build_defs/kotlin:rules.bzl", "kt_jvm_library")
load("//tools/build_defs/kotlin/release/rules/native:native_rules.bzl", "kt_native_library")

package(
    default_visibility = ["//third_party/java_src/xplat/kmpbench:__subpackages__"],
    licenses = ["notice"],
)

# TODO(b/233052620): Remove the abstract_benchmark hack
kt_jvm_library(
    name = "framework-jvm",
    srcs = glob(
        ["*.kt"],
        exclude = [
            "AbstractBenchmark.kt",
        ],
    ),
    exports = [
        "//third_party/java_src/xplat/kmpbench/java:abstract_benchmark-j2kt-jvm",
    ],
    deps = [
        "//third_party/java_src/xplat/kmpbench/java:abstract_benchmark-j2kt-jvm",
    ],
)

kt_native_library(
    name = "framework-native",
    srcs = glob(
        ["*.kt"],
        exclude = ["AbstractBenchmark.java"],
    ),
    deps = [
        "//third_party/java_src/xplat/kmpbench/java:abstract_benchmark-j2kt-native",
    ],
)
