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
import java.util.concurrent.Delayed
import java.util.concurrent.KotlinExecutor
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler

class KotlinDelayedFutureTest {

  private val scheduler = TestCoroutineScheduler()
  private val executor = KotlinExecutor(StandardTestDispatcher(scheduler), scheduler.timeSource)

  @Test
  fun getDelay_returnsInitialDelay() {
    val future = executor.schedule({}, 60000L, TimeUnit.MILLISECONDS)

    assertThat(future.getDelay(TimeUnit.MILLISECONDS)).isEqualTo(60000L)
    assertThat(future.getDelay(TimeUnit.MINUTES)).isEqualTo(1L)
    assertThat(future.getDelay(TimeUnit.SECONDS)).isEqualTo(60L)
    assertThat(future.getDelay(TimeUnit.NANOSECONDS)).isEqualTo(60000000000L)
    assertThat(future.getDelay(TimeUnit.MICROSECONDS)).isEqualTo(60000000L)
    assertThat(future.getDelay(TimeUnit.DAYS)).isEqualTo(0L)
  }

  @Test
  fun getDelay_returnsAccurateDelay() {
    val future = executor.schedule({}, 2000L, TimeUnit.MILLISECONDS)
    scheduler.advanceTimeBy(500L)

    assertThat(future.getDelay(TimeUnit.MILLISECONDS)).isEqualTo(1500L)
  }

  @Test
  fun getDelay_returnsNegativeDelay() {
    val future = executor.schedule({}, 2000L, TimeUnit.MILLISECONDS)
    scheduler.advanceTimeBy(3000L)

    assertThat(future.getDelay(TimeUnit.MILLISECONDS)).isEqualTo(-1000L)
  }

  @Test
  fun compareTo_returnsCorrectValue() {
    val future1 = executor.schedule({}, 1000L, TimeUnit.MILLISECONDS)
    val future2 = executor.schedule({}, 2000L, TimeUnit.MILLISECONDS)

    assertThat(future1.compareTo(future2)).isEqualTo(-1)
  }

  @Test
  fun compareTo_differentTypes() {
    val future = executor.schedule({}, 1000L, TimeUnit.MILLISECONDS)
    val delay = future.getDelay(TimeUnit.MILLISECONDS)
    val otherShorter = FakeDelayed(delay - 1000L)
    val otherSame = FakeDelayed(delay)
    val otherLonger = FakeDelayed(delay + 1500L)

    assertThat(future.compareTo(otherShorter)).isEqualTo(1)
    assertThat(future.compareTo(otherSame)).isEqualTo(0)
    assertThat(future.compareTo(otherLonger)).isEqualTo(-1)
  }

  @Test
  fun compareTo_trueOnSameDelay() {
    val future1 = executor.schedule({}, 1000L, TimeUnit.MILLISECONDS)
    val future2 = executor.schedule({}, 1000L, TimeUnit.MILLISECONDS)

    assertThat(future1.compareTo(future2)).isEqualTo(0)
  }

  @Test
  fun compareTo_trueOnSameDelayAfterTimepasses() {
    val future1 = executor.schedule({}, 1000L, TimeUnit.MILLISECONDS)
    val future2 = executor.schedule({}, 1000L, TimeUnit.MILLISECONDS)
    scheduler.advanceTimeBy(500L)

    assertThat(future1.compareTo(future2)).isEqualTo(0)
  }

  @Test
  fun compareTo_trueOnSameInstance() {
    val future1 = executor.schedule({}, 1000L, TimeUnit.MILLISECONDS)

    assertThat(future1.compareTo(future1)).isEqualTo(0)
  }

  @Test
  fun cancel_isCancelled() {
    val future = executor.schedule({}, 1000L, TimeUnit.MILLISECONDS)
    future.cancel(true)
    assertThat(future.isCancelled()).isTrue()
  }

  class FakeDelayed(val delay: Long = 0L) : Delayed {
    override fun getDelay(unit: TimeUnit): Long {
      return unit.convert(delay, TimeUnit.MILLISECONDS)
    }

    override fun compareTo(other: Delayed): Int {
      throw AssertionError("Should not call test object")
    }
  }
}
