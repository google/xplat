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

import java.util.function.Predicate;
import java.util.stream.Stream;
import javaemul.internal.annotations.KtName;
import javaemul.internal.annotations.KtNative;
import javaemul.internal.annotations.KtProperty;
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

/**
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html">the official
 * Java API doc</a> for details.
 */
@KtNative(
    value = "kotlin.collections.MutableCollection",
    bridgeWith = "javaemul.lang.JavaCollection")
@NullMarked
public interface Collection<E extends @Nullable Object> extends Iterable<E> {

  boolean add(E e);

  @KtName("java_addAll")
  boolean addAll(Collection<? extends E> c);

  /** Only for bookkeeping in the transpiler. Do not call */
  @KtName("addAll")
  default boolean $kotlin_addAll(KotlinCollection<? extends E> c) {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }

  void clear();

  @KtName("java_contains")
  boolean contains(@Nullable Object o);

  /** Only for bookkeeping in the transpiler. Do not call */
  @KtName("contains")
  default boolean $kotlin_contains(E e) {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }

  @KtName("java_containsAll")
  boolean containsAll(Collection<?> c);

  /** Only for bookkeeping in the transpiler. Do not call */
  @KtName("containsAll")
  default boolean $kotlin_containsAll(KotlinCollection<? extends E> c) {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }

  boolean isEmpty();

  @Override
  Iterator<E> iterator();

  default Stream<E> parallelStream() {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }

  @KtName("java_remove")
  boolean remove(@Nullable Object o);

  /** Only for bookkeeping in the transpiler. Do not call */
  @KtName("remove")
  default boolean $kotlin_remove(E e) {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }

  @KtName("java_removeAll")
  boolean removeAll(Collection<?> c);

  @KtName("removeAll")
  default boolean $kotlin_removeAll(KotlinCollection<? extends E> c) {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }

  @KtName("java_removeIf")
  default boolean removeIf(Predicate<? super E> filter) {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }

  @KtName("java_retainAll")
  boolean retainAll(Collection<?> c);

  @KtName("retainAll")
  default boolean $kotlin_retainAll(KotlinCollection<? extends E> c) {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }

  @KtProperty
  int size();

  @Override
  default Spliterator<E> spliterator() {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }

  default Stream<E> stream() {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }

  @KtName("java_toArray")
  default @Nullable Object[] toArray() {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }

  // Note: If array `a` is bigger than `this` collection, `a[this.size()]` will be set to `null`
  // even though `T` is not necessarily nullable. This is to remain consistent with JSpecify.
  @KtName("java_toArray")
  default <T extends @Nullable Object> T[] toArray(T[] a) {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }
}
