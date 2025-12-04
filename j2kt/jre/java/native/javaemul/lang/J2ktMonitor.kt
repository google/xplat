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

import java.lang.IllegalMonitorStateException
import java.util.concurrent.locks.Condition
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.experimental.ExperimentalObjCName
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.ref.Cleaner
import kotlin.native.ref.createCleaner
import kotlinx.cinterop.alloc
import kotlinx.cinterop.free
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import platform.posix.CLOCK_REALTIME
import platform.posix.ETIMEDOUT
import platform.posix.clock_gettime
import platform.posix.pthread_cond_broadcast
import platform.posix.pthread_cond_destroy
import platform.posix.pthread_cond_init
import platform.posix.pthread_cond_t
import platform.posix.pthread_cond_timedwait
import platform.posix.pthread_cond_wait
import platform.posix.pthread_mutex_destroy
import platform.posix.pthread_mutex_init
import platform.posix.pthread_mutex_lock
import platform.posix.pthread_mutex_t
import platform.posix.pthread_mutex_unlock
import platform.posix.pthread_mutexattr_init
import platform.posix.pthread_mutexattr_settype
import platform.posix.pthread_mutexattr_t
import platform.posix.timespec

/** Native monitor using pthread mutex. */
@OptIn(ExperimentalObjCRefinement::class)
open class J2ktMonitor {

  private val mutex: pthread_mutex_t =
    nativeHeap.alloc { pthread_mutex_init(ptr, RECURSIVE_ATTR.ptr) }

  private val cleaner: Cleaner =
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

  fun newCondition(): Condition =
    object : Condition {
      private val cond: pthread_cond_t = nativeHeap.alloc { pthread_cond_init(ptr, null) }

      @Suppress("unused") // The returned Cleaner must be assigned to a property
      @ExperimentalStdlibApi
      private val cleaner: Cleaner =
        createCleaner(cond) {
          pthread_cond_destroy(it.ptr)
          nativeHeap.free(it)
        }

      override fun await() {
        val errorCode = pthread_cond_wait(cond.ptr, mutex.ptr)
        if (errorCode != 0) {
          throw IllegalMonitorStateException("pthread_cond_wait error code $errorCode")
        }
      }

      override fun awaitNanos(nanos: Long): Long {
        if (nanos <= 0) {
          return nanos
        }
        memScoped {
          val ts = alloc<timespec>()

          clock_gettime(CLOCK_REALTIME.toUInt(), ts.ptr)
          val startSec = ts.tv_sec
          val startNsec = ts.tv_nsec

          val targetNanos = nanos % 1_000_000_000L + ts.tv_nsec

          ts.tv_nsec = targetNanos % 1_000_000_000L
          ts.tv_sec += targetNanos / 1_000_000_000L + nanos / 1_000_000_000L
          val errorCode = pthread_cond_timedwait(cond.ptr, mutex.ptr, ts.ptr)
          if (errorCode != 0 && errorCode != ETIMEDOUT) {
            throw IllegalMonitorStateException("pthread_cond_timedwait error code $errorCode")
          }
          clock_gettime(CLOCK_REALTIME.toUInt(), ts.ptr)
          val elapsed = (ts.tv_sec - startSec) * 1_000_000_000L + (ts.tv_nsec) - startNsec
          return nanos - elapsed
        }
      }

      override fun signal() {
        val errorCode = pthread_cond_broadcast(cond.ptr)
        if (errorCode != 0) {
          throw IllegalMonitorStateException("pthread_cond_broadcast error code $errorCode")
        }
      }

      override fun signalAll() {
        val errorCode = pthread_cond_broadcast(cond.ptr)
        if (errorCode != 0) {
          throw IllegalMonitorStateException("pthread_cond_broadcast error code $errorCode")
        }
      }
    }

  private companion object {
    private val RECURSIVE_ATTR: pthread_mutexattr_t =
      nativeHeap.alloc {
        pthread_mutexattr_init(ptr)
        pthread_mutexattr_settype(ptr, platform.posix.PTHREAD_MUTEX_RECURSIVE)
      }
  }
}
