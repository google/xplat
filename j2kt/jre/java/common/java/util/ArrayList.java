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

/**
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html">the official
 * Java API doc</a> for details.
 */
@KtNative("kotlin.collections.ArrayList")
@NullMarked
public class ArrayList<E extends @Nullable Object> implements List<E>, RandomAccess {

  public ArrayList() {}

  public ArrayList(Collection<? extends E> c) {}

  public ArrayList(int initialCapacity) {}

  @Override
  public native boolean add(E e);

  @Override
  public native void add(int index, E e);

  @Override
  public native boolean addAll(Collection<? extends E> c);

  @Override
  public native boolean addAll(int index, Collection<? extends E> c);

  @Override
  public native void clear();

  @Override
  public native boolean contains(@Nullable Object o);

  @Override
  public native boolean containsAll(Collection<? extends @Nullable Object> c);

  public native void ensureCapacity(int minCapacity);

  @Override
  public native E get(int index);

  @Override
  public native int indexOf(@Nullable Object o);

  @Override
  public native Iterator<E> iterator();

  @Override
  public native boolean isEmpty();

  @Override
  public native int lastIndexOf(@Nullable Object o);

  @Override
  public native ListIterator<E> listIterator();

  @Override
  public native ListIterator<E> listIterator(int index);

  @Override
  public native E remove(int index);

  @Override
  public native boolean remove(@Nullable Object o);

  @Override
  public native boolean $kotlin_remove(E e);

  @Override
  public native boolean removeAll(Collection<? extends @Nullable Object> c);

  @Override
  public native boolean $kotlin_removeAll(KotlinCollection<? extends E> c);

  @Override
  public native boolean retainAll(Collection<? extends @Nullable Object> c);

  @Override
  public native boolean $kotlin_retainAll(KotlinCollection<? extends E> c);

  @Override
  public native E set(int index, E e);

  @Override
  public native int size();

  @Override
  public native List<E> subList(int fromIndex, int toIndex);

  public native void trimToSize();
}
