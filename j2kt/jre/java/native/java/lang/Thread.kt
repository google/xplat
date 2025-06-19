/*
 * Copyright 2025 Google Inc.
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
@file:OptIn(ExperimentalAtomicApi::class)

package java.lang

import kotlin.concurrent.atomics.AtomicLong
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.fetchAndIncrement
import platform.Foundation.NSThread

private val nextId = AtomicLong(1)

@kotlin.native.concurrent.ThreadLocal private val curentThread = Thread(nextId.fetchAndIncrement())

class Thread internal constructor(private val id: kotlin.Long) {
  private val name = run {
    val osName = NSThread.currentThread.name
    if (osName.isNullOrBlank()) "Thread-$id" else osName
  }

  fun getId() = id

  fun getName() = name

  companion object {
    fun currentThread(): Thread = curentThread
  }
}
