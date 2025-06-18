@file:OptIn(ExperimentalObjCRefinement::class)

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
import java.lang.Throwable as JavaLangThrowable
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

val Throwable.suppressed: Array<Throwable>
  get() = suppressedExceptions.toTypedArray()

@HiddenFromObjC
fun Throwable.initCause(cause: Throwable?): Throwable =
  if (this is InitCauseCapable) initCause(cause) // Generic native bridge case
  // initCause is generally called inside a constructor or right after calling one, so this
  // restriction should not be too bad in practice:
  else throw UnsupportedOperationException("Cannot initCause for native exception")

@HiddenFromObjC
fun Throwable.printStackTrace(stream: PrintStream) =
  if (this is JavaLangThrowable) printStackTrace(stream) else stream.print(stackTraceToString())

@HiddenFromObjC
fun Throwable.printStackTrace(writer: PrintWriter) =
  if (this is JavaLangThrowable) printStackTrace(writer) else writer.write(stackTraceToString())

@HiddenFromObjC
fun Throwable.fillInStackTrace(): Throwable =
  if (this is JavaLangThrowable) fillInStackTrace() else this

@HiddenFromObjC
val Throwable.stackTrace: Array<StackTraceElement>
  get() = if (this is JavaLangThrowable) stackTrace else emptyArray<StackTraceElement>()

@HiddenFromObjC
fun Throwable.setStackTrace(trace: Array<StackTraceElement>) =
  if (this is JavaLangThrowable) setStackTrace(trace) else Unit

@HiddenFromObjC
val Throwable.localizedMessage: String?
  get() = if (this is JavaLangThrowable) localizedMessage else message
