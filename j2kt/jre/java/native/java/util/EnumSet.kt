@file:OptIn(ExperimentalObjCRefinement::class)

/*
 * Copyright 2026 Google Inc.
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
package java.util

import java.lang.Class
import javaemul.internal.InternalPreconditions.Companion.checkArgument
import javaemul.internal.InternalPreconditions.Companion.checkElement
import javaemul.internal.InternalPreconditions.Companion.checkState
import kotlin.Cloneable
import kotlin.collections.Collection as KotlinCollection
import kotlin.collections.HashSet as KotlinHashSet
import kotlin.enums.enumEntries
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

/**
 * J2KT compatible implementation of EnumSet. Notably, some methods are unsupported since
 * Enum#getDeclaringClass is unsupported for code size reasons.
 */
class EnumSet<E : Enum<E>> internal constructor() : AbstractSet<E>(), Cloneable {
  private val set = KotlinHashSet<E>()

  override fun add(element: E): Boolean = set.add(element)

  override fun remove(element: E): Boolean = set.remove(element)

  override fun contains(element: E): Boolean = set.contains(element)

  override val size: Int
    get() = set.size

  override fun clear() = set.clear()

  override fun iterator(): MutableIterator<E> {
    val sortedEnums = set.sorted()
    return object : MutableIterator<E> {
      private var i = 0
      private var last = -1

      override fun hasNext(): Boolean = i < sortedEnums.size

      override fun next(): E {
        checkElement(hasNext())

        last = i++
        return sortedEnums[last]
      }

      override fun remove() {
        checkState(last != -1)

        this@EnumSet.remove(sortedEnums[last])
        last = -1
      }
    }
  }

  override fun clone(): EnumSet<E> = copyOf(this)

  companion object {
    fun <E : Enum<E>> of(first: E): EnumSet<E> {
      val enumSet = EnumSet<E>()
      enumSet.add(first)
      return enumSet
    }

    fun <E : Enum<E>> of(first: E, vararg rest: E): EnumSet<E> {
      val enumSet = EnumSet<E>()
      enumSet.add(first)
      for (e in rest) {
        enumSet.add(e)
      }
      return enumSet
    }

    fun <E : Enum<E>> copyOf(c: KotlinCollection<E>): EnumSet<E> {
      checkArgument(c is EnumSet<*> || !c.isEmpty(), "Collection is empty")
      val enumSet = EnumSet<E>()
      for (e in c) {
        enumSet.add(e)
      }
      return enumSet
    }

    fun <E : Enum<E>> noneOf(elementType: Class<E>): EnumSet<E> = EnumSet()

    // The reified type parameter lets us recover the enum constants from the static type of the
    // call site, since Class.getEnumConstants() is not supported on Kotlin Native.
    // Storing enum constants in emulated Class objects is not done as the code size overhead
    // would defeat the benefits of J2KT optimizations.
    @HiddenFromObjC
    inline fun <reified E : Enum<E>> allOf(elementType: Class<E>): EnumSet<E> =
      noneOf(elementType).apply { addAll(enumEntries<E>()) }
  }
}
