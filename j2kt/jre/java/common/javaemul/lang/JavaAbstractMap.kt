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

import java.util.AbstractMap

/** Bridge type for `java.util.AbstractMap` */
abstract class JavaAbstractMap<K, V> : AbstractMap<K, V>(), JavaMap<K, V> {
  override fun containsKey(key: K): Boolean = super<JavaMap>.containsKey(key)

  override fun containsValue(value: V): Boolean = super<JavaMap>.containsValue(value)

  override operator fun get(key: K): V? = super<JavaMap>.get(key)

  override fun remove(key: K): V? = super<JavaMap>.remove(key)

  override fun put(key: K, value: V): V? {
    throw UnsupportedOperationException("Put not supported on this map")
  }

  // TODO(b/243046587): Rewrite to handle case in which t is not mutable
  @Suppress("UNCHECKED_CAST")
  override fun putAll(t: Map<out K, V>) = java_putAll(t as MutableMap<K, V>)

  @Suppress("UNCHECKED_CAST")
  override fun java_containsKey(key: Any?): Boolean = super<AbstractMap>.containsKey(key as K)

  @Suppress("UNCHECKED_CAST")
  override fun java_containsValue(value: Any?): Boolean =
    super<AbstractMap>.containsValue(value as V)

  @Suppress("UNCHECKED_CAST")
  override fun java_get(key: Any?): V? = super<AbstractMap>.get(key as K)

  @Suppress("UNCHECKED_CAST")
  override fun java_putAll(t: MutableMap<out K, out V>) = super<JavaMap>.putAll(t as Map<out K, V>)

  @Suppress("UNCHECKED_CAST")
  override fun java_remove(key: Any?): V? = super<AbstractMap>.remove(key as K)
}
