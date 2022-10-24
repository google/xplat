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

import java.util.Comparator as JavaComparator
import java.util.function.Function
import java.util.function.ToDoubleFunction
import java.util.function.ToIntFunction
import java.util.function.ToLongFunction
import kotlin.comparisons.reversed as default_reversed

/** See regular JRE API documentation for the methods in this file. */
fun <T> Comparator<T>.reversed(): Comparator<T> =
  if (this is JavaComparator) reversed() else default_reversed()

fun <T> Comparator<T>.thenComparing(other: Comparator<in T>): Comparator<T> =
  if (this is JavaComparator) thenComparing(other) else then(other)

fun <T, U> Comparator<T>.thenComparing(
  keyExtractor: Function<in T, out U>,
  keyComparator: Comparator<in U>
): Comparator<T> =
  if (this is JavaComparator) thenComparing(keyExtractor, keyComparator)
  else thenBy(keyComparator, keyExtractor::apply)

fun <T, U : Comparable<U>> Comparator<T>.thenComparing(
  keyExtractor: Function<in T, out U>
): Comparator<T> =
  if (this is JavaComparator) thenComparing(keyExtractor) else thenBy(keyExtractor::apply)

fun <T> Comparator<T>.thenComparingInt(keyExtractor: ToIntFunction<in T>): Comparator<T> =
  if (this is JavaComparator) thenComparingInt(keyExtractor)
  else then(JavaComparator.comparingInt(keyExtractor))

fun <T> Comparator<T>.thenComparingLong(keyExtractor: ToLongFunction<in T>): Comparator<T> =
  if (this is JavaComparator) thenComparingLong(keyExtractor)
  else then(JavaComparator.comparingLong(keyExtractor))

fun <T> Comparator<T>.thenComparingDouble(keyExtractor: ToDoubleFunction<in T>): Comparator<T> =
  if (this is JavaComparator) thenComparingDouble(keyExtractor)
  else then(JavaComparator.comparingDouble(keyExtractor))
