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

import static javaemul.internal.InternalPreconditions.checkElement;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * To keep performance characteristics in line with Java community expectations, <code>Vector</code>
 * is a wrapper around <code>ArrayList</code>. See <a
 * href="https://docs.oracle.com/javase/8/docs/api/java/util/Vector.html">the official Java API
 * doc</a> for details.
 *
 * @param <E> element type.
 */
@NullMarked
public class Vector<E extends @Nullable Object> extends AbstractList<E>
    implements List<E>, RandomAccess, Cloneable, Serializable {

  private transient ArrayList<E> arrayList;

  /**
   * Ensures that RPC will consider type parameter E to be exposed. It will be pruned by dead code
   * elimination.
   */
  @SuppressWarnings("unused")
  private @Nullable E exposeElement;

  public Vector() {
    arrayList = new ArrayList<E>();
  }

  public Vector(Collection<? extends E> c) {
    arrayList = new ArrayList<E>();
    addAll(c);
  }

  public Vector(int initialCapacity) {
    arrayList = new ArrayList<E>(initialCapacity);
  }

  /** Capacity increment is ignored. */
  @SuppressWarnings("unused")
  public Vector(int initialCapacity, int ignoredCapacityIncrement) {
    this(initialCapacity);
  }

  @Override
  public boolean add(E o) {
    return arrayList.add(o);
  }

  @Override
  public void add(int index, E o) {
    checkArrayElementIndex(index, size() + 1);
    arrayList.add(index, o);
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    return arrayList.addAll(c);
  }

  @Override
  public boolean addAll(int index, Collection<? extends E> c) {
    checkArrayElementIndex(index, size() + 1);
    return arrayList.addAll(index, c);
  }

  public void addElement(E o) {
    add(o);
  }

  public int capacity() {
    return arrayList.size();
  }

  @Override
  public void clear() {
    arrayList.clear();
  }

  @Override
  public Object clone() {
    return new Vector<E>(this);
  }

  @Override
  public boolean contains(@Nullable Object elem) {
    return arrayList.contains(elem);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return arrayList.containsAll(c);
  }

  public void copyInto(@Nullable Object[] objs) {
    int i = -1;
    int n = size();
    while (++i < n) {
      objs[i] = get(i);
    }
  }

  public E elementAt(int index) {
    return get(index);
  }

  public Enumeration<E> elements() {
    return Collections.enumeration(arrayList);
  }

  public void ensureCapacity(int capacity) {
    arrayList.ensureCapacity(capacity);
  }

  public E firstElement() {
    checkElement(!isEmpty());
    return get(0);
  }

  @Override
  public void forEach(Consumer<? super E> consumer) {
    arrayList.forEach(consumer);
  }

  @Override
  public E get(int index) {
    checkArrayElementIndex(index, size());
    return arrayList.get(index);
  }

  @Override
  public int indexOf(@Nullable Object elem) {
    return arrayList.indexOf(elem);
  }

  public int indexOf(@Nullable Object elem, int index) {
    checkArrayIndexOutOfBounds(index >= 0, index);
    for (; index < arrayList.size(); ++index) {
      if (Objects.equals(elem, arrayList.get(index))) {
        return index;
      }
    }
    return -1;
  }

  public void insertElementAt(E o, int index) {
    add(index, o);
  }

  @Override
  public boolean isEmpty() {
    return (arrayList.size() == 0);
  }

  @Override
  public Iterator<E> iterator() {
    return arrayList.iterator();
  }

  public E lastElement() {
    checkElement(!isEmpty());
    return get(size() - 1);
  }

  @Override
  public int lastIndexOf(@Nullable Object o) {
    return arrayList.lastIndexOf(o);
  }

  public int lastIndexOf(@Nullable Object o, int index) {
    checkArrayIndexOutOfBounds(index < size(), index);
    for (; index >= 0; --index) {
      if (Objects.equals(o, arrayList.get(index))) {
        return index;
      }
    }
    return -1;
  }

  @Override
  public E remove(int index) {
    checkArrayElementIndex(index, size());
    return arrayList.remove(index);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return arrayList.removeAll(c);
  }

  public void removeAllElements() {
    clear();
  }

  public boolean removeElement(Object o) {
    return remove(o);
  }

  public void removeElementAt(int index) {
    remove(index);
  }

  @Override
  public boolean removeIf(Predicate<? super E> filter) {
    return arrayList.removeIf(filter);
  }

  @Override
  public void replaceAll(UnaryOperator<E> op) {
    arrayList.replaceAll(op);
  }

  @Override
  public E set(int index, E elem) {
    checkArrayElementIndex(index, size());
    return arrayList.set(index, elem);
  }

  public void setElementAt(E o, int index) {
    set(index, o);
  }

  public void setSize(int size) {
    checkArrayIndexOutOfBounds(size >= 0, size);
    if (size > arrayList.size()) {
      removeRange(size, arrayList.size());
    } else {
      while (arrayList.size() < size) {
        arrayList.add(null);
      }
    }
  }

  @Override
  public int size() {
    return arrayList.size();
  }

  @Override
  public void sort(@Nullable Comparator<? super E> c) {
    arrayList.sort(c);
  }

  @Override
  public List<E> subList(int fromIndex, int toIndex) {
    return arrayList.subList(fromIndex, toIndex);
  }

  @Override
  public @Nullable Object[] toArray() {
    return arrayList.toArray();
  }

  @Override
  public <T extends @Nullable Object> T[] toArray(T[] a) {
    return arrayList.toArray(a);
  }

  @Override
  public String toString() {
    return arrayList.toString();
  }

  public void trimToSize() {
    arrayList.trimToSize();
  }

  @Override
  protected void removeRange(int fromIndex, int endIndex) {
    arrayList.subList(fromIndex, endIndex).clear();
  }

  private static void checkArrayElementIndex(int index, int size) {
    if (index < 0 || index >= size) {
      throw new ArrayIndexOutOfBoundsException();
    }
  }

  private static void checkArrayIndexOutOfBounds(boolean expression, int index) {
    if (!expression) {
      throw new ArrayIndexOutOfBoundsException(String.valueOf(index));
    }
  }
}
