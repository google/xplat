load("//third_party/java_src/j2cl/build_defs:rules.bzl", "j2kt_jvm_library", "j2kt_native_library")
load("//tools/build_defs/j2objc:objc_java_library.bzl", "objc_java_library")

package(
    default_visibility = ["//third_party/java_src/xplat/kmpbench:__subpackages__"],
    licenses = ["notice"],
)

# TODO(b/233052620): Deltablue and Richards don't compile with J2kt; Box2d has external deps.
SHARED_BENCHMARK_JAVA_SOURCES = glob(
    [
        "com/google/j2cl/benchmarks/octane/navierstokes/*.java",
        "com/google/j2cl/benchmarks/octane/raytrace/*.java",
    ],
) + [
    "com/google/j2cl/benchmarks/octane/NavierStokesBenchmark.java",
    "com/google/j2cl/benchmarks/octane/RayTraceBenchmark.java",
]

java_library(
    name = "benchmarks",
    srcs = SHARED_BENCHMARK_JAVA_SOURCES + glob(
        [
            "com/google/j2cl/benchmarks/*.java",
            "com/google/j2cl/benchmarks/simple/*.java",
            "com/google/j2cl/benchmarking/framework/*.java",
        ],
    ),
    deps = [
        "//java/com/google/common/annotations",
        "//java/com/google/common/base",
        "//third_party/java/gwt:gwt-jsinterop-annotations",
    ],
)

genrule(
    name = "repackaged",
    srcs = [":libbenchmarks.jar"],
    outs = ["repackaged.jar"],
    cmd = ("$(location //third_party/java/jarjar:jarjar_bin) process " +
           "$(location jarjar-rules.txt) '$<' '$@'"),
    tools = [
        "jarjar-rules.txt",
        "//third_party/java/jarjar:jarjar_bin",
    ],
)

java_import(
    name = "java",
    jars = [
        ":repackaged.jar",
    ],
    deps = [
        "//java/com/google/common/annotations",
        "//java/com/google/common/base",
        "//third_party/java/gwt:gwt-jsinterop-annotations",
    ],
)

# J2Objc

objc_java_library(
    name = "benchmarks_j2objc",
    deps = [
        ":benchmarks",
    ],
)

# TODO(b/233052620): Remove this hack
j2kt_jvm_library(
    name = "abstract_benchmark-j2kt-jvm",
    srcs = [
        "com/google/j2cl/benchmarking/framework/AbstractBenchmark.java",
    ],
)

j2kt_jvm_library(
    name = "benchmarks-j2kt-jvm",
    srcs = SHARED_BENCHMARK_JAVA_SOURCES,
    deps = [
        ":abstract_benchmark-j2kt-jvm",
        "//third_party/java/gwt:gwt-jsinterop-annotations-j2kt-jvm",
    ],
)

# J2kt native

j2kt_native_library(
    name = "abstract_benchmark-j2kt-native",
    srcs = [
        "com/google/j2cl/benchmarking/framework/AbstractBenchmark.java",
    ],
)

j2kt_native_library(
    name = "benchmarks-j2kt-native",
    srcs = SHARED_BENCHMARK_JAVA_SOURCES,
    deps = [
        ":abstract_benchmark-j2kt-native",
        "//third_party/java/gwt:gwt-jsinterop-annotations-j2kt-native",
    ],
)
