@file:OptIn(ExperimentalObjCRefinement::class, ExperimentalObjCName::class)

/*
 * Copyright 2022 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javaemul.lang

import java.io.PrintStream
import java.io.PrintWriter
import java.lang.StackTraceElement
import kotlin.experimental.ExperimentalObjCName
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC
import kotlin.native.ObjCName

val Throwable.suppressed: Array<Throwable>
  get() = suppressedExceptions.toTypedArray()

@HiddenFromObjC
fun Throwable.initCause(cause: Throwable?): Throwable =
  // LINT.IfChange
  when (this) {
    is java.lang.Throwable -> initCause(cause)
    is java.lang.Error -> initCause(cause)
    is java.lang.AssertionError -> initCause(cause)
    is java.lang.Exception -> initCause(cause)
    is java.lang.RuntimeException -> initCause(cause)
    is java.lang.ArithmeticException -> initCause(cause)
    is java.lang.ClassCastException -> initCause(cause)
    is java.lang.IllegalArgumentException -> initCause(cause)
    is java.lang.IllegalStateException -> initCause(cause)
    is java.lang.IndexOutOfBoundsException -> initCause(cause)
    is java.lang.NullPointerException -> initCause(cause)
    is java.lang.NumberFormatException -> initCause(cause)
    is java.lang.UnsupportedOperationException -> initCause(cause)
    is java.util.ConcurrentModificationException -> initCause(cause)
    is java.util.NoSuchElementException -> initCause(cause)
    is java.util.concurrent.CancellationException -> initCause(cause)
    // initCause is generally called inside a constructor or right after calling one, so this
    // restriction should not be too bad in practice:
    else -> throw UnsupportedOperationException("Cannot initCause for native exception")
  }

// LINT.ThenChange(CauseHolder.kt)

@ObjCName("java_printStackTrace")
fun Throwable.printStackTrace(@ObjCName("withJavaIoPrintStream") stream: PrintStream) =
  if (this is java.lang.Throwable) printStackTrace(stream) else stream.print(stackTraceToString())

@ObjCName("java_printStackTrace")
fun Throwable.printStackTrace(@ObjCName("withJavaIoPrintWriter") writer: PrintWriter) =
  if (this is java.lang.Throwable) printStackTrace(writer) else writer.write(stackTraceToString())

@HiddenFromObjC
fun Throwable.fillInStackTrace(): Throwable =
  if (this is java.lang.Throwable) fillInStackTrace() else this

@HiddenFromObjC
val Throwable.stackTrace: Array<StackTraceElement>
  get() = if (this is java.lang.Throwable) stackTrace else emptyArray<StackTraceElement>()

@HiddenFromObjC
fun Throwable.setStackTrace(trace: Array<StackTraceElement>) =
  if (this is java.lang.Throwable) setStackTrace(trace) else Unit

@HiddenFromObjC
val Throwable.localizedMessage: String?
  get() = if (this is java.lang.Throwable) localizedMessage else message
