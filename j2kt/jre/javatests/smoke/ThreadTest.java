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
package smoke;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class ThreadTest {

  @Test
  public void testCurrentThread() {
    assertTrue("id > 0", Thread.currentThread().getId() > 0);
    Thread.yield();
    assertSame("Identical instances", Thread.currentThread(), Thread.currentThread());
    assertFalse("Has a name", Thread.currentThread().getName().isEmpty());
  }

  @Test
  public void testThreadLocal() {
    ThreadLocal<String> threadLocal = new ThreadLocal<>();
    assertNull(threadLocal.get());
    threadLocal.set("Hello");
    assertEquals("Hello", threadLocal.get());
    threadLocal.set("World");
    assertEquals("World", threadLocal.get());
    threadLocal.remove();
    assertNull(threadLocal.get());
    threadLocal.set("Hello");
    assertEquals("Hello", threadLocal.get());

    ThreadLocal<String> withInitial = ThreadLocal.withInitial(() -> "Initial");
    assertEquals("Initial", withInitial.get());
  }

  @Test
  public void availableProcessors_atLeastOne() {
    assertTrue(Runtime.getRuntime().availableProcessors() >= 1);
  }
}
