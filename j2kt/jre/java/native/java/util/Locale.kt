@file:OptIn(ExperimentalObjCName::class)

/*
 * Copyright 2025 Google Inc.
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
package java.util

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

/**
 * A very simple emulation of Locale for shared-code patterns like `String.toUpperCase(Locale.US)`.
 *
 * Note: Any changes to this class should put into account the assumption that was made in rest of
 * the JRE emulation.
 */
@ObjCName("J2ktJavaUtilLocale", exact = true)
open class Locale private constructor() {
  companion object {
    val ROOT: Locale =
      object : Locale() {
        override fun toString(): String = ""
      }

    val ENGLISH: Locale =
      object : Locale() {
        override fun toString(): String = "en"
      }

    val US: Locale =
      object : Locale() {
        override fun toString(): String = "en_US"
      }

    private val defaultLocale: Locale =
      object : Locale() {
        override fun toString(): String = "unknown"
      }

    fun getDefault(): Locale = defaultLocale
  }
}
