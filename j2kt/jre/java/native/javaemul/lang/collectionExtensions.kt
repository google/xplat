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

import java.lang.reflect.Array as JavaLangReflectArray
import java.util.function.Predicate
import java.util.stream.Stream
import java.util.stream.StreamSupport
import kotlin.jvm.javaObjectType

fun <E> MutableCollection<E>.stream(): Stream<E> =
  if (this is JavaCollection) stream() else default_stream()

fun <E> MutableCollection<E>.parallelStream(): Stream<E> =
  if (this is JavaCollection) parallelStream() else default_parallelStream()

fun <E> MutableCollection<E>.java_addAll(c: MutableCollection<out E>): Boolean =
  if (this is JavaCollection) java_addAll(c) else addAll(c)

@Suppress("UNCHECKED_CAST")
fun <E> MutableCollection<E>.java_contains(a: Any?): Boolean =
  if (this is JavaCollection) java_contains(a) else contains(a as E)

@Suppress("UNCHECKED_CAST")
fun <E> MutableCollection<E>.java_containsAll(c: MutableCollection<*>): Boolean =
  if (this is JavaCollection) java_containsAll(c) else containsAll(c as MutableCollection<E>)

@Suppress("UNCHECKED_CAST")
fun <E> MutableCollection<E>.java_remove(a: Any?): Boolean =
  if (this is JavaCollection) java_remove(a) else remove(a as E)

@Suppress("UNCHECKED_CAST")
fun <E> MutableCollection<E>.java_removeAll(c: MutableCollection<*>): Boolean =
  if (this is JavaCollection) java_removeAll(c) else removeAll(c as MutableCollection<E>)

fun <E> MutableCollection<E>.java_removeIf(filter: Predicate<in E>): Boolean =
  if (this is JavaCollection) java_removeIf(filter) else default_removeIf(filter)

@Suppress("UNCHECKED_CAST")
fun <E> MutableCollection<E>.java_retainAll(c: MutableCollection<*>): Boolean =
  if (this is JavaCollection) java_retainAll(c) else retainAll(c as MutableCollection<E>)

fun MutableCollection<*>.java_toArray(): Array<Any?> =
  if (this is JavaCollection) java_toArray() else default_toArray()

fun <T> MutableCollection<*>.java_toArray(a: Array<T>): Array<T> =
  if (this is JavaCollection) java_toArray(a) else default_toArray(a)

internal fun <E> MutableCollection<E>.default_removeIf(filter: Predicate<in E>): Boolean {
  var removed = false
  val i = iterator()
  while (i.hasNext()) {
    if (filter.test(i.next())) {
      i.remove()
      removed = true
    }
  }
  return removed
}

internal fun MutableCollection<*>.default_toArray(): Array<Any?> = toTypedArray()

// Note: There's no relation between the element types of Collection<E> and Array<T> (same as Java).
@Suppress("UNCHECKED_CAST")
internal fun <T> MutableCollection<*>.default_toArray(a: Array<T>): Array<T> {
  if (this.size > a.size) {
    return default_toArray(
      JavaLangReflectArray.newInstance(a::class.javaObjectType.getComponentType(), size) as Array<T>
    )
  } else {
    val iterator = iterator()
    var index = 0
    while (iterator.hasNext()) {
      a[index++] = iterator.next() as T
    }
    if (index < a.size) {
      // Note: This is unsafe. JSpecify (as of Sept 2022) also ignores this case.
      a[index] = null as T
    }
    return a
  }
}
