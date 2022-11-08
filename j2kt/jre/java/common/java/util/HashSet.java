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
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/HashSet.html">the official Java
 * API doc</a> for details.
 */
@KtNative("kotlin.collections.HashSet")
@NullMarked
public class HashSet<E extends @Nullable Object> implements Set<E> {

  public HashSet() {}

  public HashSet(Collection<? extends E> c) {}

  public HashSet(int initialCapacity) {}

  public HashSet(int initialCapacity, float loadFactor) {}

  @Override
  public native boolean add(E e);

  @Override
  public native boolean addAll(Collection<? extends E> c);

  @Override
  public native boolean containsAll(Collection<? extends @Nullable Object> c);

  @Override
  public native boolean retainAll(Collection<? extends @Nullable Object> c);

  @Override
  public native boolean removeAll(Collection<? extends @Nullable Object> c);

  @Override
  public native void clear();

  @Override
  public native boolean contains(@Nullable Object o);

  @Override
  public native boolean isEmpty();

  @Override
  public native Iterator<E> iterator();

  @Override
  public native boolean remove(@Nullable Object o);

  @Override
  public native int size();
}
