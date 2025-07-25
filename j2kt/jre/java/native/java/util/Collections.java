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
import static javaemul.internal.InternalPreconditions.checkElementIndex;
import static javaemul.internal.InternalPreconditions.checkNotNull;

import java.io.Serializable;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Utility methods that operate on collections. See <a
 * href="https://docs.oracle.com/javase/8/docs/api/java/util/Collections.html">the official Java API
 * doc</a> for details.
 */
@NullMarked
public class Collections {

  private static final class LifoQueue<E extends @Nullable Object> extends AbstractQueue<E>
      implements Serializable {

    private final Deque<E> deque;

    LifoQueue(Deque<E> deque) {
      this.deque = deque;
    }

    @Override
    public Iterator<E> iterator() {
      return deque.iterator();
    }

    @Override
    public boolean offer(E e) {
      return deque.offerFirst(e);
    }

    @Override
    public @Nullable E peek() {
      return deque.peekFirst();
    }

    @Override
    public @Nullable E poll() {
      return deque.pollFirst();
    }

    @Override
    public int size() {
      return deque.size();
    }
  }

  private static final class EmptyList<E extends @Nullable Object> extends AbstractList<E>
      implements RandomAccess, Serializable {
    @Override
    public boolean contains(@Nullable Object object) {
      return false;
    }

    @Override
    public E get(int location) {
      checkElementIndex(location, 0);
      // Unreachable
      throw new AssertionError(location);
    }

    @Override
    public Iterator<E> iterator() {
      return emptyIterator();
    }

    @Override
    public ListIterator<E> listIterator() {
      return emptyListIterator();
    }

    @Override
    public int size() {
      return 0;
    }
  }

  private static final class EmptyListIterator<E extends @Nullable Object>
      implements ListIterator<E> {

    static final EmptyListIterator<@Nullable Object> INSTANCE = new EmptyListIterator<>();

    @Override
    public void add(E o) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasNext() {
      return false;
    }

    @Override
    public boolean hasPrevious() {
      return false;
    }

    @Override
    public E next() {
      throw new NoSuchElementException();
    }

    @Override
    public int nextIndex() {
      return 0;
    }

    @Override
    public E previous() {
      throw new NoSuchElementException();
    }

    @Override
    public int previousIndex() {
      return -1;
    }

    @Override
    public void remove() {
      throw new IllegalStateException();
    }

    @Override
    public void set(E o) {
      throw new IllegalStateException();
    }
  }

  private static final class EmptySet<E extends @Nullable Object> extends AbstractSet<E>
      implements Serializable {
    @Override
    public boolean contains(@Nullable Object object) {
      return false;
    }

    @Override
    public Iterator<E> iterator() {
      return emptyIterator();
    }

    @Override
    public int size() {
      return 0;
    }
  }

  private static final class EmptyMap<K extends @Nullable Object, V extends @Nullable Object>
      extends AbstractMap<K, V> implements Serializable {
    @Override
    public boolean containsKey(@Nullable Object key) {
      return false;
    }

    @Override
    public boolean containsValue(@Nullable Object value) {
      return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Map.Entry<K, V>> entrySet() {
      return (Set<Map.Entry<K, V>>) EMPTY_SET;
    }

    @Override
    public @Nullable V get(@Nullable Object key) {
      return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<K> keySet() {
      return (Set<K>) EMPTY_SET;
    }

    @Override
    public int size() {
      return 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<V> values() {
      return (List<V>) EMPTY_LIST;
    }
  }

  private static final class SetFromMap<E extends @Nullable Object> extends AbstractSet<E>
      implements Serializable {

    private final Map<E, Boolean> backingMap;
    private transient @Nullable Set<E> keySet;

    SetFromMap(Map<E, Boolean> map) {
      backingMap = map;
    }

    @Override
    public boolean add(E e) {
      return backingMap.put(e, Boolean.TRUE) == null;
    }

    @Override
    public void clear() {
      backingMap.clear();
    }

    @Override
    public boolean contains(@Nullable Object o) {
      return backingMap.containsKey(o);
    }

    @Override
    public boolean equals(@Nullable Object o) {
      return o == this || keySet().equals(o);
    }

    @Override
    public int hashCode() {
      return keySet().hashCode();
    }

    @Override
    public Iterator<E> iterator() {
      return keySet().iterator();
    }

    @Override
    public boolean remove(@Nullable Object o) {
      return backingMap.remove(o) != null;
    }

    @Override
    public int size() {
      return keySet().size();
    }

    @Override
    public String toString() {
      return keySet().toString();
    }

    /** Lazy initialize keySet to avoid NPE after deserialization. */
    private Set<E> keySet() {
      if (keySet == null) {
        keySet = backingMap.keySet();
      }
      return keySet;
    }
  }

  private static final class SingletonList<E extends @Nullable Object> extends AbstractList<E>
      implements Serializable {
    private final E element;

    public SingletonList(E element) {
      this.element = element;
    }

    @Override
    public boolean contains(@Nullable Object item) {
      return Objects.equals(element, item);
    }

    @Override
    public E get(int index) {
      checkElementIndex(index, 1);
      return element;
    }

    @Override
    public int size() {
      return 1;
    }
  }

  // The synchronized* methods were not necessary for j2kt apps yet. They could be added if a
  // new app needs them, but ideally we'd move away from them in general (see cl/499941876).
  // Another argument against adding them is that it won't be possible to use them directly as a
  // monitor, which is suggested by the original JavaDoc for iteration locking.

  /*
   * TODO: make the unmodifiable collections serializable.
   */

  static class UnmodifiableCollection<T extends @Nullable Object> implements Collection<T> {
    protected final Collection<? extends T> coll;

    public UnmodifiableCollection(Collection<? extends T> coll) {
      this.coll = coll;
    }

    @Override
    public boolean add(T o) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(@Nullable Object o) {
      return coll.contains(o);
    }

    @Override
    public boolean containsAll(Collection<? extends @Nullable Object> c) {
      return coll.containsAll(c);
    }

    @Override
    public boolean isEmpty() {
      return coll.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
      return new UnmodifiableCollectionIterator<T>(coll.iterator());
    }

    @Override
    public boolean remove(@Nullable Object o) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<? extends @Nullable Object> c) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<? extends @Nullable Object> c) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeIf(Predicate<? super T> p) {
      throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
      return coll.size();
    }

    @Override
    public @Nullable Object[] toArray() {
      return coll.toArray();
    }

    @Override
    public <E extends @Nullable Object> E[] toArray(E[] a) {
      return coll.toArray(a);
    }

    @Override
    public String toString() {
      return coll.toString();
    }
  }

  static class UnmodifiableList<T extends @Nullable Object> extends UnmodifiableCollection<T>
      implements List<T> {
    private final List<? extends T> list;

    public UnmodifiableList(List<? extends T> list) {
      super(list);
      this.list = list;
    }

    @Override
    public void add(int index, T element) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(@Nullable Object o) {
      return list.equals(o);
    }

    @Override
    public T get(int index) {
      return list.get(index);
    }

    @Override
    public int hashCode() {
      return list.hashCode();
    }

    @Override
    public int indexOf(@Nullable Object o) {
      return list.indexOf(o);
    }

    @Override
    public boolean isEmpty() {
      return list.isEmpty();
    }

    @Override
    public int lastIndexOf(@Nullable Object o) {
      return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
      return listIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int from) {
      return new UnmodifiableListIterator<T>(list.listIterator(from));
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void sort(@Nullable Comparator<? super T> c) {
      throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
      throw new UnsupportedOperationException();
    }

    @Override
    public T set(int index, T element) {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
      return new UnmodifiableList<T>(list.subList(fromIndex, toIndex));
    }
  }

  static class UnmodifiableMap<K extends @Nullable Object, V extends @Nullable Object>
      implements Map<K, V> {

    static class UnmodifiableEntrySet<K extends @Nullable Object, V extends @Nullable Object>
        extends UnmodifiableSet<Map.Entry<K, V>> {

      private static class UnmodifiableEntry<K extends @Nullable Object, V extends @Nullable Object>
          implements Map.Entry<K, V> {
        private final Map.Entry<? extends K, ? extends V> entry;

        public UnmodifiableEntry(Map.Entry<? extends K, ? extends V> entry) {
          this.entry = entry;
        }

        @Override
        public boolean equals(@Nullable Object o) {
          return entry.equals(o);
        }

        @Override
        public K getKey() {
          return entry.getKey();
        }

        @Override
        public V getValue() {
          return entry.getValue();
        }

        @Override
        public int hashCode() {
          return entry.hashCode();
        }

        @Override
        public V setValue(V value) {
          throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
          return entry.toString();
        }
      }

      @SuppressWarnings("unchecked")
      public UnmodifiableEntrySet(Set<? extends Map.Entry<? extends K, ? extends V>> s) {
        // Need to cast to raw in order to work around a limitation in the type system
        super((Set) s);
      }

      @Override
      public boolean contains(@Nullable Object o) {
        return coll.contains(o);
      }

      @Override
      public boolean containsAll(Collection<? extends @Nullable Object> o) {
        return coll.containsAll(o);
      }

      @Override
      @SuppressWarnings("unchecked")
      public Iterator<Map.Entry<K, V>> iterator() {
        final Iterator<Map.Entry<K, V>> it = (Iterator<Entry<K, V>>) coll.iterator();
        return new Iterator<Map.Entry<K, V>>() {
          @Override
          public boolean hasNext() {
            return it.hasNext();
          }

          @Override
          public Map.Entry<K, V> next() {
            return new UnmodifiableEntry<K, V>(it.next());
          }

          @Override
          public void remove() {
            throw new UnsupportedOperationException();
          }
        };
      }

      @Override
      public @Nullable Object[] toArray() {
        Object[] array = super.toArray();
        wrap(array, array.length);
        return array;
      }

      @Override
      @SuppressWarnings("unchecked")
      public <T extends @Nullable Object> T[] toArray(T[] a) {
        Object[] result = super.toArray(a);
        wrap(result, coll.size());
        return (T[]) result;
      }

      /**
       * Wrap an array of Map.Entries as UnmodifiableEntries.
       *
       * @param array array to wrap
       * @param size number of entries to wrap
       */
      @SuppressWarnings("unchecked")
      private void wrap(Object[] array, int size) {
        for (int i = 0; i < size; ++i) {
          array[i] = new UnmodifiableEntry<K, V>((Map.Entry<K, V>) array[i]);
        }
      }
    }

    private transient @Nullable UnmodifiableSet<Map.Entry<K, V>> entrySet;
    private transient @Nullable UnmodifiableSet<K> keySet;
    private final Map<? extends K, ? extends V> map;
    private transient @Nullable UnmodifiableCollection<V> valuesCollection;

    public UnmodifiableMap(Map<? extends K, ? extends V> map) {
      this.map = map;
    }

    @Override
    public void clear() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsKey(@Nullable Object key) {
      return map.containsKey(key);
    }

    @Override
    public boolean containsValue(@Nullable Object val) {
      return map.containsValue(val);
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
      if (entrySet == null) {
        entrySet = new UnmodifiableEntrySet<K, V>(map.entrySet());
      }
      return entrySet;
    }

    @Override
    public boolean equals(@Nullable Object o) {
      return map.equals(o);
    }

    @Override
    public @Nullable V get(@Nullable Object key) {
      return map.get(key);
    }

    @Override
    public int hashCode() {
      return map.hashCode();
    }

    @Override
    public boolean isEmpty() {
      return map.isEmpty();
    }

    @Override
    public Set<K> keySet() {
      if (keySet == null) {
        keySet = new UnmodifiableSet<K>(map.keySet());
      }
      return keySet;
    }

    @Override
    public @Nullable V put(K key, V value) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> t) {
      throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable V remove(@Nullable Object key) {
      throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
      return map.size();
    }

    @Override
    public String toString() {
      return map.toString();
    }

    @Override
    public Collection<V> values() {
      if (valuesCollection == null) {
        valuesCollection = new UnmodifiableCollection<V>(map.values());
      }
      return valuesCollection;
    }
  }

  static class UnmodifiableRandomAccessList<T extends @Nullable Object> extends UnmodifiableList<T>
      implements RandomAccess {
    public UnmodifiableRandomAccessList(List<? extends T> list) {
      super(list);
    }
  }

  static class UnmodifiableSet<T extends @Nullable Object> extends UnmodifiableCollection<T>
      implements Set<T> {
    public UnmodifiableSet(Set<? extends T> set) {
      super(set);
    }

    @Override
    public boolean equals(@Nullable Object o) {
      return coll.equals(o);
    }

    @Override
    public int hashCode() {
      return coll.hashCode();
    }
  }

  static class UnmodifiableSortedMap<K extends @Nullable Object, V extends @Nullable Object>
      extends UnmodifiableMap<K, V> implements SortedMap<K, V> {

    private final SortedMap<K, ? extends V> sortedMap;

    public UnmodifiableSortedMap(SortedMap<K, ? extends V> sortedMap) {
      super(sortedMap);
      this.sortedMap = sortedMap;
    }

    @Override
    public Comparator<? super K> comparator() {
      return sortedMap.comparator();
    }

    @Override
    public boolean equals(@Nullable Object o) {
      return sortedMap.equals(o);
    }

    @Override
    public K firstKey() {
      return sortedMap.firstKey();
    }

    @Override
    public int hashCode() {
      return sortedMap.hashCode();
    }

    @Override
    public SortedMap<K, V> headMap(K toKey) {
      return new UnmodifiableSortedMap<K, V>(sortedMap.headMap(toKey));
    }

    @Override
    public K lastKey() {
      return sortedMap.lastKey();
    }

    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
      return new UnmodifiableSortedMap<K, V>(sortedMap.subMap(fromKey, toKey));
    }

    @Override
    public SortedMap<K, V> tailMap(K fromKey) {
      return new UnmodifiableSortedMap<K, V>(sortedMap.tailMap(fromKey));
    }
  }

  static class UnmodifiableSortedSet<E extends @Nullable Object> extends UnmodifiableSet<E>
      implements SortedSet<E> {
    private final SortedSet<E> sortedSet;

    @SuppressWarnings("unchecked")
    public UnmodifiableSortedSet(SortedSet<? extends E> sortedSet) {
      super(sortedSet);
      this.sortedSet = (SortedSet<E>) sortedSet;
    }

    @Override
    public Comparator<? super E> comparator() {
      return sortedSet.comparator();
    }

    @Override
    public boolean equals(@Nullable Object o) {
      return sortedSet.equals(o);
    }

    @Override
    public E first() {
      return sortedSet.first();
    }

    @Override
    public int hashCode() {
      return sortedSet.hashCode();
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
      return new UnmodifiableSortedSet<E>(sortedSet.headSet(toElement));
    }

    @Override
    public E last() {
      return sortedSet.last();
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
      return new UnmodifiableSortedSet<E>(sortedSet.subSet(fromElement, toElement));
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
      return new UnmodifiableSortedSet<E>(sortedSet.tailSet(fromElement));
    }
  }

  private static class UnmodifiableCollectionIterator<T extends @Nullable Object>
      implements Iterator<T> {
    private final Iterator<? extends T> it;

    private UnmodifiableCollectionIterator(Iterator<? extends T> it) {
      this.it = it;
    }

    @Override
    public boolean hasNext() {
      return it.hasNext();
    }

    @Override
    public T next() {
      return it.next();
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  private static class UnmodifiableListIterator<T extends @Nullable Object>
      extends UnmodifiableCollectionIterator<T> implements ListIterator<T> {
    private final ListIterator<? extends T> lit;

    private UnmodifiableListIterator(ListIterator<? extends T> lit) {
      super(lit);
      this.lit = lit;
    }

    @Override
    public void add(T o) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasPrevious() {
      return lit.hasPrevious();
    }

    @Override
    public int nextIndex() {
      return lit.nextIndex();
    }

    @Override
    public T previous() {
      return lit.previous();
    }

    @Override
    public int previousIndex() {
      return lit.previousIndex();
    }

    @Override
    public void set(T o) {
      throw new UnsupportedOperationException();
    }
  }

  private static class RandomHolder {
    private static final Random rnd = new Random();
  }

  @SuppressWarnings("unchecked")
  public static final List<?> EMPTY_LIST = new EmptyList<>();

  @SuppressWarnings("unchecked")
  public static final Map<?, ?> EMPTY_MAP = new EmptyMap<>();

  @SuppressWarnings("unchecked")
  public static final Set<?> EMPTY_SET = new EmptySet<>();

  public static <T extends @Nullable Object> boolean addAll(Collection<? super T> c, T... a) {
    boolean result = false;
    for (T e : a) {
      result |= c.add(e);
    }
    return result;
  }

  public static <T> Queue<T> asLifoQueue(Deque<T> deque) {
    return new LifoQueue<T>(deque);
  }

  /**
   * Perform a binary search on a sorted List, using natural ordering.
   *
   * <p>Note: The GWT implementation differs from the JDK implementation in that it does not do an
   * iterator-based binary search for Lists that do not implement RandomAccess.
   *
   * @param sortedList object array to search
   * @param key value to search for
   * @return the index of an element with a matching value, or a negative number which is the index
   *     of the next larger value (or just past the end of the array if the searched value is larger
   *     than all elements in the array) minus 1 (to ensure error returns are negative)
   * @throws ClassCastException if <code>key</code> is not comparable to <code>sortedList</code>'s
   *     elements.
   */
  public static <T> int binarySearch(
      final List<? extends Comparable<? super T>> sortedList, final T key) {
    return binarySearch(sortedList, key, null);
  }

  /*
   * These methods are commented out because they cannot currently be
   * implemented in GWT. The signatures are included in case that changes.
   */
  // public static <E> Collection<E> checkedCollection(Collection<E> c, Class<E>
  // type) {
  // // FUTURE: implement
  // return null;
  // }
  //
  // static <E> List<E> checkedList(List<E> list, Class<E> type) {
  // // FUTURE: implement
  // return null;
  // }
  //
  // public static <K,V> Map<K,V> checkedMap(Map<K,V> list, Class<K> keyType,
  // Class<V> valueType) {
  // // FUTURE: implement
  // return null;
  // }
  //
  // public static <E> Set<E> checkedSet(Set<E> list, Class<E> type) {
  // // FUTURE: implement
  // return null;
  // }
  //
  // public static <K,V> SortedMap<K,V> checkedSortedMap(SortedMap<K,V> m,
  // Class<K> keyType, Class<V> valueType) {
  // // FUTURE: implement
  // return null;
  // }
  //
  // public static <E> SortedSet<E> checkedSortedSet(SortedSet<E> list, Class<E>
  // type) {
  // // FUTURE: implement
  // return null;
  // }
  /**
   * Perform a binary search on a sorted List, using a user-specified comparison function.
   *
   * <p>Note: The GWT implementation differs from the JDK implementation in that it does not do an
   * iterator-based binary search for Lists that do not implement RandomAccess.
   *
   * @param sortedList List to search
   * @param key value to search for
   * @param comparator comparision function, <code>null</code> indicates <i>natural ordering</i>
   *     should be used.
   * @return the index of an element with a matching value, or a negative number which is the index
   *     of the next larger value (or just past the end of the array if the searched value is larger
   *     than all elements in the array) minus 1 (to ensure error returns are negative)
   * @throws ClassCastException if <code>key</code> and <code>sortedList</code>'s elements cannot be
   *     compared by <code>comparator</code>.
   */
  public static <T extends @Nullable Object> int binarySearch(
      final List<? extends T> sortedList, final T key, @Nullable Comparator<? super T> comparator) {
    /*
     * TODO: This doesn't implement the "iterator-based binary search" described
     * in the JDK docs for non-RandomAccess Lists. Until GWT provides a
     * LinkedList, this shouldn't be an issue.
     */
    comparator = Comparators.nullToNaturalOrder(comparator);
    int low = 0;
    int high = sortedList.size() - 1;

    while (low <= high) {
      final int mid = low + ((high - low) >> 1);
      final T midVal = sortedList.get(mid);
      final int compareResult = comparator.compare(midVal, key);

      if (compareResult < 0) {
        low = mid + 1;
      } else if (compareResult > 0) {
        high = mid - 1;
      } else {
        // key found
        return mid;
      }
    }
    // key not found.
    return -low - 1;
  }

  public static <T extends @Nullable Object> void copy(
      List<? super T> dest, List<? extends T> src) {
    if (src.size() > dest.size()) {
      throw new IndexOutOfBoundsException("src does not fit in dest");
    }

    ListIterator<? super T> destIt = dest.listIterator();
    for (T e : src) {
      destIt.next();
      destIt.set(e);
    }
  }

  @SuppressWarnings("unchecked")
  public static boolean disjoint(Collection<?> c1, Collection<?> c2) {
    Collection<@Nullable Object> iterating = (Collection<@Nullable Object>) c1;
    Collection<@Nullable Object> testing = (Collection<@Nullable Object>) c2;

    // See if one of these objects possibly implements a fast contains.
    if ((c1 instanceof Set) && !(c2 instanceof Set)) {
      iterating = (Collection<@Nullable Object>) c2;
      testing = (Collection<@Nullable Object>) c1;
    }

    for (Object o : iterating) {
      if (testing.contains(o)) {
        return false;
      }
    }

    return true;
  }

  @SuppressWarnings(value = {"unchecked", "cast"})
  public static <T extends @Nullable Object> Iterator<T> emptyIterator() {
    return (Iterator<T>) EmptyListIterator.INSTANCE;
  }

  @SuppressWarnings(value = {"unchecked", "cast"})
  public static <T extends @Nullable Object> List<T> emptyList() {
    return (List<T>) EMPTY_LIST;
  }

  @SuppressWarnings(value = {"unchecked", "cast"})
  public static <T extends @Nullable Object> ListIterator<T> emptyListIterator() {
    return (ListIterator<T>) EmptyListIterator.INSTANCE;
  }

  @SuppressWarnings(value = {"unchecked", "cast"})
  public static <K extends @Nullable Object, V extends @Nullable Object> Map<K, V> emptyMap() {
    return (Map<K, V>) EMPTY_MAP;
  }

  @SuppressWarnings(value = {"unchecked", "cast"})
  public static <T extends @Nullable Object> Set<T> emptySet() {
    return (Set<T>) EMPTY_SET;
  }

  public static <T extends @Nullable Object> Enumeration<T> enumeration(Collection<T> c) {
    final Iterator<T> it = c.iterator();
    return new Enumeration<T>() {
      @Override
      public boolean hasMoreElements() {
        return it.hasNext();
      }

      @Override
      public T nextElement() {
        return it.next();
      }
    };
  }

  public static <T extends @Nullable Object> void fill(List<? super T> list, T obj) {
    for (ListIterator<? super T> it = list.listIterator(); it.hasNext(); ) {
      it.next();
      it.set(obj);
    }
  }

  public static int frequency(Collection<?> c, Object o) {
    int count = 0;
    for (Object e : c) {
      if (Objects.equals(o, e)) {
        ++count;
      }
    }
    return count;
  }

  public static <T extends @Nullable Object> ArrayList<T> list(Enumeration<T> e) {
    ArrayList<T> arrayList = new ArrayList<T>();
    while (e.hasMoreElements()) {
      arrayList.add(e.nextElement());
    }
    return arrayList;
  }

  public static <T extends Comparable<? super T>> T max(Collection<? extends T> coll) {
    return max(coll, null);
  }

  public static <T extends @Nullable Object> T max(
      Collection<? extends T> coll, @Nullable Comparator<? super T> comp) {

    comp = Comparators.nullToNaturalOrder(comp);

    Iterator<? extends T> it = coll.iterator();

    // Will throw NoSuchElementException if coll is empty.
    T max = it.next();

    while (it.hasNext()) {
      T t = it.next();
      if (comp.compare(t, max) > 0) {
        max = t;
      }
    }

    return max;
  }

  public static <T extends Comparable<? super T>> T min(Collection<? extends T> coll) {
    return min(coll, null);
  }

  public static <T extends @Nullable Object> T min(
      Collection<? extends T> coll, @Nullable Comparator<? super T> comp) {
    return max(coll, reverseOrder(comp));
  }

  public static <E extends @Nullable Object> Set<E> newSetFromMap(Map<E, Boolean> map) {
    checkArgument(map.isEmpty(), "map is not empty");
    return new SetFromMap<E>(map);
  }

  public static <T extends @Nullable Object> List<T> nCopies(int n, T o) {
    ArrayList<T> list = new ArrayList<>();
    for (int i = 0; i < n; ++i) {
      list.add(o);
    }
    return unmodifiableList(list);
  }

  public static <T extends @Nullable Object> boolean replaceAll(List<T> list, T oldVal, T newVal) {
    boolean modified = false;
    for (ListIterator<T> it = list.listIterator(); it.hasNext(); ) {
      T t = it.next();
      if (Objects.equals(t, oldVal)) {
        it.set(newVal);
        modified = true;
      }
    }
    return modified;
  }

  @SuppressWarnings("unchecked")
  public static void reverse(List<?> l) {
    if (l instanceof RandomAccess) {
      for (int iFront = 0, iBack = l.size() - 1; iFront < iBack; ++iFront, --iBack) {
        Collections.swap(l, iFront, iBack);
      }
    } else {
      ListIterator<@Nullable Object> head = (ListIterator<@Nullable Object>) l.listIterator();
      ListIterator<@Nullable Object> tail =
          (ListIterator<@Nullable Object>) l.listIterator(l.size());
      while (head.nextIndex() < tail.previousIndex()) {
        Object headElem = head.next();
        Object tailElem = tail.previous();
        head.set(tailElem);
        tail.set(headElem);
      }
    }
  }

  @SuppressWarnings("unchecked")
  public static <T extends @Nullable Object> Comparator<T> reverseOrder() {
    return (Comparator<T>) Comparator.<Comparable>reverseOrder();
  }

  public static <T extends @Nullable Object> Comparator<T> reverseOrder(
      @Nullable Comparator<T> cmp) {
    return cmp == null ? reverseOrder() : cmp.reversed();
  }

  /**
   * Rotates the elements in {@code lst} by the distance {@code dist}
   *
   * <p>e.g. for a given list with elements [1, 2, 3, 4, 5, 6, 7, 8, 9, 0], calling rotate(list, 3)
   * or rotate(list, -7) would modify the list to look like this: [8, 9, 0, 1, 2, 3, 4, 5, 6, 7]
   *
   * @param lst the list whose elements are to be rotated.
   * @param dist is the distance the list is rotated. This can be any valid integer. Negative values
   *     rotate the list backwards.
   */
  @SuppressWarnings("unchecked")
  public static void rotate(List<?> lst, int dist) {
    checkNotNull(lst);
    int size = lst.size();

    // Rotating an empty collection results in the same empty collection
    if (size == 0) {
      return;
    }

    // Normalize the distance
    int normdist = dist % size;
    if (normdist == 0) {
      return;
    }
    // Transform a rotation to the left into the equivalent rotation to the right.
    if (normdist < 0) {
      normdist += size;
    }

    if (lst instanceof RandomAccess) {
      List<@Nullable Object> list = (List<@Nullable Object>) lst;
      // Move each element to the new location.
      Object temp = list.get(0);
      int index = 0, beginIndex = 0;
      for (int i = 0; i < size; i++) {
        index = (index + normdist) % size;
        temp = list.set(index, temp);
        if (index == beginIndex) {
          index = ++beginIndex;
          temp = list.get(beginIndex);
        }
      }
    } else {
      int divideIndex = size - normdist;
      List<?> sublist1 = lst.subList(0, divideIndex);
      List<?> sublist2 = lst.subList(divideIndex, size);
      reverse(sublist1);
      reverse(sublist2);
      reverse(lst);
    }
  }

  public static void shuffle(List<?> list) {
    shuffle(list, RandomHolder.rnd);
  }

  @SuppressWarnings("unchecked")
  public static void shuffle(List<?> list, Random rnd) {
    if (list instanceof RandomAccess) {
      for (int i = list.size() - 1; i >= 1; i--) {
        swapImpl(list, i, rnd.nextInt(i + 1));
      }
    } else {
      Object arr[] = list.toArray();
      for (int i = arr.length - 1; i >= 1; i--) {
        swapImpl(arr, i, rnd.nextInt(i + 1));
      }

      ListIterator<@Nullable Object> it = (ListIterator<@Nullable Object>) list.listIterator();
      for (Object e : arr) {
        it.next();
        it.set(e);
      }
    }
  }

  public static <T extends @Nullable Object> Set<T> singleton(T o) {
    HashSet<T> set = new HashSet<T>(1);
    set.add(o);
    return unmodifiableSet(set);
  }

  // TODO(tobyr) Is it worth creating custom singleton sets, lists, and maps?
  // More efficient at runtime, but more code bloat to download

  public static <T extends @Nullable Object> List<T> singletonList(T o) {
    return new SingletonList<T>(o);
  }

  public static <K extends @Nullable Object, V extends @Nullable Object> Map<K, V> singletonMap(
      K key, V value) {
    Map<K, V> map = new HashMap<K, V>(1);
    map.put(key, value);
    return unmodifiableMap(map);
  }

  public static <T extends Comparable<? super T>> void sort(List<T> target) {
    target.sort(null);
  }

  public static <T extends @Nullable Object> void sort(
      List<T> target, @Nullable Comparator<? super T> c) {
    target.sort(c);
  }

  public static void swap(List<?> list, int i, int j) {
    swapImpl(list, i, j);
  }

  public static <T extends @Nullable Object> Collection<T> unmodifiableCollection(
      final Collection<? extends T> coll) {
    return new UnmodifiableCollection<T>(coll);
  }

  public static <T extends @Nullable Object> List<T> unmodifiableList(List<? extends T> list) {
    return (list instanceof RandomAccess)
        ? new UnmodifiableRandomAccessList<T>(list)
        : new UnmodifiableList<T>(list);
  }

  public static <K extends @Nullable Object, V extends @Nullable Object> Map<K, V> unmodifiableMap(
      final Map<? extends K, ? extends V> map) {
    return new UnmodifiableMap<K, V>(map);
  }

  public static <T extends @Nullable Object> Set<T> unmodifiableSet(Set<? extends T> set) {
    return new UnmodifiableSet<T>(set);
  }

  public static <K extends @Nullable Object, V extends @Nullable Object>
      SortedMap<K, V> unmodifiableSortedMap(SortedMap<K, ? extends V> map) {
    return new UnmodifiableSortedMap<K, V>(map);
  }

  public static <T extends @Nullable Object> SortedSet<T> unmodifiableSortedSet(SortedSet<T> set) {
    return new UnmodifiableSortedSet<T>(set);
  }

  /** Computes hash code without preserving elements order (e.g. HashSet). */
  static <T extends @Nullable Object> int hashCode(Iterable<T> collection) {
    int hashCode = 0;
    for (T e : collection) {
      hashCode = hashCode + Objects.hashCode(e);
    }
    return hashCode;
  }

  /** Computes hash code preserving collection order (e.g. ArrayList). */
  static <T extends @Nullable Object> int hashCode(List<T> list) {
    int hashCode = 1;
    for (T e : list) {
      hashCode = 31 * hashCode + Objects.hashCode(e);
    }
    return hashCode;
  }

  private static <T extends @Nullable Object> void swapImpl(List<T> list, int i, int j) {
    T t = list.get(i);
    list.set(i, list.get(j));
    list.set(j, t);
  }

  private static void swapImpl(Object[] a, int i, int j) {
    Object obj = a[i];
    a[i] = a[j];
    a[j] = obj;
  }

  private Collections() {}
}
