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

import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.NullMarked;

/** Exposes collection-related methods for the corresponding interop test. */
@NullMarked
public class J2KtCollections {

  /** Accepts any list. */
  public static <T> void accept(List<T> list) {}

  /** Returns new list which is mutable. */
  public static <T> List<T> newMutableList() {
    return new ArrayList<>();
  }

  /** Returns new list which is read-only. */
  public static <T> List<T> newReadonlyList() {
    return unmodifiableList(newMutableList());
  }
}
