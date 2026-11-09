/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:OptIn(ExperimentalObjCName::class)

package javaemul.lang

import kotlin.concurrent.Volatile
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.experimental.ExperimentalObjCName
import kotlin.experimental.ExperimentalObjCRefinement
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

/** Native monitor using [SynchronizedObject]. */
@OptIn(ExperimentalObjCRefinement::class)
open class J2ktMonitor {

  @Volatile private var monitor: SynchronizedObject? = null

  private fun getOrCreateMonitor(): SynchronizedObject =
    getOrCreate({ monitor }, { monitor = it }, { SynchronizedObject() })

  @OptIn(ExperimentalContracts::class)
  @HiddenFromObjC
  inline fun <T> synchronizedImpl(block: () -> T): T {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    lock()
    return try {
      block()
    } finally {
      unlock()
    }
  }

  fun lock() {
    getOrCreateMonitor().lock()
  }

  fun unlock() {
    getOrCreateMonitor().unlock()
  }

  internal companion object {
    internal val INIT_LOCK = SynchronizedObject()

    internal inline fun <T : Any> getOrCreate(get: () -> T?, set: (T) -> Unit, create: () -> T): T =
      get() ?: synchronized(INIT_LOCK) { get() ?: create().also(set) }
  }
}
