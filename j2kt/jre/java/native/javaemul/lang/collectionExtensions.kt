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

import java.util.Collection as JavaUtilCollection
import java.util.Map as JavaUtilMap
import java.util.function.Predicate
import java.util.stream.Stream
import java.util.stream.StreamSupport
import javaemul.internal.CollectionHelper

fun <E> MutableCollection<E>.stream(): Stream<E> =
  (this as? JavaUtilCollection<E>)?.run { stream() }
    ?: StreamSupport.stream(spliterator(), parallel = false)

fun <E> MutableCollection<E>.parallelStream(): Stream<E> =
  (this as? JavaUtilCollection<E>)?.run { parallelStream() }
    ?: StreamSupport.stream(spliterator(), parallel = true)

fun <E> MutableCollection<E>.java_addAll(c: MutableCollection<out E>): Boolean = addAll(c)

fun <V> MutableCollection<V>.java_contains(value: Any?): Boolean =
  (this as Collection<Any>).contains(value)

@Suppress("UNCHECKED_CAST")
fun <E> MutableCollection<E>.java_containsAll(c: MutableCollection<*>): Boolean =
  containsAll(c as MutableCollection<E>)

fun <V> MutableCollection<V>.java_remove(value: Any?): Boolean =
  (this as MutableCollection<Any?>).remove(value)

@Suppress("UNCHECKED_CAST")
fun <E> MutableCollection<E>.java_removeAll(c: MutableCollection<*>): Boolean =
  removeAll(c as MutableCollection<E>)

fun <E> MutableCollection<E>.removeIf(filter: Predicate<in E>): Boolean =
  (this as? JavaUtilCollection)?.run { removeIf(filter) } ?: removeAll(filter::test)

@Suppress("UNCHECKED_CAST")
fun <E> MutableCollection<E>.java_retainAll(c: MutableCollection<*>): Boolean =
  retainAll(c as MutableCollection<E>)

fun MutableCollection<*>.toArray(): Array<Any?> =
  (this as? JavaUtilCollection<*>)?.run { toArray() } ?: CollectionHelper.toArray(this)

fun <T> MutableCollection<*>.toArray(a: Array<T>): Array<T> =
  (this as? JavaUtilCollection<*>)?.run { toArray(a) } ?: CollectionHelper.toArray(this, a)

fun <V> MutableList<V>.java_addAll(index: Int, c: MutableCollection<out V>): Boolean =
  addAll(index, c as Collection<V>)

fun <V> MutableList<V>.java_indexOf(value: Any?): Int = (this as MutableList<Any?>).indexOf(value)

fun <V> MutableList<V>.java_lastIndexOf(value: Any?): Int =
  (this as MutableList<Any?>).lastIndexOf(value)

fun <K, V> MutableMap<K, V>.java_containsKey(key: Any?): Boolean =
  (this as MutableMap<Any?, Any?>).containsKey(key)

fun <K, V> MutableMap<K, V>.java_containsValue(value: Any?): Boolean =
  (this as MutableMap<Any?, Any?>).containsValue(value)

fun <K, V> MutableMap<K, V>.java_get(key: Any?): V? = (this as MutableMap<Any?, V>).get(key)

fun <K, V> MutableMap<K, V>.java_getOrDefault(key: Any?, defaultValue: V?): V? =
  (this as MutableMap<Any?, V?>).run {
    if (this is JavaUtilMap) getOrDefault(key, defaultValue)
    else default_getOrDefault(key, defaultValue)
  }

fun <K, V> MutableMap<K, V>.java_putAll(map: MutableMap<out K, out V>): Unit =
  putAll(map as Map<out K, V>)

fun <K, V> MutableMap<K, V>.java_remove(key: Any?): V? = (this as MutableMap<Any?, V>).remove(key)

fun <K, V> MutableMap<K, V>.java_remove(key: Any?, value: Any?): Boolean =
  (this as MutableMap<Any?, Any?>).run {
    if (this is JavaUtilMap) remove(key, value) else default_remove(key, value)
  }
