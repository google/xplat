/*
 * Copyright 2008 Google Inc.
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

import javaemul.internal.annotations.KtNative;
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

// TODO(b/240106068): Native LinkedHashMap is final. But this class should be non-final.
/**
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/LinkedHashMap.html">the official
 * Java API doc</a> for details.
 */
@KtNative("kotlin.collections.LinkedHashMap")
@NullMarked
public final class LinkedHashMap<K extends @Nullable Object, V extends @Nullable Object>
    implements Map<K, V> {

  public LinkedHashMap() {}

  public LinkedHashMap(int initialCapacity) {}

  public LinkedHashMap(int initialCapacity, float loadFactor) {}

  public LinkedHashMap(Map<? extends K, ? extends V> original) {}

  @Override
  public native void clear();

  @Override
  public native Set<K> keySet();

  @Override
  public native Collection<V> values();

  @Override
  public native boolean containsKey(@Nullable Object key);

  @Override
  public native boolean containsValue(@Nullable Object value);

  @Override
  public native Set<Map.Entry<K, V>> entrySet();

  @Override
  public native @Nullable V get(@Nullable Object key);

  @Override
  public native @Nullable V put(K key, V value);

  @Override
  public native @Nullable V remove(@Nullable Object key);

  @Override
  public native void putAll(Map<? extends K, ? extends V> m);

  @Override
  public native int size();

  @Override
  public native boolean isEmpty();
}
