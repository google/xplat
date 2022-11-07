// CHECKSTYLE_OFF: Copyrighted to Guava Authors.
/*
 * Copyright (C) 2009 The Guava Authors
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

import javaemul.internal.annotations.KtName;
import javaemul.internal.annotations.KtNative;

/** Kotli version of {@link AtomicInteger} based on kotlinx_atomicfu. */
@KtNative("java.util.concurrent.atomic.AtomicInteger")
public class AtomicInteger extends Number implements java.io.Serializable {

  public AtomicInteger(int initialValue) {}

  public AtomicInteger() {}

  public native int get();

  public native void set(int newValue);

  public native void lazySet(int newValue);

  public native int getAndSet(int newValue);

  public native boolean compareAndSet(int expect, int update);

  public native int getAndIncrement();

  public native int getAndDecrement();

  public native int getAndAdd(int delta);

  public native int incrementAndGet();

  public native int decrementAndGet();

  public native int addAndGet(int delta);

  @Override
  public native String toString();

  @Override
  @KtName("toInt")
  public native int intValue();

  @Override
  @KtName("toLong")
  public native long longValue();

  @Override
  @KtName("toFloat")
  public native float floatValue();

  @Override
  @KtName("toDouble")
  public native double doubleValue();
}
