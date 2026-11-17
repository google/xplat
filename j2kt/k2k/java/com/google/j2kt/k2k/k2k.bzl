"""Build rules for running K2K Kotlin cleaner."""

def _k2k_clean_impl(ctx):
    outputs = []
    for src in ctx.files.srcs:
        out = ctx.actions.declare_file(ctx.label.name + "/" + src.path)
        outputs.append(out)

    output_dir = (
        ctx.bin_dir.path + "/" +
        (ctx.label.package + "/" if ctx.label.package else "") +
        ctx.label.name
    )

    args = ctx.actions.args()
    args.add("-output", output_dir)
    args.add_all(ctx.files.srcs)

    ctx.actions.run(
        inputs = depset(direct = ctx.files.srcs),
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
        "_cleaner": attr.label(
            default = Label("//third_party/java_src/xplat/j2kt/k2k/java/com/google/j2kt/k2k:cleaner"),
            executable = True,
            cfg = "exec",
        ),
    },
    doc = "Cleans a list of Kotlin sources using K2K and provides the cleaned sources.",
)
