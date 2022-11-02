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
import static smoke.Asserts.fail;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import javaemul.internal.EmulatedCharset;

public class StringTest {

  static byte[] ABC = {(byte) 65, (byte) 66, (byte) 67};
  static byte[] AEBC = {(byte) 0xC3, (byte) 0x84, (byte) 66, (byte) 67};
  static byte[] AEBC_ISO = {(byte) 0xC4, (byte) 66, (byte) 67};

  static void testStrings() throws Exception {
    testCharsets();
    testJavaEmul();
    testString();
    testStringBuilder();
    testCodePointMethods();
  }

  // TODO(b/236003566): Test OutputStreamWriter instead after cl/438505991 is submitted.
  private static void testJavaEmul() {
    assertEquals(AEBC, EmulatedCharset.UTF_8.getBytes(new char[] {'Ä', 'B', 'C'}, 0, 3));
  }

  private static void testString() {
    char[] cArray = {'h', 'e', 'l', 'l', 'o'};
    assertEquals("ello", new String(cArray, 1, 4));

    assertEquals("hello", String.copyValueOf(cArray));
    assertEquals("ell", String.copyValueOf(cArray, 1, 3));

    assertEquals("a", String.valueOf('a'));
    assertEquals("5", String.valueOf(5));
    assertEquals("null", String.valueOf((Object) null));

    assertEquals(0, "A".compareToIgnoreCase("a"));
    assertEquals(-1, "A".compareToIgnoreCase("b"));
    assertEquals(1, "b".compareToIgnoreCase("A"));

    assertEquals("HELLO", "hello".toUpperCase(Locale.US));

    char dstCharArray[] = new char[3];
    "abcde".getChars(1, 4, dstCharArray, 0);
    assertEquals("bcd", new String(dstCharArray));

    CharSequence target = "aa", replacement = "c";
    String targetStr = "aaabaa";
    assertEquals("cabc", targetStr.replace(target, replacement));

    String str1 = "hello1hello2hello";
    String[] strArray1 = str1.split("[12]");
    assertEquals(3, strArray1.length);
    assertEquals("hello", strArray1[0]);
    assertEquals("hello", strArray1[1]);
    assertEquals("hello", strArray1[2]);

    assertEquals(str1, new String(str1));
    assertFalse(str1 == new String(str1));

    String str2 = "hello1hello2hello";
    String[] strArray2 = str2.split("[12]", 2);
    assertEquals(2, strArray2.length);
    assertEquals("hello", strArray2[0]);
    assertEquals("hello2hello", strArray2[1]);

    assertTrue("ABC".equalsIgnoreCase("abc"));
    assertFalse("ABCD".equalsIgnoreCase("abc"));

    assertTrue("---ABC---".matches(".*ABC.*"));
  }

  private static void testCharsets() throws UnsupportedEncodingException {
    assertEquals("ÄBC", new String(AEBC));
    assertEquals("ÄBC", new String(AEBC, "UTF-8"));
    assertEquals("ÄBC", new String(AEBC, StandardCharsets.UTF_8));

    assertEquals("ÄBC", new String(AEBC_ISO, "ISO-8859-1"));
    assertEquals("ÄBC", new String(AEBC_ISO, StandardCharsets.ISO_8859_1));

    assertEquals("ABC", new String(ABC, "ASCII"));
    assertEquals("ABC", new String(ABC, StandardCharsets.US_ASCII));

    assertEquals("Ä", new String(AEBC, 0, 2));
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

    assertEquals(AEBC, "ÄBC".getBytes());
    assertEquals(AEBC, "ÄBC".getBytes("UTF-8"));
    assertEquals(AEBC, "ÄBC".getBytes(StandardCharsets.UTF_8));

    assertEquals(AEBC_ISO, "ÄBC".getBytes("ISO-8859-1"));
    assertEquals(AEBC_ISO, "ÄBC".getBytes(StandardCharsets.ISO_8859_1));

    assertEquals(ABC, "ABC".getBytes("ASCII"));
    assertEquals(ABC, "ABC".getBytes(StandardCharsets.US_ASCII));

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

  private static void testStringBuilder() {
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

  private static void testCodePointMethods() {
    assertEquals(0xc4, "Ä".codePointAt(0));
    assertEquals(0x1f602, "😂".codePointAt(0));
    assertEquals(0xde02, "😂".codePointAt(1));

    assertEquals(1, Character.charCount(0xc4));
    assertEquals(2, Character.charCount(0x1f602));

    assertEquals(new char[] {0xd83d, 0xde02}, Character.toChars(0x1f602));
    char[] charsOut = new char[3];
    Character.toChars(0x1f602, charsOut, 1);
    assertEquals(new char[] {0, 0xd83d, 0xde02}, charsOut);

    assertEquals(
        "Ä😂", new StringBuilder().appendCodePoint(0xc4).appendCodePoint(0x1f602).toString());
  }
}
