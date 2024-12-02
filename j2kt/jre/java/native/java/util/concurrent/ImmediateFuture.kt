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

package java.util.concurrent

/**
 * A lightweight [Future] in a particular terminal state.
 *
 * Increased visibility to allow better testing.
 */
abstract class ImmediateFuture<T> : Future<T> {

  override fun cancel(mayInterruptIfRunning: Boolean): Boolean = false

  override fun isCancelled(): Boolean = false

  override fun isDone(): Boolean = true

  override fun get(timeout: Long, unit: TimeUnit): T = get()

  private class ImmediateCanceledFuture<T>() : ImmediateFuture<T>() {
    override fun isCancelled(): Boolean = true

    override fun get(): T {
      throw CancellationException()
    }
  }

  private class ImmediateFailedFuture<T>(private val exception: Throwable) : ImmediateFuture<T>() {
    override fun get(): T {
      throw ExecutionException(exception)
    }
  }

  private class ImmediateCompletedFuture<T>(private val value: T) : ImmediateFuture<T>() {
    override fun get(): T = value
  }

  companion object {
    fun <T> immediateFuture(value: T): ImmediateFuture<T> = ImmediateCompletedFuture(value)

    fun <T> immediateFailedFuture(exception: Throwable): ImmediateFuture<T> =
      ImmediateFailedFuture(exception)

    fun <T> immediateCanceledFuture(): ImmediateFuture<T> = ImmediateCanceledFuture()
  }
}
