/*
 * Copyright 2023 Google Inc.
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
package javaemul.lang

import java.util.Map as JavaUtilMap
import java.util.function.BiConsumer
import java.util.function.BiFunction
import java.util.function.Function

fun <K, V> MutableMap<K, V>.compute(key: K, mappingFunction: BiFunction<in K, in V?, out V?>): V? =
  if (this is JavaUtilMap) (this as JavaUtilMap<K, V>).compute(key, mappingFunction)
  else default_compute(key, mappingFunction)

fun <K, V> MutableMap<K, V>.computeIfAbsent(key: K, mappingFunction: Function<in K, out V>): V =
  if (this is JavaUtilMap) (this as JavaUtilMap<K, V>).computeIfAbsent(key, mappingFunction)
  else default_computeIfAbsent(key, mappingFunction)

fun <K, V> MutableMap<K, V>.computeIfPresent(
  key: K,
  mappingFunction: BiFunction<in K, in V & Any, out V?>,
): V? =
  if (this is JavaUtilMap) computeIfPresent(key, mappingFunction)
  else default_computeIfPresent(key, mappingFunction)

fun <K, V> MutableMap<K, V>.forEach(action: BiConsumer<in K, in V>) =
  if (this is JavaUtilMap) (this as JavaUtilMap<K, V>).forEach(action) else default_forEach(action)

fun <K, V> MutableMap<K, V>.merge(
  key: K,
  value: V & Any,
  remap: BiFunction<in V & Any, in V & Any, out V?>,
): V? = if (this is JavaUtilMap) merge(key, value, remap) else default_merge(key, value, remap)

fun <K, V> MutableMap<K, V>.putIfAbsent(key: K, value: V): V? =
  if (this is JavaUtilMap) (this as JavaUtilMap<K, V>).putIfAbsent(key, value)
  else default_putIfAbsent(key, value)

fun <K, V> MutableMap<K, V>.replace(key: K, value: V): V? =
  if (this is JavaUtilMap) (this as JavaUtilMap<K, V>).replace(key, value)
  else default_replace(key, value)

fun <K, V> MutableMap<K, V>.replace(key: K, oldValue: V, newValue: V): Boolean =
  if (this is JavaUtilMap) (this as JavaUtilMap<K, V>).replace(key, oldValue, newValue)
  else default_replace(key, oldValue, newValue)

fun <K, V> MutableMap<K, V>.replaceAll(function: BiFunction<in K, in V, out V>) =
  if (this is JavaUtilMap) (this as JavaUtilMap<K, V>).replaceAll(function)
  else default_replaceAll(function)

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

internal fun <K, V> Map<K, V>.default_getOrDefault(key: K, defaultValue: V): V {
  @Suppress("UNCHECKED_CAST")
  return this[key].let { if (it != null || containsKey(key)) it as V else defaultValue }
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

internal fun <K, V> MutableMap<K, V>.default_putAll(t: MutableMap<out K, out V>) {
  putAll(t.toList())
}

internal fun <K, V> MutableMap<K, V>.default_putIfAbsent(key: K, value: V): V? {
  var v: V? = get(key)
  if (v == null) {
    v = put(key, value)
  }
  return v
}

fun <K, V> MutableMap<K, V>.default_remove(key: K, value: V): Boolean {
  if (containsKey(key) && this[key] == value) {
    remove(key)
    return true
  } else {
    return false
  }
}

internal fun <K, V> MutableMap<K, V>.default_replace(key: K, value: V): V? =
  if (this.containsKey(key)) this.put(key, value) else null

internal fun <K, V> MutableMap<K, V>.default_replace(key: K, oldValue: V, newValue: V): Boolean {
  if (this.containsKey(key) && this[key] == oldValue) {
    this[key] = newValue
    return true
  } else {
    return false
  }
}

internal fun <K, V> MutableMap<K, V>.default_replaceAll(function: BiFunction<in K, in V, out V>) {
  forEach { entry -> this[entry.key] = function.apply(entry.key, entry.value) }
}
