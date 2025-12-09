/*
 * Copyright 2025 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package java.util.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/** Minimum implementation of count-down latch. */
public class CountDownLatch {

  private final Lock lock = new ReentrantLock();
  private final Condition condition = lock.newCondition();
  private int count;

  public CountDownLatch(int count) {
    if (count < 0) {
      throw new IllegalArgumentException("Count must be non-negative");
    }
    this.count = count;
  }

  public void await() throws InterruptedException {
    lock.lock();
    try {
      while (count > 0) {
        condition.await(); // Releases the lock, waits, and reacquires on wake-up
      }
    } finally {
      lock.unlock();
    }
  }

  public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
    long remaningNanos = unit.toNanos(timeout);
    lock.lock();
    try {
      while (count > 0) {
        remaningNanos = condition.awaitNanos(remaningNanos);
        if (remaningNanos <= 0) {
          return false;
        }
      }
    } finally {
      lock.unlock();
    }
    return true;
  }

  public void countDown() {
    lock.lock();
    try {
      if (count > 0) {
        count--;
        if (count == 0) {
          condition.signalAll(); // Wake up all threads waiting on this condition
        }
      }
    } finally {
      lock.unlock();
    }
  }

  public int getCount() {
    lock.lock();
    try {
      return count;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public String toString() {
    return super.toString() + "[Count = " + count + "]";
  }
}
