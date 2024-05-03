Minimal stub satisfying internal compile time dependency requirements:
Google http client relies on reflection, which J2kt doesn't support because
Kotlin native doesn't support it and it's used in an internal subproject
that's hard to disable at compile time but not essential at runtime at this
point.