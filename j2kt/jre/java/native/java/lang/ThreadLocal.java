// CHECKSTYLE_OFF: Copyrighted to Guava Authors.
/*
 * Copyright (C) 2017 The Guava Authors
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

package java.lang;

import java.util.function.Supplier;
import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Provides an implementation of {@link java.lang.ThreadLocal}. Please make sure to use remove() if
 * the thread is expected to out-live the intended scope of a thread local to avoid leaking the
 * corresponding memory until the thread ends.
 *
 * @param <T> value type.
 */
@NullMarked
@KtNative
public class ThreadLocal<T extends @Nullable Object> {

  public ThreadLocal() {
  }

  public native @Nullable T get();

  public native void set(T value);

  public native void remove();

  protected native @Nullable T initialValue();

  public static native <S extends @Nullable Object> ThreadLocal<S> withInitial(
      Supplier<? extends S> supplier);
}
