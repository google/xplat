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
package java.lang

import java.io.PrintStream
import java.io.PrintWriter
import javaemul.lang.CauseHolder
import javaemul.lang.InitCauseCapable
import kotlin.String as KotlinString
import kotlin.Throwable as KotlinThrowable

open class Throwable(message: KotlinString? = null) : KotlinThrowable(message), InitCauseCapable {
  override val causeHolder = CauseHolder()
  override val cause
    get() = causeHolder.cause

  constructor(cause: KotlinThrowable?) : this() {
    initCause(cause)
  }

  constructor(message: KotlinString?, cause: KotlinThrowable?) : this(message) {
    initCause(cause)
  }

  constructor(
    message: KotlinString?,
    cause: KotlinThrowable?,
    enableSuppression: Boolean,
    writableStackTrace: Boolean,
  ) : this(message) {
    initCause(cause)
  }

  override fun initCause(cause: KotlinThrowable?): KotlinThrowable =
    super<InitCauseCapable>.initCause(cause)

  open fun printStackTrace(stream: PrintStream) {
    stream.print(stackTraceToString())
  }

  open fun printStackTrace(writer: PrintWriter) {
    writer.write(stackTraceToString())
  }

  open fun fillInStackTrace(): KotlinThrowable = this

  // TODO(b/276846862): We may want to wrap native stack trace elements in StackTraceElement
  //   and emulating the "original" StackTraceElement as far as possible.
  open val stackTrace: Array<StackTraceElement>
    get() = emptyArray<StackTraceElement>()

  open fun setStackTrace(trace: Array<StackTraceElement>) {}

  open val localizedMessage: KotlinString?
    get() = message
}
