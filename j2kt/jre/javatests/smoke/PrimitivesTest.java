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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PrimitivesTest {
  private static final float EPSILON = 1E-6f;

  @Test
  public void testBoolean() {
    assertEquals((Object) true, Boolean.TRUE);
    assertEquals((Object) false, Boolean.FALSE);
    assertTrue(Boolean.TRUE.booleanValue());
    assertFalse(Boolean.FALSE.booleanValue());
    assertEquals("true", Boolean.toString(true));
    assertEquals("false", Boolean.toString(false));
    assertEquals(Boolean.TRUE, Boolean.valueOf("true"));
    assertEquals(Boolean.TRUE, Boolean.valueOf("TRUE"));
    assertEquals(Boolean.FALSE, Boolean.valueOf(null));
    assertEquals(true, Boolean.parseBoolean("true"));
    assertEquals(true, Boolean.parseBoolean("TRUE"));
    assertEquals(false, Boolean.parseBoolean(null));
    assertTrue((Object) Boolean.TRUE == (Object) Boolean.valueOf(true));
    assertTrue((Object) Boolean.FALSE == (Object) Boolean.valueOf(false));
  }

  @Test
  public void testInt() {
    assertEquals((Object) 9, Integer.valueOf(9));
    assertEquals((Object) 9, Integer.valueOf("9"));
    assertEquals((Object) 14, Integer.valueOf("16", 8));

    assertTrue(Integer.compare(9, 9) == 0);
    assertTrue(Integer.compare(9, 16) < 0);
    assertTrue(Integer.compare(16, 9) > 0);

    assertEquals("9", Integer.toString(9));
    assertEquals("20", Integer.toString(16, 8));
    assertEquals("87", Integer.toUnsignedString(87));
    assertEquals("127", Integer.toUnsignedString(87, 8));
    assertEquals("57", Integer.toHexString(87));
    assertEquals("ffffffa9", Integer.toHexString(-87));
    assertEquals("4294967209", Integer.toUnsignedString(-87));
    assertEquals("37777777651", Integer.toUnsignedString(-87, 8));
    assertEquals("2147483648", Integer.toUnsignedString(Integer.MIN_VALUE));
    assertEquals("20000000000", Integer.toUnsignedString(Integer.MIN_VALUE, 8));

    assertEquals(16, Integer.parseInt("16"));
    assertEquals(14, Integer.parseInt("16", 8));

    assertEquals(87, Integer.hashCode(87));

    assertEquals(28, Integer.numberOfLeadingZeros(10));
    assertEquals(1, Integer.numberOfTrailingZeros(10));

    assertEquals(64, Integer.highestOneBit(87));
    assertEquals(5, Integer.bitCount(87));

    assertEquals(1, Integer.signum(87));
    assertEquals(-1, Integer.signum(-87));
    assertEquals(0, Integer.signum(0));

    assertEquals(0x0FFFFFF0, 0x00FFFFFF << 4);
    assertEquals(0xFFFFFFF0, 0xFFFFFFFF << 4);
    assertEquals(0x000FFFFF, 0x00FFFFFF >> 4);
    assertEquals(0xFFFFFFFF, 0xFFFFFFFF >> 4);
    assertEquals(0x000FFFFF, 0x00FFFFFF >>> 4);
    assertEquals(0x0FFFFFFF, 0xFFFFFFFF >>> 4);
  }

  @Test
  public void testLong() {
    assertEquals((Object) 9L, Long.valueOf(9));
    assertEquals((Object) 9L, Long.valueOf("9"));
    assertEquals((Object) 14L, Long.valueOf("16", 8));

    assertTrue(Long.compare(9, 9) == 0);
    assertTrue(Long.compare(9, 16) < 0);
    assertTrue(Long.compare(16, 9) > 0);

    assertEquals("9", Long.toString(9));
    assertEquals("20", Long.toString(16, 8));
    assertEquals("87", Long.toUnsignedString(87));
    assertEquals("127", Long.toUnsignedString(87, 8));
    assertEquals("57", Long.toHexString(87));
    assertEquals("ffffffffffffffa9", Long.toHexString(-87));
    assertEquals("18446744073709551529", Long.toUnsignedString(-87));
    assertEquals("1777777777777777777651", Long.toUnsignedString(-87, 8));
    assertEquals("9223372036854775808", Long.toUnsignedString(Long.MIN_VALUE));
    assertEquals("1000000000000000000000", Long.toUnsignedString(Long.MIN_VALUE, 8));

    assertEquals(16L, Long.parseLong("16"));
    assertEquals(14L, Long.parseLong("16", 8));

    assertEquals(87, Long.hashCode(87));
    assertEquals(86, Long.hashCode(-87));

    assertEquals(60, Long.numberOfLeadingZeros(10));
    assertEquals(1, Long.numberOfTrailingZeros(10));

    assertEquals(5, Long.bitCount(87));

    assertEquals(1, Long.signum(87));
    assertEquals(-1, Long.signum(-87));
    assertEquals(0, Long.signum(0));

    assertEquals(0x0FFFFFFFFFFFFFF0L, 0x00FFFFFFFFFFFFFFL << 4);
    assertEquals(0xFFFFFFFFFFFFFFF0L, 0xFFFFFFFFFFFFFFFFL << 4);
    assertEquals(0x000FFFFFFFFFFFFFL, 0x00FFFFFFFFFFFFFFL >> 4);
    assertEquals(0xFFFFFFFFFFFFFFFFL, 0xFFFFFFFFFFFFFFFFL >> 4);
    assertEquals(0x000FFFFFFFFFFFFFL, 0x00FFFFFFFFFFFFFFL >>> 4);
    assertEquals(0x0FFFFFFFFFFFFFFFL, 0xFFFFFFFFFFFFFFFFL >>> 4);
  }

  @Test
  public void testDouble() {
    Double dNan = Double.NaN, dInf = Double.POSITIVE_INFINITY;

    assertEquals(9.5, Double.valueOf(9.5), EPSILON);
    assertEquals(9.5, Double.valueOf("9.5"), EPSILON);

    assertTrue(Double.compare(9.5, 9.5) == 0);
    assertTrue(Double.compare(9.5, 16.0) < 0);
    assertTrue(Double.compare(16, 9.5) > 0);

    assertEquals("9.5", Double.toString(9.5));

    assertEquals(16.0, Double.parseDouble("16"), EPSILON);

    // Alternate condition for JS
    assertTrue(Double.hashCode(8) == 1075838976 || Double.hashCode(8) == 8);

    assertTrue(Double.isNaN(dNan));
    assertFalse(Double.isNaN(0.0));

    assertTrue(Double.isInfinite(dInf));
    assertFalse(Double.isInfinite(0.0));

    assertEquals(3.1711515E-317, Double.longBitsToDouble(6418482L), EPSILON);
    assertEquals(0x7ff8000000000000L, Double.doubleToLongBits(Double.NaN));
    assertEquals(0x7ff8000000000000L, Double.doubleToRawLongBits(Double.NaN));

    assertTrue(Double.isNaN(Double.longBitsToDouble(0x7ff8000000000001L)));
    assertEquals(
        0x7ff8000000000000L, Double.doubleToLongBits(Double.longBitsToDouble(0x7ff8000000000001L)));
    assertEquals(
        0x7ff8000000000001L,
        Double.doubleToRawLongBits(Double.longBitsToDouble(0x7ff8000000000001L)));

    assertEquals(30, Math.getExponent(1234567890d));
  }

  @Test
  public void testShort() {
    short a = 9, b = 16, c = 14;

    assertEquals((Object) a, Short.valueOf(a));
    assertEquals((Object) a, Short.valueOf("9"));
    assertEquals((Object) c, Short.valueOf("16", 8));

    assertTrue(Short.compare(a, a) == 0);
    assertTrue(Short.compare(a, b) < 0);
    assertTrue(Short.compare(b, a) > 0);

    assertEquals("9", Short.toString(a));

    assertEquals(b, Short.parseShort("16"));
    assertEquals(c, Short.parseShort("16", 8));

    assertEquals(9, Short.hashCode(a));

    assertEquals(0x0000FFF0, ((short) 0x0FFF) << 4);
    assertEquals(0xFFFFFFF0, ((short) 0xFFFF) << 4);
    assertEquals(0x000000FF, ((short) 0x0FFF) >> 4);
    assertEquals(0xFFFFFFFF, ((short) 0xFFFF) >> 4);
    assertEquals(0x000000FF, ((short) 0x0FFF) >>> 4);
    assertEquals(0x0FFFFFFF, ((short) 0xFFFF) >>> 4);

    assertEquals(8, a & 0xA);
  }

  @Test
  public void testByte() {
    byte a = 9, b = 16, c = 14;

    assertEquals((Object) a, Byte.valueOf(a));
    assertEquals((Object) a, Byte.valueOf("9"));
    assertEquals((Object) c, Byte.valueOf("16", 8));

    assertTrue(Byte.compare(a, a) == 0);
    assertTrue(Byte.compare(a, b) < 0);
    assertTrue(Byte.compare(b, a) > 0);

    assertEquals("9", Byte.toString(a));

    assertEquals(b, Byte.parseByte("16"));
    assertEquals(c, Byte.parseByte("16", 8));

    assertEquals(9, Byte.hashCode(a));

    assertEquals(0x0000003C, ((byte) 0x0F) << 2);
    assertEquals(0xFFFFFFFC, ((byte) 0xFF) << 2);
    assertEquals(0x00000003, ((byte) 0x0F) >> 2);
    assertEquals(0xFFFFFFFF, ((byte) 0xFF) >> 2);
    assertEquals(0x00000003, ((byte) 0x0F) >>> 2);
    assertEquals(0x3FFFFFFF, ((byte) 0xFF) >>> 2);

    assertEquals(8, a & 0xA);
  }

  @Test
  public void testFloat() {
    float a = 9, b = 16, fNan = Float.NaN, fInf = Float.POSITIVE_INFINITY;

    assertEquals((Object) 9F, Float.valueOf(a));
    assertEquals((Object) 9F, Float.valueOf("9"));

    assertTrue(Float.compare(a, a) == 0);
    assertTrue(Float.compare(a, b) < 0);
    assertTrue(Float.compare(b, a) > 0);

    // Alternate condition for JS
    assertTrue(Float.toString(a).equals("9") || Float.toString(a).equals("9.0"));

    assertEquals(16F, Float.parseFloat("16"), EPSILON);

    // Alternate condition for JS
    assertTrue(Float.hashCode(9) == 1091567616 || Float.hashCode(9) == 9);

    assertTrue(Float.isNaN(fNan));
    assertFalse(Float.isNaN(a));

    assertTrue(Float.isInfinite(fInf));
    assertFalse(Float.isInfinite(0));

    assertEquals(1.729997E-39F, Float.intBitsToFloat(1234567), EPSILON);

    assertEquals(1234613304, Float.floatToIntBits(1234567F));

    assertEquals(0x7fc00000, Float.floatToIntBits(Float.NaN));
    assertEquals(0x7fc00000, Float.floatToIntBits(Float.NaN));

    assertTrue(Float.isNaN(Float.intBitsToFloat(0x7fc00001)));
    assertEquals(0x7fc00000, Float.floatToIntBits(Float.intBitsToFloat(0x7fc00001)));
    assertEquals(0x7fc00001, Float.floatToRawIntBits(Float.intBitsToFloat(0x7fc00001)));

    assertEquals(13, Math.getExponent(12345f));
  }

  @Test
  public void testCharacter() {
    assertEquals((Object) 'a', Character.valueOf('a'));

    assertEquals(0, Character.compare('a', 'a'));
    assertEquals(-1, Character.compare('a', 'b'));
    assertEquals(1, Character.compare('c', 'b'));

    assertTrue(Character.isDigit('1'));
    assertFalse(Character.isDigit('a'));

    assertTrue(Character.isLetter('a'));
    assertFalse(Character.isLetter('1'));

    assertTrue(Character.isUpperCase('A'));
    assertFalse(Character.isUpperCase('a'));

    assertTrue(Character.isLowerCase('a'));
    assertFalse(Character.isLowerCase('A'));

    assertTrue(Character.isLetterOrDigit('a'));
    assertFalse(Character.isLetterOrDigit('$'));

    assertTrue(Character.isWhitespace(' '));
    assertFalse(Character.isWhitespace('a'));

    assertEquals('a', Character.forDigit(10, 16));

    assertEquals(98, Character.hashCode('b'));

    assertEquals(1, Character.charCount(0x9999));
    assertEquals(2, Character.charCount(0x10001));

    char[] cArray1 = {'a', 'b'};
    assertEquals(1, Character.toChars(97, cArray1, 1));
    assertEquals('a', cArray1[1]);

    char[] cArray2 = {'a', 'b'};
    assertEquals(2, Character.toChars(80000, cArray2, 0));

    char[] cArray3 = {'a', 'b'};
    assertEquals(97, Character.codePointAt(cArray3, 0, 1));

    CharSequence charSeq1 = "abcd";
    assertEquals(99, Character.codePointAt(charSeq1, 2));

    assertTrue(Character.isLowSurrogate('\uDD00'));
    assertFalse(Character.isLowSurrogate('\uD000'));

    assertTrue(Character.isHighSurrogate('\uD900'));
    assertFalse(Character.isHighSurrogate('\uDD00'));

    assertEquals(10, Character.digit('a', 15));
    assertEquals(-1, Character.digit('a', 5));

    assertEquals(0x0000FFF0, ((char) 0x0FFF) << 4);
    assertEquals(0x000FFFF0, ((char) 0xFFFF) << 4);
    assertEquals(0x000000FF, ((char) 0x0FFF) >> 4);
    assertEquals(0x00000FFF, ((char) 0xFFFF) >> 4);
    assertEquals(0x000000FF, ((char) 0x0FFF) >>> 4);
    assertEquals(0x00000FFF, ((char) 0xFFFF) >>> 4);

    assertEquals(-65, -'A');
  }

  @Test
  public void testDecode() throws Exception {
    assertEquals(123, Integer.decode("123").intValue());
    assertEquals(123, Integer.decode("+123").intValue());
    assertEquals(-123, Integer.decode("-123").intValue());
    assertEquals(0123, Integer.decode("0123").intValue());
    assertEquals(+0123, Integer.decode("+0123").intValue());
    assertEquals(-0123, Integer.decode("-0123").intValue());
    assertEquals(0x123, Integer.decode("0x123").intValue());
    assertEquals(+0x123, Integer.decode("+0x123").intValue());
    assertEquals(-0x123, Integer.decode("-0x123").intValue());
    assertEquals(0x123, Integer.decode("#123").intValue());
    assertEquals(+0x123, Integer.decode("+#123").intValue());
    assertEquals(-0x123, Integer.decode("-#123").intValue());

    assertEquals((byte) 0100, Byte.decode("0100").byteValue());
    assertEquals((short) 0x1234, Short.decode("0x1234").shortValue());
    assertEquals(0xaaaabbbbccccL, Long.decode("#aaaabbbbcccc").longValue());
  }
}
