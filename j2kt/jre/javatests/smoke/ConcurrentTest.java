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
package smoke;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class ConcurrentTest {

  @Test
  public void futureTask_smoke() {
    FutureTask<String> task = new FutureTask<>(() -> "Hello World");
    Assert.assertFalse(task.isCancelled());
    Assert.assertFalse(task.isDone());
  }

  @Test
  public void lockSupport_smoke() {
    LockSupport.unpark(Thread.currentThread());
    LockSupport.park();
    LockSupport.parkNanos("smoke", 1);
  }

  @Test
  public void reentrantLock_isReentrant() {
    Lock lock = new ReentrantLock();
    lock.lock();
    try {
      lock.lock();
      try {
        assertTrue(true);
      } finally {
        lock.unlock();
      }
    } finally {
      lock.unlock();
    }
    assertTrue(true);
  }

  @Test
  public void linkedBlockingQueue_smoke() {
    LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
    Assert.assertTrue(queue.isEmpty());
    queue.add("Hello");
    Assert.assertFalse(queue.isEmpty());
    queue.add("World");
    Assert.assertFalse(queue.isEmpty());
    Assert.assertEquals("Hello", queue.peek());
    Assert.assertEquals("Hello", queue.remove());
    Assert.assertFalse(queue.isEmpty());
    Assert.assertEquals("World", queue.poll());
    Assert.assertNull(queue.poll());
    Assert.assertTrue(queue.isEmpty());
  }
}
