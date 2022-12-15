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

import java.util.Spliterator

abstract class JavaAbstractCollection<E> : AbstractMutableCollection<E>(), JavaCollection<E> {
  override fun addAll(c: Collection<E>): Boolean = super<JavaCollection>.addAll(c)

  override fun contains(e: E): Boolean = super<JavaCollection>.contains(e)

  override fun containsAll(c: Collection<E>): Boolean = super<JavaCollection>.containsAll(c)

  override fun remove(e: E): Boolean = super<JavaCollection>.remove(e)

  override fun removeAll(c: Collection<E>): Boolean = super<JavaCollection>.removeAll(c)

  override fun retainAll(c: Collection<E>): Boolean = super<JavaCollection>.retainAll(c)

  override fun spliterator(): Spliterator<E> = super<JavaCollection>.spliterator()

  override fun add(e: E): Boolean = throw UnsupportedOperationException()

  override fun clear(): Unit = throw UnsupportedOperationException()

  override fun java_addAll(c: MutableCollection<out E>): Boolean =
    super<AbstractMutableCollection>.addAll(c)

  @Suppress("UNCHECKED_CAST")
  override fun java_contains(a: Any?): Boolean = super<AbstractMutableCollection>.contains(a as E)

  @Suppress("UNCHECKED_CAST")
  override fun java_containsAll(c: MutableCollection<*>): Boolean =
    super<AbstractMutableCollection>.containsAll(c as MutableCollection<E>)

  @Suppress("UNCHECKED_CAST")
  override fun java_remove(a: Any?): Boolean = throw UnsupportedOperationException()

  @Suppress("UNCHECKED_CAST")
  override fun java_removeAll(c: MutableCollection<*>): Boolean =
    super<AbstractMutableCollection>.removeAll(c as MutableCollection<E>)

  @Suppress("UNCHECKED_CAST")
  override fun java_retainAll(c: MutableCollection<*>): Boolean =
    super<AbstractMutableCollection>.retainAll(c as MutableCollection<E>)
}
