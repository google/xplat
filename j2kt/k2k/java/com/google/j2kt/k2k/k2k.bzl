"""Build rules for running K2K Kotlin cleaner."""

load("//third_party/bazel_rules/rules_java/java/common:java_info.bzl", "JavaInfo")

# Copied from _derive_module_name in //third_party/bazel_rules/rules_kotlin/kotlin/common.bzl
# to match rules_kotlin's module naming without depending on its internal helper.
def _derive_module_name(ctx):
    return "_".join([p for p in [ctx.label.package, ctx.label.name] if p]).replace("/", ".")

def _k2k_clean_impl(ctx):
    kt_outputs = []
    map_outputs = []
    readable_map_outputs = []
    for src in ctx.files.srcs:
        out = ctx.actions.declare_file(ctx.label.name + "/" + src.path)
        kt_outputs.append(out)
        map_out = ctx.actions.declare_file(ctx.label.name + "/" + src.path + ".map")
        map_outputs.append(map_out)
        if ctx.attr.readable_source_maps:
            readable_map_out = ctx.actions.declare_file(
                ctx.label.name + "/" + src.path + ".mappings",
            )
            readable_map_outputs.append(readable_map_out)

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
    if ctx.attr.readable_source_maps:
        args.add("-readablesourcemaps")
    kotlincopts = ["-module-name=" + _derive_module_name(ctx)] + ctx.attr.kotlincopts
    args.add_all(kotlincopts, format_each = "-kotlincOptions=%s")
    args.add_joined("-classpath", classpath_depset, join_with = ctx.configuration.host_path_separator)
    args.add_all(ctx.files.srcs)

    ctx.actions.run(
        inputs = depset(
            direct = ctx.files.srcs,
            transitive = [classpath_depset],
        ),
        outputs = kt_outputs + map_outputs + readable_map_outputs,
        executable = ctx.executable._cleaner,
        arguments = [args],
        mnemonic = "K2kClean",
        progress_message = "Cleaning Kotlin sources: %{label}",
    )

    return [
        DefaultInfo(files = depset(kt_outputs)),
        OutputGroupInfo(
            kt = depset(kt_outputs),
            source_maps = depset(map_outputs),
            readable_source_maps = depset(readable_map_outputs),
        ),
    ]

k2k_clean = rule(
    implementation = _k2k_clean_impl,
    attrs = {
        "srcs": attr.label_list(
            mandatory = True,
            allow_files = [".kt"],
            doc = "The list of Kotlin source files to clean.",
        ),
        "deps": attr.label_list(
            doc = "The list of compiled libraries / dependencies to put on the classpath.",
            providers = [JavaInfo],
        ),
        "kotlincopts": attr.string_list(
            doc = "Additional kotlinc flags passed to K2K FIR analysis.",
        ),
        "readable_source_maps": attr.bool(
            default = False,
            doc = "Whether to generate human-readable .kt.mappings files.",
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
