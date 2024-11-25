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

import java.util.AbstractMap
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

/** Bridge type for `java.util.AbstractMap` */
@ObjCName("JavaemulLangJavaAbstractMap", exact = true)
abstract class JavaAbstractMap<K, V> : AbstractMap<K, V>(), JavaMap<K, V> {
  override fun put(key: K, value: V): V? {
    throw UnsupportedOperationException("Put not supported on this map")
  }

  override fun remove(key: K, value: V): Boolean = super<JavaMap>.remove(key, value)
}
