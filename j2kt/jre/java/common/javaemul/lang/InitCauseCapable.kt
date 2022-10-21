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

/** Interface for [Throwable]s that implement [initCause]. */
internal interface InitCauseCapable {
  val causeHolder: CauseHolder

  // This will have to be overridden by all implementers to disambiguate with Throwable's cause.
  // But having an implementation here will trigger an error if the implementer forgets to do that.
  val cause
    get() = causeHolder.cause

  fun initCause(cause: Throwable?): Throwable {
    checkCriticalArgument(cause !== this, "Self-causation not permitted")
    causeHolder.cause = cause
    return this as Throwable
  }
}
