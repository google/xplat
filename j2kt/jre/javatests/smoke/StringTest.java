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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;
import org.jspecify.annotations.Nullable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class StringTest {

  static byte[] ABC = {(byte) 65, (byte) 66, (byte) 67};
  static byte[] AEBC = {(byte) 0xC3, (byte) 0x84, (byte) 66, (byte) 67};
  static byte[] AEBC_ISO = {(byte) 0xC4, (byte) 66, (byte) 67};

  @Test
  public void testStringReader() throws IOException {
    Reader reader = new StringReader("Hello World");
    assertEquals((int) 'H', reader.read());
  }

  @Test
  public void testStringWriter() throws IOException {
    Writer writer = new StringWriter();
    writer.write("Hello World");
    assertEquals("Hello World", writer.toString());
  }

  @Test
  public void testString() {
    char[] cArray = {'h', 'e', 'l', 'l', 'o'};
    assertEquals("ello", new String(cArray, 1, 4));

    assertEquals("hello", String.copyValueOf(cArray));
    assertEquals("ell", String.copyValueOf(cArray, 1, 3));

    assertEquals("a", String.valueOf('a'));
    assertEquals("5", String.valueOf(5));
    assertEquals("null", String.valueOf((@Nullable Object) null));

    assertEquals(0, "A".compareToIgnoreCase("a"));
    assertEquals(-1, "A".compareToIgnoreCase("b"));
    assertEquals(1, "b".compareToIgnoreCase("A"));

    assertEquals("HELLO", "hello".toUpperCase(Locale.US));

    char[] dstCharArray = new char[3];
    "abcde".getChars(1, 4, dstCharArray, 0);
    assertEquals("bcd", new String(dstCharArray));

    assertEquals("cabc", "aaabaa".replace(/* target= */ "aa", /* replacement= */ "c"));
    assertEquals(
        "1<br>2<br>3<br><br>4",
        "1\n2\r\n3\n\n4".replaceAll(/* regex= */ "\r?\n|\r", /* replacement= */ "<br>"));

    String str1 = "hello1hello2hello";
    String[] strArray1 = str1.split("[12]");
    assertEquals(3, strArray1.length);
    assertEquals("hello", strArray1[0]);
    assertEquals("hello", strArray1[1]);
    assertEquals("hello", strArray1[2]);

    assertEquals(str1, new String(str1));
    assertFalse(str1 == new String(str1));

    assertEquals("Hello, World", "Hello".concat(", World"));

    String str2 = "hello1hello2hello";
    String[] strArray2 = str2.split("[12]", 2);
    assertEquals(2, strArray2.length);
    assertEquals("hello", strArray2[0]);
    assertEquals("hello2hello", strArray2[1]);

    assertTrue("ABC".equalsIgnoreCase("abc"));
    assertFalse("ABCD".equalsIgnoreCase("abc"));

    assertTrue("---ABC---".matches(".*ABC.*"));

    assertEquals("wordwordword", "word".repeat(3));
  }

  @Test
  public void testCharsets() throws UnsupportedEncodingException {
    assertEquals("ÄBC", new String(AEBC, "UTF-8"));
    assertEquals("ÄBC", new String(AEBC, StandardCharsets.UTF_8));

    assertEquals("ÄBC", new String(AEBC_ISO, "ISO-8859-1"));
    assertEquals("ÄBC", new String(AEBC_ISO, StandardCharsets.ISO_8859_1));

    assertEquals("ABC", new String(ABC, "ASCII"));
    assertEquals("ABC", new String(ABC, StandardCharsets.US_ASCII));

    assertEquals("Ä", new String(AEBC, 0, 2, "UTF-8"));
    assertEquals("Ä", new String(AEBC, 0, 2, StandardCharsets.UTF_8));

    assertEquals("Ä", new String(AEBC_ISO, 0, 1, "ISO-8859-1"));
    assertEquals("Ä", new String(AEBC_ISO, 0, 1, StandardCharsets.ISO_8859_1));

    assertEquals("A", new String(ABC, 0, 1));
    assertEquals("A", new String(ABC, 0, 1, "ASCII"));
    assertEquals("A", new String(ABC, 0, 1, StandardCharsets.US_ASCII));

    assertEquals("BC", new String(AEBC, 2, 2));
    assertEquals("BC", new String(AEBC, 2, 2, "UTF-8"));
    assertEquals("BC", new String(AEBC, 2, 2, StandardCharsets.UTF_8));

    try {
      String unused = new String(AEBC, "FooBar");
      fail("UnsupportedEncodingException expected");
    } catch (UnsupportedEncodingException e) {
      // This is expected.
    }

    assertArrayEquals(AEBC, "ÄBC".getBytes("UTF-8"));
    assertArrayEquals(AEBC, "ÄBC".getBytes(StandardCharsets.UTF_8));

    assertArrayEquals(AEBC_ISO, "ÄBC".getBytes("ISO-8859-1"));
    assertArrayEquals(AEBC_ISO, "ÄBC".getBytes(StandardCharsets.ISO_8859_1));

    assertArrayEquals(ABC, "ABC".getBytes("ASCII"));
    assertArrayEquals(ABC, "ABC".getBytes(StandardCharsets.US_ASCII));

    assertEquals(-1, "ABCDEABCDE".indexOf('F'));
    assertEquals(-1, "ABCDEABCDE".indexOf(0x1f602));
    assertEquals(-1, "ABCDEABCDE".lastIndexOf('F'));
    assertEquals(-1, "ABCDEABCDE".lastIndexOf(0x1f602));
    assertEquals(1, "ABCDEABCDE".indexOf('B'));
    assertEquals(6, "ABCDEABCDE".indexOf('B', 4));
    assertEquals(-1, "ABCDEABCDE".indexOf('B', 9));
    assertEquals(6, "ABCDEABCDE".lastIndexOf('B'));
    assertEquals(1, "ABCDEABCDE".lastIndexOf('B', 5));
    assertEquals(-1, "ABCDEABCDE".lastIndexOf('B', 0));
    assertEquals(2, "😴😂☕😴😂☕".indexOf(0x1f602));
    assertEquals(7, "😴😂☕😴😂☕".indexOf(0x1f602, 4));
    assertEquals(7, "😴😂☕😴😂☕".lastIndexOf(0x1f602));
    assertEquals(2, "😴😂☕😴😂☕".lastIndexOf(0x1f602, 6));

    // No exception for illegal sequence in constructors.
    assertEquals("\ufffdBC", new String(AEBC_ISO, StandardCharsets.UTF_8));
  }

  @Test
  public void testStringBuilder() {
    StringBuilder strBuilder1 = new StringBuilder("0123");
    char[] cArray = {'h', 'e', 'l', 'l', 'o'};

    strBuilder1.insert(1, cArray, 1, 3);
    assertEquals("0ell123", strBuilder1.toString());
    strBuilder1.insert(6, cArray, 0, 5);
    assertEquals("0ell12hello3", strBuilder1.toString());

    StringBuilder strBuilder2 = new StringBuilder("0123");
    CharSequence charSeq = "hello";

    strBuilder2.insert(1, charSeq, 1, 3);
    assertEquals("0el123", strBuilder2.toString());
    strBuilder2.insert(6, charSeq, 0, 5);
    assertEquals("0el123hello", strBuilder2.toString());

    StringBuilder strBuilder3 = new StringBuilder("H");
    strBuilder3.append(cArray, 1, 4);
    assertEquals("Hello", strBuilder3.toString());
  }

  @Test
  public void testStringBuffer() {
    StringBuffer strBuffer1 = new StringBuffer("0123");
    char[] cArray = {'h', 'e', 'l', 'l', 'o'};

    strBuffer1.insert(1, cArray, 1, 3);
    assertEquals("0ell123", strBuffer1.toString());
    strBuffer1.insert(6, cArray, 0, 5);
    assertEquals("0ell12hello3", strBuffer1.toString());

    StringBuffer strBuffer2 = new StringBuffer("0123");
    CharSequence charSeq = "hello";

    strBuffer2.insert(1, charSeq, 1, 3);
    assertEquals("0el123", strBuffer2.toString());
    strBuffer2.insert(6, charSeq, 0, 5);
    assertEquals("0el123hello", strBuffer2.toString());

    StringBuffer strBuffer3 = new StringBuffer("H");
    strBuffer3.append(cArray, 1, 4);
    assertEquals("Hello", strBuffer3.toString());
  }

  @Test
  public void testCodePointMethods() {
    assertEquals(0xc4, "Ä".codePointAt(0));
    assertEquals(0x1f602, "😂".codePointAt(0));
    assertEquals(0xde02, "😂".codePointAt(1));

    assertEquals(1, Character.charCount(0xc4));
    assertEquals(2, Character.charCount(0x1f602));

    assertArrayEquals(new char[] {0xd83d, 0xde02}, Character.toChars(0x1f602));
    char[] charsOut = new char[3];
    Character.toChars(0x1f602, charsOut, 1);
    assertArrayEquals(new char[] {0, 0xd83d, 0xde02}, charsOut);

    assertEquals(
        "Ä😂", new StringBuilder().appendCodePoint(0xc4).appendCodePoint(0x1f602).toString());

    assertTrue(Character.isValidCodePoint(0));
    assertTrue(Character.isValidCodePoint(0x10ffff));
    assertFalse(Character.isValidCodePoint(0x10ffff + 1));
  }

  @Test
  public void testRegionMatches() {
    assertTrue("HelloWorld".regionMatches(1, "WelloNorth", 1, 4));
    assertTrue("HelloWorld".regionMatches(true, 1, "WELLONORTH", 1, 4));
    assertFalse("HelloWorld".regionMatches(false, 1, "WELLONORTH", 1, 4));
  }

  @Test
  public void testStringFormat() {
    assertEquals("hello, world", String.format("hello, world"));

    assertEquals("hello, world!", String.format("%s", "hello, world!"));

    assertEquals("hello, 1234!", String.format("hello, %d!", 1234));
    assertEquals("hello, 0001!", String.format("hello, %04d!", 1));

    // Default precision is 6
    assertEquals("hello, 123.456700!", String.format("hello, %f!", 123.4567));

    // Test if padding works
    assertEquals("hello, 123.45678900!", String.format("hello, %.8f!", 123.456789));
    assertEquals("hello,     123.4560!", String.format("hello, %12.4f!", 123.456));

    // Test octal and hex format specifier
    assertEquals(
        "123456 to octal = 361100, to hex = 1e240",
        String.format("%1$d to octal = %1$o, to hex = %1$x", 123456));

    // Test if rounding works
    assertEquals("hello, 123.46!", String.format("hello, %.2f!", 123.456789));
    assertEquals("hello, 2.0000!", String.format("hello, %.4f!", 1.99999999));
    assertEquals("hello, 1.23!", String.format("hello, %.2f!", 1.2349999));
    assertEquals("hello, 2!", String.format("hello, %.0f!", 1.50001));
    assertEquals("hello, 1!", String.format("hello, %.0f!", 1.49999));

    // If the '#' flag is given, then the decimal separator will always be present.
    assertEquals("hello, 2.!", String.format("hello, %#.0f!", 1.50001));

    // NaN and Infinity
    assertEquals(
        "NaN, Infinity, and -Infinity", String.format("%f, %f, and %f", 0d / 0, 1d / 0, -1d / 0));

    // Test BigDecimal argument
    assertEquals(
        "hello, 123456.789012!", String.format("hello, %f!", new BigDecimal("123456.789012")));

    // Test argument index
    assertEquals("C A B D B", String.format("%3$s %1$s %2$s %4$s %2$s", "A", "B", "C", "D"));
    assertEquals("C A A B C D", String.format("%3$s %s %<s %s %s %s", "A", "B", "C", "D"));

    // Test uppercase conversion
    assertEquals("ALTERNATING CAPS", String.format("%S", "aLtErNaTiNg cApS"));

    // Test Int to Unicode character conversion
    assertEquals("Face with 😂 Tears of Joy", String.format("Face with %c Tears of Joy", 0x1f602));

    // Make sure null argument works.
    assertEquals("null test!", String.format("%s %s!", null, "test"));
  }

  @Test
  public void testStringJoin() {
    // Test varargs
    assertEquals("Grace Kelly", String.join(" ", "Grace", "Kelly"));
    // Single parameter
    assertEquals("Mika", String.join("performed by ", "Mika"));
    // Many parameters
    assertEquals(
        "I can be brown I can be blue I can be violet sky",
        String.join("I can be ", "", "brown ", "blue ", "violet sky"));

    // Test iterable
    assertEquals(
        "I can be hurtful I can be purple I can be anything you like",
        String.join("I can be ", Arrays.asList("", "hurtful ", "purple ", "anything you like")));
    // Empty parameters
    assertEquals("", String.join("I've gone identity", Arrays.asList()));
    // Empty delimiter
    assertEquals("maaaaaaaad", String.join("", Arrays.asList("ma", "a", "aa", "aaa", "ad")));
  }

  @Test
  public void testStringSplit() {
    String[] parts = "one cat two cats in the yard".split("c[aou]t", -1);
    assertEquals(3, parts.length);
    assertEquals("one ", parts[0]);
    assertEquals(" two ", parts[1]);
    assertEquals("s in the yard", parts[2]);

    parts = "one cat two cats in the yard".split("c[aou]t", 2);
    assertEquals(2, parts.length);
    assertEquals("one ", parts[0]);
    assertEquals(" two cats in the yard", parts[1]);
  }

  @Test
  public void testUppercaseLowercase_withLocale() {
    assertEquals("NICE", "nice".toUpperCase(Locale.ROOT));
    assertEquals("nice", "NICE".toLowerCase(Locale.ROOT));
    assertEquals("NİCE", "nice".toUpperCase(Locale.forLanguageTag("tr")));
    assertEquals("nıce", "NICE".toLowerCase(Locale.forLanguageTag("tr")));
  }
}
