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
package javaemul.lang

/** Bridge class for java.util.AbstractSet. */
abstract class JavaAbstractSet<E> : AbstractMutableSet<E>(), JavaSet<E> {
  override fun addAll(c: Collection<E>): Boolean = super<JavaSet>.addAll(c)

  override fun contains(e: E): Boolean = super<JavaSet>.contains(e)

  override fun containsAll(c: Collection<E>): Boolean = super<JavaSet>.containsAll(c)

  override fun remove(e: E): Boolean = super<JavaSet>.remove(e)

  override fun removeAll(c: Collection<E>): Boolean = super<JavaSet>.removeAll(c)

  override fun retainAll(c: Collection<E>): Boolean = super<JavaSet>.retainAll(c)

  override fun java_addAll(c: MutableCollection<out E>): Boolean =
    super<AbstractMutableSet>.addAll(c)

  @Suppress("UNCHECKED_CAST")
  override fun java_contains(a: Any?): Boolean = super<AbstractMutableSet>.contains(a as E)

  @Suppress("UNCHECKED_CAST")
  override fun java_containsAll(c: MutableCollection<out Any?>): Boolean =
    super<AbstractMutableSet>.containsAll(c as MutableCollection<E>)

  @Suppress("UNCHECKED_CAST")
  override fun java_remove(a: Any?): Boolean = super<AbstractMutableSet>.remove(a as E)

  @Suppress("UNCHECKED_CAST")
  override fun java_removeAll(c: MutableCollection<out Any?>): Boolean =
    super<AbstractMutableSet>.removeAll(c as MutableCollection<E>)

  @Suppress("UNCHECKED_CAST")
  override fun java_retainAll(c: MutableCollection<out Any?>): Boolean =
    super<AbstractMutableSet>.retainAll(c as MutableCollection<E>)

  override fun add(element: E): Boolean {
    throw UnsupportedOperationException()
  }
}
