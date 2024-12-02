/*
 * Copyright 2024 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package smoke

import com.google.common.truth.Truth.Companion.assertThat
import com.google.common.truth.Truth.Companion.assertWithMessage
import java.util.concurrent.Callable
import java.util.concurrent.CancellationException
import java.util.concurrent.ExecutionException
import java.util.concurrent.KotlinExecutor
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.concurrent.AtomicInt
import kotlin.concurrent.AtomicReference
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.fail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

class KotlinExecutorTest {

  private val scheduler = TestCoroutineScheduler()
  private val executor = KotlinExecutor(StandardTestDispatcher(scheduler), scheduler.timeSource)
  private val realTimeExecutor = KotlinExecutor(Dispatchers.Default)

  @Test
  fun execute_runsAfterIdle() = runTest {
    val didRun: AtomicReference<Boolean> = AtomicReference(false)

    executor.execute { didRun.getAndSet(true) }
    scheduler.advanceUntilIdle()

    assertThat(didRun.value).isTrue()
  }

  @Test
  fun invokeAny_racesCallables() = runTest {
    val didRunThrowingThrow: AtomicReference<Boolean> = AtomicReference(false)
    val didRunFast: AtomicReference<Boolean> = AtomicReference(false)
    val didRunSlow: AtomicReference<Boolean> = AtomicReference(false)
    assertThat(
        realTimeExecutor.invokeAny(
          mutableListOf<Callable<Boolean>>(
            Callable {
              runBlocking { delay(1000) }
              didRunSlow.getAndSet(true)
              false
            },
            Callable {
              runBlocking { delay(10) }
              didRunFast.getAndSet(true)
              true
            },
            Callable {
              didRunThrowingThrow.getAndSet(true)
              throw RuntimeException("should not run")
              false
            },
          )
        )
      )
      .isTrue()
    assertThat(didRunThrowingThrow.value).isTrue()
    assertThat(didRunFast.value).isTrue()
    assertThat(didRunSlow.value).isFalse()
  }

  @Test
  fun invokeAny_withZeroTimeout_throws() = runTest {
    assertFailsWith<TimeoutException> {
      executor.invokeAny(
        mutableListOf<Callable<Boolean>>(
          Callable {
            runBlocking { delay(1000) }
            false
          }
        ),
        0,
        TimeUnit.SECONDS,
      )
    }
  }

  @Test
  fun invokeAny_afterTimeout_throws() = runTest {
    assertFailsWith<TimeoutException> {
      realTimeExecutor.invokeAny(
        mutableListOf<Callable<Boolean>>(
          Callable {
            runBlocking { delay(1000) }
            false
          }
        ),
        10,
        TimeUnit.MILLISECONDS,
      )
    }
  }

  @Test
  fun invokeAny_allThrow_throws() = runTest {
    val error =
      assertFailsWith<ExecutionException> {
        realTimeExecutor.invokeAny(
          mutableListOf<Callable<Boolean>>(
            Callable {
              runBlocking { delay(10) }
              throw RuntimeException("failure 1")
              false
            },
            Callable {
              runBlocking { delay(10) }
              throw RuntimeException("failure 2")
              false
            },
            Callable {
              runBlocking { delay(100) }
              throw RuntimeException("failure 3")
              false
            },
          )
        )
      }
    assertThat(error.cause?.message?.contains("failure 3")).isTrue()
  }

  @Test
  fun invokeAll_runsAll() {
    realTimeExecutor
      .invokeAll(
        mutableListOf<Callable<Boolean>>(
          Callable {
            runBlocking { delay(1000) }
            true
          },
          Callable {
            runBlocking { delay(10) }
            true
          },
          Callable { true },
        )
      )
      .forEach { assertThat(it.get()).isTrue() }
  }

  @Test
  fun invokeAll_withExceptions_returnsFailedFutures() {
    val didRun = AtomicReference(false)

    val future =
      realTimeExecutor
        .invokeAll(
          mutableListOf<Callable<Boolean>>(
            Callable {
              didRun.getAndSet(true)
              runBlocking { delay(10) }
              throw RuntimeException("should not run")
              true
            }
          )
        )
        .first()
    // The future failed exceptionally.
    assertWithMessage("Future is done").that(future.isDone()).isTrue()
    assertWithMessage("Future is cancelled").that(future.isCancelled()).isFalse()
    assertFailsWith<Exception>("Resolving the future") { future.get() }
    assertWithMessage("Task ran").that(didRun.value).isTrue()
  }

  @Test
  fun invokeAll_withSomeExceptions_returnsFailedAndSuccessfulFutures() {
    val didRun = AtomicReference(false)

    val futures =
      realTimeExecutor.invokeAll(
        mutableListOf<Callable<Boolean>>(
          Callable {
            runBlocking { delay(5) }
            throw RuntimeException("should not return")
            true
          },
          Callable {
            runBlocking { delay(10) }
            true
          },
        )
      )
    val failingFuture = futures.first()
    val successfulFuture = futures.last()

    // The failing future failed exceptionally.
    assertThat(failingFuture.isDone()).isTrue()
    assertThat(failingFuture.isCancelled()).isFalse()
    assertFailsWith<Exception> { failingFuture.get() }
    // The successful future succeeded.
    assertThat(successfulFuture.isDone()).isTrue()
    assertThat(successfulFuture.isCancelled()).isFalse()
    assertThat(successfulFuture.get()).isTrue()
  }

  @Test
  fun invokeAll_withZeroTimeout_returnsCancelled() {
    val didRun = AtomicReference(false)
    realTimeExecutor
      .invokeAll(
        mutableListOf<Callable<Boolean>>(
          Callable {
            runBlocking { delay(1000) }
            didRun.getAndSet(true)
            true
          },
          Callable {
            runBlocking { delay(10) }
            didRun.getAndSet(true)
            true
          },
          Callable {
            didRun.getAndSet(true)
            throw RuntimeException("should not run")
            true
          },
        ),
        0,
        TimeUnit.SECONDS,
      )
      .forEach {
        assertThat(it.isCancelled()).isTrue()
        assertThat(it.isDone()).isTrue()
        assertFailsWith<CancellationException> { it.get() }
      }
    assertThat(didRun.value).isFalse()
  }

  @Test
  fun awaitTermination_fullyShutDown_returnsTrue() = runTest {
    executor.shutdownNow()

    assertThat(executor.awaitTermination(1000L, TimeUnit.MILLISECONDS)).isTrue()
    assertThat(executor.awaitTermination(0, TimeUnit.MILLISECONDS)).isTrue()
  }

  @Test
  fun awaitTermination_notShutDown_timesOut() = runTest {
    assertThat(executor.awaitTermination(1000L, TimeUnit.MILLISECONDS)).isFalse()
  }

  @Test
  fun awaitTermination_softlyshutDown_timesOut() = runTest {
    val didRun: AtomicReference<Boolean> = AtomicReference(false)

    val future = executor.schedule({ fail("should not happen!") }, 2000L, TimeUnit.MILLISECONDS)
    executor.shutdown()

    assertThat(executor.awaitTermination(1000L, TimeUnit.MILLISECONDS)).isFalse()
    assertThat(executor.isTerminated()).isFalse()
  }

  @Test
  fun awaitTermination_softlyshutDown_returnsTrueOnlyAfterAllTasksAreCancelled() = runTest {
    val didScheduledTaskRun: AtomicReference<Boolean> = AtomicReference(false)
    val repeatedTaskRunCount: AtomicInt = AtomicInt(0)

    executor.schedule({ didScheduledTaskRun.getAndSet(true) }, 2000L, TimeUnit.MILLISECONDS)
    executor.scheduleAtFixedRate(
      { repeatedTaskRunCount.getAndIncrement() },
      500L,
      1000L,
      TimeUnit.MILLISECONDS,
    )
    scheduler.advanceTimeBy(1000L)
    scheduler.runCurrent()
    executor.shutdown()

    assertThat(executor.awaitTermination(1000L, TimeUnit.MILLISECONDS)).isFalse()
    // executor is still running.
    assertThat(didScheduledTaskRun.value).isFalse()
    assertThat(executor.isTerminated()).isFalse()
    assertThat(repeatedTaskRunCount.value).isEqualTo(1)
    // executor safely cancels tasks.
    scheduler.advanceTimeBy(1000L)
    scheduler.runCurrent()
    assertThat(didScheduledTaskRun.value).isFalse()
    assertThat(repeatedTaskRunCount.value).isEqualTo(1)
    assertThat(executor.awaitTermination(2000L, TimeUnit.MILLISECONDS)).isTrue()
  }

  @Test
  fun submit_runsAfterIdle() = runTest {
    val didRun: AtomicReference<Boolean> = AtomicReference(false)

    val future = executor.submit { didRun.getAndSet(true) }

    assertThat(future.isDone()).isFalse()

    scheduler.advanceUntilIdle()

    assertThat(future.isDone()).isTrue()
    assertThat(future.isCancelled()).isFalse()
    assertThat(didRun.value).isTrue()
  }

  @Test
  fun schedule_runsAfterDelay() = runTest {
    val didRun: AtomicReference<Boolean> = AtomicReference(false)

    val future: ScheduledFuture<*> =
      executor.schedule({ didRun.getAndSet(true) }, 1000L, TimeUnit.MILLISECONDS)

    assertThat(didRun.value).isFalse()
    assertThat(future.isDone()).isFalse()
    assertThat(future.isCancelled()).isFalse()
    assertThat(future.getDelay(TimeUnit.MILLISECONDS)).isEqualTo(1000L)

    scheduler.advanceTimeBy(2000L)

    assertThat(future.isDone()).isTrue()
    assertThat(future.isCancelled()).isFalse()
    assertThat(didRun.value).isTrue()
  }

  @Test
  fun scheduleAtFixedRate_runsRegularlyAfterDelay() = runTest {
    val didRun: AtomicInt = AtomicInt(0)

    val future: ScheduledFuture<*> =
      executor.scheduleAtFixedRate(
        {
          didRun.getAndIncrement()
          // Simulate a long running task.
          scheduler.advanceTimeBy(500L)
        },
        2000L,
        1000L,
        TimeUnit.MILLISECONDS,
      )

    assertThat(future.isDone()).isFalse()
    // Delay should be the initial delay, not the period.
    assertThat(future.getDelay(TimeUnit.MILLISECONDS)).isEqualTo(2000L)

    scheduler.advanceTimeBy(2010L)
    assertThat(didRun.value).isEqualTo(1)

    scheduler.advanceTimeBy(1000L)
    assertThat(didRun.value).isEqualTo(2)

    scheduler.advanceTimeBy(1000L)
    assertThat(didRun.value).isEqualTo(3)

    scheduler.advanceTimeBy(1000L)
    assertThat(didRun.value).isEqualTo(4)

    scheduler.advanceTimeBy(1000L)
    assertThat(didRun.value).isEqualTo(5)

    // The task is still not complete
    assertThat(future.isDone()).isFalse()
    assertThat(future.isCancelled()).isFalse()
  }

  @Test
  fun scheduleAtFixedRate_delayTicksOverToNegativeOnceRunnableHasRun() = runTest {
    val didRun: AtomicInt = AtomicInt(0)

    val future: ScheduledFuture<*> =
      executor.scheduleWithFixedDelay(
        {
          didRun.getAndIncrement()
          // Simulate a long running task.
          scheduler.advanceTimeBy(500L)
        },
        2000L,
        1000L,
        TimeUnit.MILLISECONDS,
      )

    assertWithMessage("Future is done").that(future.isDone()).isFalse()
    // Delay should be the initial delay, not the period.
    assertWithMessage("Future delay").that(future.getDelay(TimeUnit.MILLISECONDS)).isEqualTo(2000L)

    scheduler.advanceTimeBy(2000L)
    scheduler.runCurrent()
    assertWithMessage("Run count").that(didRun.value).isEqualTo(1)
    // Delay should *now* be the period.
    assertWithMessage("Future delay after advancement")
      .that(future.getDelay(TimeUnit.MILLISECONDS))
      .isEqualTo(1000L)

    scheduler.advanceTimeBy(500L)
    assertWithMessage("Future delay after second advancement")
      .that(future.getDelay(TimeUnit.MILLISECONDS))
      .isEqualTo(500L)

    scheduler.advanceTimeBy(3500L)
    assertWithMessage("Future delay after final advancement")
      .that(future.getDelay(TimeUnit.MILLISECONDS))
      .isEqualTo(0L)
    scheduler.runCurrent()
    assertWithMessage("Future delay after running")
      .that(future.getDelay(TimeUnit.MILLISECONDS))
      .isEqualTo(1000L)
    assertWithMessage("Run count after delays").that(didRun.value).isEqualTo(4)

    future.cancel(/* mayInterruptIfRunning= */ true)
    scheduler.advanceTimeBy(3500L)
    // Delay should be negative once the runnable has been canceled.
    assertWithMessage("Future delay after cancel")
      .that(future.getDelay(TimeUnit.MILLISECONDS))
      .isEqualTo(-2500L)
    assertWithMessage("Future is cancelled").that(future.isCancelled()).isTrue()
  }

  @Test
  fun scheduleWithFixedDelay_runsAfterDelay() = runTest {
    val didRun: AtomicInt = AtomicInt(0)

    val future: ScheduledFuture<*> =
      executor.scheduleWithFixedDelay(
        {
          didRun.getAndIncrement()
          // Simulate a long running task.
          scheduler.advanceTimeBy(500L)
        },
        2000L,
        1000L,
        TimeUnit.MILLISECONDS,
      )

    assertThat(future.isDone()).isFalse()

    scheduler.advanceTimeBy(2010L)
    assertThat(didRun.value).isEqualTo(1)

    scheduler.advanceTimeBy(1000L)
    assertThat(didRun.value).isEqualTo(1)

    scheduler.advanceTimeBy(500L)
    assertThat(didRun.value).isEqualTo(2)

    scheduler.advanceTimeBy(1500L)
    assertThat(didRun.value).isEqualTo(3)

    // The task is still not complete
    assertThat(future.isDone()).isFalse()
  }

  @Test
  fun shutdown_rejectsNewTasks() = runTest {
    val runCount: AtomicInt = AtomicInt(0)

    executor.shutdown()
    assertFailsWith<RejectedExecutionException> { executor.submit { runCount.getAndIncrement() } }
    assertFailsWith<RejectedExecutionException> { executor.execute { runCount.getAndIncrement() } }
    assertFailsWith<RejectedExecutionException> {
      executor.schedule({ runCount.getAndIncrement() }, 1L, TimeUnit.SECONDS)
    }
    assertFailsWith<RejectedExecutionException> {
      executor.scheduleWithFixedDelay({ runCount.getAndIncrement() }, 1L, 1L, TimeUnit.SECONDS)
    }
    assertFailsWith<RejectedExecutionException> {
      executor.scheduleAtFixedRate({ runCount.getAndIncrement() }, 1L, 1L, TimeUnit.SECONDS)
    }
    scheduler.advanceTimeBy(5000L)
    scheduler.advanceUntilIdle()

    assertThat(runCount.value).isEqualTo(0)
  }

  @Test
  fun shutdownNow_rejectsNewTasks() = runTest {
    val runCount: AtomicInt = AtomicInt(0)

    executor.shutdownNow()
    assertFailsWith<RejectedExecutionException> { executor.submit { runCount.getAndIncrement() } }
    assertFailsWith<RejectedExecutionException> { executor.execute { runCount.getAndIncrement() } }
    assertFailsWith<RejectedExecutionException> {
      executor.schedule({ runCount.getAndIncrement() }, 1L, TimeUnit.SECONDS)
    }
    assertFailsWith<RejectedExecutionException> {
      executor.scheduleWithFixedDelay({ runCount.getAndIncrement() }, 1L, 1L, TimeUnit.SECONDS)
    }
    assertFailsWith<RejectedExecutionException> {
      executor.scheduleAtFixedRate({ runCount.getAndIncrement() }, 1L, 1L, TimeUnit.SECONDS)
    }
    scheduler.advanceTimeBy(5000L)
    scheduler.advanceUntilIdle()

    assertThat(runCount.value).isEqualTo(0)
  }

  @Test
  fun shutdown_doesNotCancelTasks() = runTest {
    val didRun: AtomicReference<Boolean> = AtomicReference(false)

    val future = executor.submit { didRun.getAndSet(true) }
    executor.shutdown()
    assertThat(executor.isShutdown()).isTrue()
    assertThat(executor.isTerminated()).isFalse()
    scheduler.advanceUntilIdle()

    // The executor is shutdown and terminated.
    assertThat(executor.isShutdown()).isTrue()
    assertThat(executor.isTerminated()).isTrue()
    // The task is not canceled.
    assertThat(future.isDone()).isTrue()
    assertThat(future.isCancelled()).isFalse()
    assertThat(didRun.value).isTrue()
  }

  @Test
  fun shutdown_cancelsScheduledTasks() = runTest {
    val runCount: AtomicInt = AtomicInt(0)

    val future = executor.schedule({ runCount.getAndIncrement() }, 1L, TimeUnit.SECONDS)
    scheduler.advanceTimeBy(500L)
    executor.shutdown()
    assertThat(executor.isShutdown()).isTrue()
    assertThat(executor.isTerminated()).isFalse()
    scheduler.advanceTimeBy(1000L)
    scheduler.advanceUntilIdle()

    assertThat(future.isDone()).isTrue()
    assertThat(future.isCancelled()).isTrue()
    assertThat(runCount.value).isEqualTo(0)
  }

  @Test
  fun shutdown_cancelsFixedRateTasks() = runTest {
    val runCount: AtomicInt = AtomicInt(0)

    val future =
      executor.scheduleAtFixedRate({ runCount.getAndIncrement() }, 2L, 1L, TimeUnit.SECONDS)
    // Advance time by 3 seconds to run the task twice.
    scheduler.advanceTimeBy(3200L)
    // Shutdown the executor.
    executor.shutdown()
    assertThat(executor.isShutdown()).isTrue()
    assertThat(executor.isTerminated()).isFalse()
    // Advance time by 1 second attempt to run the task again.
    scheduler.advanceTimeBy(1000L)
    scheduler.advanceUntilIdle()

    assertThat(future.isDone()).isTrue()
    assertThat(future.isCancelled()).isTrue()
    assertThat(runCount.value).isEqualTo(2)
  }

  @Test
  fun shutdown_cancelsFixedDelayTasks() = runTest {
    val runCount: AtomicInt = AtomicInt(0)

    val future =
      executor.scheduleWithFixedDelay({ runCount.getAndIncrement() }, 2L, 1L, TimeUnit.SECONDS)
    // Advance time by 3 seconds to run the task twice.
    scheduler.advanceTimeBy(3200L)
    // Shutdown the executor.
    executor.shutdown()
    assertThat(executor.isShutdown()).isTrue()
    assertThat(executor.isTerminated()).isFalse()
    // Advance time by 1 second attempt to run the task again.
    scheduler.advanceTimeBy(1000L)
    scheduler.advanceUntilIdle()

    assertThat(future.isDone()).isTrue()
    assertThat(future.isCancelled()).isTrue()
    assertThat(runCount.value).isEqualTo(2)
  }

  @Test
  fun shutdownNow_cancelsTasks() = runTest {
    val didRun: AtomicReference<Boolean> = AtomicReference(false)

    val future = executor.submit { didRun.getAndSet(true) }
    executor.shutdownNow()
    scheduler.advanceUntilIdle()

    // The executor is shutdown and terminated.
    assertWithMessage("Executor is shutdown").that(executor.isShutdown()).isTrue()
    // Canceled tasks are terminated and should no longer be on the list of running tasks.
    assertWithMessage("Executor is terminated").that(executor.isTerminated()).isTrue()
    // The task is canceled.
    assertWithMessage("Future is done").that(future.isDone()).isTrue()
    assertWithMessage("Future is cancelled").that(future.isCancelled()).isTrue()
    assertWithMessage("Task was executed").that(didRun.value).isFalse()
  }
}
