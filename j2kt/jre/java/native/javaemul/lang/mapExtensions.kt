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

import java.util.function.BiConsumer
import java.util.function.BiFunction
import java.util.function.Function

fun <K, V> MutableMap<K, V>.compute(key: K, mappingFunction: BiFunction<in K, in V?, out V?>): V? =
  if (this is JavaMap) (this as JavaMap<K, V>).compute(key, mappingFunction)
  else default_compute(key, mappingFunction)

fun <K, V> MutableMap<K, V>.computeIfAbsent(key: K, mappingFunction: Function<in K, out V>): V =
  if (this is JavaMap) (this as JavaMap<K, V>).computeIfAbsent(key, mappingFunction)
  else default_computeIfAbsent(key, mappingFunction)

fun <K, V> MutableMap<K, V>.forEach(action: BiConsumer<in K, in V>) =
  if (this is JavaMap) (this as JavaMap<K, V>).forEach(action) else default_forEach(action)

fun <K, V> MutableMap<K, V>.putIfAbsent(key: K, value: V): V? =
  if (this is JavaMap) (this as JavaMap<K, V>).putIfAbsent(key, value)
  else default_putIfAbsent(key, value)

fun <K, V> MutableMap<K, V>.replace(key: K, value: V): V? =
  if (this is JavaMap) (this as JavaMap<K, V>).replace(key, value) else default_replace(key, value)

fun <K, V> MutableMap<K, V>.replace(key: K, oldValue: V, newValue: V): Boolean =
  if (this is JavaMap) (this as JavaMap<K, V>).replace(key, oldValue, newValue)
  else default_replace(key, oldValue, newValue)

fun <K, V> MutableMap<K, V>.replaceAll(function: BiFunction<in K, in V, out V>) =
  if (this is JavaMap) (this as JavaMap<K, V>).replaceAll(function)
  else default_replaceAll(function)
