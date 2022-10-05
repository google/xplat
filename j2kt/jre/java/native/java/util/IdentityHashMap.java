/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package java.util;

import java.io.Serializable;
import jsinterop.annotations.JsNonNull;
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

// TODO(b/228163266): Should implemenent Cloneable but its current mapping doesn't work here.
/**
 * IdentityHashMap is a variant on HashMap which tests equality by reference instead of equality by
 * value. Basically, keys and values are compared for equality by checking if their references are
 * equal rather than by calling the "equals" function.
 *
 * <p><a href="https://java.sun.com/j2se/1.5.0/docs/api/java/util/IdentityHashMap.html">Oracle
 * docs</a>
 */
@NullMarked
public class IdentityHashMap<K extends @Nullable Object, V extends @Nullable Object>
    extends AbstractMap<K, V> implements Map<K, V>, Serializable /* , Cloneable */ {

  /*
   * The internal data structure to hold key value pairs This array holds keys
   * and values in an alternating fashion.
   */
  private transient @Nullable Object[] elementData;

  // Lazily-initialized key set (for implementing {@link #keySet}).
  private @Nullable Set<K> backingKeySet;

  // Lazily-initialized values collection (for implementing {@link #values}).
  private @Nullable Collection<V> valuesCollection;

  /* Actual number of key-value pairs. */
  private int mapSize;

  /*
   * maximum number of elements that can be put in this map before having to
   * rehash.
   */
  private transient int threshold;

  /*
   * default threshold value that an IdentityHashMap created using the default
   * constructor would have.
   */
  private static final int DEFAULT_MAX_SIZE = 21;

  /* Default load factor of 0.75; */
  private static final int loadFactor = 7500;

  /*
   * modification count, to keep track of structural modifications between the
   * IdentityHashMap and the iterator
   */
  private transient int modCount = 0;

  /*
   * Object used to represent null keys and values. This is used to
   * differentiate a literal 'null' key value pair from an empty spot in the
   * map.
   */
  private static final Object NULL_OBJECT = new Object(); // $NON-LOCK-1$

  static class IdentityHashMapEntry<K extends @Nullable Object, V extends @Nullable Object>
      extends AbstractMapEntry<K, V> {
    private final IdentityHashMap<K, V> map;

    IdentityHashMapEntry(IdentityHashMap<K, V> map, K theKey, V theValue) {
      super(theKey, theValue);
      this.map = map;
    }

    public Object clone() throws CloneNotSupportedException {
      return new IdentityHashMapEntry<>(map, getKey(), getValue());
    }

    @Override
    public boolean equals(@Nullable Object object) {
      if (this == object) {
        return true;
      }
      if (object instanceof Map.Entry) {
        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) object;
        return (getKey() == entry.getKey()) && (getValue() == entry.getValue());
      }
      return false;
    }

    @Override
    public int hashCode() {
      return System.identityHashCode(getKey()) ^ System.identityHashCode(getValue());
    }

    @Override
    public V setValue(V object) {
      V result = super.setValue(object);
      map.put(getKey(), object);
      return result;
    }
  }

  static class IdentityHashMapIterator<
          E extends @Nullable Object, KT extends @Nullable Object, VT extends @Nullable Object>
      implements Iterator<E> {
    private int position = 0; // the current position

    // the position of the entry that was last returned from next()
    private int lastPosition = 0;

    final IdentityHashMap<KT, VT> associatedMap;

    int expectedModCount;

    final MapEntryType<E, KT, VT> type;

    boolean canRemove = false;

    IdentityHashMapIterator(MapEntryType<E, KT, VT> value, IdentityHashMap<KT, VT> hm) {
      associatedMap = hm;
      type = value;
      expectedModCount = hm.modCount;
    }

    public boolean hasNext() {
      while (position < associatedMap.elementData.length) {
        // if this is an empty spot, go to the next one
        if (associatedMap.elementData[position] == null) {
          position += 2;
        } else {
          return true;
        }
      }
      return false;
    }

    void checkConcurrentMod() throws ConcurrentModificationException {
      if (expectedModCount != associatedMap.modCount) {
        throw new ConcurrentModificationException();
      }
    }

    public E next() {
      checkConcurrentMod();
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      IdentityHashMapEntry<KT, VT> result = associatedMap.getEntry(position);
      lastPosition = position;
      position += 2;

      canRemove = true;
      return type.get(result);
    }

    public void remove() {
      checkConcurrentMod();
      if (!canRemove) {
        throw new IllegalStateException();
      }

      canRemove = false;
      associatedMap.remove(associatedMap.elementData[lastPosition]);
      position = lastPosition;
      expectedModCount++;
    }
  }

  static class IdentityHashMapEntrySet<KT extends @Nullable Object, VT extends @Nullable Object>
      extends AbstractSet<Map.Entry<KT, VT>> {
    private final IdentityHashMap<KT, VT> associatedMap;

    public IdentityHashMapEntrySet(IdentityHashMap<KT, VT> hm) {
      associatedMap = hm;
    }

    IdentityHashMap<KT, VT> hashMap() {
      return associatedMap;
    }

    @Override
    public int size() {
      return associatedMap.mapSize;
    }

    @Override
    public void clear() {
      associatedMap.clear();
    }

    @Override
    public boolean remove(Object object) {
      if (contains(object)) {
        associatedMap.remove(((Map.Entry<?, ?>) object).getKey());
        return true;
      }
      return false;
    }

    @Override
    public boolean contains(Object object) {
      if (object instanceof Map.Entry) {
        IdentityHashMapEntry<?, ?> entry =
            associatedMap.getEntry(((Map.Entry<?, ?>) object).getKey());
        // we must call equals on the entry obtained from "this"
        return entry != null && entry.equals(object);
      }
      return false;
    }

    @Override
    public Iterator<Map.Entry<KT, VT>> iterator() {
      return new IdentityHashMapIterator<Map.@JsNonNull Entry<KT, VT>, KT, VT>(
          entry -> entry, associatedMap);
    }
  }

  public IdentityHashMap() {
    this(DEFAULT_MAX_SIZE);
  }

  public IdentityHashMap(int maxSize) {
    if (maxSize < 0) {
      throw new IllegalArgumentException("maxSize < 0: " + maxSize);
    }
    mapSize = 0;
    threshold = getThreshold(maxSize);
    elementData = newElementArray(computeElementArraySize());
  }

  private int getThreshold(int maxSize) {
    // assign the threshold to maxSize initially, this will change to a
    // higher value if rehashing occurs.
    return maxSize > 3 ? maxSize : 3;
  }

  private int computeElementArraySize() {
    int arraySize = (int) (((long) threshold * 10000) / loadFactor) * 2;
    // ensure arraySize is positive, the above cast from long to int type
    // leads to overflow and negative arraySize if threshold is too big
    return arraySize < 0 ? -arraySize : arraySize;
  }

  private Object[] newElementArray(int s) {
    return new Object[s];
  }

  public IdentityHashMap(Map<? extends K, ? extends V> map) {
    this(map.size() < 6 ? 11 : map.size() * 2);
    putAllImpl(map);
  }

  @SuppressWarnings("unchecked")
  private @Nullable V massageValue(@Nullable Object value) {
    return (@Nullable V) ((value == NULL_OBJECT) ? null : value);
  }

  @Override
  public void clear() {
    mapSize = 0;
    for (int i = 0; i < elementData.length; i++) {
      elementData[i] = null;
    }
    modCount++;
  }

  @Override
  public boolean containsKey(@Nullable Object key) {
    if (key == null) {
      key = NULL_OBJECT;
    }

    int index = findIndex(key, elementData);
    return elementData[index] == key;
  }

  @Override
  public boolean containsValue(@Nullable Object value) {
    if (value == null) {
      value = NULL_OBJECT;
    }

    for (int i = 1; i < elementData.length; i = i + 2) {
      if (elementData[i] == value) {
        return true;
      }
    }
    return false;
  }

  @Override
  public @Nullable V get(@Nullable Object key) {
    if (key == null) {
      key = NULL_OBJECT;
    }

    int index = findIndex(key, elementData);

    if (elementData[index] == key) {
      Object result = elementData[index + 1];
      return massageValue(result);
    }

    return null;
  }

  private IdentityHashMapEntry<K, V> getEntry(@Nullable Object key) {
    if (key == null) {
      key = NULL_OBJECT;
    }

    int index = findIndex(key, elementData);
    if (elementData[index] == key) {
      return getEntry(index);
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  private IdentityHashMapEntry<K, V> getEntry(int index) {
    Object key = elementData[index];
    Object value = elementData[index + 1];

    if (key == NULL_OBJECT) {
      key = null;
    }
    if (value == NULL_OBJECT) {
      value = null;
    }

    return new IdentityHashMapEntry<>(this, (K) key, (V) value);
  }

  private int findIndex(@Nullable Object key, @Nullable Object[] array) {
    int length = array.length;
    int index = getModuloHash(key, length);
    int last = (index + length - 2) % length;
    while (index != last) {
      if (array[index] == key || (array[index] == null)) {
        /*
         * Found the key, or the next empty spot (which means key is not
         * in the table)
         */
        break;
      }
      index = (index + 2) % length;
    }
    return index;
  }

  private int getModuloHash(@Nullable Object key, int length) {
    return ((secondaryIdentityHash(key) & 0x7FFFFFFF) % (length / 2)) * 2;
  }

  @Override
  public @Nullable V put(K key, V value) {
    Object _key = key;
    Object _value = value;
    if (_key == null) {
      _key = NULL_OBJECT;
    }

    if (_value == null) {
      _value = NULL_OBJECT;
    }

    int index = findIndex(_key, elementData);

    // if the key doesn't exist in the table
    if (elementData[index] != _key) {
      modCount++;
      if (++mapSize > threshold) {
        rehash();
        index = findIndex(_key, elementData);
      }

      // insert the key and assign the value to null initially
      elementData[index] = _key;
      elementData[index + 1] = null;
    }

    // insert value to where it needs to go, return the old value
    Object result = elementData[index + 1];
    elementData[index + 1] = _value;

    return massageValue(result);
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> map) {
    putAllImpl(map);
  }

  private void rehash() {
    int newlength = elementData.length * 2;
    if (newlength == 0) {
      newlength = 1;
    }
    Object[] newData = newElementArray(newlength);
    for (int i = 0; i < elementData.length; i = i + 2) {
      Object key = elementData[i];
      if (key != null) {
        // if not empty
        int index = findIndex(key, newData);
        newData[index] = key;
        newData[index + 1] = elementData[i + 1];
      }
    }
    elementData = newData;
    computeMaxSize();
  }

  private void computeMaxSize() {
    threshold = (int) ((long) (elementData.length / 2) * loadFactor / 10000);
  }

  @Override
  public @Nullable V remove(@Nullable Object key) {
    if (key == null) {
      key = NULL_OBJECT;
    }

    boolean hashedOk;
    int index, next, hash;
    Object result, object;
    index = next = findIndex(key, elementData);

    if (elementData[index] != key) {
      return null;
    }

    // store the value for this key
    result = elementData[index + 1];

    // shift the following elements up if needed
    // until we reach an empty spot
    int length = elementData.length;
    while (true) {
      next = (next + 2) % length;
      object = elementData[next];
      if (object == null) {
        break;
      }

      hash = getModuloHash(object, length);
      hashedOk = hash > index;
      if (next < index) {
        hashedOk = hashedOk || (hash <= next);
      } else {
        hashedOk = hashedOk && (hash <= next);
      }
      if (!hashedOk) {
        elementData[index] = object;
        elementData[index + 1] = elementData[next + 1];
        index = next;
      }
    }

    mapSize--;
    modCount++;

    // clear both the key and the value
    elementData[index] = null;
    elementData[index + 1] = null;

    return massageValue(result);
  }

  @Override
  public Set<Map.Entry<K, V>> entrySet() {
    return new IdentityHashMapEntrySet<>(this);
  }

  @Override
  public Set<K> keySet() {
    if (backingKeySet == null) {
      backingKeySet =
          new AbstractSet<K>() {
            @Override
            public boolean contains(@Nullable Object object) {
              return containsKey(object);
            }

            @Override
            public int size() {
              return IdentityHashMap.this.size();
            }

            @Override
            public void clear() {
              IdentityHashMap.this.clear();
            }

            @Override
            public boolean remove(@Nullable Object key) {
              if (containsKey(key)) {
                IdentityHashMap.this.remove(key);
                return true;
              }
              return false;
            }

            @Override
            public Iterator<K> iterator() {
              return new IdentityHashMapIterator<>(Entry::getKey, IdentityHashMap.this);
            }
          };
    }
    return backingKeySet;
  }

  @Override
  public Collection<V> values() {
    if (valuesCollection == null) {
      valuesCollection =
          new AbstractCollection<V>() {
            @Override
            public boolean contains(@Nullable Object object) {
              return containsValue(object);
            }

            @Override
            public int size() {
              return IdentityHashMap.this.size();
            }

            @Override
            public void clear() {
              IdentityHashMap.this.clear();
            }

            @Override
            public Iterator<V> iterator() {
              return new IdentityHashMapIterator<>(Entry::getValue, IdentityHashMap.this);
            }

            @Override
            public boolean remove(@Nullable Object object) {
              Iterator<?> it = iterator();
              while (it.hasNext()) {
                if (object == it.next()) {
                  it.remove();
                  return true;
                }
              }
              return false;
            }
          };
    }
    return valuesCollection;
  }

  @Override
  public boolean equals(@Nullable Object object) {
    /*
     * We need to override the equals method in AbstractMap because
     * AbstractMap.equals will call ((Map) object).entrySet().contains() to
     * determine equality of the entries, so it will defer to the argument
     * for comparison, meaning that reference-based comparison will not take
     * place. We must ensure that all comparison is implemented by methods
     * in this class (or in one of our inner classes) for reference-based
     * comparison to take place.
     */
    if (this == object) {
      return true;
    }
    if (object instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) object;
      if (size() != map.size()) {
        return false;
      }

      Set<Map.Entry<K, V>> set = entrySet();
      // ensure we use the equals method of the set created by "this"
      return set.equals(map.entrySet());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  public Object clone() {
    return new IdentityHashMap<>(this);
  }

  @Override
  public boolean isEmpty() {
    return mapSize == 0;
  }

  @Override
  public int size() {
    return mapSize;
  }

  private void putAllImpl(Map<? extends K, ? extends V> map) {
    if (map.entrySet() != null) {
      super.putAll(map);
    }
  }

  @FunctionalInterface
  private interface MapEntryType<
      R extends @Nullable Object, K extends @Nullable Object, V extends @Nullable Object> {
    R get(AbstractMapEntry<K, V> entry);
  }

  private static int secondaryIdentityHash(@Nullable Object key) {
    return secondaryHash(System.identityHashCode(key));
  }

  private static int secondaryHash(int h) {
    // Spread bits to regularize both segment and index locations,
    // using variant of single-word Wang/Jenkins hash.
    h += (h << 15) ^ 0xffffcd7d;
    h ^= (h >>> 10);
    h += (h << 3);
    h ^= (h >>> 6);
    h += (h << 2) + (h << 14);
    return h ^ (h >>> 16);
  }
}
