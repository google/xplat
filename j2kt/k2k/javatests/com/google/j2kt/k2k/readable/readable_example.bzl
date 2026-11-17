"""readable_example build macro for K2K Kotlin cleaner."""

load("//third_party/bazel_rules/rules_shell/shell:sh_test.bzl", "sh_test")
load("//third_party/java_src/xplat/j2kt/k2k/java/com/google/j2kt/k2k:k2k.bzl", "k2k_clean")
load("//tools/build_defs/build_test:build_test.bzl", "build_test")
load("//tools/build_defs/kotlin:rules.bzl", "kt_jvm_library")

def readable_example(
        srcs,
        deps = [],
        name = "readable"):
    """Cleans Kotlin sources and verifies readable golden outputs and compilability.

    Args:
      srcs: The Kotlin source files to clean.
      deps: Dependencies required to compile the Kotlin sources.
      name: Base name for the generated library target.
    """
    k2k_clean(
        name = "cleaned",
        srcs = srcs,
        deps = deps,
    )

    _golden_output(
        testonly = 1,
        name = "readable_golden",
        target = ":cleaned",
    )

    sh_test(
        name = "readable_golden_test",
        srcs = ["//third_party/java_src/xplat/j2kt/k2k/javatests/com/google/j2kt/k2k/readable:diff_check"],
        data = native.glob(["output/**"]) + [":readable_golden"],
        args = [
            '"%s/output"' % native.package_name(),
            '"$(rootpath :readable_golden)"',
        ],
        tags = ["j2kt", "golden"],
    )

    kt_jvm_library(
        name = name,
        srcs = [":cleaned"],
        deps = deps + ["//third_party/java_src/xplat/j2kt/jre/java:jre-j2kt-jvm"],
        tags = ["manual"],
    )

    build_test(
        name = name + "_build_test",
        targets = [":" + name],
        tags = ["j2kt"],
    )

def _golden_output_impl(ctx):
    output = ctx.actions.declare_directory(ctx.label.name)
    pkg = ctx.label.package + "/" if ctx.label.package else ""
    prefix = pkg + ctx.attr.target.label.name + "/" + pkg
    args = ctx.actions.args()
    args.add(output.path)
    for f in ctx.files.target:
        rel = f.short_path.removeprefix(prefix)
        args.add(f.path)
        args.add(rel + ".txt")
    ctx.actions.run_shell(
        inputs = ctx.files.target,
        outputs = [output],
        arguments = [args],
        mnemonic = "K2kGoldenOutputCopy",
        command = 'set -e; out="$1"; shift; while [ "$#" -gt 0 ]; do src="$1"; rel="$2"; shift 2; mkdir -p "$(dirname "$out/$rel")"; cp -f "$src" "$out/$rel"; done',
    )
    return [DefaultInfo(files = depset([output]), runfiles = ctx.runfiles([output]))]

_golden_output = rule(
    implementation = _golden_output_impl,
    doc = "Copies cleaned K2K outputs into a golden directory artifact with .txt suffix.",
    attrs = {
        "target": attr.label(
            allow_files = True,
            mandatory = True,
        ),
    },
)
