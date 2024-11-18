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

import java.util.function.BiConsumer
import java.util.function.BiFunction
import java.util.function.Function
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@ObjCName("JavaemulLangJavaMap", exact = true)
interface JavaMap<K, V> : MutableMap<K, V> {

  // TODO(b/243046587): Rewrite to handle case in which t is not mutable
  override fun putAll(t: Map<out K, V>) = java_putAll(t as MutableMap<K, V>)

  fun compute(key: K, remappingFunction: BiFunction<in K, in V?, out V?>): V? =
    default_compute(key, remappingFunction)

  fun computeIfAbsent(key: K, mappingFunction: Function<in K, out V>): V =
    default_computeIfAbsent(key, mappingFunction)

  fun computeIfPresent(key: K, remappingFunction: BiFunction<in K, in V & Any, out V?>): V? =
    default_computeIfPresent(key, remappingFunction)

  fun forEach(action: BiConsumer<in K, in V>) = default_forEach(action)

  fun putIfAbsent(key: K, value: V): V? = default_putIfAbsent(key, value)

  fun replace(key: K, value: V): V? = default_replace(key, value)

  fun replace(key: K, oldValue: V, newValue: V): Boolean = default_replace(key, oldValue, newValue)

  fun replaceAll(function: BiFunction<in K, in V, out V>) = default_replaceAll(function)

  fun java_getOrDefault(key: Any?, defaultValue: V?): V? = default_getOrDefault(key, defaultValue)

  fun merge(key: K, value: V & Any, remap: BiFunction<in V & Any, in V & Any, out V?>): V? =
    default_merge(key, value, remap)

  fun java_putAll(t: MutableMap<out K, out V>)

  fun java_remove(key: Any?, value: Any?): Boolean = default_remove(key, value)
}

// Note: No need to check for the runtime type below. The bridge interface is
// only necessary to allow overriding with the Java signature. Calling with the
// Java signature does not require any trampolines, only manual type erasure.

@Suppress("UNCHECKED_CAST")
fun <K, V> MutableMap<K, V>.java_putAll(t: MutableMap<out K, out V>) =
  if (this is JavaMap) (this as JavaMap).putAll(t) else default_putAll(t)

fun <K, V> MutableMap<K, V>.java_getOrDefault(key: Any?, defaultValue: V?): V? =
  if (this is JavaMap) java_getOrDefault(key, defaultValue)
  else default_getOrDefault(key, defaultValue)

fun <K, V> MutableMap<K, V>.java_remove(key: Any?, value: Any?): Boolean =
  if (this is JavaMap) java_remove(key, value) else default_remove(key, value)

internal inline fun <K, V> MutableMap<K, V>.default_forEach(action: BiConsumer<in K, in V>) {
  this.forEach { entry -> action.accept(entry.key, entry.value) }
}

internal fun <K, V> MutableMap<K, V>.default_compute(
  key: K,
  remappingFunction: BiFunction<in K, in V?, out V?>,
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

internal fun <K, V> MutableMap<K, V>.default_computeIfAbsent(
  key: K,
  mappingFunction: Function<in K, out V>,
): V {
  val oldValue = this[key]
  if (oldValue == null) {
    val newValue = mappingFunction.apply(key)
    if (newValue != null) this.put(key, newValue)
    return newValue
  }
  return oldValue
}

internal fun <K, V> MutableMap<K, V>.default_computeIfPresent(
  key: K,
  remappingFunction: BiFunction<in K, in V & Any, out V?>,
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
internal fun <K, V> MutableMap<K, V>.default_getOrDefault(key: Any?, defaultValue: V?): V? {
  if (this.containsKey(key as K)) {
    return this[key as K]
  }
  return defaultValue
}

internal fun <K, V> MutableMap<K, V>.default_merge(
  key: K,
  value: V & Any,
  remap: BiFunction<in V & Any, in V & Any, out V?>,
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

internal fun <K, V> MutableMap<K, V>.default_putAll(t: MutableMap<out K, out V>) =
  putAll(t.toList())

internal fun <K, V> MutableMap<K, V>.default_putIfAbsent(key: K, value: V): V? {
  var v: V? = get(key)
  if (v == null) {
    v = put(key, value)
  }
  return v
}

@Suppress("UNCHECKED_CAST")
private fun <K, V> MutableMap<K, V>.default_remove(key: Any?, value: Any?): Boolean {
  if (this.containsKey(key as K) && this[key as K] == value as V) {
    this.remove(key as K)
    return true
  } else return false
}

internal fun <K, V> MutableMap<K, V>.default_replace(key: K, value: V): V? =
  if (this.containsKey(key)) this.put(key, value) else null

internal fun <K, V> MutableMap<K, V>.default_replace(key: K, oldValue: V, newValue: V): Boolean {
  if (this.containsKey(key) && this[key] == oldValue) {
    this[key] = newValue
    return true
  } else return false
}

internal fun <K, V> MutableMap<K, V>.default_replaceAll(function: BiFunction<in K, in V, out V>) {
  this.forEach { entry -> this[entry.key] = function.apply(entry.key, entry.value) }
}
