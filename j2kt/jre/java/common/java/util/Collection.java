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

import java.util.function.Predicate;
import java.util.stream.Stream;
import javaemul.internal.annotations.KtName;
import javaemul.internal.annotations.KtNative;
import javaemul.internal.annotations.KtProperty;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html">the official
 * Java API doc</a> for details.
 */
@KtNative(
    name = "kotlin.collections.MutableCollection",
    bridgeName = "javaemul.lang.JavaCollection")
@NullMarked
public interface Collection<E extends @Nullable Object> extends Iterable<E>, ReadonlyCollection<E> {

  boolean add(E e);

  boolean addAll(Collection<? extends E> c);

  void clear();

  boolean contains(@Nullable Object o);

  boolean containsAll(Collection<?> c);

  boolean isEmpty();

  @Override
  Iterator<E> iterator();

  default Stream<E> parallelStream() {
    return ktNative();
  }

  boolean remove(@Nullable Object o);

  boolean removeAll(Collection<?> c);

  default boolean removeIf(Predicate<? super E> filter) {
    return ktNative();
  }

  boolean retainAll(Collection<?> c);

  @KtProperty
  int size();

  @Override
  default Spliterator<E> spliterator() {
    return ktNative();
  }

  default Stream<E> stream() {
    return ktNative();
  }

  @KtName("java_toArray")
  @Nullable Object[] toArray();

  // Note: If array `a` is bigger than `this` collection, `a[this.size()]` will be set to `null`
  // even though `T` is not necessarily nullable. This is to remain consistent with JSpecify.
  @KtName("java_toArray")
  <T extends @Nullable Object> T[] toArray(T[] a);
}
