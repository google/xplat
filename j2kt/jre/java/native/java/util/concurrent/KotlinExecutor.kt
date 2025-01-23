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

@file:OptIn(ExperimentalObjCName::class)

package java.util.concurrent

import java.lang.InterruptedException
import java.lang.Runnable
import kotlin.collections.MutableCollection
import kotlin.collections.MutableList
import kotlin.concurrent.AtomicInt
import kotlin.concurrent.AtomicReference
import kotlin.experimental.ExperimentalObjCName
import kotlin.jvm.Throws
import kotlin.native.ObjCName
import kotlin.time.ComparableTimeMark
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.yield

/**
 * A [ScheduledExecutorService] that executes tasks on a [CoroutineDispatcher].
 *
 * @param dispatcher the dispatcher to execute tasks on.
 */
@ObjCName("J2ktKotlinExecutor", exact = true)
open class KotlinExecutor(
  private val dispatcher: CoroutineDispatcher,
  private val timeSource: TimeSource.WithComparableMarks = TimeSource.Monotonic,
) : ScheduledExecutorService {

  private var isShutdown = AtomicReference(false)
  private val supervisorJob = SupervisorJob()
  private val scope: CoroutineScope = CoroutineScope(dispatcher + supervisorJob)

  override fun execute(command: Runnable) {
    checkNotShutdown()
    scope.launch { command.run() }
  }

  override fun <T> invokeAll(tasks: MutableCollection<out Callable<T>>): MutableList<Future<T>> {
    checkNotShutdown()
    return invokeAllInternal(tasks, 0, TimeUnit.MILLISECONDS)
  }

  override fun <T> invokeAll(
    tasks: MutableCollection<out Callable<T>>,
    timeout: Long,
    unit: TimeUnit,
  ): MutableList<Future<T>> {
    if (timeout <= 0) {
      // Immediate timeout, return all futures canceled.
      return tasks.map { ImmediateFuture.immediateCanceledFuture<T>() }.toMutableList<Future<T>>()
    }
    return invokeAllInternal(tasks, timeout, unit)
  }

  /**
   * Invokes all tasks in parallel (based on the dispatcher) and returns a list of completed
   * futures.
   *
   * If the timeout is reached before all tasks complete, the remaining tasks are canceled. The Java
   * API requires that the list of tasks is mutable, but the behavior is undefined if the list is
   * modified during the execution of this method. In this case, removing all tasks from the list
   * may result in the invocation never completing.
   *
   * @param tasks the tasks to invoke.
   * @param timeout the timeout for the tasks to complete. (pass 0 for no timeout)
   * @param unit the time unit of the timeout.
   * @return a list of futures representing the results of the tasks.
   */
  private fun <T> invokeAllInternal(
    tasks: MutableCollection<out Callable<T>>,
    timeout: Long,
    unit: TimeUnit,
  ): MutableList<Future<T>> {
    checkNotShutdown()
    if (tasks.isEmpty()) {
      return mutableListOf()
    }
    val taskCount = AtomicInt(tasks.size)
    val allCompleted = CompletableDeferred<Boolean>()
    val list =
      tasks.map {
        val task = scope.async { it.call() }
        task.invokeOnCompletion({
          if (taskCount.decrementAndGet() == 0) allCompleted.complete(true)
        })
        task
      }
    return runBlocking {
      if (timeout > 0) {
        try {
          withTimeout(unit.toMillis(timeout)) { allCompleted.await() }
        } catch (e: TimeoutCancellationException) {
          // Cancel all tasks, this has no effect if the task is already completed.
          list.forEach { it.cancel() }
        }
      } else {
        allCompleted.await()
      }
      // Return all completed tasks.
      list.map { KotlinFuture(it) }.toMutableList()
    }
  }

  @Throws(ExecutionException::class)
  override fun <T> invokeAny(tasks: MutableCollection<out Callable<T>>): T {
    return invokeAnyInternal(tasks, 0, TimeUnit.MILLISECONDS)
  }

  @Throws(ExecutionException::class, TimeoutException::class)
  override fun <T> invokeAny(
    tasks: MutableCollection<out Callable<T>>,
    timeout: Long,
    unit: TimeUnit,
  ): T {
    if (timeout <= 0) {
      throw TimeoutException("No task succeeded within timeout")
    }
    return invokeAnyInternal(tasks, timeout, unit)
  }

  /**
   * Invokes all tasks in parallel (based on the dispatcher) and returns the result of the first
   * completed task, canceling the remaining tasks.
   *
   * If the timeout is reached before any task completes, a [TimeoutException] is thrown. If all
   * tasks fail, an [ExecutionException] is thrown. The Java API requires that the list of tasks is
   * mutable, but the behavior is undefined if the list is modified during the execution of this
   * method. In this case, removing all tasks from the list may result in the invocation never
   * completing.
   *
   * @param tasks the tasks to invoke.
   * @param timeout the timeout for the tasks to complete. (pass 0 for no timeout)
   * @param unit the time unit of the timeout.
   * @return the result of the first completed task.
   * @throws TimeoutException if no task succeeds within the timeout.
   * @throws ExecutionException if all tasks fail.
   * @throws IllegalArgumentException if the list of tasks is empty.
   */
  private fun <T> invokeAnyInternal(
    tasks: MutableCollection<out Callable<T>>,
    timeout: Long,
    unit: TimeUnit,
  ): T {
    if (tasks.isEmpty()) {
      throw IllegalArgumentException("Must provide at least one task")
    }
    val taskCount = AtomicInt(tasks.size)
    val firstCompleted = CompletableDeferred<T>()
    val list =
      tasks.map {
        scope.async {
          try {
            firstCompleted.complete(it.call())
          } catch (e: Exception) {
            if (taskCount.decrementAndGet() == 0) {
              firstCompleted.completeExceptionally(ExecutionException(e))
            }
          }
        }
      }
    return runBlocking {
      if (timeout > 0) {
        try {
          withTimeout(unit.toMillis(timeout)) {
            val result = firstCompleted.await()
            list.forEach { it.cancel() }
            result
          }
        } catch (e: TimeoutCancellationException) {
          list.forEach { it.cancel() }
          throw TimeoutException("No task succeeded within timeout")
        }
      } else {
        yield()
        val result = firstCompleted.await()
        list.forEach { it.cancel() }
        result
      }
    }
  }

  override fun isShutdown(): Boolean {
    return isShutdown.value
  }

  @ObjCName("awaitTermination")
  @Throws(InterruptedException::class)
  fun awaitTermination(
    @ObjCName("withLong") timeout: Long,
    @ObjCName("withJavaUtilConcurrentTimeUnit") unit: TimeUnit,
  ): Boolean {
    return runBlocking {
      if (timeout <= 0) {
        supervisorJob.join()
        true
      } else {
        withTimeoutOrNull(unit.toMillis(timeout)) {
          supervisorJob.join()
          true
        } ?: false
      }
    }
  }

  override fun isTerminated(): Boolean {
    return isShutdown.value && !supervisorJob.children.any()
  }

  override fun shutdown() {
    if (!isShutdown.compareAndSet(false, true)) {
      // Already shutdown.
      return
    }
    // Schedule a task on the same dispatcher (but not on the same supervisor) to shutdown the
    // supervisor once all children are done.
    CoroutineScope(dispatcher).launch {
      // Safe because no additional children can be added at this point.
      supervisorJob.children.forEach {
        try {
          it.join()
        } catch (e: Exception) {
          // ignoring any/all exceptions
        }
      }
      supervisorJob.cancel()
    }
  }

  override fun shutdownNow(): MutableList<Runnable> {
    shutdown()
    supervisorJob.cancel()
    // Currently this attempts to cancel all tasks.
    return mutableListOf()
  }

  override fun submit(task: Runnable): Future<*> {
    checkNotShutdown()
    return KotlinFuture<Unit>(scope.async { task.run() })
  }

  override fun <T> submit(task: Runnable, result: T): Future<T> {
    checkNotShutdown()
    return KotlinFuture<T>(
      scope.async {
        task.run()
        result
      }
    )
  }

  override fun <T> submit(task: Callable<T>): Future<T> {
    checkNotShutdown()
    return KotlinFuture<T>(scope.async { task.call() })
  }

  override fun schedule(command: Runnable, delay: Long, unit: TimeUnit): ScheduledFuture<*> {
    return scheduleInternal(delay, 0, unit) { command.run() }
  }

  override fun <T> schedule(
    callable: Callable<T>,
    delay: Long,
    unit: TimeUnit,
  ): ScheduledFuture<T> {
    return scheduleInternal(delay, 0, unit) { callable.call() }
  }

  override fun scheduleAtFixedRate(
    command: Runnable,
    initialDelay: Long,
    period: Long,
    unit: TimeUnit,
  ): ScheduledFuture<*> {
    if (period <= 0) {
      throw IllegalArgumentException("Period must be >= 0")
    }
    return scheduleInternal(initialDelay, period, unit) { launch { command.run() } }
  }

  override fun scheduleWithFixedDelay(
    command: Runnable,
    initialDelay: Long,
    delay: Long,
    unit: TimeUnit,
  ): ScheduledFuture<*> {
    if (delay <= 0) {
      throw IllegalArgumentException("Delay must be >= 0")
    }
    return scheduleInternal(initialDelay, delay, unit) { command.run() }
  }

  /**
   * Schedules a task to run on the executor either once or periodically. Note that the block is
   * executed in a coroutine, so if you add a delay in the block, it will delay the coroutine and
   * induce lag before the next execution. This can be prevented by launching a new coroutine in the
   * block.
   *
   * @param initialDelay the initial delay before the first execution of the task.
   * @param period the period between executions of the task (if 0, the task will only run once)
   * @param unit the time unit of the delay and period.
   * @param block the task to execute.
   * @return a [ScheduledFuture] representing pending completion of the task.
   */
  private fun <T> scheduleInternal(
    initialDelay: Long,
    period: Long,
    unit: TimeUnit,
    block: suspend CoroutineScope.() -> T,
  ): ScheduledFuture<T> {
    checkNotShutdown()
    val scheduledExecutionTime: AtomicReference<ComparableTimeMark> =
      AtomicReference(timeSource.markNow() + unit.toMillis(initialDelay).milliseconds)
    val task =
      scope.async {
        if (initialDelay > 0L) {
          delay(unit.toMillis(initialDelay))
        }
        var result: T? = null
        while (true) {
          checkNotShutdown()
          result = block()
          if (period == 0L) {
            // If period is 0, we should only run once.
            break
          }
          scheduledExecutionTime.compareAndSet(
            scheduledExecutionTime.value,
            timeSource.markNow() + unit.toMillis(period).milliseconds,
          )
          delay(unit.toMillis(period))
        }
        return@async result as T
      }
    return KotlinDelayedFuture<T>(task, scheduledExecutionTime)
  }

  /**
   * Checks that the executor can still execute tasks.
   *
   * @throws RejectedExecutionException if the executor is shutdown.
   */
  private fun checkNotShutdown() {
    if (isShutdown.value) {
      throw RejectedExecutionException("Executor is shutdown")
    }
  }

  companion object Factory {
    internal fun createSingleThreadScheduledExecutor(): ScheduledExecutorService =
      KotlinExecutor(Dispatchers.Default.limitedParallelism(1))

    internal fun createScheduledExecutor(threads: Int): ScheduledExecutorService =
      KotlinExecutor(Dispatchers.Default.limitedParallelism(threads))

    fun createMainThreadExecutor(): ScheduledExecutorService = KotlinExecutor(Dispatchers.Main)
  }
}
