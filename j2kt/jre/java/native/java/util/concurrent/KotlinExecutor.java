/*
 * Copyright 2024 Google Inc.
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

import java.util.Collection;
import java.util.List;
import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;

/**
 * Simple Java wrapper for the Kotlin implementation of the same name. See the Kotlin implementation
 * for more details.
 */
@NullMarked
@KtNative
public class KotlinExecutor implements ScheduledExecutorService {

  static native ScheduledExecutorService createSingleThreadScheduledExecutor();

  // TODO: b/380306429 - Restrict visibility to only xplat-owned code.
  public static native ScheduledExecutorService createMainThreadExecutor();

  static native ScheduledExecutorService createScheduledExecutor(int threads);

  @Override
  public native boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;

  @Override
  public native ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit);

  @Override
  public native <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit);

  @Override
  public native ScheduledFuture<?> scheduleAtFixedRate(
      Runnable command, long initialDelay, long period, TimeUnit unit);

  @Override
  public native ScheduledFuture<?> scheduleWithFixedDelay(
      Runnable command, long initialDelay, long delay, TimeUnit unit);

  @Override
  public native void shutdown();

  @Override
  public native List<Runnable> shutdownNow();

  @Override
  public native boolean isShutdown();

  @Override
  public native boolean isTerminated();

  @Override
  public native <T> Future<T> submit(Callable<T> task);

  @Override
  public native <T> Future<T> submit(Runnable task, T result);

  @Override
  public native Future<?> submit(Runnable task);

  @Override
  public native <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks);

  @Override
  public native <T> List<Future<T>> invokeAll(
      Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
      throws InterruptedException;

  @Override
  public native <T> T invokeAny(Collection<? extends Callable<T>> tasks)
      throws InterruptedException, ExecutionException;

  @Override
  public native <T> T invokeAny(
      Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException;

  @Override
  public native void execute(Runnable command);
}
