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

import java.util.function.Function
import java.util.function.ToDoubleFunction
import java.util.function.ToIntFunction
import java.util.function.ToLongFunction
import kotlin.comparisons.reversed as default_reversed

fun interface Comparator<T> : kotlin.Comparator<T> {
  override fun compare(a: T, b: T): Int

  fun reversed(): kotlin.Comparator<T> = default_reversed()

  fun thenComparing(other: kotlin.Comparator<in T>): kotlin.Comparator<T> = then(other)

  fun <U> thenComparing(
    keyExtractor: Function<in T, out U>,
    keyComparator: kotlin.Comparator<in U>
  ): kotlin.Comparator<T> = thenBy(keyComparator, keyExtractor::apply)

  fun <U : Comparable<U>> thenComparing(keyExtractor: Function<in T, out U>): kotlin.Comparator<T> =
    thenBy(keyExtractor::apply)

  fun thenComparingInt(keyExtractor: ToIntFunction<in T>): kotlin.Comparator<T> =
    then(comparingInt(keyExtractor))

  fun thenComparingLong(keyExtractor: ToLongFunction<in T>): kotlin.Comparator<T> =
    then(comparingLong(keyExtractor))

  fun thenComparingDouble(keyExtractor: ToDoubleFunction<in T>): kotlin.Comparator<T> =
    then(comparingDouble(keyExtractor))

  companion object {
    fun <T, U> comparing(
      keyExtractor: Function<in T, out U>,
      keyComparator: kotlin.Comparator<in U>
    ): kotlin.Comparator<T> = compareBy(keyComparator, keyExtractor::apply)

    fun <T, U : Comparable<U>> comparing(
      keyExtractor: Function<in T, out U>
    ): kotlin.Comparator<T> = compareBy(naturalOrder(), keyExtractor::apply)

    fun <T> comparingDouble(keyExtractor: ToDoubleFunction<in T>): kotlin.Comparator<T> =
      Comparator<T> { a, b ->
        keyExtractor.applyAsDouble(a).compareTo(keyExtractor.applyAsDouble(b))
      }

    fun <T> comparingInt(keyExtractor: ToIntFunction<in T>): kotlin.Comparator<T> =
      Comparator<T> { a, b -> keyExtractor.applyAsInt(a).compareTo(keyExtractor.applyAsInt(b)) }

    fun <T> comparingLong(keyExtractor: ToLongFunction<in T>): kotlin.Comparator<T> =
      Comparator<T> { a, b -> keyExtractor.applyAsLong(a).compareTo(keyExtractor.applyAsLong(b)) }

    fun <T> nullsFirst(comparator: kotlin.Comparator<in T>): kotlin.Comparator<T?> =
      kotlin.comparisons.nullsFirst(comparator)

    fun <T> nullsLast(comparator: kotlin.Comparator<in T>): kotlin.Comparator<T?> =
      kotlin.comparisons.nullsLast(comparator)

    fun <T : Comparable<T>> naturalOrder(): kotlin.Comparator<T> = kotlin.comparisons.naturalOrder()

    fun <T : Comparable<T>> reverseOrder(): kotlin.Comparator<T> = kotlin.comparisons.reverseOrder()
  }
}
