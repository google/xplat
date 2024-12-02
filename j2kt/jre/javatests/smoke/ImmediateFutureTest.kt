/*
 * Copyright 2024 Google Inc.
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

import java.lang.RuntimeException
import java.util.concurrent.ExecutionException
import java.util.concurrent.ImmediateFuture
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ImmediateFutureTest {

  @Test
  fun testGet() {
    assertEquals("foo", ImmediateFuture.immediateFuture<String>("foo").get())
  }

  @Test
  fun testGetNull() {
    assertNull(ImmediateFuture.immediateFuture<String?>(null).get())
  }

  @Test
  fun testGetWithTimeout() {
    assertEquals(
      "foo",
      ImmediateFuture.immediateFuture<String>("foo").get(1L, TimeUnit.MILLISECONDS),
    )
  }

  @Test
  fun testCancel() {
    val stringFuture = ImmediateFuture.immediateFuture<String>("foo")
    assertFalse(stringFuture.cancel(false))
    assertFalse(stringFuture.isCancelled())
  }

  @Test
  fun testIsDone() {
    assertTrue(ImmediateFuture.immediateFuture<String>("foo").isDone())
    assertTrue(ImmediateFuture.immediateCanceledFuture<String>().isDone())
    assertEquals(
      true,
      ImmediateFuture.immediateFailedFuture<String>(RuntimeException("test")).isDone(),
    )
  }

  @Test
  fun testIsCancelled() {
    assertTrue(ImmediateFuture.immediateCanceledFuture<String>().isCancelled())
    assertFalse(ImmediateFuture.immediateFuture<String>("foo").isCancelled())
    assertFalse(
      ImmediateFuture.immediateFailedFuture<String>(RuntimeException("test")).isCancelled()
    )
  }

  @Test
  fun testErrorInstance() {
    val re = RuntimeException("test")
    val errorFuture = ImmediateFuture.immediateFailedFuture<String>(re)
    val ex = assertFailsWith<ExecutionException> { errorFuture.get() }
    assertSame(re, ex.cause)
  }
}
