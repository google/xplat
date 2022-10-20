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
package javaemul.lang

// See regular JRE API documentation for other methods in this file.

fun Throwable.getSuppressed(): Array<Throwable> = suppressedExceptions.toTypedArray()

// TODO(b/254412607): Generalize to other exceptions.
fun kotlin.NumberFormatException.initCause(cause: Throwable?): Throwable =
  if (this is java.lang.NumberFormatException) initCause(cause)
  // initCause is generally called inside a constructor or right after calling one, so this
  // restriction should not be too bad in practice:
  else throw UnsupportedOperationException("Cannot initCause for native exception")
