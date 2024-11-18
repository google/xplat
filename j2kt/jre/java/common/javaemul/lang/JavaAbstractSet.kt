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

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

/** Bridge class for java.util.AbstractSet. */
@ObjCName("JavaemulLangJavaAbstractSet", exact = true)
abstract class JavaAbstractSet<E> : AbstractMutableSet<E>(), JavaSet<E> {
  override fun addAll(c: Collection<E>): Boolean = super<JavaSet>.addAll(c)

  override fun containsAll(c: Collection<E>): Boolean = super<JavaSet>.containsAll(c)

  override fun removeAll(c: Collection<E>): Boolean = super<JavaSet>.removeAll(c)

  override fun retainAll(c: Collection<E>): Boolean = super<JavaSet>.retainAll(c)

  override fun java_addAll(c: MutableCollection<out E>): Boolean =
    super<AbstractMutableSet>.addAll(c)

  @Suppress("UNCHECKED_CAST")
  override fun java_containsAll(c: MutableCollection<out Any?>): Boolean =
    super<AbstractMutableSet>.containsAll(c as MutableCollection<E>)

  @Suppress("UNCHECKED_CAST")
  override fun java_removeAll(c: MutableCollection<out Any?>): Boolean =
    super<AbstractMutableSet>.removeAll(c as MutableCollection<E>)

  @Suppress("UNCHECKED_CAST")
  override fun java_retainAll(c: MutableCollection<out Any?>): Boolean =
    super<AbstractMutableSet>.retainAll(c as MutableCollection<E>)

  override fun add(element: E): Boolean {
    throw UnsupportedOperationException()
  }

  override fun java_toArray(): Array<Any?> = default_toArray()

  override fun <T> java_toArray(a: Array<T>): Array<T> = default_toArray(a)
}
