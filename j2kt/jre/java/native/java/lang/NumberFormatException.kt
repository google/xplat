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

@ObjCName("J2ktJavaLangNumberFormatException", exact = true)
open class NumberFormatException(message: String? = null) :
  kotlin.NumberFormatException(message), InitCauseCapable {
  override val causeHolder = CauseHolder()
  override val cause
    get() = causeHolder.cause
}
