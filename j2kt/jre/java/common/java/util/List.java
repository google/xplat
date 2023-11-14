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

import static javaemul.internal.KtNativeUtils.ktNative;

import java.util.function.UnaryOperator;
import javaemul.internal.annotations.KtName;
import javaemul.internal.annotations.KtNative;
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

/**
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/List.html">the official Java API
 * doc</a> for details.
 */
@KtNative(name = "kotlin.collections.MutableList", bridgeName = "javaemul.lang.JavaList")
@NullMarked
public interface List<E extends @Nullable Object> extends Collection<E> {

  void add(int index, E element);

  @KtName("java_addAll")
  boolean addAll(int index, Collection<? extends E> c);

  @Override
  default boolean $kotlin_addAll($Kotlin_Collection<? extends E> c) {
    return ktNative();
  }

  @Override
  default boolean $kotlin_contains(E e) {
    return ktNative();
  }

  @Override
  default boolean $kotlin_containsAll($Kotlin_Collection<? extends E> c) {
    return ktNative();
  }

  E get(int index);

  @KtName("java_indexOf")
  int indexOf(@Nullable Object o);

  @KtName("java_lastIndexOf")
  int lastIndexOf(@Nullable Object o);

  ListIterator<E> listIterator();

  ListIterator<E> listIterator(int from);

  @Override
  default boolean $kotlin_remove(E e) {
    return ktNative();
  }

  @Override
  default boolean $kotlin_removeAll($Kotlin_Collection<? extends E> c) {
    return ktNative();
  }

  @KtName("removeAt")
  E remove(int index);

  @Override
  default boolean $kotlin_retainAll($Kotlin_Collection<? extends E> c) {
    return ktNative();
  }

  default void replaceAll(UnaryOperator<E> operator) {
    ktNative();
  }

  E set(int index, E element);

  default void sort(@Nullable Comparator<? super E> c) {
    ktNative();
  }

  List<E> subList(int fromIndex, int toIndex);
}
