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

import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;
import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;

@KtNative
@NullMarked
public final class AtomicLongArray {
  public AtomicLongArray(long[] array) {}

  public AtomicLongArray(int length) {}

  public native long get(int i);

  public native long getAndAccumulate(int i, long x, LongBinaryOperator accumulatorFunction);

  public native long getAndAdd(int i, long delta);

  public native long getAndIncrement(int i);

  public native long getAndDecrement(int i);

  public native long getAndSet(int i, long newValue);

  public native long getAndUpdate(int i, LongUnaryOperator updateFunction);

  public native long accumulateAndGet(int i, long x, LongBinaryOperator accumulatorFunction);

  public native long addAndGet(int i, long delta);

  public native long incrementAndGet(int i);

  public native long decrementAndGet(int i);

  public native long updateAndGet(int i, LongUnaryOperator updateFunction);

  public native boolean compareAndSet(int i, long expect, long update);

  public native boolean weakCompareAndSet(int i, long expect, long update);

  public native void set(int i, long newValue);

  public native void lazySet(int i, long newValue);

  public native int length();

  @Override
  public native String toString();
}
