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

import javaemul.internal.annotations.KtName;
import javaemul.internal.annotations.KtNative;
import javaemul.internal.annotations.KtPropagateNullability;
import javaemul.internal.annotations.KtProperty;
import jsinterop.annotations.JsNonNull;
import org.jspecify.nullness.Nullable;

/**
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Map.html">the official Java API
 * doc</a> for details.
 */
@KtNative(value = "kotlin.collections.MutableMap", bridgeWith = "javaemul.lang.JavaMap")
public interface Map<K extends @Nullable Object, V extends @Nullable Object> {

  /** Represents an individual map entry. */
  @KtNative("kotlin.collections.MutableMap.MutableEntry")
  interface Entry<K extends @Nullable Object, V extends @Nullable Object> {
    @KtProperty
    @KtPropagateNullability
    K getKey();

    @KtProperty
    @KtPropagateNullability
    V getValue();

    @KtPropagateNullability
    V setValue(@JsNonNull V value);

    static <K extends Comparable<? super K>, V> Comparator<Map.Entry<K,V>> comparingByKey() {
      // native interface method
      throw new IllegalStateException();
    }

    static <K extends @Nullable Object, V extends @Nullable Object>
        Comparator<Map.Entry<K, V>> comparingByKey(Comparator<? super K> cmp) {
      // native interface method
      throw new IllegalStateException();
    }

    static <K extends @Nullable Object, V extends Comparable<? super V>>
        Comparator<Map.Entry<K, V>> comparingByValue() {
      // native interface method
      throw new IllegalStateException();
    }

    static <K extends @Nullable Object, V> Comparator<Map.Entry<K, V>> comparingByValue(
        Comparator<? super V> cmp) {
      // native interface method
      throw new IllegalStateException();
    }
  }

  void clear();

  @KtName("java_containsKey")
  boolean containsKey(@Nullable Object key);

  @KtName("java_containsValue")
  boolean containsValue(@Nullable Object value);

  @JsNonNull
  @KtPropagateNullability
  @KtProperty
  @KtName("entries")
  Set<Entry<K, V>> entrySet();

  @KtName("java_get")
  @Nullable V get(@Nullable Object key);

  boolean isEmpty();

  @JsNonNull
  @KtPropagateNullability
  @KtProperty
  @KtName("keys")
  Set<K> keySet();

  @KtPropagateNullability
  @Nullable V put(K key, V value);

  @KtName("java_putAll")
  @KtPropagateNullability
  void putAll(@JsNonNull Map<? extends K, V> t);

  @KtName("java_remove")
  @Nullable V remove(@Nullable Object key);

  @KtProperty
  int size();

  @JsNonNull
  @KtProperty
  @KtPropagateNullability
  Collection<V> values();
}
