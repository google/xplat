/*
 * Copyright 2022 Google Inc.
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

import static smoke.Asserts.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AtomicTest {
  private AtomicTest() {}

  static void testAtomic() {
    testInt();
    testLong();
  }

  private static void testInt() {
    AtomicInteger atomicInt = new AtomicInteger(1234);
    testNumber(atomicInt, 1234);

    atomicInt.set(5678);
    assertEquals(atomicInt.get(), 5678);
  }

  private static void testLong() {
    AtomicLong atomicLong = new AtomicLong(1234L);
    testNumber(atomicLong, 1234);

    atomicLong.set(5678L);
    assertEquals(atomicLong.get(), 5678L);
  }

  /**
   * Tests compilation and implementation of {@code Number} abstract class and some methods we
   * override in our implementation (e.g. {@code toString()})
   */
  private static <T extends Number> void testNumber(T n, Integer value) {
    assertEquals(n.byteValue(), value.byteValue());
    assertEquals(n.doubleValue(), value.doubleValue());
    assertEquals(n.floatValue(), value.floatValue());
    assertEquals(n.intValue(), value);
    assertEquals(n.longValue(), value.longValue());
    assertEquals(n.shortValue(), value.shortValue());
    assertEquals(n.toString(), value.toString());
  }
}
