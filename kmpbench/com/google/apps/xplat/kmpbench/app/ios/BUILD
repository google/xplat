load("//tools/build_defs/apple:ios.bzl", "ios_application", "ios_unit_test")
load("//tools/build_defs/kotlin/release/rules/native:native_rules.bzl", "kt_apple_framework", "kt_apple_test_library")
load("//tools/build_defs/swift:swift.bzl", "swift_library")

kt_apple_test_library(
    name = "benchmark_test_library_apple",
    srcs = glob(["*.kt"]),
    custom_kotlincopts = ["//tools/build_defs/kotlin/opts/memory_model:experimental"],
    deps = [
        "//third_party/java/j2objc:jre_core_lib",
        "//third_party/java_src/xplat/kmpbench/com/google/apps/xplat/kmpbench/combination:combination-native",
        "//third_party/java_src/xplat/kmpbench/java:benchmarks_j2objc",
    ],
)

ios_unit_test(
    name = "IosBenchmarkTest",
    minimum_os_version = "14.0",
    runner = "//testing/utp/ios:IOS_14",
    tags = [
        "manual",
        "notap",
    ],
    deps = [
        ":benchmark_test_library_apple",
        "//third_party/java/j2objc:jre_testing_lib",
    ],
)

kt_apple_framework(
    name = "BenchmarkManager",
    srcs = ["IosBenchmarkManager.kt"],
    custom_kotlincopts = ["//tools/build_defs/kotlin/opts/memory_model:experimental"],
    deps = [
        "//third_party/java/j2objc:jre_core_lib",
        "//third_party/java_src/xplat/kmpbench/com/google/apps/xplat/kmpbench/combination:combination-native",
        "//third_party/java_src/xplat/kmpbench/java:benchmarks_j2objc",
    ],
)

swift_library(
    name = "KmpBenchLib",
    srcs = glob([
        "*.swift",
    ]),
    deps = [
        ":BenchmarkManager",
        "//third_party/apple_frameworks:SwiftUI",
    ],
)

ios_application(
    name = "KmpBenchApp",
    bundle_id = "com.google.apps.xplat.helloworld.swift",
    families = [
        "iphone",
    ],
    infoplists = ["info.plist"],
    minimum_os_version = "15.0",
    deps = [
        ":KmpBenchLib",
        "//third_party/java/j2objc:jre_core_lib",
        "//third_party/java/j2objc:jre_net_lib",
        "//third_party/java/j2objc:jre_security_lib",
        "//third_party/java/j2objc:jre_util_lib",
    ],
)
