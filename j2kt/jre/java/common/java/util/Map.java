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

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
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
    V setValue(V value);

    static <K extends Comparable<? super K>, V> Comparator<Map.Entry<K, V>> comparingByKey() {
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

  @KtName("java_compute")
  @KtPropagateNullability
  default @Nullable V compute(
      K key, @JsNonNull BiFunction<? super K, ? super @Nullable V, ? extends V> remappingFunction) {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }

  @KtName("java_computeIfAbsent")
  @KtPropagateNullability
  default V computeIfAbsent(K key, @JsNonNull Function<? super K, ? extends V> mappingFunction) {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }

  @KtName("java_computeIfPresent")
  @KtPropagateNullability
  default @Nullable V computeIfPresent(
      K key, @JsNonNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }

  @KtName("java_containsKey")
  boolean containsKey(@Nullable Object key);

  @KtName("java_containsValue")
  boolean containsValue(@Nullable Object value);

  @JsNonNull
  @KtPropagateNullability
  @KtProperty
  @KtName("entries")
  Set<@JsNonNull Entry<K, V>> entrySet();

  @KtName("java_forEach")
  @KtPropagateNullability
  default void forEach(@JsNonNull BiConsumer<? super K, ? super V> action) {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }

  @KtName("java_get")
  @Nullable V get(@Nullable Object key);

  @KtName("java_getOrDefault")
  @KtPropagateNullability
  default @Nullable V getOrDefault(@Nullable Object key, @Nullable V defaultValue) {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }

  boolean isEmpty();

  @JsNonNull
  @KtPropagateNullability
  @KtProperty
  @KtName("keys")
  Set<K> keySet();

  @KtPropagateNullability
  @KtName("java_merge")
  default @Nullable V merge(
      K key,
      V value,
      @JsNonNull BiFunction<? super V, ? super V, ? extends @Nullable V> remappingFunction) {
    throw new IllegalStateException("Native interface method should not be transpiled.");
  }

  @KtPropagateNullability
  @Nullable V put(K key, V value);
  
  @KtName("java_putAll")
  @KtPropagateNullability
  void putAll(@JsNonNull Map<? extends K, ? extends V> t);

  @KtName("java_putIfAbsent")
  @KtPropagateNullability
  default @Nullable V putIfAbsent(K key, V value) {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }

  @KtName("java_remove")
  @Nullable V remove(@Nullable Object key);

  @KtName("java_remove")
  default boolean remove(@Nullable Object key, @Nullable Object value) {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }

  @KtName("java_replace")
  @KtPropagateNullability
  default @Nullable V replace(K key, V value) {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }

  @KtName("java_replace")
  @KtPropagateNullability
  default boolean replace(K key, V oldValue, V newValue) {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }

  @KtName("java_replaceAll")
  @KtPropagateNullability
  default void replaceAll(@JsNonNull BiFunction<? super K, ? super V, ? extends V> function) {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }

  @KtProperty
  int size();

  @JsNonNull
  @KtProperty
  @KtPropagateNullability
  Collection<V> values();
}
