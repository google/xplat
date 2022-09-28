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

import static javaemul.internal.InternalPreconditions.checkArgument;
import static javaemul.internal.InternalPreconditions.checkState;

import javaemul.internal.MapUtils;
import jsinterop.annotations.JsNonNull;
import org.jspecify.nullness.Nullable;

/**
 * A {@link java.util.Map} of {@link Enum}s. <a
 * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/EnumMap.html">[Sun docs]</a>
 *
 * @param <K> key type
 * @param <V> value type
 */
public class EnumMap<K extends Enum<K>, V extends @Nullable Object>
    extends AbstractMap<K, V> /* implements Cloneable */ {

  private final class EntrySet extends AbstractSet<@JsNonNull Entry<K, V>> {

    @Override
    public void clear() {
      EnumMap.this.clear();
    }

    @Override
    public boolean contains(Object o) {
      if (o instanceof Map.Entry) {
        return MapUtils.containsEntry(EnumMap.this, (Map.Entry<?, ?>) o);
      }
      return false;
    }

    @Override
    public Iterator<@JsNonNull Entry<K, V>> iterator() {
      return new EntrySetIterator();
    }

    @Override
    public boolean remove(Object entry) {
      if (contains(entry)) {
        Object key = ((Map.Entry<?, ?>) entry).getKey();
        EnumMap.this.remove(key);
        return true;
      }
      return false;
    }

    @Override
    public int size() {
      return EnumMap.this.size();
    }
  }

  private final class EntrySetIterator implements Iterator<@JsNonNull Entry<K, V>> {
    private final Iterator<K> it = enumMapKeySet.iterator();
    private K key;

    @Override
    public boolean hasNext() {
      return it.hasNext();
    }

    @Override
    public @JsNonNull Entry<K, V> next() {
      key = it.next();
      return new MapEntry(key);
    }

    @Override
    public void remove() {
      checkState(key != null);

      EnumMap.this.remove(key);
      key = null;
    }
  }

  private class MapEntry extends AbstractMapEntry<K, V> {

    public MapEntry(K key) {
      // Note: the value is going to be ignored.
      super(key, enumMapValues.get(key.ordinal()));
    }

    @Override
    public V getValue() {
      return enumMapValues.get(getKey().ordinal());
    }

    @Override
    public V setValue(V value) {
      return setAt(getKey(), value);
    }
  }

  private EnumSet<K> enumMapKeySet;
  private ArrayList<V> enumMapValues;

  public EnumMap(@JsNonNull Class<K> type) {
    init();
  }

  public EnumMap(@JsNonNull EnumMap<K, ? extends V> m) {
    init(m);
  }

  public EnumMap(@JsNonNull Map<K, ? extends V> m) {
    if (m instanceof EnumMap) {
      init((EnumMap<K, ? extends V>) m);
    } else {
      checkArgument(!m.isEmpty(), "Specified map is empty");
      init();
      putAll(m);
    }
  }

  @Override
  public void clear() {
    enumMapKeySet.clear();
    enumMapValues.clear();
  }

  public EnumMap<K, V> clone() {
    return new EnumMap<>(this);
  }

  @Override
  public boolean containsKey(Object key) {
    return enumMapKeySet.contains(key);
  }

  @Override
  public boolean containsValue(Object value) {
    for (K key : enumMapKeySet) {
      if (Objects.equals(value, enumMapValues.get(key.ordinal()))) {
        return true;
      }
    }
    return false;
  }

  @Override
  public @JsNonNull Set<Map.@JsNonNull Entry<K, V>> entrySet() {
    return new EntrySet();
  }

  @Override
  public V get(Object k) {
    return enumMapKeySet.contains(k) ? enumMapValues.get(asOrdinal(k)) : null;
  }

  @Override
  public V put(K key, V value) {
    enumMapKeySet.add(key);
    return setAt(key, value);
  }

  @Override
  public V remove(Object key) {
    return enumMapKeySet.remove(key) ? setAt((K) key, null) : null;
  }

  @Override
  public int size() {
    return enumMapKeySet.size();
  }

  /**
   * Returns <code>key</code> as <code>K</code>. Only runtime checks that
   * key is an Enum, not that it's the particular Enum K. Should only be called
   * when you are sure <code>key</code> is of type <code>K</code>.
   */
  private K asKey(Object key) {
    return (K) key;
  }

  private int asOrdinal(Object key) {
    return asKey(key).ordinal();
  }

  private void init() {
    enumMapKeySet = new EnumSet<>();
    enumMapValues = new ArrayList<>();
  }

  private void init(EnumMap<K, ? extends V> m) {
    enumMapKeySet = m.enumMapKeySet.clone();
    enumMapValues = new ArrayList<>(m.size());
    enumMapValues.addAll(m.values());
  }

  private V setAt(K key, V value) {
    int index = key.ordinal();
    while (enumMapValues.size() <= key.ordinal()) {
      enumMapValues.add(null);
    }
    V oldValue = enumMapValues.get(index);
    enumMapValues.set(index, value);
    return oldValue;
  }
}
