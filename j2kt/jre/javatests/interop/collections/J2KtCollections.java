/*
 * Copyright 2023 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package interop.collections;

import static java.util.Collections.unmodifiableList;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/** Exposes collection-related methods for the corresponding interop test. */
@NullMarked
public class J2KtCollections {

  /** Returns the first element from {@code list}, or {@code null} if empty. */
  public static <T> @Nullable T getFirstOrNull(List<T> list) {
    return list.isEmpty() ? null : list.get(0);
  }

  /** Adds {@code element} to a base {@code List} parameter. */
  public static <T> void addTo(List<T> list, T element) {
    list.add(element);
  }

  /** Adds {@code element} to a lower-bounded {@code List<? super T>} parameter. */
  public static <T> void addToLowerBounded(List<? super T> list, T element) {
    list.add(element);
  }

  /** Removes all {@code elements} from {@code from}. */
  public static <T> boolean removeAll(Collection<T> from, Collection<?> elements) {
    return from.removeAll(elements);
  }

  /** Returns a mutable {@code ArrayList} typed as the base {@code List} interface. */
  public static <T> List<T> newMutableListAsList() {
    return new ArrayList<>();
  }

  /** Returns a mutable {@code ArrayList} typed as the {@code AbstractList} subtype. */
  public static <T> AbstractList<T> newMutableListSubtype() {
    return new ArrayList<>();
  }

  /** Returns an unmodifiable {@code List} typed as the base {@code List} interface. */
  public static <T> List<T> newUnmodifiableList() {
    return unmodifiableList(new ArrayList<>());
  }

  private J2KtCollections() {}
}
