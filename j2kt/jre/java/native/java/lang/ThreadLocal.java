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
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

/**
 * Provides an implementation of {@link java.lang.ThreadLocal} for GWT.
 *
 * @param <T> value type.
 */
@NullMarked
public class ThreadLocal<T extends @Nullable Object> {

  private @Nullable T value;

  public ThreadLocal() {
    value = initialValue();
  }

  public @Nullable T get() {
    return value;
  }

  public void set(T value) {
    this.value = value;
  }

  public void remove() {
    value = null;
  }

  protected @Nullable T initialValue() {
    return null;
  }

  public static <S extends @Nullable Object> ThreadLocal<S> withInitial(
      Supplier<? extends S> supplier) {
    ThreadLocal<S> threadLocal = new ThreadLocal<>();
    threadLocal.set(supplier.get());
    return threadLocal;
  }
}
