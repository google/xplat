# K2K: J2KT Code Cleaner

K2K is a Kotlin cleanup tool designed to post-process Kotlin code transpiled from Java by J2KT.

## Test Structure

Readable tests are organized under `javatests/com/google/j2kt/k2k/readable/<name>/`:

- **`BUILD`**: Declares a `readable_example` target that cleans input `.kt` files (optionally generating readable `.kt.mappings` source maps), verifies that the cleaned Kotlin compiles via `kt_jvm_library`, and compares the output against `output/`.
- **`<file>.kt`**: Input Kotlin source.
- **`output/<file>.kt.txt`** (plus optional **`output/<file>.kt.mappings.txt`**): Expected cleaned golden outputs.

## Workflow

### 1. Running Readable Tests

Run the readable golden and compilation tests:

```bash
blaze test //third_party/java_src/xplat/j2kt/k2k/javatests/com/google/j2kt/k2k/readable/...
```

### 2. Regenerating Readable Outputs

When adding new test cases or updating cleaning passes, regenerate the `output/` golden files:

```bash
third_party/java_src/xplat/j2kt/k2k/javatests/com/google/j2kt/k2k/gen.py
```
