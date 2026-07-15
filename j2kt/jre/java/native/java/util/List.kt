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
package java.util

import java.util.Collections.Companion.unmodifiableList
import java.util.function.UnaryOperator
import kotlin.Comparator as KotlinComparator
import kotlin.collections.List as KotlinList
import kotlin.collections.replaceAll as kotlinReplaceAll

interface List<E> : MutableList<E>, Collection<E> {
  fun replaceAll(operator: UnaryOperator<E>): Unit = kotlinReplaceAll(operator::apply)

  fun sort(c: KotlinComparator<in E>?): Unit =
    if (c == null) {
      @Suppress("UNCHECKED_CAST") sortBy { it as Comparable<Any> }
    } else {
      sortWith(c)
    }

  fun getFirst(): E = if (isEmpty()) throw NoSuchElementException() else get(0)

  fun getLast(): E = if (isEmpty()) throw NoSuchElementException() else get(size - 1)

  companion object {
    fun <E> of(): KotlinList<E> = createInternal<E>()

    fun <E : Any> of(e1: E): KotlinList<E> = createInternal(e1)

    fun <E : Any> of(e1: E, e2: E): KotlinList<E> = createInternal(e1, e2)

    fun <E : Any> of(e1: E, e2: E, e3: E): KotlinList<E> = createInternal(e1, e2, e3)

    fun <E : Any> of(e1: E, e2: E, e3: E, e4: E): KotlinList<E> = createInternal(e1, e2, e3, e4)

    fun <E : Any> of(e1: E, e2: E, e3: E, e4: E, e5: E): KotlinList<E> =
      createInternal(e1, e2, e3, e4, e5)

    fun <E : Any> of(e1: E, e2: E, e3: E, e4: E, e5: E, e6: E): KotlinList<E> =
      createInternal(e1, e2, e3, e4, e5, e6)

    fun <E : Any> of(e1: E, e2: E, e3: E, e4: E, e5: E, e6: E, e7: E): KotlinList<E> =
      createInternal(e1, e2, e3, e4, e5, e6, e7)

    fun <E : Any> of(e1: E, e2: E, e3: E, e4: E, e5: E, e6: E, e7: E, e8: E): KotlinList<E> =
      createInternal(e1, e2, e3, e4, e5, e6, e7, e8)

    fun <E : Any> of(e1: E, e2: E, e3: E, e4: E, e5: E, e6: E, e7: E, e8: E, e9: E): KotlinList<E> =
      createInternal(e1, e2, e3, e4, e5, e6, e7, e8, e9)

    fun <E : Any> of(
      e1: E,
      e2: E,
      e3: E,
      e4: E,
      e5: E,
      e6: E,
      e7: E,
      e8: E,
      e9: E,
      e10: E,
    ): KotlinList<E> = createInternal(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10)

    fun <E : Any> of(vararg elements: E): KotlinList<E> = return createInternal(*elements)

    private fun <E> createInternal(vararg elements: E): KotlinList<E> =
      unmodifiableList(listOf(*elements))

    fun <E : Any> copyOf(coll: kotlin.collections.Collection<out E>): KotlinList<E> = buildList {
      for (element in coll) {
        add(Objects.requireNonNull(element))
      }
    }
  }
}
