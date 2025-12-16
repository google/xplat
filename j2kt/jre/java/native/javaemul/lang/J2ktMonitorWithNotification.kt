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
package javaemul.lang

/** A native j2kt monitor that supports wait / notify */
open class J2ktMonitorWithNotification : J2ktMonitor() {
  private val condition = newCondition()

  fun wait() {
    condition.await()
  }

  fun notify() {
    condition.signal()
  }

  fun notifyAll() {
    condition.signalAll()
  }

  fun wait(timeout: Long, nanos: Int) {
    // toLong() returns Long.MAX_VALUE if it's bigger than Long.MAX_VALUE,
    // see https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-double/to-long.html
    condition.awaitNanos((timeout * 1_000_000.0 + nanos).toLong())
  }
}
