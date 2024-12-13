/*
 * Copyright 2007 Google Inc.
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

import static javaemul.internal.InternalPreconditions.checkNotNull;

import java.io.Serializable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
class Comparators {
  /*
   * This is a utility class that provides default Comparators. This class
   * exists so Arrays and Collections can share the natural comparator without
   * having to know internals of each other.
   *
   * This class is package protected since it is not in the JRE.
   */

  private static final Comparator<@Nullable Comparable<Object>> INTERNAL_NATURAL_ORDER =
      new NaturalOrderComparator();

  // Note: NaturalOrderComparator would technically not have to handle @Nullable Object, only
  // Object. But client code might unchecked-cast the comparator and then compare with null. This
  // can cause hard crashes on Kotlin Native. By moving the null-check to runtime, the crashes
  // become regular NullPointerExceptions.
  private static final class NaturalOrderComparator
      implements Comparator<@Nullable Comparable<Object>>, Serializable {

    @Override
    public int compare(@Nullable Comparable<Object> a, @Nullable Comparable<Object> b) {
      return checkNotNull(a).compareTo(checkNotNull(b));
    }
  }

  /**
   * Returns the given comparator if it is non-null; natural order comparator otherwise. This
   * comparator must not be the same object as {@link kotlin.comparisons.naturalOrder} comparator
   * because it's used to mask out client provided comparators in TreeMap and PriorityQueue in
   * {@link Comparators#naturalOrderToNull(Comparator)}.
   *
   * <p>See: {@link Arrays#binarySearch(Object[], Object, Comparator)} {@link
   * Arrays#binarySearch(Object[], int, int, Object, Comparator)} {@link Arrays#sort(Object[],
   * Comparator)} {@link Arrays#sort(Object[], int, int, Comparator)} {@link
   * TreeMap#TreeMap(Comparator)} {@link PriorityQueue#PriorityQueue(Comparator)}
   */
  @SuppressWarnings("unchecked")
  public static <T extends @Nullable Object> Comparator<T> nullToNaturalOrder(
      @Nullable Comparator<T> cmp) {
    return cmp == null ? (Comparator<T>) INTERNAL_NATURAL_ORDER : cmp;
  }

  /**
   * Return null if the given comparator is natural order comparator returned by {@link
   * Comparators#nullToNaturalOrder(Comparator)}; given comparator otherwise.
   *
   * <p>See: {@link TreeMap#comparator()} {@link PriorityQueue#comparator()}
   */
  public static <T extends @Nullable Object> @Nullable Comparator<T> naturalOrderToNull(
      @Nullable Comparator<T> cmp) {
    return cmp == INTERNAL_NATURAL_ORDER ? null : cmp;
  }

  private Comparators() {}
}
