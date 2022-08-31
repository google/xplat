/*
 * Copyright 2016 Google Inc.
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

import javaemul.internal.annotations.KtName;
import javaemul.internal.annotations.KtNative;
import jsinterop.annotations.JsNonNull;
import org.jspecify.nullness.Nullable;

/**
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/ArrayDeque.html">the official
 * Java API doc</a> for details.
 */
@KtNative("kotlin.collections.ArrayDeque")
public class ArrayDeque<E extends @Nullable Object> extends AbstractList<E> {

  public ArrayDeque() {}

  public ArrayDeque(int numElements) {}

  public ArrayDeque(Collection<? extends E> c) {}

  @Override
  public native boolean add(E e);

  @Override
  public native void add(int index, E element);

  public native void addFirst(E e);

  public native void addLast(E e);

  @Override
  public native void clear();

  @Override
  public native boolean contains(@Nullable Object o);

  @KtName("first")
  public native E element();

  @Override
  public native E get(int index);

  @KtName("first")
  public native E getFirst();

  @KtName("last")
  public native E getLast();

  @Override
  public native boolean isEmpty();

  @Override
  public native @JsNonNull Iterator<E> iterator();

  @KtName("firstOrNull")
  public native @Nullable E peek();

  @KtName("firstOrNull")
  public native @Nullable E peekFirst();

  @KtName("lastOrNull")
  public native @Nullable E peekLast();

  @KtName("removeFirstOrNull")
  public native @Nullable E poll();

  @KtName("removeFirstOrNull")
  public native @Nullable E pollFirst();

  @KtName("removeLastOrNull")
  public native @Nullable E pollLast();

  @KtName("removeFirst")
  public native @Nullable E pop();

  @KtName("addFirst")
  public native void push(E e);

  @KtName("removeFirst")
  public native E remove();

  @Override
  @KtName("removeAt")
  public native E remove(int index);

  @Override
  public native boolean remove(@Nullable Object o);

  public native E removeFirst();

  public native E removeLast();

  @Override
  public native E set(int index, E element);

  @Override
  public native int size();
}
