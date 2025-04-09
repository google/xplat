/*
 * Copyright 2025 Google Inc.
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

package java.util.concurrent.atomic;

import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;
import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;

@KtNative
@NullMarked
public final class AtomicIntegerArray {
  public AtomicIntegerArray(int[] array) {}

  public AtomicIntegerArray(int length) {}

  public native int get(int i);

  public native int getAndAccumulate(int i, int x, IntBinaryOperator accumulatorFunction);

  public native int getAndAdd(int i, int delta);

  public native int getAndIncrement(int i);

  public native int getAndDecrement(int i);

  public native int getAndSet(int i, int newValue);

  public native int getAndUpdate(int i, IntUnaryOperator updateFunction);

  public native int accumulateAndGet(int i, int x, IntBinaryOperator accumulatorFunction);

  public native int addAndGet(int i, int delta);

  public native int incrementAndGet(int i);

  public native int decrementAndGet(int i);

  public native int updateAndGet(int i, IntUnaryOperator updateFunction);

  public native boolean compareAndSet(int i, int expect, int update);

  public native boolean weakCompareAndSet(int i, int expect, int update);

  public native void set(int i, int newValue);

  public native void lazySet(int i, int newValue);

  public native int length();

  @Override
  public native String toString();
}
