/*
 * Copyright 2022 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
@file:OptIn(ExperimentalObjCName::class)

package javaemul.lang

import java.io.PrintWriter
import java.lang.StackTraceElement
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

// Bridge type for transpiled subclasses of Throwable.
// TODO(b/276846862): We may want to wrap native stack trace elements in StackTraceElement
//   and emulating the "original" StackTraceElement as far as possible.
@ObjCName("JavaemulLangJavaThrowable", exact = true)
open class JavaThrowable(message: String? = null) : Throwable(message), InitCauseCapable {
  override val causeHolder = CauseHolder()
  override val cause
    get() = causeHolder.cause

  constructor(cause: Throwable?) : this() {
    initCause(cause)
  }

  constructor(message: String?, cause: Throwable?) : this(message) {
    initCause(cause)
  }

  constructor(
    message: String?,
    cause: Throwable?,
    enableSuppression: Boolean,
    writableStackTrace: Boolean
  ) : this(message) {
    initCause(cause)
  }

  override fun initCause(cause: Throwable?): Throwable = super<InitCauseCapable>.initCause(cause)

  fun printStackTrace(writer: PrintWriter) = default_printStackTrace(writer)

  open fun fillInStackTrace() = default_fillInStackTrace()

  fun java_getStackTrace(): Array<StackTraceElement> = default_getStackTrace()

  fun setStackTrace(trace: Array<StackTraceElement>) = default_setStackTrace(trace)

  fun getLocalizedMessage() = default_getLocalizedMessage()
}

fun Throwable.getSuppressed(): Array<Throwable> = suppressedExceptions.toTypedArray()

fun Throwable.initCause(cause: Throwable?): Throwable =
  if (this is InitCauseCapable) initCause(cause) // Generic native bridge case
  // initCause is generally called inside a constructor or right after calling one, so this
  // restriction should not be too bad in practice:
  else throw UnsupportedOperationException("Cannot initCause for native exception")

fun Throwable.printStackTrace(writer: PrintWriter) =
  if (this is JavaThrowable) printStackTrace(writer) else default_printStackTrace(writer)

fun Throwable.fillInStackTrace(): Throwable =
  if (this is JavaThrowable) fillInStackTrace() else default_fillInStackTrace()

fun Throwable.java_getStackTrace(): Array<StackTraceElement> =
  if (this is JavaThrowable) java_getStackTrace() else default_getStackTrace()

fun Throwable.setStackTrace(trace: Array<StackTraceElement>) =
  if (this is JavaThrowable) setStackTrace(trace) else default_setStackTrace(trace)

fun Throwable.getLocalizedMessage() =
  if (this is JavaThrowable) getLocalizedMessage() else default_getLocalizedMessage()

private fun Throwable.default_printStackTrace(writer: PrintWriter) {
  writer.write(stackTraceToString())
}

private fun Throwable.default_fillInStackTrace() = this

private fun Throwable.default_getStackTrace(): Array<StackTraceElement> =
  emptyArray<StackTraceElement>()

private fun Throwable.default_setStackTrace(trace: Array<StackTraceElement>) {}

private fun Throwable.default_getLocalizedMessage() = message
