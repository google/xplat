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
import javaemul.internal.CollectionHelper
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

/** Bridge class for java.util.Collection. */
@ObjCName("JavaemulLangJavaCollection", exact = true)
interface JavaCollection<E> : MutableCollection<E>, JavaIterable<E> {
  fun removeIf(filter: Predicate<in E>): Boolean = default_removeIf(filter)

  override fun spliterator(): Spliterator<E> = super<JavaIterable>.spliterator()

  fun stream(): Stream<E> = default_stream()

  fun parallelStream(): Stream<E> = default_parallelStream()

  fun toArray(): Array<Any?>

  fun <T> toArray(a: Array<T>): Array<T>
}

internal fun <E> MutableCollection<E>.default_removeIf(filter: Predicate<in E>): Boolean =
  removeAll(filter::test)

internal fun <E> MutableCollection<E>.default_stream(): Stream<E> =
  StreamSupport.stream(spliterator(), /* parallel= */ false)

internal fun <E> MutableCollection<E>.default_parallelStream(): Stream<E> =
  StreamSupport.stream(spliterator(), /* parallel= */ true)

internal fun MutableCollection<*>.default_toArray(): Array<Any?> = CollectionHelper.toArray(this)

// Note: There's no relation between the element types of Collection<E> and Array<T> (same as Java).
internal fun <T> MutableCollection<*>.default_toArray(a: Array<T>): Array<T> =
  CollectionHelper.toArray(this, a)
