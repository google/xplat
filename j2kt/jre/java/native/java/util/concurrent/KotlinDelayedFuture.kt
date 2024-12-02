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
import kotlin.math.sign
import kotlin.time.ComparableTimeMark
import kotlinx.coroutines.Deferred

/**
 * A [ScheduledFuture] that wraps a [Deferred] scheduled to be run at a specific
 * [ComparableTimeMark]. The [ComparableTimeMark] should be set to the future to the time that the
 * task should start running, not the time that the task is scheduled. It is also wrapped in an
 * [AtomicReference] so that it can be updated if the task is rescheduled.
 *
 * Note that this class' Comparability is error prone.
 */
internal class KotlinDelayedFuture<T>(
  deferred: Deferred<T>,
  private var offsetStartTime: AtomicReference<ComparableTimeMark>,
) : KotlinFuture<T>(deferred), ScheduledFuture<T> {

  override fun getDelay(unit: TimeUnit): Long {
    return unit.convert(
      -offsetStartTime.value.elapsedNow().inWholeMilliseconds,
      TimeUnit.MILLISECONDS,
    )
  }

  override fun compareTo(other: Delayed): Int {
    if (other is KotlinDelayedFuture<*>) {
      return offsetStartTime.value.compareTo(other.offsetStartTime.value)
    } else {
      // Compare the delay in milliseconds. This means that any two delayed objects that have the
      // same delay in milliseconds will be considered equal, which causes the compareTo
      // method to return incorrect values sometimes.
      return (getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS)).sign
    }
  }
}
