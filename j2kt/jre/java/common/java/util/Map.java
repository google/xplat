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

import static javaemul.internal.KtNativeUtils.ktNative;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import javaemul.internal.annotations.KtName;
import javaemul.internal.annotations.KtNative;
import javaemul.internal.annotations.KtProperty;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Map.html">the official Java API
 * doc</a> for details.
 */
@KtNative(name = "kotlin.collections.MutableMap", bridgeName = "javaemul.lang.JavaMap")
@NullMarked
public interface Map<K extends @Nullable Object, V extends @Nullable Object>
    extends ReadonlyMap<K, V> {

  /** Represents an individual map entry. */
  @KtNative(
      name = "kotlin.collections.MutableMap.MutableEntry",
      // The name of the type containing the companion
      companionName = "java.util.Map.Entry")
  interface Entry<K extends @Nullable Object, V extends @Nullable Object> {
    @KtProperty
    K getKey();

    @KtProperty
    V getValue();

    V setValue(V value);

    static <K extends Comparable<? super K>, V> Comparator<Map.Entry<K, V>> comparingByKey() {
      return ktNative();
    }

    static <K extends @Nullable Object, V extends @Nullable Object>
        Comparator<Map.Entry<K, V>> comparingByKey(Comparator<? super K> cmp) {
      return ktNative();
    }

    static <K extends @Nullable Object, V extends Comparable<? super V>>
        Comparator<Map.Entry<K, V>> comparingByValue() {
      return ktNative();
    }

    static <K extends @Nullable Object, V> Comparator<Map.Entry<K, V>> comparingByValue(
        Comparator<? super V> cmp) {
      return ktNative();
    }
  }

  void clear();

  default @Nullable V compute(
      K key, BiFunction<? super K, ? super @Nullable V, ? extends @Nullable V> remappingFunction) {
    return ktNative();
  }

  default V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
    return ktNative();
  }

  default @Nullable V computeIfPresent(
      K key, BiFunction<? super K, ? super @NonNull V, ? extends @Nullable V> remappingFunction) {
    return ktNative();
  }

  boolean containsKey(@Nullable Object key);

  boolean containsValue(@Nullable Object value);

  @KtProperty
  @KtName("entries")
  Set<Entry<K, V>> entrySet();

  default void forEach(BiConsumer<? super K, ? super V> action) {
    ktNative();
  }

  @Nullable V get(@Nullable Object key);

  @KtName("java_getOrDefault")
  default @Nullable V getOrDefault(@Nullable Object key, @Nullable V defaultValue) {
    return ktNative();
  }

  boolean isEmpty();

  @KtProperty
  @KtName("keys")
  Set<K> keySet();

  default @Nullable V merge(
      K key,
      @NonNull V value,
      BiFunction<? super @NonNull V, ? super @NonNull V, ? extends @Nullable V> remappingFunction) {
    return ktNative();
  }

  @Nullable V put(K key, V value);

  void putAll(Map<? extends K, ? extends V> t);

  default @Nullable V putIfAbsent(K key, V value) {
    return ktNative();
  }

  @Nullable V remove(@Nullable Object key);

  default boolean remove(@Nullable Object key, @Nullable Object value) {
    return ktNative();
  }

  default @Nullable V replace(K key, V value) {
    return ktNative();
  }

  default boolean replace(K key, V oldValue, V newValue) {
    return ktNative();
  }

  default void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
    ktNative();
  }

  @KtProperty
  int size();

  @KtProperty
  Collection<V> values();
}
