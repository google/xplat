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
import javaemul.internal.annotations.KtProperty;
import jsinterop.annotations.JsNonNull;
import org.jspecify.nullness.Nullable;

/**
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html">the official
 * Java API doc</a> for details.
 */
@KtNative(
    value = "kotlin.collections.MutableCollection",
    bridgeWith = "javaemul.lang.JavaCollection")
public interface Collection<E> extends Iterable<E> {

  boolean add(E e);

  @KtName("java_addAll")
  @KtPropagateNullability
  boolean addAll(@JsNonNull Collection<? extends E> c);

  void clear();

  @KtPropagateNullability
  @KtName("java_contains")
  boolean contains(@Nullable Object o);

  @KtName("java_containsAll")
  @KtPropagateNullability
  boolean containsAll(@JsNonNull Collection<? extends @Nullable Object> c);

  boolean isEmpty();

  @Override
  @KtPropagateNullability
  @JsNonNull
  Iterator<E> iterator();

  @KtPropagateNullability
  @KtName("java_remove")
  boolean remove(@Nullable Object o);

  @KtName("java_removeAll")
  @KtPropagateNullability
  boolean removeAll(@JsNonNull Collection<? extends @Nullable Object> c);

  @KtName("java_retainAll")
  @KtPropagateNullability
  boolean retainAll(@JsNonNull Collection<? extends @Nullable Object> c);

  @KtProperty
  int size();

  @KtName("java_toArray")
  @KtPropagateNullability
  default Object @JsNonNull [] toArray() {
    return null;
  }

  @KtName("java_toArray")
  @KtPropagateNullability
  default <T extends @Nullable Object> T @JsNonNull [] toArray(T[] a) {
    return null;
  }
}
