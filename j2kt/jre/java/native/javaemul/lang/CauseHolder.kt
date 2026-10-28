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

import javaemul.internal.InternalPreconditions.Companion.checkCriticalArgument
import javaemul.internal.InternalPreconditions.Companion.checkState

internal class CauseHolder private constructor(private val throwable: Throwable) {
  // LINT.IfChange
  // The primary constructor is private to ensure new users have to add a constructor here which has
  // the reminder to update the when-dispatch in throwableExtensions.kt.
  constructor(throwable: java.lang.Throwable) : this(throwable as Throwable)

  constructor(throwable: java.lang.Error) : this(throwable as Throwable)

  constructor(throwable: java.lang.AssertionError) : this(throwable as Throwable)

  constructor(throwable: java.lang.Exception) : this(throwable as Throwable)

  constructor(throwable: java.lang.RuntimeException) : this(throwable as Throwable)

  constructor(throwable: java.lang.ArithmeticException) : this(throwable as Throwable)

  constructor(throwable: java.lang.ClassCastException) : this(throwable as Throwable)

  constructor(throwable: java.lang.IllegalArgumentException) : this(throwable as Throwable)

  constructor(throwable: java.lang.IllegalStateException) : this(throwable as Throwable)

  constructor(throwable: java.lang.IndexOutOfBoundsException) : this(throwable as Throwable)

  constructor(throwable: java.lang.NullPointerException) : this(throwable as Throwable)

  constructor(throwable: java.lang.NumberFormatException) : this(throwable as Throwable)

  constructor(throwable: java.lang.UnsupportedOperationException) : this(throwable as Throwable)

  constructor(throwable: java.util.ConcurrentModificationException) : this(throwable as Throwable)

  constructor(throwable: java.util.NoSuchElementException) : this(throwable as Throwable)

  constructor(throwable: java.util.concurrent.CancellationException) : this(throwable as Throwable)

  // LINT.ThenChange(throwableExtensions.kt)

  // Holds the cause (a Throwable or null), or `throwable`, if no cause was set.
  private var throwableOrCause: Throwable? = throwable

  val cause: Throwable?
    get() = if (hasCause()) throwableOrCause else null

  fun initCause(cause: Throwable?): Throwable {
    checkCriticalArgument(cause !== throwable, "Self-causation not permitted")
    checkState(!hasCause(), "Can't overwrite cause")
    throwableOrCause = cause
    return throwable
  }

  private fun hasCause(): Boolean = throwableOrCause !== throwable
}
