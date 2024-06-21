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

package javaemul.lang

import kotlin.native.internal.createCleaner
import kotlinx.cinterop.Arena
import kotlinx.cinterop.alloc
import kotlinx.cinterop.ptr
import platform.posix.pthread_mutex_destroy
import platform.posix.pthread_mutex_init
import platform.posix.pthread_mutex_lock
import platform.posix.pthread_mutex_t
import platform.posix.pthread_mutex_unlock
import platform.posix.pthread_mutexattr_destroy
import platform.posix.pthread_mutexattr_init
import platform.posix.pthread_mutexattr_settype
import platform.posix.pthread_mutexattr_t

/** Native monitor based on androidx.collection.internal.Lock. */
class J2ktMonitor {

  private val resources = Resources()

  @Suppress("unused") // The returned Cleaner must be assigned to a property
  @ExperimentalStdlibApi
  private val cleaner = createCleaner(resources, Resources::destroy)

  inline fun <T> synchronizedImpl(block: () -> T): T {
    lock()
    return try {
      block()
    } finally {
      unlock()
    }
  }

  fun lock() {
    pthread_mutex_lock(resources.mutex.ptr)
  }

  fun unlock() {
    pthread_mutex_unlock(resources.mutex.ptr)
  }

  private class Resources {
    private val arena = Arena()
    private val attr: pthread_mutexattr_t = arena.alloc()
    val mutex: pthread_mutex_t = arena.alloc()

    init {
      pthread_mutexattr_init(attr.ptr)
      pthread_mutexattr_settype(attr.ptr, platform.posix.PTHREAD_MUTEX_RECURSIVE)
      pthread_mutex_init(mutex.ptr, attr.ptr)
    }

    fun destroy() {
      pthread_mutex_destroy(mutex.ptr)
      pthread_mutexattr_destroy(attr.ptr)
      arena.clear()
    }
  }
}
