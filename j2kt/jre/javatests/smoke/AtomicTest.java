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

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import org.jspecify.annotations.Nullable;
import org.junit.Test;

public class AtomicTest {
  private static final float EPSILON = 1E-6f;

  @Test
  public void testInt() {
    AtomicInteger defaultInt = new AtomicInteger();
    assertEquals(0, defaultInt.get());
    assertEquals("0", defaultInt.toString());

    AtomicInteger atomicInt = new AtomicInteger(1234);
    testNumber(atomicInt, 1234);

    atomicInt.set(5678);
    assertEquals(5678, atomicInt.get());

    atomicInt.lazySet(-42);
    assertEquals(-42, atomicInt.get());
    assertEquals("-42", atomicInt.toString());

    assertEquals(-42, atomicInt.getAndSet(10));
    assertEquals(10, atomicInt.get());

    assertFalse(atomicInt.compareAndSet(99, 20));
    assertEquals(10, atomicInt.get());
    assertTrue(atomicInt.compareAndSet(10, 20));
    assertEquals(20, atomicInt.get());

    assertEquals(20, atomicInt.getAndIncrement());
    assertEquals(21, atomicInt.get());
    assertEquals(22, atomicInt.incrementAndGet());
    assertEquals(22, atomicInt.get());

    assertEquals(22, atomicInt.getAndDecrement());
    assertEquals(21, atomicInt.get());
    assertEquals(20, atomicInt.decrementAndGet());
    assertEquals(20, atomicInt.get());

    assertEquals(20, atomicInt.getAndAdd(5));
    assertEquals(25, atomicInt.get());
    assertEquals(15, atomicInt.addAndGet(-10));
    assertEquals(15, atomicInt.get());

    // Overflow wraparound.
    AtomicInteger maxInt = new AtomicInteger(Integer.MAX_VALUE);
    assertEquals(Integer.MAX_VALUE, maxInt.getAndIncrement());
    assertEquals(Integer.MIN_VALUE, maxInt.get());
    assertEquals(Integer.MAX_VALUE, maxInt.decrementAndGet());
    assertEquals(Integer.MIN_VALUE, maxInt.addAndGet(1));
  }

  @Test
  public void testLong() {
    AtomicLong defaultLong = new AtomicLong();
    assertEquals(0L, defaultLong.get());
    assertEquals("0", defaultLong.toString());

    AtomicLong atomicLong = new AtomicLong(1234L);
    testNumber(atomicLong, 1234);

    atomicLong.set(5678L);
    assertEquals(5678L, atomicLong.get());

    atomicLong.lazySet(-9876543210L);
    assertEquals(-9876543210L, atomicLong.get());
    assertEquals("-9876543210", atomicLong.toString());

    assertEquals(-9876543210L, atomicLong.getAndSet(10L));
    assertEquals(10L, atomicLong.get());

    assertFalse(atomicLong.compareAndSet(99L, 20L));
    assertEquals(10L, atomicLong.get());
    assertTrue(atomicLong.compareAndSet(10L, 20L));
    assertEquals(20L, atomicLong.get());

    assertEquals(20L, atomicLong.getAndIncrement());
    assertEquals(21L, atomicLong.get());
    assertEquals(22L, atomicLong.incrementAndGet());
    assertEquals(22L, atomicLong.get());

    assertEquals(22L, atomicLong.getAndDecrement());
    assertEquals(21L, atomicLong.get());
    assertEquals(20L, atomicLong.decrementAndGet());
    assertEquals(20L, atomicLong.get());

    assertEquals(20L, atomicLong.getAndAdd(5L));
    assertEquals(25L, atomicLong.get());
    assertEquals(15L, atomicLong.addAndGet(-10L));
    assertEquals(15L, atomicLong.get());

    // Overflow wraparound.
    AtomicLong maxLong = new AtomicLong(Long.MAX_VALUE);
    assertEquals(Long.MAX_VALUE, maxLong.getAndIncrement());
    assertEquals(Long.MIN_VALUE, maxLong.get());
    assertEquals(Long.MAX_VALUE, maxLong.decrementAndGet());
    assertEquals(Long.MIN_VALUE, maxLong.addAndGet(1L));
  }

  @Test
  public void testBoolean() {
    AtomicBoolean defaultBoolean = new AtomicBoolean();
    assertFalse(defaultBoolean.get());
    assertEquals("false", defaultBoolean.toString());

    AtomicBoolean atomicBoolean = new AtomicBoolean(true);
    assertTrue(atomicBoolean.get());
    assertEquals("true", atomicBoolean.toString());

    atomicBoolean.set(false);
    assertFalse(atomicBoolean.get());

    atomicBoolean.lazySet(true);
    assertTrue(atomicBoolean.get());

    assertTrue(atomicBoolean.getAndSet(false));
    assertFalse(atomicBoolean.get());
    assertFalse(atomicBoolean.getAndSet(true));
    assertTrue(atomicBoolean.get());

    assertFalse(atomicBoolean.compareAndSet(false, false));
    assertTrue(atomicBoolean.get());
    assertTrue(atomicBoolean.compareAndSet(true, false));
    assertFalse(atomicBoolean.get());

    assertFalse(atomicBoolean.weakCompareAndSet(true, true));
    assertFalse(atomicBoolean.get());
    assertTrue(atomicBoolean.weakCompareAndSet(false, true));
    assertTrue(atomicBoolean.get());
  }

  @Test
  public void testReference() {
    AtomicReference<String> ref = new AtomicReference<>("");
    ref.set("Hello world!");
    assertEquals("Hello world!", ref.get());
    assertEquals("Hello world!", ref.toString());

    ref.lazySet("Lazy");
    assertEquals("Lazy", ref.get());
    assertEquals("Lazy", ref.getAndSet("Swapped"));
    assertEquals("Swapped", ref.get());

    AtomicReference<@Nullable String> nullableRef = new AtomicReference<>();
    assertNull(nullableRef.get());
    assertEquals("null", nullableRef.toString());

    assertFalse(nullableRef.compareAndSet("non-null", "x"));
    assertNull(nullableRef.get());
    assertTrue(nullableRef.compareAndSet(null, "Hello world!"));
    assertEquals("Hello world!", nullableRef.get());
    assertTrue(nullableRef.weakCompareAndSet("Hello world!", null));
    assertNull(nullableRef.get());

    // Identity comparison: distinct EqualValue instances are .equals() but not ==.
    EqualValue first = new EqualValue(7);
    EqualValue equalCopy = new EqualValue(7);
    EqualValue other = new EqualValue(8);
    assertEquals(first, equalCopy);
    assertNotSame(first, equalCopy);

    AtomicReference<EqualValue> identityRef = new AtomicReference<>(first);
    assertFalse(identityRef.compareAndSet(equalCopy, other));
    assertSame(first, identityRef.get());
    assertFalse(identityRef.weakCompareAndSet(equalCopy, other));
    assertSame(first, identityRef.get());
    assertTrue(identityRef.compareAndSet(first, other));
    assertSame(other, identityRef.get());
    assertTrue(identityRef.weakCompareAndSet(other, first));
    assertSame(first, identityRef.get());
  }

  @Test
  public void testIntegerArray() {
    AtomicIntegerArray empty = new AtomicIntegerArray(0);
    assertEquals(0, empty.length());
    assertEquals("[]", empty.toString());
    assertThrowsIndexOutOfBounds(() -> empty.get(-1));
    assertThrowsIndexOutOfBounds(() -> empty.get(0));

    AtomicIntegerArray byLength = new AtomicIntegerArray(3);
    assertEquals(3, byLength.length());
    assertEquals(0, byLength.get(0));
    assertEquals(0, byLength.get(1));
    assertEquals(0, byLength.get(2));

    int[] source = new int[] {1, 2, 3};
    AtomicIntegerArray array = new AtomicIntegerArray(source);
    source[0] = 999;
    assertEquals(3, array.length());
    assertEquals(1, array.get(0));
    assertEquals("[1, 2, 3]", array.toString());

    array.set(0, 10);
    assertEquals(10, array.get(0));
    array.lazySet(1, -20);
    assertEquals(-20, array.get(1));
    assertEquals(3, array.getAndSet(2, 30));
    assertEquals(30, array.get(2));
    assertEquals("[10, -20, 30]", array.toString());

    assertFalse(array.compareAndSet(0, 99, 40));
    assertEquals(10, array.get(0));
    assertTrue(array.compareAndSet(0, 10, 40));
    assertEquals(40, array.get(0));

    assertFalse(array.weakCompareAndSet(0, 99, 50));
    assertEquals(40, array.get(0));
    assertTrue(array.weakCompareAndSet(0, 40, 50));
    assertEquals(50, array.get(0));

    assertEquals(50, array.getAndIncrement(0));
    assertEquals(51, array.get(0));
    assertEquals(52, array.incrementAndGet(0));
    assertEquals(52, array.getAndDecrement(0));
    assertEquals(50, array.decrementAndGet(0));
    assertEquals(50, array.getAndAdd(0, 7));
    assertEquals(57, array.get(0));
    assertEquals(42, array.addAndGet(0, -15));
    assertEquals(42, array.get(0));

    // Overflow wraparound.
    array.set(0, Integer.MAX_VALUE);
    assertEquals(Integer.MAX_VALUE, array.getAndIncrement(0));
    assertEquals(Integer.MIN_VALUE, array.get(0));
    assertEquals(Integer.MAX_VALUE, array.decrementAndGet(0));

    // Unary update methods.
    array.set(0, 5);
    assertEquals(5, array.getAndUpdate(0, v -> v * 3 + 1));
    assertEquals(16, array.get(0));
    assertEquals(33, array.updateAndGet(0, v -> v * 2 + 1));
    assertEquals(33, array.get(0));

    assertThrowsIndexOutOfBounds(() -> array.get(-1));
    assertThrowsIndexOutOfBounds(() -> array.get(array.length()));
  }

  @Test
  public void testIntegerArrayAccumulate() {
    AtomicIntegerArray array = new AtomicIntegerArray(new int[] {1, 1});
    assertEquals(1, array.getAndAccumulate(0, 2, (a, b) -> a * 10 + b));
    assertEquals(12, array.get(0));
    assertEquals(12, array.accumulateAndGet(1, 2, (a, b) -> a * 10 + b));
    assertEquals(12, array.get(1));
  }

  @Test
  public void testLongArray() {
    AtomicLongArray empty = new AtomicLongArray(0);
    assertEquals(0, empty.length());
    assertEquals("[]", empty.toString());
    assertThrowsIndexOutOfBounds(() -> empty.get(-1));
    assertThrowsIndexOutOfBounds(() -> empty.get(0));

    AtomicLongArray byLength = new AtomicLongArray(3);
    assertEquals(3, byLength.length());
    assertEquals(0L, byLength.get(0));
    assertEquals(0L, byLength.get(1));
    assertEquals(0L, byLength.get(2));

    long[] source = new long[] {1L, 2L, 3L};
    AtomicLongArray array = new AtomicLongArray(source);
    source[0] = 999L;
    assertEquals(3, array.length());
    assertEquals(1L, array.get(0));
    assertEquals("[1, 2, 3]", array.toString());

    array.set(0, 10L);
    assertEquals(10L, array.get(0));
    array.lazySet(1, -20L);
    assertEquals(-20L, array.get(1));
    assertEquals(3L, array.getAndSet(2, 30L));
    assertEquals(30L, array.get(2));
    assertEquals("[10, -20, 30]", array.toString());

    assertFalse(array.compareAndSet(0, 99L, 40L));
    assertEquals(10L, array.get(0));
    assertTrue(array.compareAndSet(0, 10L, 40L));
    assertEquals(40L, array.get(0));

    assertFalse(array.weakCompareAndSet(0, 99L, 50L));
    assertEquals(40L, array.get(0));
    assertTrue(array.weakCompareAndSet(0, 40L, 50L));
    assertEquals(50L, array.get(0));

    assertEquals(50L, array.getAndIncrement(0));
    assertEquals(51L, array.get(0));
    assertEquals(52L, array.incrementAndGet(0));
    assertEquals(52L, array.getAndDecrement(0));
    assertEquals(50L, array.decrementAndGet(0));
    assertEquals(50L, array.getAndAdd(0, 7L));
    assertEquals(57L, array.get(0));
    assertEquals(42L, array.addAndGet(0, -15L));
    assertEquals(42L, array.get(0));

    // Overflow wraparound.
    array.set(0, Long.MAX_VALUE);
    assertEquals(Long.MAX_VALUE, array.getAndIncrement(0));
    assertEquals(Long.MIN_VALUE, array.get(0));
    assertEquals(Long.MAX_VALUE, array.decrementAndGet(0));

    // Unary update methods.
    array.set(0, 5L);
    assertEquals(5L, array.getAndUpdate(0, v -> v * 3L + 1L));
    assertEquals(16L, array.get(0));
    assertEquals(33L, array.updateAndGet(0, v -> v * 2L + 1L));
    assertEquals(33L, array.get(0));

    assertThrowsIndexOutOfBounds(() -> array.get(-1));
    assertThrowsIndexOutOfBounds(() -> array.get(array.length()));
  }

  @Test
  public void testLongArrayAccumulate() {
    AtomicLongArray array = new AtomicLongArray(new long[] {1L, 1L});
    assertEquals(1L, array.getAndAccumulate(0, 2L, (a, b) -> a * 10L + b));
    assertEquals(12L, array.get(0));
    assertEquals(12L, array.accumulateAndGet(1, 2L, (a, b) -> a * 10L + b));
    assertEquals(12L, array.get(1));
  }

  @Test
  public void testReferenceArray() {
    AtomicReferenceArray<@Nullable String> empty = new AtomicReferenceArray<>(0);
    assertEquals(0, empty.length());
    assertThrowsIndexOutOfBounds(() -> empty.get(-1));
    assertThrowsIndexOutOfBounds(() -> empty.get(0));

    String[] source = new String[] {"a", "b"};
    AtomicReferenceArray<String> array = new AtomicReferenceArray<>(source);
    source[0] = "mutated";
    assertEquals(2, array.length());
    assertEquals("a", array.get(0));

    array.set(0, "Hello world!");
    assertEquals("Hello world!", array.get(0));
    array.lazySet(1, "Lazy");
    assertEquals("Lazy", array.get(1));
    assertEquals("Lazy", array.getAndSet(1, "Swapped"));
    assertEquals("Swapped", array.get(1));

    AtomicReferenceArray<@Nullable String> nullableArray = new AtomicReferenceArray<>(2);
    assertEquals(2, nullableArray.length());
    assertNull(nullableArray.get(0));
    assertNull(nullableArray.get(1));

    assertFalse(nullableArray.compareAndSet(0, "wrong", "x"));
    assertNull(nullableArray.get(0));
    assertTrue(nullableArray.compareAndSet(0, null, "Hello world!"));
    assertEquals("Hello world!", nullableArray.get(0));
    assertTrue(nullableArray.weakCompareAndSet(0, "Hello world!", null));
    assertNull(nullableArray.get(0));

    // Identity comparison in compareAndSet and weakCompareAndSet.
    EqualValue first = new EqualValue(11);
    EqualValue equalCopy = new EqualValue(11);
    EqualValue other = new EqualValue(12);
    assertEquals(first, equalCopy);
    assertNotSame(first, equalCopy);

    AtomicReferenceArray<EqualValue> identityArray =
        new AtomicReferenceArray<>(new EqualValue[] {first});
    assertFalse(identityArray.compareAndSet(0, equalCopy, other));
    assertSame(first, identityArray.get(0));
    assertFalse(identityArray.weakCompareAndSet(0, equalCopy, other));
    assertSame(first, identityArray.get(0));
    assertTrue(identityArray.compareAndSet(0, first, other));
    assertSame(other, identityArray.get(0));
    assertTrue(identityArray.weakCompareAndSet(0, other, first));
    assertSame(first, identityArray.get(0));

    assertThrowsIndexOutOfBounds(() -> array.get(-1));
    assertThrowsIndexOutOfBounds(() -> array.get(array.length()));
  }

  @Test
  public void testReferenceArrayToString() {
    AtomicReferenceArray<@Nullable String> empty = new AtomicReferenceArray<>(0);
    assertEquals("[]", empty.toString());

    AtomicReferenceArray<@Nullable String> withNull =
        new AtomicReferenceArray<>(new @Nullable String[] {"a", null});
    assertEquals("[a, null]", withNull.toString());

    AtomicReferenceArray<String> three = new AtomicReferenceArray<>(new String[] {"1", "2", "3"});
    assertEquals("[1, 2, 3]", three.toString());
  }

  @Test
  public void testConcurrentOperations() throws Exception {
    int numTasks = 4;
    int opsPerTask = 10_000;
    CountDownLatch startGate = new CountDownLatch(1);
    AtomicInteger atomicInt = new AtomicInteger(0);
    AtomicLong atomicLong = new AtomicLong(0L);
    AtomicIntegerArray atomicIntArray = new AtomicIntegerArray(3);
    AtomicReference<Counter> atomicRef = new AtomicReference<>(new Counter(0));
    AtomicBoolean spinLock = new AtomicBoolean(false);
    PlainInt plainInt = new PlainInt();

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(numTasks);
    try {
      List<Future<@Nullable Void>> futures = new ArrayList<>(numTasks);
      for (int t = 0; t < numTasks; t++) {
        Callable<@Nullable Void> worker =
            () -> {
              if (!startGate.await(10, SECONDS)) {
                throw new AssertionError("Timed out waiting for startGate");
              }
              for (int i = 0; i < opsPerTask; i++) {
                atomicInt.incrementAndGet();
                atomicLong.getAndAdd(2L);
                atomicIntArray.incrementAndGet(1);
                while (true) {
                  Counter current = atomicRef.get();
                  if (atomicRef.compareAndSet(current, new Counter(current.value + 1))) {
                    break;
                  }
                }
                while (!spinLock.compareAndSet(false, true)) {
                  Thread.yield();
                }
                plainInt.value++;
                spinLock.set(false);
              }
              return null;
            };
        futures.add(executor.submit(worker));
      }

      startGate.countDown();
      for (Future<@Nullable Void> future : futures) {
        future.get(10, SECONDS);
      }
    } finally {
      executor.shutdown();
    }

    int expectedTotal = numTasks * opsPerTask;
    assertEquals(expectedTotal, atomicInt.get());
    assertEquals(expectedTotal * 2L, atomicLong.get());
    assertEquals(0, atomicIntArray.get(0));
    assertEquals(expectedTotal, atomicIntArray.get(1));
    assertEquals(0, atomicIntArray.get(2));
    assertEquals(expectedTotal, atomicRef.get().value);
    assertEquals(expectedTotal, plainInt.value);
    assertFalse(spinLock.get());
  }

  private static void assertThrowsIndexOutOfBounds(Runnable runnable) {
    try {
      runnable.run();
      fail("Expected IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException expected) {
      // expected
    }
  }

  private static final class EqualValue {
    private final int id;

    EqualValue(int id) {
      this.id = id;
    }

    @Override
    public boolean equals(@Nullable Object other) {
      return other instanceof EqualValue that && that.id == id;
    }

    @Override
    public int hashCode() {
      return id;
    }

    @Override
    public String toString() {
      return "EqualValue(" + id + ")";
    }
  }

  private static final class Counter {
    final int value;

    Counter(int value) {
      this.value = value;
    }
  }

  private static final class PlainInt {
    int value;
  }

  /**
   * Tests compilation and implementation of {@code Number} abstract class and some methods we
   * override in our implementation (e.g. {@code toString()})
   */
  private static <T extends Number> void testNumber(T n, Integer value) {
    assertEquals(n.byteValue(), value.byteValue());
    assertEquals(n.doubleValue(), value.doubleValue(), EPSILON);
    assertEquals(n.floatValue(), value.floatValue(), EPSILON);
    assertEquals(n.intValue(), value.intValue());
    assertEquals(n.longValue(), value.longValue());
    assertEquals(n.shortValue(), value.shortValue());
    assertEquals(n.toString(), value.toString());
  }
}
