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
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/AbstractList.html">the official
 * Java API doc</a> for details.
 */
@KtNative(
    value = "kotlin.collections.AbstractMutableList",
    bridgeWith = "javaemul.lang.JavaAbstractList")
@NullMarked
public abstract class AbstractList<E extends @Nullable Object> extends AbstractCollection<E>
    implements List<E> {

  protected transient int modCount;

  protected AbstractList() {}

  @Override
  public native boolean add(E element);

  @Override
  public native void add(int index, E element);

  @Override
  public native boolean addAll(int index, Collection<? extends E> c);

  @Override
  public native int indexOf(@Nullable Object toFind);

  @Override
  public native Iterator<E> iterator();

  @Override
  public native int lastIndexOf(@Nullable Object toFind);

  @Override
  public native ListIterator<E> listIterator();

  @Override
  public native ListIterator<E> listIterator(int from);

  @Override
  public native E remove(int index);

  @Override
  public native boolean removeAll(Collection<? extends @Nullable Object> c);

  protected native void removeRange(int fromIndex, int endIndex);

  @Override
  public native boolean retainAll(Collection<? extends @Nullable Object> c);

  @Override
  public native E set(int index, E e);

  @Override
  public native List<E> subList(int fromIndex, int toIndex);
}
