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
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

/**
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Set.html">the official Java API
 * doc</a> for details.
 */
@KtNative(value = "kotlin.collections.MutableSet", bridgeWith = "javaemul.lang.JavaSet")
@NullMarked
public interface Set<E extends @Nullable Object> extends Collection<E> {
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

  @Override
  default boolean $kotlin_remove(E e) {
    return ktNative();
  }

  @Override
  default boolean $kotlin_removeAll($Kotlin_Collection<? extends E> c) {
    return ktNative();
  }

  @Override
  default boolean $kotlin_retainAll($Kotlin_Collection<? extends E> c) {
    return ktNative();
  }

  @Override
  default Spliterator<E> spliterator() {
    return ktNative();
  }
}
