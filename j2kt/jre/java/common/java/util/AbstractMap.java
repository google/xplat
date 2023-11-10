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

import javaemul.internal.annotations.KtNative;
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

// Note: this is emulated _and_ bridged because we need the bridges on the JVM as well, so
// we cannot fold the bridge code into the emulation class (native/java.util.AbstractMap).
/**
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/AbstractMap.html">the official
 * Java API doc</a> for details.
 */
@KtNative(name = "java.util.AbstractMap", bridgeName = "javaemul.lang.JavaAbstractMap")
@NullMarked
public abstract class AbstractMap<K extends @Nullable Object, V extends @Nullable Object>
    implements Map<K, V> {

  /** A mutable {@link Map.Entry} shared by several {@link Map} implementations. */
  @KtNative(name = "java.util.AbstractMap.SimpleEntry")
  public static class SimpleEntry<K extends @Nullable Object, V extends @Nullable Object>
      implements Map.Entry<K, V> {
    public SimpleEntry(K key, V value) {}

    public SimpleEntry(Map.Entry<? extends K, ? extends V> entry) {}

    @Override
    public native K getKey();

    @Override
    public native V getValue();

    @Override
    public native V setValue(V value);
  }

  /** An immutable {@link Map.Entry} shared by several {@link Map} implementations. */
  @KtNative(name = "java.util.AbstractMap.SimpleImmutableEntry")
  public static class SimpleImmutableEntry<K extends @Nullable Object, V extends @Nullable Object>
      implements Map.Entry<K, V> {
    public SimpleImmutableEntry(K key, V value) {}

    public SimpleImmutableEntry(Map.Entry<? extends K, ? extends V> entry) {}

    @Override
    public native K getKey();

    @Override
    public native V getValue();

    @Override
    public native V setValue(V value);
  }

  protected AbstractMap() {}

  @Override
  public native void clear();

  @Override
  public native boolean containsKey(@Nullable Object key);

  @Override
  public native boolean containsValue(@Nullable Object value);

  native boolean containsEntry(@Nullable Object value);

  @Override
  public native @Nullable V get(@Nullable Object key);

  @Override
  public abstract Set<Entry<K, V>> entrySet();

  @Override
  public native boolean isEmpty();

  @Override
  public native Set<K> keySet();

  @Override
  public native @Nullable V put(K key, V value);

  @Override
  public native void putAll(Map<? extends K, ? extends V> map);

  @Override
  public native @Nullable V remove(@Nullable Object key);

  @Override
  public native int size();

  @Override
  public native Collection<V> values();
}
