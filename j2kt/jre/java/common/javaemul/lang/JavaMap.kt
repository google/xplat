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

import java.util.function.BiConsumer
import java.util.function.BiFunction
import java.util.function.Function

interface JavaMap<K, V> : MutableMap<K, V> {

  override fun containsKey(key: K): Boolean = java_containsKey(key)

  override fun containsValue(value: V): Boolean = java_containsValue(value)

  override operator fun get(key: K): V? = java_get(key)

  override fun remove(key: K): V? = java_remove(key)

  // TODO(b/243046587): Rewrite to handle case in which t is not mutable
  override fun putAll(t: Map<out K, V>) = java_putAll(t as MutableMap<K, V>)

  fun java_compute(key: K, remappingFunction: BiFunction<in K, in V?, out V>): V? =
    default_compute(key, remappingFunction)

  fun java_computeIfAbsent(key: K, mappingFunction: Function<in K, out V>): V =
    default_computeIfAbsent(key, mappingFunction)

  fun java_computeIfPresent(key: K, remappingFunction: BiFunction<in K, in V, out V>): V? =
    default_computeIfPresent(key, remappingFunction)

  fun java_containsKey(key: Any?): Boolean

  fun java_containsValue(value: Any?): Boolean

  fun java_get(key: Any?): V?

  fun java_getOrDefault(key: Any?, defaultValue: V?): V? = default_getOrDefault(key, defaultValue)

  fun java_forEach(action: BiConsumer<in K, in V>) = default_forEach(action)

  // The Java `merge` function exists on Kotlin/JVM but is undocumented. So we rename our `merge` to
  // avoid a collision.
  fun java_merge(key: K, value: V, remap: BiFunction<in V, in V, out V?>): V? =
    default_merge(key, value, remap)

  fun java_putAll(t: MutableMap<out K, out V>)

  fun java_putIfAbsent(key: K, value: V): V? = default_putIfAbsent(key, value)

  fun java_remove(key: Any?): V?

  fun java_remove(key: Any?, value: Any?): Boolean = default_remove(key, value)

  fun java_replace(key: K, value: V): V? = default_replace(key, value)

  fun java_replace(key: K, oldValue: V, newValue: V): Boolean =
    default_replace(key, oldValue, newValue)

  fun java_replaceAll(function: BiFunction<in K, in V, out V>) = default_replaceAll(function)
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
fun <K, V> MutableMap<K, V>.java_putAll(t: MutableMap<out K, out V>) = putAll(t as Map<out K, V>)

@Suppress("UNCHECKED_CAST")
fun <K, V> MutableMap<K, V>.java_remove(key: Any?): V? = remove(key as K)

fun <K, V> MutableMap<K, V>.java_compute(
  key: K,
  mappingFunction: BiFunction<in K, in V?, out V>
): V? =
  if (this is JavaMap) java_compute(key, mappingFunction) else default_compute(key, mappingFunction)

fun <K, V> MutableMap<K, V>.java_computeIfAbsent(
  key: K,
  mappingFunction: Function<in K, out V>
): V =
  if (this is JavaMap) java_computeIfAbsent(key, mappingFunction)
  else default_computeIfAbsent(key, mappingFunction)

fun <K, V> MutableMap<K, V>.java_computeIfPresent(
  key: K,
  mappingFunction: BiFunction<in K, in V, out V>
): V? =
  if (this is JavaMap) java_computeIfPresent(key, mappingFunction)
  else default_computeIfPresent(key, mappingFunction)

fun <K, V> MutableMap<K, V>.java_forEach(action: BiConsumer<in K, in V>) =
  if (this is JavaMap) java_forEach(action) else default_forEach(action)

fun <K, V> MutableMap<K, V>.java_getOrDefault(key: Any?, defaultValue: V?): V? =
  if (this is JavaMap) java_getOrDefault(key, defaultValue)
  else default_getOrDefault(key, defaultValue)

fun <K, V> MutableMap<K, V>.java_merge(
  key: K,
  value: V,
  remap: BiFunction<in V, in V, out V?>
): V? = if (this is JavaMap) java_merge(key, value, remap) else default_merge(key, value, remap)

fun <K, V> MutableMap<K, V>.java_putIfAbsent(key: K, value: V): V? =
  if (this is JavaMap) java_putIfAbsent(key, value) else default_putIfAbsent(key, value)

fun <K, V> MutableMap<K, V>.java_remove(key: Any?, value: Any?): Boolean =
  if (this is JavaMap) java_remove(key, value) else default_remove(key, value)

fun <K, V> MutableMap<K, V>.java_replace(key: K, value: V): V? =
  if (this is JavaMap) java_replace(key, value) else default_replace(key, value)

fun <K, V> MutableMap<K, V>.java_replace(key: K, oldValue: V, newValue: V): Boolean =
  if (this is JavaMap) java_replace(key, oldValue, newValue)
  else default_replace(key, oldValue, newValue)

fun <K, V> MutableMap<K, V>.java_replaceAll(function: BiFunction<in K, in V, out V>) =
  if (this is JavaMap) java_replaceAll(function) else default_replaceAll(function)

private fun <K, V> MutableMap<K, V>.default_forEach(action: BiConsumer<in K, in V>) {
  this.forEach { entry -> action.accept(entry.key, entry.value) }
}

private fun <K, V> MutableMap<K, V>.default_compute(
  key: K,
  remappingFunction: BiFunction<in K, in V?, out V>
): V? {
  val oldValue = this[key]
  val newValue = remappingFunction.apply(key, oldValue)
  if (oldValue != null) {
    if (newValue != null) {
      this.put(key, newValue)
      return newValue
    } else {
      this.remove(key)
      return null
    }
  } else if (newValue != null) {
    this.put(key, newValue)
    return newValue
  }
  return null
}

private fun <K, V> MutableMap<K, V>.default_computeIfAbsent(
  key: K,
  mappingFunction: Function<in K, out V>
): V {
  val oldValue = this[key]
  if (oldValue == null) {
    val newValue = mappingFunction.apply(key)
    if (newValue != null) this.put(key, newValue)
    return newValue
  }
  return oldValue
}

private fun <K, V> MutableMap<K, V>.default_computeIfPresent(
  key: K,
  remappingFunction: BiFunction<in K, in V, out V>
): V? {
  val oldValue = this[key]
  if (oldValue != null) {
    val newValue = remappingFunction.apply(key, oldValue)
    if (newValue != null) this.put(key, newValue) else this.remove(key)
    return newValue
  }
  return null
}

@Suppress("UNCHECKED_CAST")
private fun <K, V> MutableMap<K, V>.default_getOrDefault(key: Any?, defaultValue: V?): V? {
  if (this.containsKey(key as K)) {
    return this[key as K]
  }
  return defaultValue
}

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

private fun <K, V> MutableMap<K, V>.default_putIfAbsent(key: K, value: V): V? {
  return getOrPut(key) { value }
}

@Suppress("UNCHECKED_CAST")
private fun <K, V> MutableMap<K, V>.default_remove(key: Any?, value: Any?): Boolean {
  if (this.containsKey(key as K) && this[key as K] == value as V) {
    this.remove(key as K)
    return true
  } else return false
}

private fun <K, V> MutableMap<K, V>.default_replace(key: K, value: V): V? =
  if (this.containsKey(key)) this.put(key, value) else null

private fun <K, V> MutableMap<K, V>.default_replace(key: K, oldValue: V, newValue: V): Boolean {
  if (this.containsKey(key) && this[key] == oldValue) {
    this[key] = newValue
    return true
  } else return false
}

private fun <K, V> MutableMap<K, V>.default_replaceAll(function: BiFunction<in K, in V, out V>) {
  this.forEach { entry -> function.apply(entry.key, entry.value) }
}
