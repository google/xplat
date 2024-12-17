/*
 * Copyright 2019 Google Inc.
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

import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * GWT emulation of AtomicReference.
 *
 * @param <V> The type of object referred to by this reference
 */
@KtNative
@NullMarked
public class AtomicReference<V extends @Nullable Object> {

  public AtomicReference() {}

  public AtomicReference(V initialValue) {}

  public final native boolean compareAndSet(V expect, V update);

  public final native V get();

  public final native V getAndSet(V newValue);

  public final native void lazySet(V newValue);

  public final native void set(V newValue);

  public final native boolean weakCompareAndSet(V expect, V update);

  @Override
  public native String toString();
}
