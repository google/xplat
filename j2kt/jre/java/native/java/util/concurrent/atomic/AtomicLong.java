// CHECKSTYLE_OFF: Copyrighted to Guava Authors.
/*
 * Copyright (C) 2011 The Guava Authors
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
import org.jspecify.nullness.NullMarked;

/** JRE AtomicLong stub; see AtomicLong.kt for the actual wrapper. */
@KtNative(name = "java.util.concurrent.atomic.AtomicLong")
@NullMarked
public class AtomicLong extends Number implements java.io.Serializable {

  public AtomicLong(long initialValue) {}

  public AtomicLong() {}

  public native long get();

  public native void set(long newValue);

  public native void lazySet(long newValue);

  public native long getAndSet(long newValue);

  public native boolean compareAndSet(long expect, long update);

  public native long getAndIncrement();

  public native long getAndDecrement();

  public native long getAndAdd(long delta);

  public native long incrementAndGet();

  public native long decrementAndGet();

  public native long addAndGet(long delta);

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
