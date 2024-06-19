/*
 * Copyright 2014 Google Inc.
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

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Sorted map providing additional query operations and views.
 *
 * @param <K> key type.
 * @param <V> value type.
 */
@NullMarked
public interface NavigableMap<K extends @Nullable Object, V extends @Nullable Object>
    extends SortedMap<K, V> {
  Map.@Nullable Entry<K, V> ceilingEntry(K key);

  @Nullable K ceilingKey(K key);

  NavigableSet<K> descendingKeySet();

  NavigableMap<K, V> descendingMap();

  Map.@Nullable Entry<K, V> firstEntry();

  Map.@Nullable Entry<K, V> floorEntry(K key);

  @Nullable K floorKey(K key);

  NavigableMap<K, V> headMap(K toKey, boolean inclusive);

  Map.@Nullable Entry<K, V> higherEntry(K key);

  @Nullable K higherKey(K key);

  Map.@Nullable Entry<K, V> lastEntry();

  Map.@Nullable Entry<K, V> lowerEntry(K key);

  @Nullable K lowerKey(K key);

  NavigableSet<K> navigableKeySet();

  Map.@Nullable Entry<K, V> pollFirstEntry();

  Map.@Nullable Entry<K, V> pollLastEntry();

  NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive);

  NavigableMap<K, V> tailMap(K fromKey, boolean inclusive);
}
