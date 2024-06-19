/*
 * Copyright 2023 Google Inc.
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
package java.util.concurrent;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * FutureTask stub provided for unsupported compile time dependencies. Throws
 * "UnsupportedOperationException" for all operations.
 */
@NullMarked
public class FutureTask<V extends @Nullable Object> implements RunnableFuture<V> {

  public FutureTask(Callable<V> callable) {
    throw new UnsupportedOperationException();
  }

  public FutureTask(Runnable runnable, V result) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isCancelled() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isDone() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    throw new UnsupportedOperationException();
  }

  @Override
  public V get() throws InterruptedException, ExecutionException {
    throw new UnsupportedOperationException();
  }

  @Override
  public V get(long timeout, TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException {
    throw new UnsupportedOperationException();
  }

  protected void done() {}

  protected void set(V v) {
    throw new UnsupportedOperationException();
  }

  protected void setException(Throwable t) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void run() {
    throw new UnsupportedOperationException();
  }

  protected boolean runAndReset() {
    throw new UnsupportedOperationException();
  }
}
