/*
 * Copyright 2026 Google Inc.
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
package smoke

import java.lang.Thread
import kotlin.native.concurrent.ObsoleteWorkersApi
import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ThreadInteropTest {

  @OptIn(ObsoleteWorkersApi::class)
  @Test
  fun currentThread_attachedThreadNames() {
    assertTrue(platform.Foundation.NSThread.isMainThread)
    assertEquals("main", Thread.currentThread().getName())

    val unnamedWorker = Worker.start()
    try {
      val (isMain, id, name) =
        unnamedWorker
          .execute(TransferMode.SAFE, {}) {
            val current = Thread.currentThread()
            Triple(platform.Foundation.NSThread.isMainThread, current.getId(), current.getName())
          }
          .result
      assertFalse(isMain)
      assertEquals("Thread-$id", name)
    } finally {
      unnamedWorker.requestTermination().result
    }

    val namedWorker = Worker.start()
    try {
      val name =
        namedWorker
          .execute(TransferMode.SAFE, {}) {
            platform.Foundation.NSThread.currentThread.name = "custom-os-thread"
            Thread.currentThread().getName()
          }
          .result
      assertEquals("custom-os-thread", name)
    } finally {
      namedWorker.requestTermination().result
    }
  }

  @OptIn(ObsoleteWorkersApi::class)
  @Test
  fun toString_includesNameAndDefaultPriority() {
    assertTrue(platform.Foundation.NSThread.isMainThread)
    assertEquals("Thread[main,5]", Thread.currentThread().toString())

    val unnamedWorker = Worker.start()
    try {
      val (id, string) =
        unnamedWorker
          .execute(TransferMode.SAFE, {}) {
            val current = Thread.currentThread()
            current.getId() to current.toString()
          }
          .result
      assertEquals("Thread[Thread-$id,5]", string)
    } finally {
      unnamedWorker.requestTermination().result
    }

    val namedWorker = Worker.start()
    try {
      val string =
        namedWorker
          .execute(TransferMode.SAFE, {}) {
            platform.Foundation.NSThread.currentThread.name = "custom-os-thread"
            Thread.currentThread().toString()
          }
          .result
      assertEquals("Thread[custom-os-thread,5]", string)
    } finally {
      namedWorker.requestTermination().result
    }
  }
}
