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
import kotlin.native.ref.createCleaner
import kotlinx.cinterop.Arena
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import platform.Foundation.NSThread
import platform.posix.CLOCK_REALTIME
import platform.posix.ETIMEDOUT
import platform.posix.clock_gettime
import platform.posix.pthread_cond_destroy
import platform.posix.pthread_cond_init
import platform.posix.pthread_cond_signal
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
import platform.posix.sched_yield
import platform.posix.timespec

private val nextId = AtomicLong(1)

@kotlin.native.concurrent.ThreadLocal private val curentThread = Thread(nextId.fetchAndIncrement())

class Thread internal constructor(private val id: kotlin.Long) {
  private val name = run {
    val osName = NSThread.currentThread.name
    if (osName.isNullOrBlank()) "Thread-$id" else osName
  }

  internal val parking: Parking by lazy { Parking() }

  // J2KT threads do not support interruption, so this method is a no-op.
  fun interrupt() {}

  // J2KT threads do not support interruption, so this method always returns false.
  fun isInterrupted(): kotlin.Boolean = false

  fun getId() = id

  fun getName() = name

  companion object {
    fun currentThread(): Thread = curentThread

    // J2KT threads do not support interruption, so this method always returns false.
    fun interrupted(): kotlin.Boolean = false

    fun yield() {
      sched_yield()
    }

    private val MUTEX_ATTR =
      nativeHeap.alloc<pthread_mutexattr_t>().also {
        pthread_mutexattr_init(it.ptr)
        pthread_mutexattr_settype(
          it.ptr,
          platform.posix.PTHREAD_MUTEX_RECURSIVE or platform.posix.PTHREAD_MUTEX_ERRORCHECK,
        )
      }
  }

  internal class Parking() {
    private enum class State {
      NOT_PARKED,
      PARKED,
      PREUNPARKED,
    }

    private val nativeMonitor = NativeMonitor()
    private var parked = State.NOT_PARKED
    private var blocker: Any? = null

    @Suppress("unused") // The returned Cleaner must be assigned to a property
    @ExperimentalStdlibApi
    private val cleaner = createCleaner(nativeMonitor) { it.dispose() }

    fun park(blocker: Any, nanos: kotlin.Long? = null) {
      nativeMonitor.lock()
      this.blocker = blocker
      when (parked) {
        State.NOT_PARKED -> {
          parked = State.PARKED
          if (nanos == null) {
            nativeMonitor.wait()
          } else {
            nativeMonitor.wait(nanos)
          }
          parked = State.NOT_PARKED
        }
        State.PREUNPARKED -> parked = State.NOT_PARKED
        else -> throw IllegalStateException("Invalid parked state for park($blocker): $parked")
      }
      this.blocker = null
      nativeMonitor.unlock()
    }

    fun unpark() {
      nativeMonitor.lock()
      when (parked) {
        State.NOT_PARKED -> parked = State.PREUNPARKED
        State.PARKED -> nativeMonitor.notify()
        else ->
          throw IllegalStateException(
            "Invalid parked state for unpark(): $parked; blocker: $blocker"
          )
      }
      nativeMonitor.unlock()
    }
  }

  private final class NativeMonitor {
    private val arena: Arena = Arena()
    private val cond =
      arena.alloc<pthread_cond_t>().also { require(pthread_cond_init(it.ptr, null) == 0) }
    private val mutex =
      arena.alloc<pthread_mutex_t>().also {
        require(pthread_mutex_init(it.ptr, MUTEX_ATTR.ptr) == 0)
      }

    fun lock() {
      val errorCode = pthread_mutex_lock(mutex.ptr)
      if (errorCode != 0) {
        throw IllegalMonitorStateException("pthread_mutex_lock error code $errorCode")
      }
    }

    fun unlock() {
      val errorCode = pthread_mutex_unlock(mutex.ptr)
      if (errorCode != 0) {
        throw IllegalMonitorStateException("pthread_mutex_unlock error code $errorCode")
      }
    }

    fun wait() {
      val errorCode = pthread_cond_wait(cond.ptr, mutex.ptr)
      if (errorCode != 0) {
        throw IllegalMonitorStateException("pthread_cond_wait error code $errorCode")
      }
    }

    fun wait(nanos: kotlin.Long) {
      if (nanos <= 0) {
        return
      }
      memScoped {
        val ts = alloc<timespec>()

        clock_gettime(CLOCK_REALTIME.toUInt(), ts.ptr)

        val targetNanos = nanos % 1_000_000_000L + ts.tv_nsec

        ts.tv_nsec = targetNanos % 1_000_000_000L
        ts.tv_sec += targetNanos / 1_000_000_000L + nanos / 1_000_000_000L
        val errorCode = pthread_cond_timedwait(cond.ptr, mutex.ptr, ts.ptr)
        if (errorCode != 0 && errorCode != ETIMEDOUT) {
          throw IllegalMonitorStateException("pthread_cond_timedwait error code $errorCode")
        }
      }
    }

    fun notify() {
      val errorCode = pthread_cond_signal(cond.ptr)
      if (errorCode != 0) {
        throw IllegalMonitorStateException("pthread_cond_signal error code $errorCode")
      }
    }

    fun dispose() {
      pthread_cond_destroy(cond.ptr)
      pthread_mutex_destroy(mutex.ptr)
      arena.clear()
    }
  }

  fun interface UncaughtExceptionHandler {
    fun uncaughtException(t: Thread, e: kotlin.Throwable)
  }
}
