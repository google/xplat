# J2KT Java Runtime Emulation Code for Kotlin Native

## Subdirectories

### `annotations`

This folder contains annotations that are used to map Java types to types
implemented in Kotlin, with the possibility for further adjustments (such as
using different method names in Java and Kotlin or mapping getters or
getter/setter pairs to Kotlin properties).

The annotations are exclusive to JRE emulation code and are not available to
user code.

### `common`

In the Kotlin/JVM stdlib, fundamental types are implemented by mapping them to
Java types. Think: `kotlin.String` compiles to `java.lang.String` bytecode. For
emulating these Java types in J2KT, we in turn define the Java types in terms of
Kotlin types. So `java.lang.String` transpiles to `kotlin.String` Kotlin code.

`common` defines these mappings (in `common/java`), using the annotations above.

Kotlin types don’t always provide the full functionality of their corresponding
Java types. `kotlin.String`, for example, has one constructor whereas
`java.lang.String` has two dozen constructors. Such missing APIs are added via
Kotlin extension functions (in `common/javaemul/lang`).

There are also helper methods used across the JRE emulation code or referenced
in transpiler output. Following J2CL’s conventions, this code is in
`common/javaemul/internal`.

### `native`

Types that are not in the “built-in” category are implemented as transpiled Java
code (wherever possible, to reuse existing JRE code) or hand-written Kotlin code
(where necessary to use Kotlin stdlib APIs).

When an `native` type is implemented in Kotlin, it must be accompanied by a Java
type defined in terms of `native` methods and the annotations above so that it
can be referenced by other Java code.

## What is compiled where

`copmmon` types are used on all J2KT platforms. In other words, built-in types
are always mapped. For example J2KT output never references `java.lang.String`,
only `kotlin.String`. APIs that are missing from Kotlin are going through the
`javaemul.lang` extension functions on all platforms. Therefore `common` code
is included in the J2KT runtime target on all platforms.

`native` types are not necessary on Kotlin/JVM or K2CL. The implementations from
the JVM’s JRE (or J2CL’s JRE) can be used directly. Therefore `native` code is
only included in the J2KT runtime for Kotlin Native.

At transpilation time, `common` and `native` together make up the
“bootclasspath” that the Java frontend of the transpiler uses on all platforms.
That guarantees that the transpiler output is identical on all platforms (any
annotations that can influence how types are mapped or how methods are renamed
are coming from the same source files, so they are identical). As a side-effect,
Java APIs that are not available on Kotlin Native are inaccessible when
transpiling J2KT for JVM.


|                    | Transpile time JVM & Native | Runtime (JVM) | Runtime (Native) |
|--------------------|-----------------------------|---------------|------------------|
| `common/**/*.java` | ✔                           | ✔             | ✔                |
| `common/**/*.kt`   | ✘                           | ✔             | ✔                |
| `native/**/*.java` | ✔                           | ✘             | ✔                |
| `native/**/*.kt`   | ✘                           | ✘             | ✔                |
