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

fun <E> MutableCollection<E>.java_contains(a: Any?): Boolean = asJavaUtilCollection().contains(a)

fun <E> MutableCollection<E>.java_containsAll(c: MutableCollection<*>): Boolean =
  asJavaUtilCollection().containsAll(c)

fun <E> MutableCollection<E>.java_remove(a: Any?): Boolean = asJavaUtilCollection().remove(a)

fun <E> MutableCollection<E>.java_removeAll(c: MutableCollection<*>): Boolean =
  asJavaUtilCollection().removeAll(c)

fun <E> MutableCollection<E>.java_retainAll(c: MutableCollection<*>): Boolean =
  asJavaUtilCollection().retainAll(c)

fun MutableCollection<*>.java_toArray(): Array<Any?> = default_toArray()

fun <T> MutableCollection<*>.java_toArray(a: Array<T>): Array<T> = default_toArray(a)

internal fun MutableCollection<*>.default_toArray(): Array<Any?> = asJavaUtilCollection().toArray()

internal fun <T> MutableCollection<*>.default_toArray(a: Array<T>): Array<T> =
  asJavaUtilCollection().toArray(a)

@Suppress("UNCHECKED_CAST")
private fun <E> MutableCollection<E>.asJavaUtilCollection(): java.util.Collection<E> =
  this as java.util.Collection<E>
