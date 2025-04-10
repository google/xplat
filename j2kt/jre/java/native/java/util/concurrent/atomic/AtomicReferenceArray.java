// CHECKSTYLE_OFF: Copyrighted to Guava Authors.
/*
 * Copyright (C) 2015 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// CHECKSTYLE_ON

package java.util.concurrent.atomic;

import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * J2KT Native emulated version of AtomicReferenceArray.
 *
 * @param <V> the element type.
 */
@KtNative
@NullMarked
public class AtomicReferenceArray<V extends @Nullable Object> {

  public AtomicReferenceArray(V[] array) {}

  public AtomicReferenceArray(int length) {}

  public native boolean compareAndSet(int i, V expect, V update);

  public native V get(int i);

  public native V getAndSet(int i, V x);

  public native void lazySet(int i, V x);

  public native int length();

  public native void set(int i, V x);

  public native boolean weakCompareAndSet(int i, V expect, V update);

  @Override
  public native String toString();
}
