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
package javaemul.internal;

import java.util.Comparator;
import javaemul.internal.annotations.KtNative;
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

@KtNative("javaemul.internal.Comparators")
@NullMarked
public class Comparators {
  /*
   * This is a utility class that provides default Comparators. This class
   * exists so Arrays and Collections can share the natural comparator without
   * having to know internals of each other.
   */

  public static native <T extends @Nullable Object> Comparator<T> nullToNaturalOrder(
      @Nullable Comparator<T> cmp);

  public static native <T extends @Nullable Object> @Nullable Comparator<T> naturalOrderToNull(
      Comparator<T> cmp);

  private Comparators() {}
}
