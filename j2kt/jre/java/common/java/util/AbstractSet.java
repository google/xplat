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
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/AbstractSet.html">the official
 * Java API doc</a> for details.
 */
@KtNative(
    name = "kotlin.collections.AbstractMutableSet",
    bridgeName = "javaemul.lang.JavaAbstractSet")
@NullMarked
public abstract class AbstractSet<E extends @Nullable Object> implements Set<E> {

  protected AbstractSet() {}

  @Override
  public native boolean add(E e);

  @Override
  public native boolean addAll(Collection<? extends E> c);

  @Override
  public native boolean $kotlin_addAll($Kotlin_Collection<? extends E> c);

  @Override
  public native void clear();

  @Override
  public native boolean contains(@Nullable Object o);

  @Override
  public native boolean $kotlin_contains(E e);

  @Override
  public native boolean containsAll(Collection<?> c);

  @Override
  public native boolean $kotlin_containsAll($Kotlin_Collection<? extends E> c);

  @Override
  public native boolean isEmpty();

  @Override
  public abstract Iterator<E> iterator();

  @Override
  public native boolean remove(@Nullable Object o);

  @Override
  public native boolean $kotlin_remove(E e);

  @Override
  public native boolean removeAll(Collection<?> c);

  @Override
  public native boolean $kotlin_removeAll($Kotlin_Collection<? extends E> c);

  @Override
  public native boolean retainAll(Collection<?> c);

  @Override
  public native boolean $kotlin_retainAll($Kotlin_Collection<? extends E> c);

  @Override
  public native @Nullable Object[] toArray();

  @Override
  public native <T extends @Nullable Object> T[] toArray(T[] a);
}
