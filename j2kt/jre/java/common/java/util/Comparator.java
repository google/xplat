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
package java.util;

import static javaemul.internal.KtNativeUtils.ktNative;

import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import javaemul.internal.annotations.KtNative;
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

/**
 * An interface used a basis for implementing custom ordering. <a
 * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/Comparator.html">[Sun docs]</a>
 *
 * @param <T> the type to be compared.
 */
@KtNative(
    value = "kotlin.Comparator",
    bridgeWith = "java.util.Comparator",
    companionObject = "java.util.Comparator")
@FunctionalInterface
@NullMarked
public interface Comparator<T extends @Nullable Object> {

  int compare(T a, T b);

  default Comparator<T> reversed() {
    return ktNative();
  }

  default Comparator<T> thenComparing(Comparator<? super T> other) {
    return ktNative();
  }

  default <U extends @Nullable Object> Comparator<T> thenComparing(
      Function<? super T, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
    return ktNative();
  }

  default <U extends Comparable<? super U>> Comparator<T> thenComparing(
      Function<? super T, ? extends U> keyExtractor) {
    return ktNative();
  }

  default Comparator<T> thenComparingInt(ToIntFunction<? super T> keyExtractor) {
    return ktNative();
  }

  default Comparator<T> thenComparingLong(ToLongFunction<? super T> keyExtractor) {
    return ktNative();
  }

  default Comparator<T> thenComparingDouble(ToDoubleFunction<? super T> keyExtractor) {
    return ktNative();
  }

  static <T extends @Nullable Object, U extends @Nullable Object> Comparator<T> comparing(
      Function<? super T, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
    return ktNative();
  }

  static <T extends @Nullable Object, U extends Comparable<? super U>> Comparator<T> comparing(
      Function<? super T, ? extends U> keyExtractor) {
    return ktNative();
  }

  static <T extends @Nullable Object> Comparator<T> comparingDouble(
      ToDoubleFunction<? super T> keyExtractor) {
    return ktNative();
  }

  static <T extends @Nullable Object> Comparator<T> comparingInt(
      ToIntFunction<? super T> keyExtractor) {
    return ktNative();
  }

  static <T extends @Nullable Object> Comparator<T> comparingLong(
      ToLongFunction<? super T> keyExtractor) {
    return ktNative();
  }

  static <T extends Comparable<? super T>> Comparator<T> naturalOrder() {
    return ktNative();
  }

  static <T> Comparator<@Nullable T> nullsFirst(Comparator<? super T> comparator) {
    return ktNative();
  }

  static <T> Comparator<@Nullable T> nullsLast(Comparator<? super T> comparator) {
    return ktNative();
  }

  static <T extends Comparable<? super T>> Comparator<T> reverseOrder() {
    return ktNative();
  }
}
