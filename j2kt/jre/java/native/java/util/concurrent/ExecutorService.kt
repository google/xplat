@file:OptIn(ExperimentalObjCName::class)

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
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@ObjCName("J2ktJavaUtilConcurrentExecutorService", exact = true)
interface ExecutorService : Executor {
  fun shutdown()

  fun shutdownNow(): MutableList<Runnable>

  fun isShutdown(): Boolean

  fun isTerminated(): Boolean

  fun awaitTermination(
    @ObjCName("withLong") timeout: Long,
    @ObjCName("withJavaUtilConcurrentTimeUnit") unit: TimeUnit,
  ): Boolean

  fun <T> submit(@ObjCName("withJavaUtilConcurrentCallable") task: Callable<T>): Future<T>

  fun <T> submit(
    @ObjCName("withJavaLangRunnable") task: Runnable,
    @ObjCName("withId") result: T,
  ): Future<T>

  fun submit(@ObjCName("withJavaLangRunnable") task: Runnable): Future<*>

  fun <T> invokeAll(
    @ObjCName("withJavaUtilCollection") tasks: MutableCollection<out Callable<T>>
  ): MutableList<Future<T>>

  fun <T> invokeAll(
    @ObjCName("withJavaUtilCollection") tasks: MutableCollection<out Callable<T>>,
    @ObjCName("withLong") timeout: Long,
    @ObjCName("withJavaUtilConcurrentTimeUnit") unit: TimeUnit,
  ): MutableList<Future<T>>

  fun <T> invokeAny(
    @ObjCName("withJavaUtilCollection") tasks: MutableCollection<out Callable<T>>
  ): T

  fun <T> invokeAny(
    @ObjCName("withJavaUtilCollection") tasks: MutableCollection<out Callable<T>>,
    @ObjCName("withLong") timeout: Long,
    @ObjCName("withJavaUtilConcurrentTimeUnit") unit: TimeUnit,
  ): T
}
