@file:OptIn(ExperimentalObjCRefinement::class)

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
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun <E> Collection<E>.stream(): Stream<E> =
  (this as? JavaUtilCollection<E>)?.run { stream() }
    ?: StreamSupport.stream(spliterator(), parallel = false)

@HiddenFromObjC
fun <E> Collection<E>.parallelStream(): Stream<E> =
  (this as? JavaUtilCollection<E>)?.run { parallelStream() }
    ?: StreamSupport.stream(spliterator(), parallel = true)

@Suppress("UNCHECKED_CAST")
@HiddenFromObjC
fun <V> Collection<V>.java_contains(value: Any?): Boolean =
  (this as Collection<Any>).contains(value)

@Suppress("UNCHECKED_CAST")
@HiddenFromObjC
fun <E> Collection<E>.java_containsAll(c: Collection<*>): Boolean = containsAll(c as Collection<E>)

@Suppress("UNCHECKED_CAST")
@HiddenFromObjC
fun <V> MutableCollection<V>.java_remove(value: Any?): Boolean =
  (this as MutableCollection<Any?>).remove(value)

@Suppress("UNCHECKED_CAST")
@HiddenFromObjC
fun <E> MutableCollection<E>.java_removeAll(c: Collection<*>): Boolean =
  removeAll(c as MutableCollection<E>)

@HiddenFromObjC
fun <E> MutableCollection<E>.removeIf(filter: Predicate<in E>): Boolean =
  (this as? JavaUtilCollection)?.run { removeIf(filter) } ?: removeAll(filter::test)

@Suppress("UNCHECKED_CAST")
@HiddenFromObjC
fun <E> MutableCollection<E>.java_retainAll(c: Collection<*>): Boolean =
  retainAll(c as Collection<E>)

@HiddenFromObjC
fun Collection<*>.toArray(): Array<Any?> =
  (this as? JavaUtilCollection<*>)?.run { toArray() } ?: CollectionHelper.toArray(this)

@HiddenFromObjC
fun <T> Collection<*>.toArray(a: Array<T>): Array<T> =
  (this as? JavaUtilCollection<*>)?.run { toArray(a) } ?: CollectionHelper.toArray(this, a)

@HiddenFromObjC
internal inline fun <reified T> Any.asMutable(): T =
  this as? T ?: throw UnsupportedOperationException()

@HiddenFromObjC fun <T> Collection<T>.asMutableCollection(): MutableCollection<T> = asMutable()

@HiddenFromObjC fun <T> MutableCollection<T>.asMutableCollection(): MutableCollection<T> = this

@HiddenFromObjC fun <V> List<V>.java_indexOf(value: Any?): Int = (this as List<Any?>).indexOf(value)

@HiddenFromObjC
fun <V> List<V>.java_lastIndexOf(value: Any?): Int = (this as List<Any?>).lastIndexOf(value)

@HiddenFromObjC fun <V> Set<V>.asMutableSet(): MutableSet<V> = asMutable()

@HiddenFromObjC fun <V> MutableSet<V>.asMutableSet(): MutableSet<V> = this

@Suppress("UNCHECKED_CAST")
@HiddenFromObjC
fun <K, V> Map<K, V>.java_containsKey(key: Any?): Boolean =
  (this as Map<Any?, Any?>).containsKey(key)

@Suppress("UNCHECKED_CAST")
@HiddenFromObjC
fun <K, V> Map<K, V>.java_containsValue(value: Any?): Boolean =
  (this as Map<Any?, Any?>).containsValue(value)

@Suppress("UNCHECKED_CAST")
@HiddenFromObjC
fun <K, V> Map<K, V>.java_get(key: Any?): V? = (this as Map<Any?, V>).get(key)

@Suppress("UNCHECKED_CAST")
@HiddenFromObjC
fun <K, V> Map<K, V>.java_getOrDefault(key: Any?, defaultValue: V?): V? =
  (this as Map<Any?, V?>).run {
    if (this is JavaUtilMap) getOrDefault(key, defaultValue)
    else default_getOrDefault(key, defaultValue)
  }

@Suppress("UNCHECKED_CAST")
@HiddenFromObjC
fun <K, V> MutableMap<K, V>.java_remove(key: Any?): V? = (this as MutableMap<Any?, V>).remove(key)

@Suppress("UNCHECKED_CAST")
@HiddenFromObjC
fun <K, V> MutableMap<K, V>.java_remove(key: Any?, value: Any?): Boolean =
  (this as MutableMap<Any?, Any?>).run {
    if (this is JavaUtilMap) remove(key, value) else default_remove(key, value)
  }
