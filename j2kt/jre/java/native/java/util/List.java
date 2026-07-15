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
import javaemul.internal.annotations.KtOut;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/List.html">the official Java API
 * doc</a> for details.
 */
@KtNative(
    name = "kotlin.collections.List",
    mutableName = "kotlin.collections.MutableList",
    bridgeName = "javaemul.lang.MutableListJvm",
    companionName = "java.util.List")
@NullMarked
public interface List<@KtOut E extends @Nullable Object> extends Collection<E> {
  static <E> List<E> copyOf(Collection<? extends E> coll) {
    return ktNative();
  }

  static <E extends @Nullable Object> List<E> of() {
    return ktNative();
  }

  static <E> List<E> of(E e1) {
    return ktNative();
  }

  static <E> List<E> of(E e1, E e2) {
    return ktNative();
  }

  static <E> List<E> of(E e1, E e2, E e3) {
    return ktNative();
  }

  static <E> List<E> of(E e1, E e2, E e3, E e4) {
    return ktNative();
  }

  static <E> List<E> of(E e1, E e2, E e3, E e4, E e5) {
    return ktNative();
  }

  static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
    return ktNative();
  }

  static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
    return ktNative();
  }

  static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
    return ktNative();
  }

  static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
    return ktNative();
  }

  static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
    return ktNative();
  }

  @SafeVarargs
  static <E> List<E> of(E... elements) {
    return ktNative();
  }

  void add(int index, E element);

  boolean addAll(int index, Collection<? extends E> c);

  E get(int index);

  int indexOf(@Nullable Object o);

  int lastIndexOf(@Nullable Object o);

  ListIterator<E> listIterator();

  ListIterator<E> listIterator(int from);

  @KtName("removeAt")
  E remove(int index);

  default void replaceAll(UnaryOperator<E> operator) {
    ktNative();
  }

  E set(int index, E element);

  default void sort(@Nullable Comparator<? super E> c) {
    ktNative();
  }

  default E getFirst() {
    return ktNative();
  }

  default E getLast() {
    return ktNative();
  }

  default MutableList<E> asMutableList() {
    return ktNative();
  }

  List<E> subList(int fromIndex, int toIndex);

  // Override equals() to teach Error Prone that an ArrayList can be equal to a LinkedList.
  @Override
  boolean equals(@Nullable Object o);
}
