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

import java.util.function.BiFunction

interface JavaMap<K, V> : MutableMap<K, V> {

  override fun containsKey(key: K): Boolean = java_containsKey(key)

  override fun containsValue(value: V): Boolean = java_containsValue(value)

  override operator fun get(key: K): V? = java_get(key)

  override fun remove(key: K): V? = java_remove(key)

  // TODO(b/243046587): Rewrite to handle case in which t is not mutable
  override fun putAll(t: Map<out K, V>) = java_putAll(t as MutableMap<K, V>)

  fun java_containsKey(key: Any?): Boolean

  fun java_containsValue(value: Any?): Boolean

  fun java_get(key: Any?): V?

  // The Java `merge` function exists on Kotlin/JVM but is undocumented. So we rename our `merge` to
  // avoid a collision.
  fun java_merge(key: K, value: V, remap: BiFunction<in V, in V, out V?>): V? =
    default_merge(key, value, remap)

  fun java_remove(key: Any?): V?

  fun java_putAll(t: MutableMap<out K, out V>)
}

// Note: No need to check for the runtime type below. The bridge interface is
// only necessary to allow overriding with the Java signature. Calling with the
// Java signature does not require any trampolines, only manual type erasure.

@Suppress("UNCHECKED_CAST")
fun <K, V> MutableMap<K, V>.java_containsKey(key: Any?): Boolean = containsKey(key as K)

@Suppress("UNCHECKED_CAST")
fun <K, V> MutableMap<K, V>.java_containsValue(value: Any?): Boolean = containsValue(value as V)

@Suppress("UNCHECKED_CAST") fun <K, V> MutableMap<K, V>.java_get(key: Any?): V? = get(key as K)

@Suppress("UNCHECKED_CAST")
fun <K, V> MutableMap<K, V>.java_remove(key: Any?): V? = remove(key as K)

@Suppress("UNCHECKED_CAST")
fun <K, V> MutableMap<K, V>.java_putAll(t: MutableMap<out K, out V>) = putAll(t as Map<out K, V>)

fun <K, V> MutableMap<K, V>.java_merge(
  key: K,
  value: V,
  remap: BiFunction<in V, in V, out V?>
): V? = if (this is JavaMap) java_merge(key, value, remap) else default_merge(key, value, remap)

private fun <K, V> MutableMap<K, V>.default_merge(
  key: K,
  value: V,
  remap: BiFunction<in V, in V, out V?>
): V? {
  val oldValue = get(key)
  val newValue: V? = if (oldValue == null) value else remap.apply(oldValue, value)
  if (newValue == null) {
    remove(key)
  } else {
    put(key, newValue)
  }
  return newValue
}
