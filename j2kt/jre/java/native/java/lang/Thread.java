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
package java.lang;

import java.util.concurrent.atomic.AtomicLong;

/** Thread subset supporting a threadId for simple logging and thread identity checks. */
public final class Thread {

  private static final AtomicLong nextId = new AtomicLong(1);

  private static final ThreadLocal<Thread> currentThread =
      new ThreadLocal<Thread>() {
        @Override
        protected Thread initialValue() {
          return new Thread(nextId.getAndIncrement());
        }
      };

  public static Thread currentThread() {
    return currentThread.get();
  }

  private final long id;

  private Thread(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return "Thread-" + id;
  }
}
