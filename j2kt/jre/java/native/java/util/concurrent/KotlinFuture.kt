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

import kotlin.concurrent.AtomicReference
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

/** A [Future] that wraps a [Deferred]. */
internal open class KotlinFuture<T>(private val deferred: Deferred<T>) : Future<T> {

  private var didCompleteExceptionally = AtomicReference(false)

  init {
    deferred.invokeOnCompletion {
      // This operation is called synchronously upon task completion and cancellation, therefore
      // it will be called before the Job is in the CANCELLED state (while in the CANCELLING state).
      // If the task is canceled, the exception will be a CancellationException.
      // If the executor is shutdown, the exception will be a RejectedExecutionException.
      // If the task is completed with an exception, the exception will be that exception.
      didCompleteExceptionally.value =
        it is Exception && !(it is RejectedExecutionException || it is CancellationException)
    }
  }

  override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
    if (deferred.isCancelled || deferred.isCompleted || !deferred.isActive) {
      return false
    }
    if (!mayInterruptIfRunning && deferred.isActive) {
      return false
    }
    deferred.cancel()
    return true
  }

  override fun isCancelled(): Boolean {
    // Java Futures consider a future canceled if it is canceled and completed, but not if it is
    // completed with an exception.
    return deferred.isCancelled && !didCompleteExceptionally.value
  }

  override fun isDone(): Boolean {
    return deferred.isCompleted || didCompleteExceptionally.value
  }

  override fun get(): T {
    return runBlocking { deferred.await() }
  }

  override fun get(timeout: Long, unit: TimeUnit): T {
    return runBlocking { withTimeout(unit.toMillis(timeout)) { deferred.await() } }
  }
}
