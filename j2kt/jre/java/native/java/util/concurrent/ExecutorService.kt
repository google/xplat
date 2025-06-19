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

package java.util.concurrent

import java.lang.Runnable
import kotlin.collections.MutableCollection
import kotlin.collections.MutableList

interface ExecutorService : Executor {
  fun shutdown()

  fun shutdownNow(): MutableList<Runnable>

  fun isShutdown(): Boolean

  fun isTerminated(): Boolean

  fun awaitTermination(timeout: Long, unit: TimeUnit): Boolean

  fun <T> submit(task: Callable<T>): Future<T>

  fun <T> submit(task: Runnable, result: T): Future<T>

  fun submit(task: Runnable): Future<*>

  fun <T> invokeAll(tasks: MutableCollection<out Callable<T>>): MutableList<Future<T>>

  fun <T> invokeAll(
    tasks: MutableCollection<out Callable<T>>,
    timeout: Long,
    unit: TimeUnit,
  ): MutableList<Future<T>>

  fun <T> invokeAny(tasks: MutableCollection<out Callable<T>>): T

  fun <T> invokeAny(tasks: MutableCollection<out Callable<T>>, timeout: Long, unit: TimeUnit): T
}
