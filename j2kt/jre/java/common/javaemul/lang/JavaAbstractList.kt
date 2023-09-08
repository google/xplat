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
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

/** Bridge class for java.util.AbstractList. */
@ObjCName("JavaemulLangJavaAbstractList", exact = true)
abstract class JavaAbstractList<E> : AbstractMutableList<E>(), JavaList<E> {
  override fun addAll(index: Int, c: Collection<E>) = super<JavaList>.addAll(index, c)

  override fun addAll(c: Collection<E>): Boolean = super<JavaList>.addAll(c)

  override fun contains(e: E): Boolean = super<JavaList>.contains(e)

  override fun containsAll(c: Collection<E>): Boolean = super<JavaList>.containsAll(c)

  override fun indexOf(e: E): Int = super<JavaList>.indexOf(e)

  override fun lastIndexOf(e: E): Int = super<JavaList>.lastIndexOf(e)

  override fun remove(e: E): Boolean = super<JavaList>.remove(e)

  override fun removeAll(c: Collection<E>): Boolean = super<JavaList>.removeAll(c)

  override fun retainAll(c: Collection<E>): Boolean = super<JavaList>.retainAll(c)

  override fun spliterator(): Spliterator<E> = super<JavaList>.spliterator()

  override fun java_addAll(index: Int, c: MutableCollection<out E>): Boolean =
    super<AbstractMutableList>.addAll(index, c)

  override fun java_addAll(c: MutableCollection<out E>): Boolean =
    super<AbstractMutableList>.addAll(c)

  @Suppress("UNCHECKED_CAST")
  override fun java_contains(a: Any?): Boolean = super<AbstractMutableList>.contains(a as E)

  @Suppress("UNCHECKED_CAST")
  override fun java_containsAll(c: MutableCollection<out Any?>): Boolean =
    super<AbstractMutableList>.containsAll(c as MutableCollection<E>)

  @Suppress("UNCHECKED_CAST")
  override fun java_indexOf(a: Any?): Int = super<AbstractMutableList>.indexOf(a as E)

  @Suppress("UNCHECKED_CAST")
  override fun java_lastIndexOf(a: Any?): Int = super<AbstractMutableList>.lastIndexOf(a as E)

  @Suppress("UNCHECKED_CAST")
  override fun java_remove(a: Any?): Boolean = super<AbstractMutableList>.remove(a as E)

  @Suppress("UNCHECKED_CAST")
  override fun java_removeAll(c: MutableCollection<out Any?>): Boolean =
    super<AbstractMutableList>.removeAll(c as MutableCollection<E>)

  @Suppress("UNCHECKED_CAST")
  override fun java_retainAll(c: MutableCollection<out Any?>): Boolean =
    super<AbstractMutableList>.retainAll(c as MutableCollection<E>)

  override fun add(index: Int, element: E) {
    throw UnsupportedOperationException()
  }
  override fun set(index: Int, element: E): E {
    throw UnsupportedOperationException()
  }
  override fun removeAt(index: Int): E {
    throw UnsupportedOperationException()
  }

  override fun java_toArray(): Array<Any?> = default_toArray()

  override fun <T> java_toArray(a: Array<T>): Array<T> = default_toArray(a)
}
