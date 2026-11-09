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

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.Assert;
import org.junit.Test;

public final class ConcurrentTest {

  // Because its methods use `synchronized`, the J2KT transpiler makes CounterMonitor inherit from
  // J2ktMonitor.
  private static final class CounterMonitor {
    private int count = 0;

    synchronized void increment() {
      count++;
    }

    synchronized int getCount() {
      return count;
    }
  }

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

  @Test
  public void concurrentHashMap_concurrentIteration() {
    ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

    map.put("Foo", 1);
    map.put("Bar", 2);

    Iterator<String> keyIterator = map.keySet().iterator();

    assertTrue(keyIterator.hasNext());
    keyIterator.next();

    map.put("Baz", 3);

    assertTrue(keyIterator.hasNext());
    keyIterator.next();
  }

  @Test
  public void synchronizedMonitor_lazyInitAndContention() throws Exception {
    // Starts with monitor = null (no SynchronizedObject allocated yet).
    CounterMonitor reentrantMonitor = new CounterMonitor();
    // Outer synchronized block locks it for the first time, lazily creating its SynchronizedObject;
    // calling increment() inside locks the same monitor a second time on the same thread to verify
    // it reuses that SynchronizedObject instead of creating a new one or deadlocking.
    synchronized (reentrantMonitor) {
      reentrantMonitor.increment();
    }
    assertEquals(1, reentrantMonitor.getCount());

    // Create 1,000 brand-new monitors (each with monitor = null): large enough (~1-3 ms) to
    // outlast CountDownLatch's ~1 us wakeup head start so both threads genuinely overlap on
    // uninitialized monitors, while keeping runtime tiny.
    List<CounterMonitor> freshMonitors = new ArrayList<>(1_000);
    for (int i = 0; i < 1_000; i++) {
      freshMonitors.add(new CounterMonitor());
    }
    // Closed starting gate (counter starts at 1; opens when countDown() drops it to 0).
    CountDownLatch startLatch = new CountDownLatch(1);
    // Pool of 2 idle background threads waiting to be given work.
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
    try {
      // Instructions for each thread (does not run until submitted below): pause at startLatch (10s
      // is just a safety timeout against hangs) until the gate opens, then add 1 to each monitor.
      Callable<Integer> worker =
          () -> {
            assertTrue(startLatch.await(10, SECONDS));
            for (CounterMonitor monitor : freshMonitors) {
              monitor.increment();
            }
            return 0;
          };
      // Put Thread 1 and Thread 2 at the closed gate, then open it so both wake up together and
      // loop through freshMonitors in parallel, racing to initialize `monitor` on each instance.
      Future<Integer> f1 = executor.submit(worker);
      Future<Integer> f2 = executor.submit(worker);
      startLatch.countDown();
      // Wait for both threads to finish (10s safety timeout) before checking final counts.
      f1.get(10, SECONDS);
      f2.get(10, SECONDS);
    } finally {
      executor.shutdownNow();
    }
    // Thread 1 added 1 and Thread 2 added 1, so regardless of what order the two threads lock each
    // monitor, its count must be 2 (catches lost updates if racing first-locks ever created two
    // separate SynchronizedObject instances on the same monitor).
    for (CounterMonitor monitor : freshMonitors) {
      assertEquals(2, monitor.getCount());
    }
  }
}
