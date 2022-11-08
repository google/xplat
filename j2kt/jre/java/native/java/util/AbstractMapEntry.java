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

import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

/** Basic {@link Map.Entry} implementation that implements hashCode, equals, and toString. */
@NullMarked
abstract class AbstractMapEntry<K extends @Nullable Object, V extends @Nullable Object>
    implements Map.Entry<K, V> {
  private final K entryKey;
  private V entryValue;

  /** A mutable {@link Map.Entry} shared by several {@link Map} implementations. */
  public static class SimpleEntry<K extends @Nullable Object, V extends @Nullable Object>
      extends AbstractMapEntry<K, V> {
    public SimpleEntry(K key, V value) {
      super(key, value);
    }

    public SimpleEntry(Map.Entry<? extends K, ? extends V> entry) {
      super(entry.getKey(), entry.getValue());
    }
  }

  /** An immutable {@link Map.Entry} shared by several {@link Map} implementations. */
  public static class SimpleImmutableEntry<K extends @Nullable Object, V extends @Nullable Object>
      extends AbstractMapEntry<K, V> {
    public SimpleImmutableEntry(K key, V value) {
      super(key, value);
    }

    public SimpleImmutableEntry(Map.Entry<? extends K, ? extends V> entry) {
      super(entry.getKey(), entry.getValue());
    }

    @Override
    public V setValue(V value) {
      throw new UnsupportedOperationException();
    }
  }

  protected AbstractMapEntry(K key, V value) {
    this.entryKey = key;
    this.entryValue = value;
  }

  @Override
  public K getKey() {
    return entryKey;
  }

  @Override
  public V getValue() {
    return entryValue;
  }

  @Override
  public V setValue(V value) {
    V oldValue = this.entryValue;
    this.entryValue = value;
    return oldValue;
  }

  @Override
  public boolean equals(@Nullable Object other) {
    if (!(other instanceof Map.Entry)) {
      return false;
    }
    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) other;
    return Objects.equals(getKey(), entry.getKey()) && Objects.equals(getValue(), entry.getValue());
  }

  /** Calculate the hash code using Sun's specified algorithm. */
  @Override
  public int hashCode() {
    return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
  }

  @Override
  public final String toString() {
    // for compatibility with the real Jre: issue 3422
    return getKey() + "=" + getValue();
  }
}
