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

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.experimental.ExperimentalObjCName
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.ref.createCleaner
import kotlinx.cinterop.alloc
import kotlinx.cinterop.free
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import platform.posix.pthread_mutex_destroy
import platform.posix.pthread_mutex_init
import platform.posix.pthread_mutex_lock
import platform.posix.pthread_mutex_t
import platform.posix.pthread_mutex_unlock
import platform.posix.pthread_mutexattr_init
import platform.posix.pthread_mutexattr_settype
import platform.posix.pthread_mutexattr_t

/** Native monitor using pthread mutex. */
@OptIn(ExperimentalObjCRefinement::class)
open class J2ktMonitor {

  private val mutex: pthread_mutex_t = nativeHeap.alloc()

  init {
    pthread_mutex_init(mutex.ptr, RECURSIVE_ATTR.ptr)
  }

  @Suppress("unused") // The returned Cleaner must be assigned to a property
  @ExperimentalStdlibApi
  private val cleaner =
    createCleaner(mutex) {
      pthread_mutex_destroy(it.ptr)
      nativeHeap.free(it)
    }

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
    pthread_mutex_lock(mutex.ptr)
  }

  fun unlock() {
    pthread_mutex_unlock(mutex.ptr)
  }

  private companion object {
    private val RECURSIVE_ATTR: pthread_mutexattr_t = nativeHeap.alloc()

    init {
      pthread_mutexattr_init(RECURSIVE_ATTR.ptr)
      pthread_mutexattr_settype(RECURSIVE_ATTR.ptr, platform.posix.PTHREAD_MUTEX_RECURSIVE)
    }
  }
}
