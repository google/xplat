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

import kotlin.collections.Set as KotlinSet

interface Set<E> : MutableSet<E>, Collection<E> {
  companion object {
    fun <E> of(): KotlinSet<E> = createInternal<E>()

    fun <E : Any> of(e1: E): KotlinSet<E> = createInternal(e1)

    fun <E : Any> of(e1: E, e2: E): KotlinSet<E> = createInternal(e1, e2)

    fun <E : Any> of(e1: E, e2: E, e3: E): KotlinSet<E> = createInternal(e1, e2, e3)

    fun <E : Any> of(e1: E, e2: E, e3: E, e4: E): KotlinSet<E> = createInternal(e1, e2, e3, e4)

    fun <E : Any> of(e1: E, e2: E, e3: E, e4: E, e5: E): KotlinSet<E> =
      createInternal(e1, e2, e3, e4, e5)

    fun <E : Any> of(e1: E, e2: E, e3: E, e4: E, e5: E, e6: E): KotlinSet<E> =
      createInternal(e1, e2, e3, e4, e5, e6)

    fun <E : Any> of(e1: E, e2: E, e3: E, e4: E, e5: E, e6: E, e7: E): KotlinSet<E> =
      createInternal(e1, e2, e3, e4, e5, e6, e7)

    fun <E : Any> of(e1: E, e2: E, e3: E, e4: E, e5: E, e6: E, e7: E, e8: E): KotlinSet<E> =
      createInternal(e1, e2, e3, e4, e5, e6, e7, e8)

    fun <E : Any> of(e1: E, e2: E, e3: E, e4: E, e5: E, e6: E, e7: E, e8: E, e9: E): KotlinSet<E> =
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
    ): KotlinSet<E> = createInternal(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10)

    fun <E : Any> of(vararg elements: E): KotlinSet<E> = createInternal(*elements)

    private fun <E> createInternal(vararg elements: E): KotlinSet<E> = buildSet {
      for (element in elements) {
        if (!add(element)) {
          throw IllegalArgumentException("Duplicate element: $element")
        }
      }
    }

    fun <E : Any> copyOf(coll: kotlin.collections.Collection<out E>): KotlinSet<E> = buildSet {
      for (element in coll) {
        add(Objects.requireNonNull(element))
      }
    }
  }
}
