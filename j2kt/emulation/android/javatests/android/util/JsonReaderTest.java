/*
 * Copyright (C) 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import org.jspecify.annotations.NullMarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link JsonReader} and {@link JsonWriter}. */
@NullMarked
@RunWith(JUnit4.class)
public class JsonReaderTest {

  @Test
  public void testJsonReader_peekBareIntegerAtEof() throws IOException {
    JsonReader reader = new JsonReader("123");
    reader.setLenient(true);
    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(123, reader.nextInt());
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  @Test
  public void testJsonReader_peekBareBooleanAtEof() throws IOException {
    JsonReader trueReader = new JsonReader("true");
    trueReader.setLenient(true);
    assertEquals(JsonToken.BOOLEAN, trueReader.peek());
    assertTrue(trueReader.nextBoolean());
    assertEquals(JsonToken.END_DOCUMENT, trueReader.peek());

    JsonReader falseReader = new JsonReader("false");
    falseReader.setLenient(true);
    assertEquals(JsonToken.BOOLEAN, falseReader.peek());
    assertFalse(falseReader.nextBoolean());
    assertEquals(JsonToken.END_DOCUMENT, falseReader.peek());
  }

  @Test
  public void testJsonReader_peekBareNullAtEof() throws IOException {
    JsonReader reader = new JsonReader("null");
    reader.setLenient(true);
    assertEquals(JsonToken.NULL, reader.peek());
    reader.nextNull();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  @Test
  public void testJsonReader_peekBareUnquotedStringAtEof() throws IOException {
    JsonReader reader = new JsonReader("unquoted");
    reader.setLenient(true);
    assertEquals(JsonToken.STRING, reader.peek());
    assertEquals("unquoted", reader.nextString());
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());

    JsonReader minusReader = new JsonReader("-");
    minusReader.setLenient(true);
    assertEquals(JsonToken.STRING, minusReader.peek());
    assertEquals("-", minusReader.nextString());
    assertEquals(JsonToken.END_DOCUMENT, minusReader.peek());
  }

  @Test
  public void testJsonReader_numberBoundariesAndConversions() throws IOException {
    String json =
        "[0,-0,2147483647,-2147483648,2147483648,9223372036854775807,-9223372036854775808,"
            + "9223372036854775808,-9223372036854775809,10000000000000000000,10.0,10.5,42]";
    for (JsonReader reader :
        new JsonReader[] {new JsonReader(json), new JsonReader(new StringReader(json))}) {
      reader.beginArray();
      assertEquals(0, reader.nextInt());
      // -0 must not take the integer fast path so negative zero (-0.0) is preserved
      assertEquals(0, Double.compare(-0.0, reader.nextDouble()));
      assertEquals(Integer.MAX_VALUE, reader.nextInt());
      assertEquals(Integer.MIN_VALUE, reader.nextInt());
      // 2147483648 overflows int, but succeeds if read as long after failed nextInt()
      assertThrows(NumberFormatException.class, reader::nextInt);
      assertEquals(2147483648L, reader.nextLong());
      assertEquals(Long.MAX_VALUE, reader.nextLong());
      assertEquals(Long.MIN_VALUE, reader.nextLong());
      // 9223372036854775808 (+2^63) overflows signed 64-bit long (accumulated == Long.MIN_VALUE
      // with isNegative == false); verify it does not wrap to negative Long.MIN_VALUE.
      assertEquals(9.223372036854776E18, reader.nextDouble(), 0.0);
      // -9223372036854775809 (-2^63 - 1) overflows signed 64-bit long
      assertEquals(-9.223372036854776E18, reader.nextDouble(), 0.0);
      // 10000000000000000000 (10^19) overflows both Long.parseLong and double-to-long fallback
      assertThrows(NumberFormatException.class, reader::nextLong);
      assertEquals("10000000000000000000", reader.nextString());
      // 10.0 is an exact integer via double fallback
      assertEquals(10, reader.nextInt());
      // 10.5 is not an exact integer
      assertThrows(NumberFormatException.class, reader::nextInt);
      assertEquals(10.5, reader.nextDouble(), 0.0);
      // Reading an unquoted integer via nextString() lazily materializes the string
      assertEquals(JsonToken.NUMBER, reader.peek());
      assertEquals("42", reader.nextString());
      reader.endArray();
      assertEquals(JsonToken.END_DOCUMENT, reader.peek());
      reader.close();
    }
  }

  @Test
  public void testJsonReader_bomAndLenientComments() throws IOException {
    String json = "\ufeff// line comment\n# hash comment\n/* block comment */[1, 2]";
    for (JsonReader reader :
        new JsonReader[] {new JsonReader(json), new JsonReader(new StringReader(json))}) {
      reader.setLenient(true);
      reader.beginArray();
      assertEquals(1, reader.nextInt());
      reader.skipValue();
      reader.endArray();
      assertEquals(JsonToken.END_DOCUMENT, reader.peek());
      reader.close();
    }
  }

  @Test
  public void testJsonWriter_escapedStringSpans() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter writer = new JsonWriter(stringWriter);
    writer.beginArray();
    writer.value("");
    writer.value("\"quoted\"");
    writer.value("\\backslash\\");
    writer.value("\u0000\u001f\t\b\n\r\f\u2028\u2029");
    writer.endArray();
    writer.close();

    JsonReader reader = new JsonReader(stringWriter.toString());
    reader.beginArray();
    assertEquals("", reader.nextString());
    assertEquals("\"quoted\"", reader.nextString());
    assertEquals("\\backslash\\", reader.nextString());
    assertEquals("\u0000\u001f\t\b\n\r\f\u2028\u2029", reader.nextString());
    reader.endArray();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }
}
