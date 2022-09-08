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

import javaemul.internal.annotations.KtName;
import javaemul.internal.annotations.KtNative;
import javaemul.internal.annotations.KtPropagateNullability;
import jsinterop.annotations.JsNonNull;
import org.jspecify.nullness.Nullable;

/**
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/List.html">the official Java API
 * doc</a> for details.
 */
@KtNative(value = "kotlin.collections.MutableList", bridgeWith = "javaemul.lang.JavaList")
public interface List<E extends @Nullable Object> extends Collection<E> {

  void add(int index, E element);

  @KtName("java_addAll")
  @KtPropagateNullability
  boolean addAll(int index, @JsNonNull Collection<? extends E> c);

  E get(int index);

  @KtPropagateNullability
  @KtName("java_indexOf")
  int indexOf(@Nullable Object o);

  @KtPropagateNullability
  @KtName("java_lastIndexOf")
  int lastIndexOf(@Nullable Object o);

  @KtPropagateNullability
  @JsNonNull
  ListIterator<E> listIterator();

  @KtPropagateNullability
  @JsNonNull
  ListIterator<E> listIterator(int from);

  @KtName("removeAt")
  E remove(int index);

  E set(int index, E element);

  default void sort(@Nullable Comparator<? super E> c) {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }

  @KtPropagateNullability
  @JsNonNull
  List<E> subList(int fromIndex, int toIndex);
}
