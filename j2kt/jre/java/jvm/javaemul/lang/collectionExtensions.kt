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

fun <E> MutableCollection<E>.java_addAll(c: MutableCollection<out E>): Boolean =
  asJavaUtilCollection().addAll(c)

fun <V> Collection<V>.java_contains(value: Any?): Boolean = asJavaUtilCollection().contains(value)

fun <E> Collection<E>.java_containsAll(c: Collection<*>): Boolean =
  asJavaUtilCollection().containsAll(c)

fun <V> MutableCollection<V>.java_remove(value: Any?): Boolean =
  asJavaUtilCollection().remove(value)

fun <E> MutableCollection<E>.java_removeAll(c: Collection<*>): Boolean =
  asJavaUtilCollection().removeAll(c)

fun <E> MutableCollection<E>.java_retainAll(c: Collection<*>): Boolean =
  asJavaUtilCollection().retainAll(c)

fun Collection<*>.toArray(): Array<Any?> = asJavaUtilCollection().toArray()

fun <T> Collection<*>.toArray(a: Array<T>): Array<T> = asJavaUtilCollection().toArray(a)

@Suppress("UNCHECKED_CAST")
private fun <E> Collection<E>.asJavaUtilCollection(): java.util.Collection<E> =
  this as java.util.Collection<E>

@Suppress("UNCHECKED_CAST")
private fun <E> List<E>.asJavaUtilList(): java.util.List<E> = this as java.util.List<E>

@Suppress("UNCHECKED_CAST")
private fun <K, V> Map<K, V>.asJavaUtilMap(): java.util.Map<K, V> = this as java.util.Map<K, V>

fun <V> MutableList<V>.java_addAll(index: Int, c: Collection<V>): Boolean =
  asJavaUtilList().addAll(index, c)

fun <V> List<V>.java_indexOf(value: Any?): Int = asJavaUtilList().indexOf(value)

fun <V> List<V>.java_lastIndexOf(value: Any?): Int = asJavaUtilList().lastIndexOf(value)

fun <V> MutableList<V>.sort(c: Comparator<in V>?): Unit = asJavaUtilList().sort(c)

fun <K, V> MutableMap<K, V>.java_putAll(map: MutableMap<out K, out V>): Unit =
  asJavaUtilMap().putAll(map)

fun <K, V> Map<K, V>.java_containsKey(key: Any?): Boolean = asJavaUtilMap().containsKey(key)

fun <K, V> Map<K, V>.java_containsValue(value: Any?): Boolean = asJavaUtilMap().containsValue(value)

fun <K, V> Map<K, V>.java_get(key: Any?): V? = asJavaUtilMap().get(key)

fun <K, V> Map<K, V>.java_getOrDefault(key: Any?, defaultValue: V?): V? =
  asJavaUtilMap().getOrDefault(key, defaultValue)

fun <K, V> MutableMap<K, V>.java_remove(key: Any?): V? = asJavaUtilMap().remove(key)

fun <K, V> MutableMap<K, V>.java_remove(key: Any?, value: Any?): Boolean =
  asJavaUtilMap().remove(key as K, value as V)
