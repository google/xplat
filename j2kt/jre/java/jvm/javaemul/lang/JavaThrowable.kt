/*
 * Copyright 2023 Google Inc.
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

package javaemul.lang

// Bridge type for transpiled subclasses of Throwable.
open class JavaThrowable(message: String?, cause: Throwable?) : Throwable(message, cause) {

  constructor(cause: Throwable?) : this(cause?.toString(), cause)

  constructor(message: String?) : this(message, null)

  constructor() : this(null, null)

  override fun getStackTrace(): Array<StackTraceElement> = java_getStackTrace()

  fun java_getStackTrace(): Array<StackTraceElement> = super.getStackTrace()
}

fun Throwable.java_getStackTrace(): Array<StackTraceElement> =
  (this as java.lang.Throwable).stackTrace
