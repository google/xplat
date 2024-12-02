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
@file:OptIn(ExperimentalObjCName::class)

package javaemul.lang

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@ObjCName("JavaemulLangJavaMap", exact = true)
interface JavaMap<K, V> : MutableMapJvm<K, V> {
  // TODO: b/381836571 - Move this to j2kt-native's java.util.Map when konanc can handle that.
  fun getOrDefault(key: K, defaultValue: V): V = default_getOrDefault(key, defaultValue)
}

internal fun <K, V> MutableMap<K, V>.default_getOrDefault(key: K, defaultValue: V): V =
  this[key].let { if (it != null || containsKey(key)) it as V else defaultValue }
