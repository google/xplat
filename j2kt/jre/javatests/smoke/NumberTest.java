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
package smoke;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class NumberTest {

  private static class MyNumber extends Number {
    private final int value;

    public MyNumber(int value) {
      this.value = value;
    }

    @Override
    public int intValue() {
      return value;
    }

    @Override
    public long longValue() {
      return value;
    }

    @Override
    public float floatValue() {
      return value;
    }

    @Override
    public double doubleValue() {
      return value;
    }
  }

  private abstract static class AbstractNumber extends Number {
    private final int value;

    public AbstractNumber(int value) {
      this.value = value;
    }

    @Override
    public int intValue() {
      return value;
    }

    @Override
    public long longValue() {
      return value;
    }

    @Override
    public float floatValue() {
      return value;
    }

    @Override
    public double doubleValue() {
      return value;
    }
  }

  private static class ConcreteNumber extends AbstractNumber {
    public ConcreteNumber(int value) {
      super(value);
    }
  }

  @Test
  public void testNumberOverrides() {
    MyNumber number = new MyNumber(12345);
    assertEquals(12345, number.intValue());
    assertEquals(57, number.byteValue()); // 12345 % 256 = 57
    assertEquals(12345, number.shortValue());

    MyNumber largeNumber = new MyNumber(1000000);
    assertEquals(1000000, largeNumber.intValue());
    assertEquals(64, largeNumber.byteValue()); // 1000000 % 256 = 64
    assertEquals(16960, largeNumber.shortValue()); // 1000000 % 65536 = 16960
  }

  @Test
  public void testAbstractNumberOverrides() {
    ConcreteNumber number = new ConcreteNumber(123);
    assertEquals(123, number.intValue());
    assertEquals(123, number.byteValue());
    assertEquals(123, number.shortValue());
  }
}
