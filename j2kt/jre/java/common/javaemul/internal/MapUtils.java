/*
 * Copyright 2022 Google Inc.
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
package javaemul.internal;

import java.util.Map;
import java.util.Objects;
import org.jspecify.nullness.Nullable;

/** Map utilities. */
public class MapUtils {

  public static <K extends @Nullable Object, V extends @Nullable Object> boolean containsEntry(
      Map<K, V> map, Map.Entry<?, ?> entry) {
    Object key = entry.getKey();
    Object value = entry.getValue();
    Object ourValue = map.get(key);

    if (!Objects.equals(value, ourValue)) {
      return false;
    }

    // Perhaps it was null and we don't contain the key?
    if (ourValue == null && !map.containsKey(key)) {
      return false;
    }

    return true;
  }

  public static <K extends @Nullable Object, V extends @Nullable Object> K getEntryKeyOrNull(
      Map.Entry<K, V> entry) {
    return entry == null ? null : entry.getKey();
  }
}
