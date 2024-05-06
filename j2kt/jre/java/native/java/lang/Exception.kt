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
@file:OptIn(ExperimentalObjCName::class)

package java.lang

import javaemul.lang.CauseHolder
import javaemul.lang.InitCauseCapable
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@ObjCName("J2ktJavaLangException", exact = true)
open class Exception(message: kotlin.String? = null) : kotlin.Exception(message), InitCauseCapable {
  /**
   * Kotlin throwables do not support [initCause] for initializing the cause outside of the
   * constructor. The readonly [cause] property however is `open` so we override it to let
   * transpiled code use `initCause` for exceptions that were constructed in transpiled code.
   *
   * Kotlin code can only access `cause` through the property (the backing field is not exposed at
   * all), so we do not need to use the Kotlin [cause] constructor parameter.
   */
  override val causeHolder = CauseHolder()
  override val cause
    get() = causeHolder.cause

  constructor(message: kotlin.String?, cause: kotlin.Throwable?) : this(message) {
    initCause(cause)
  }

  constructor(cause: kotlin.Throwable?) : this(cause?.toString(), cause)
}
