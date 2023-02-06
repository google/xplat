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
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

// Bridge type for transpiled subclasses of Throwable.
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

  override fun initCause(cause: Throwable?): Throwable = super<InitCauseCapable>.initCause(cause)

  fun java_printStackTrace(writer: PrintWriter) = default_printStackTrace(writer)
}

fun Throwable.getSuppressed(): Array<Throwable> = suppressedExceptions.toTypedArray()

fun Throwable.initCause(cause: Throwable?): Throwable =
  if (this is InitCauseCapable) initCause(cause) // Generic native bridge case
  else if (this is java.lang.Throwable) initCause(cause) // JVM or direct Throwable subclass case
  // initCause is generally called inside a constructor or right after calling one, so this
  // restriction should not be too bad in practice:
  else throw UnsupportedOperationException("Cannot initCause for native exception")

// This is renamed because Kotlin's stackTraceToString is implemented in terms of printStackTrace.
// If we don't rename the method, we'll end up with infinite recursion in Kotlin/JVM.
fun Throwable.java_printStackTrace(writer: PrintWriter) =
  if (this is JavaThrowable) java_printStackTrace(writer) else default_printStackTrace(writer)

private fun Throwable.default_printStackTrace(writer: PrintWriter) {
  writer.write(stackTraceToString())
}
