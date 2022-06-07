load("//tools/build_defs/kotlin:rules.bzl", "kt_jvm_library")
load("//tools/build_defs/kotlin/release/rules/native:native_rules.bzl", "kt_native_library")

package(
    default_visibility = ["//third_party/java_src/xplat/kmpbench:__subpackages__"],
    licenses = ["notice"],
)

kt_jvm_library(
    name = "simple-jvm",
    srcs = glob(["*.kt"]),
    deps = ["//third_party/java_src/xplat/kmpbench/kotlin/com/google/j2cl/benchmarking/framework:framework-jvm"],
)

kt_native_library(
    name = "simple-native",
    srcs = glob(["*.kt"]),
    deps = ["//third_party/java_src/xplat/kmpbench/kotlin/com/google/j2cl/benchmarking/framework:framework-native"],
)
