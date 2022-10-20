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

import javaemul.internal.InternalPreconditions.Companion.checkCriticalArgument
import javaemul.internal.InternalPreconditions.Companion.checkState

open class NumberFormatException(message: String?) : kotlin.NumberFormatException(message) {
  constructor() : this(null)

  override var cause: Throwable? = this // Sentinel value for unset cause
    get() = if (field === this) null else field
    internal set(value: Throwable?) {
      checkState(field === this, "Can't overwrite cause")
      checkCriticalArgument(value !== this, "Self-causation not permitted")
      field = value
    }

  open fun initCause(cause: Throwable?): Throwable = apply { this.cause = cause }
}
