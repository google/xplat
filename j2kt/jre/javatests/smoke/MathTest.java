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
import static smoke.Asserts.assertFalse;
import static smoke.Asserts.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class MathTest {

  private MathTest() {}

  public static void testMath() {
    testBigDecimal();
    testBigInteger();
    testRoundingMode();
  }

  static void testBigDecimal() {
    assertEquals(0.5, BigDecimal.valueOf(0.5).doubleValue());
    assertEquals(BigDecimal.ONE, BigDecimal.valueOf(1));
    assertEquals(1, BigDecimal.ONE.intValue());

    assertEquals(123.456789123456789, new BigDecimal(123.456789123456789).doubleValue());
    assertEquals(1.99999999, BigDecimal.valueOf(1.99999999).doubleValue());
  }

  static void testBigInteger() {
    assertEquals(0, BigInteger.ZERO.intValue());
    assertEquals(1, BigInteger.ONE.intValue());
    assertEquals(10, BigInteger.TEN.intValue());

    assertTrue(new BigInteger("35742549198872617291353508656626642567").isProbablePrime(10));
    assertFalse(new BigInteger("35742549198872617291353508656626642568").isProbablePrime(10));
  }

  static void testRoundingMode() {
    String s = "precision=5 roundingMode=HALF_UP";
    assertEquals(new MathContext(5, RoundingMode.HALF_UP), new MathContext(s));
    assertEquals(s, new MathContext(5, RoundingMode.HALF_UP).toString());
  }
}
