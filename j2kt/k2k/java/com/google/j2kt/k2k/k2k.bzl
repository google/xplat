"""Build rules for running K2K Kotlin cleaner."""

load("//third_party/bazel_rules/rules_java/java/common:java_info.bzl", "JavaInfo")

def _k2k_clean_impl(ctx):
    outputs = []
    for src in ctx.files.srcs:
        out = ctx.actions.declare_file(ctx.label.name + "/" + src.path)
        outputs.append(out)

    classpath_depset = depset(
        transitive = [
            dep[JavaInfo].transitive_compile_time_jars
            for dep in ctx.attr.deps + [ctx.attr._jre]
        ],
    )

    output_dir = (
        ctx.bin_dir.path + "/" +
        (ctx.label.package + "/" if ctx.label.package else "") +
        ctx.label.name
    )

    args = ctx.actions.args()
    args.use_param_file("@%s", use_always = True)
    args.set_param_file_format("multiline")
    args.add("-output", output_dir)
    args.add_joined("-classpath", classpath_depset, join_with = ctx.configuration.host_path_separator)
    args.add_all(ctx.files.srcs)

    ctx.actions.run(
        inputs = depset(
            direct = ctx.files.srcs,
            transitive = [classpath_depset],
        ),
        outputs = outputs,
        executable = ctx.executable._cleaner,
        arguments = [args],
        mnemonic = "K2kClean",
        progress_message = "Cleaning Kotlin sources: %{label}",
    )

    return [DefaultInfo(files = depset(outputs))]

k2k_clean = rule(
    implementation = _k2k_clean_impl,
    attrs = {
        "srcs": attr.label_list(
            mandatory = True,
            allow_files = True,
            doc = "The list of Kotlin source files to clean.",
        ),
        "deps": attr.label_list(
            doc = "The list of compiled libraries / dependencies to put on the classpath.",
            providers = [JavaInfo],
        ),
        "_cleaner": attr.label(
            default = Label("//third_party/java_src/xplat/j2kt/k2k/java/com/google/j2kt/k2k:cleaner"),
            executable = True,
            cfg = "exec",
        ),
        "_jre": attr.label(
            default = Label("//third_party/java_src/xplat/j2kt/jre/java:jre-j2kt-jvm"),
        ),
    },
    doc = "Cleans a list of Kotlin sources using K2K and provides the cleaned sources.",
)
