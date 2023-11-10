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
import org.jspecify.nullness.NullMarked;

/** GWT emulation of AtomicBoolean. */
@KtNative(name = "java.util.concurrent.atomic.AtomicBoolean")
@NullMarked
public class AtomicBoolean implements java.io.Serializable {

  public AtomicBoolean(boolean initialValue) {}

  public AtomicBoolean() {}

  public final native boolean get();

  public final native boolean compareAndSet(boolean expect, boolean update);

  public native boolean weakCompareAndSet(boolean expect, boolean update);

  public final native void set(boolean newValue);

  public final native void lazySet(boolean newValue);

  public final native boolean getAndSet(boolean newValue);

  @Override
  public native String toString();
}
