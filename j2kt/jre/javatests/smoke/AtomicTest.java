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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import org.jspecify.nullness.Nullable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AtomicTest {

  @Test
  public void testInt() {
    AtomicInteger atomicInt = new AtomicInteger(1234);
    testNumber(atomicInt, 1234);

    atomicInt.set(5678);
    assertEquals(atomicInt.get(), 5678);
  }

  @Test
  public void testLong() {
    AtomicLong atomicLong = new AtomicLong(1234L);
    testNumber(atomicLong, 1234);

    atomicLong.set(5678L);
    assertEquals(atomicLong.get(), 5678L);
  }

  @Test
  public void testReference() {
    AtomicReference<String> ref = new AtomicReference<>("");
    ref.set("Hello world!");
    String result = ref.get();
    assertEquals("Hello world!", result);

    AtomicReference<@Nullable String> nullableRef = new AtomicReference<>();
    nullableRef.set("Hello world!");
    @Nullable String nullableResult = nullableRef.get();
    assertEquals("Hello world!", nullableResult);
  }

  @Test
  public void testReferenceArray() {
    AtomicReferenceArray<String> array = new AtomicReferenceArray<>(new String[] {""});
    array.set(0, "Hello world!");
    String result = array.get(0);
    assertEquals("Hello world!", result);

    AtomicReferenceArray<@Nullable String> nullableArray = new AtomicReferenceArray<>(1);
    nullableArray.set(0, "Hello world!");
    @Nullable String nullableResult = nullableArray.get(0);
    assertEquals("Hello world!", nullableResult);
  }

  @Test
  public void testBoolean() {
    AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    boolean initialValue = atomicBoolean.getAndSet(true);
    assertEquals(false, initialValue);
    assertEquals(true, atomicBoolean.get());
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
