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
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/List.html">the official Java API
 * doc</a> for details.
 */
@KtNative(name = "kotlin.collections.MutableList", bridgeName = "javaemul.lang.MutableListJvm")
@NullMarked
public interface List<E extends @Nullable Object> extends Collection<E> {

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

  List<E> subList(int fromIndex, int toIndex);

  // Override equals() to teach Error Prone that an ArrayList can be equal to a LinkedList.
  @Override
  boolean equals(@Nullable Object o);
}
