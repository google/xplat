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

import javaemul.internal.annotations.KtNative;
import javaemul.internal.annotations.KtOut;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Set.html">the official Java API
 * doc</a> for details.
 */
@KtNative(
    name = "kotlin.collections.Set",
    mutableName = "kotlin.collections.MutableSet",
    bridgeName = "javaemul.lang.MutableSetJvm",
    companionName = "java.util.Set")
@NullMarked
public interface Set<@KtOut E extends @Nullable Object> extends Collection<E> {
  static <E> Set<E> copyOf(Collection<? extends E> coll) {
    return ktNative();
  }

  static <E extends @Nullable Object> Set<E> of() {
    return ktNative();
  }

  static <E> Set<E> of(E e1) {
    return ktNative();
  }

  static <E> Set<E> of(E e1, E e2) {
    return ktNative();
  }

  static <E> Set<E> of(E e1, E e2, E e3) {
    return ktNative();
  }

  static <E> Set<E> of(E e1, E e2, E e3, E e4) {
    return ktNative();
  }

  static <E> Set<E> of(E e1, E e2, E e3, E e4, E e5) {
    return ktNative();
  }

  static <E> Set<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
    return ktNative();
  }

  static <E> Set<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
    return ktNative();
  }

  static <E> Set<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
    return ktNative();
  }

  static <E> Set<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
    return ktNative();
  }

  static <E> Set<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
    return ktNative();
  }

  @SafeVarargs
  static <E> Set<E> of(E... elements) {
    return ktNative();
  }

  @Override
  default Spliterator<E> spliterator() {
    return ktNative();
  }

  default MutableSet<E> asMutableSet() {
    return ktNative();
  }
}
