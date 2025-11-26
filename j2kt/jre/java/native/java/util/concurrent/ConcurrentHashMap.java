// CHECKSTYLE_OFF: Copyrighted to Guava Authors.
/*
 * Copyright (C) 2009 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// CHECKSTYLE_ON

package java.util.concurrent;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javaemul.lang.J2ktMonitor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Minimal emulation of {@link java.util.concurrent.ConcurrentHashMap}.
 *
 * @param <K> key type
 * @param <V> value type
 */
// TODO(b/458160722): The current implementation provides synchronized access methods, but
//   doesn't provide a safe entry set or value collection and also doesn't provide safe iterators,
//   which the original implementation does. So we need to either also wrap the entry set in a safe
//   way and provide safe itertors or port the original source code, probably replacing unsafe
//   access with atomics.
@NullMarked
public class ConcurrentHashMap<K extends @Nullable Object, V extends @Nullable Object>
    extends AbstractMap<K, V> implements ConcurrentMap<K, V> {

  private final Map<K, V> backingMap;
  private final J2ktMonitor lock = new J2ktMonitor();

  public ConcurrentHashMap() {
    this.backingMap = new HashMap<K, V>();
  }

  public ConcurrentHashMap(int initialCapacity) {
    this.backingMap = new HashMap<K, V>(initialCapacity);
  }

  public ConcurrentHashMap(int initialCapacity, float loadFactor) {
    this.backingMap = new HashMap<K, V>(initialCapacity, loadFactor);
  }

  public ConcurrentHashMap(Map<? extends K, ? extends V> t) {
    this.backingMap = new HashMap<K, V>(t);
  }

  @Override
  public void clear() {
    synchronized (lock) {
      backingMap.clear();
    }
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    synchronized (lock) {
      return backingMap.equals(obj);
    }
  }

  @Override
  public @Nullable V getOrDefault(@Nullable Object key, @Nullable V defaultValue) {
    synchronized (lock) {
      return backingMap.getOrDefault(key, defaultValue);
    }
  }

  @Override
  public int hashCode() {
    synchronized (lock) {
      return backingMap.hashCode();
    }
  }

  @Override
  public Set<K> keySet() {
    synchronized (lock) {
      // Make sure to use a wrapping implementation by calling super.
      return super.keySet();
    }
  }

  @Override
  public @Nullable V putIfAbsent(K key, V value) {
    synchronized (lock) {
      if (!containsKey(key)) {
        return put(key, value);
      } else {
        return get(key);
      }
    }
  }

  @Override
  public boolean remove(@Nullable Object key, @Nullable Object value) {
    synchronized (lock) {
      if (containsKey(key) && get(key).equals(value)) {
        remove(key);
        return true;
      } else {
        return false;
      }
    }
  }

  @Override
  public boolean replace(K key, V oldValue, V newValue) {
    synchronized (lock) {
      if (oldValue == null || newValue == null) {
        throw new NullPointerException();
      } else if (containsKey(key) && get(key).equals(oldValue)) {
        put(key, newValue);
        return true;
      } else {
        return false;
      }
    }
  }

  @Override
  public @Nullable V replace(K key, V value) {
    synchronized (lock) {
      if (value == null) {
        throw new NullPointerException();
      } else if (containsKey(key)) {
        return put(key, value);
      } else {
        return null;
      }
    }
  }

  @Override
  public boolean containsKey(@Nullable Object key) {
    if (key == null) {
      throw new NullPointerException();
    }
    synchronized (lock) {
      return backingMap.containsKey(key);
    }
  }

  @Override
  public @Nullable V get(@Nullable Object key) {
    if (key == null) {
      throw new NullPointerException();
    }
    synchronized (lock) {
      return backingMap.get(key);
    }
  }

  @Override public @Nullable V put(K key, V value) {
    if (key == null || value == null) {
      throw new NullPointerException();
    }
    return backingMap.put(key, value);
  }

  @Override
  public boolean containsValue(@Nullable Object value) {
    if (value == null) {
      throw new NullPointerException();
    }
    synchronized (lock) {
      return backingMap.containsValue(value);
    }
  }

  @Override
  public @Nullable V remove(@Nullable Object key) {
    if (key == null) {
      throw new NullPointerException();
    }
    synchronized (lock) {
      return backingMap.remove(key);
    }
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    synchronized (lock) {
      return backingMap.entrySet();
    }
  }

  public boolean contains(@Nullable Object value) {
    return containsValue(value);
  }

  public Enumeration<V> elements() {
    synchronized (lock) {
      return Collections.enumeration(values());
    }
  }

  public Enumeration<K> keys() {
    synchronized (lock) {
      return Collections.enumeration(keySet());
    }
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> map) {
    synchronized (lock) {
      backingMap.putAll(map);
    }
  }

  @Override
  public int size() {
    synchronized (lock) {
      return backingMap.size();
    }
  }

  @Override
  public String toString() {
    synchronized (lock) {
      return backingMap.toString();
    }
  }

  private String toString(@Nullable Object o) {
    return o == this ? "(this Map)" : String.valueOf(o);
  }

  @Override
  public Collection<V> values() {
    synchronized (lock) {
      return backingMap.values();
    }
  }

  public static <T extends @Nullable Object> Set<T> newKeySet() {
    return Collections.newSetFromMap(new ConcurrentHashMap<T, Boolean>());
  }
}
