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

import java.util.Spliterator
import java.util.function.Predicate
import java.util.stream.Stream
import java.util.stream.StreamSupport
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

/** Bridge class for java.util.Collection. */
@ObjCName("JavaemulLangJavaCollection", exact = true)
interface JavaCollection<E> : MutableCollection<E>, JavaIterable<E> {
  // TODO(b/243046587): Rewrite to handle case in which c is not mutable
  override fun addAll(c: Collection<E>): Boolean = java_addAll(c as MutableCollection<E>)

  override fun contains(e: E): Boolean = java_contains(e)

  override fun containsAll(c: Collection<E>): Boolean = java_containsAll(c as MutableCollection<E>)

  override fun remove(e: E): Boolean = java_remove(e)

  override fun removeAll(c: Collection<E>): Boolean = java_removeAll(c as MutableCollection<E>)

  override fun retainAll(c: Collection<E>): Boolean = java_retainAll(c as MutableCollection<E>)

  // TODO(233944334): On JVM, MutableCollection has a hidden implementation of spliterator.
  override fun spliterator(): Spliterator<E> = super<JavaIterable>.spliterator()

  fun stream(): Stream<E> = default_stream()

  fun parallelStream(): Stream<E> = default_parallelStream()

  fun java_addAll(c: MutableCollection<out E>): Boolean

  fun java_contains(a: Any?): Boolean

  fun java_containsAll(c: MutableCollection<*>): Boolean

  fun java_remove(a: Any?): Boolean

  fun java_removeAll(c: MutableCollection<*>): Boolean

  fun java_removeIf(filter: Predicate<in E>): Boolean = default_removeIf(filter)

  fun java_retainAll(c: MutableCollection<*>): Boolean

  fun java_toArray(): Array<Any?>

  fun <T> java_toArray(a: Array<T>): Array<T>
}

internal fun <E> MutableCollection<E>.default_stream(): Stream<E> =
  StreamSupport.stream(spliterator(), /* parallel= */ false)

internal fun <E> MutableCollection<E>.default_parallelStream(): Stream<E> =
  StreamSupport.stream(spliterator(), /* parallel= */ true)
