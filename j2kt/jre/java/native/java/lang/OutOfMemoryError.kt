/*
 * Copyright 2024 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package java.lang

import kotlin.experimental.ExperimentalObjCName

/**
 * Bridge for `kotlin.OutOfMemoryError`.
 *
 * This trivial-looking subclass is needed for ObjC interop: Kotlin/Native only exports stdlib
 * classes to ObjC if they are referenced from exported API, so without it,
 * `GKOTKotlinOutOfMemoryError` would typically not be exported and ObjC code referring to
 * `JavaLangOutOfMemoryError` would fail to link. It also provides J2ObjC-compatible initializers.
 */
@OptIn(ExperimentalObjCName::class)
@ObjCName("J2ktJavaLangOutOfMemoryError", exact = true)
open class OutOfMemoryError(message: kotlin.String?) : kotlin.OutOfMemoryError(message) {
  constructor() : this(message = null)
}
