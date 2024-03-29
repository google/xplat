/*
 * Copyright 2022 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *a
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package javaemul.internal

internal object Comparators {

  /**
   * Returns the given comparator if it is non-null; a natural order comparator otherwise. This
   * comparator must not be the same object as the [naturalOrder()] comparator because it's used to
   * mask out client provided comparators in TreeMap and PriorityQueue in
   * [Comparators.naturalOrderToNull].
   */
  @Suppress("UNCHECKED_CAST")
  fun <T> nullToNaturalOrder(comparator: Comparator<T>?): Comparator<T> =
    comparator ?: InternalNaturalOrderComparator as Comparator<T>

  fun <T> naturalOrderToNull(comparator: Comparator<T>?): Comparator<T>? =
    if (comparator === InternalNaturalOrderComparator) null else comparator
}

private object InternalNaturalOrderComparator : Comparator<Comparable<Any?>?> {
  // Note: We use runtime null-checks here. Due to the unchecked casts above we can't rely on
  // non-nullable parameter types with compile-time checks.
  override fun compare(o1: Comparable<Any?>?, o2: Comparable<Any?>?) = o1!!.compareTo(o2!!)
}
