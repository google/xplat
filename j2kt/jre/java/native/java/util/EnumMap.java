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
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

/**
 * A {@link java.util.Map} of {@link Enum}s. <a
 * href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/EnumMap.html">[Sun docs]</a>
 *
 * @param <K> key type
 * @param <V> value type
 */
@NullMarked
public class EnumMap<K extends Enum<K>, V extends @Nullable Object> extends AbstractMap<K, V>
    implements Cloneable {

  private final class EntrySet extends AbstractSet<Entry<K, V>> {

    @Override
    public void clear() {
      EnumMap.this.clear();
    }

    @Override
    public boolean contains(@Nullable Object o) {
      if (o instanceof Map.Entry) {
        return MapUtils.containsEntry(EnumMap.this, (Map.Entry<?, ?>) o);
      }
      return false;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
      return new EntrySetIterator();
    }

    @Override
    public boolean remove(@Nullable Object entry) {
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

  private final class EntrySetIterator implements Iterator<Entry<K, V>> {
    private final Iterator<K> it = keySet.iterator();
    private @Nullable K key;

    @Override
    public boolean hasNext() {
      return it.hasNext();
    }

    @Override
    public Entry<K, V> next() {
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
      super(key, values.get(key.ordinal()));
    }

    @Override
    public V getValue() {
      return values.get(getKey().ordinal());
    }

    @Override
    public V setValue(V value) {
      return setAt(getKey(), value);
    }
  }

  private final EnumSet<K> keySet;
  private final ArrayList<@Nullable V> values;

  public EnumMap(Class<K> type) {
    keySet = new EnumSet<>();
    values = new ArrayList<>();
  }

  @SuppressWarnings("unchecked")
  public EnumMap(EnumMap<K, ? extends V> m) {
    keySet = m.isEmpty() ? new EnumSet<>() : m.keySet.clone();
    values = new ArrayList<>((Collection<V>) m.values);
  }

  @SuppressWarnings("unchecked")
  public EnumMap(Map<K, ? extends V> m) {
    if (m instanceof EnumMap) {
      EnumMap<K, V> enumMap = (EnumMap<K, V>) m;
      keySet = enumMap.keySet.clone();
      values = new ArrayList<>((Collection<V>) enumMap.values);
    } else {
      checkArgument(!m.isEmpty(), "Specified map is empty");
      keySet = new EnumSet<>();
      values = new ArrayList<>();
      putAll(m);
    }
  }

  @Override
  public void clear() {
    keySet.clear();
    values.clear();
  }

  @Override
  public EnumMap<K, V> clone() {
    return new EnumMap<>(this);
  }

  @Override
  public boolean containsKey(@Nullable Object key) {
    return keySet.contains(key);
  }

  @Override
  public boolean containsValue(@Nullable Object value) {
    for (K key : keySet) {
      if (Objects.equals(value, values.get(key.ordinal()))) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Set<Map.Entry<K, V>> entrySet() {
    return new EntrySet();
  }

  @Override
  public @Nullable V get(@Nullable Object k) {
    return keySet.contains(k) ? values.get(asOrdinal(k)) : null;
  }

  @Override
  public @Nullable V put(K key, V value) {
    keySet.add(key);
    return setAt(key, value);
  }

  @Override
  public @Nullable V remove(@Nullable Object key) {
    return keySet.remove(key) ? setAt((K) key, null) : null;
  }

  @Override
  public int size() {
    return keySet.size();
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

  private @Nullable V setAt(K key, @Nullable V value) {
    int index = key.ordinal();
    while (values.size() <= key.ordinal()) {
      values.add(null);
    }
    V oldValue = values.get(index);
    values.set(index, value);
    return oldValue;
  }
}
