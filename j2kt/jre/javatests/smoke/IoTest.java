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

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.PushbackInputStream;
import java.io.SequenceInputStream;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.Random;
import org.junit.Test;

public class IoTest {

  @Test
  public void testStringReader() throws IOException {
    StringBuilder sb = new StringBuilder();
    for (char c = 0; c < 4096; c++) {
      sb.append(c);
    }
    String s = sb.toString();
    StringReader reader = new StringReader(s);
    reader.skip(2048);
    assertEquals(2048, reader.read());
    reader.close();
  }

  @Test
  public void testInputStreamReader() throws Exception {
    ByteArrayInputStream is = new ByteArrayInputStream("Hello, World".getBytes(UTF_8));
    InputStreamReader reader = new InputStreamReader(is, UTF_8);
    char[] buf = new char[128];
    int count = reader.read(buf);
    assertEquals(12, count);
    assertEquals("Hello, World", String.valueOf(buf, 0, count));
  }

  @Test
  public void testInputStreamReaderLargeStreamWithUnicodeTerminating() throws Exception {
    StringBuilder expected = new StringBuilder();
    for (int i = 0; i < 2000; i++) {
      expected.append("Line ").append(i).append(": some unicode data \u20ac\n");
    }
    byte[] utf8Bytes = expected.toString().getBytes(UTF_8);
    assertTrue(utf8Bytes.length > 8192);
    ByteArrayInputStream is = new ByteArrayInputStream(utf8Bytes);
    InputStreamReader reader = new InputStreamReader(is, UTF_8);

    StringBuilder actual = new StringBuilder();
    char[] buf = new char[512];
    int read;
    while ((read = reader.read(buf, 0, buf.length)) != -1) {
      actual.append(buf, 0, read);
    }
    assertEquals(expected.length(), actual.length());
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testInputStreamReaderChunkedStreamWithUnicode() throws Exception {
    StringBuilder expected = new StringBuilder();
    for (int i = 0; i < 5000; i++) {
      expected.append("Chunk ").append(i).append(" test \u00e9\u00e0\u00f4\n");
    }
    byte[] utf8Bytes = expected.toString().getBytes(UTF_8);

    // An InputStream that returns at most 37 bytes per read to force many buffer refills
    InputStream chunkedIs =
        new InputStream() {
          private int pos = 0;

          @Override
          public int read() {
            if (pos >= utf8Bytes.length) {
              return -1;
            }
            return utf8Bytes[pos++] & 0xFF;
          }

          @Override
          public int read(byte[] b, int off, int len) {
            if (pos >= utf8Bytes.length) {
              return -1;
            }
            int toRead = Math.min(len, Math.min(37, utf8Bytes.length - pos));
            System.arraycopy(utf8Bytes, pos, b, off, toRead);
            pos += toRead;
            return toRead;
          }
        };

    InputStreamReader reader = new InputStreamReader(chunkedIs, UTF_8);
    StringBuilder actual = new StringBuilder();
    char[] buf = new char[100];
    int read;
    while ((read = reader.read(buf, 0, buf.length)) != -1) {
      actual.append(buf, 0, read);
    }
    assertEquals(expected.toString(), actual.toString());
  }

  /** Tests exact multiples of the 8192-byte internal buffer size with small-buffer reads. */
  @Test
  public void testInputStreamReader_exactBufferBoundaries_drainsAndRefillsWithoutStalling()
      throws Exception {
    int bufferSize = 8192;
    int totalBytes = bufferSize * 3; // 24,576 bytes (exact 3x multiple)
    byte[] testData = new byte[totalBytes];
    for (int i = 0; i < totalBytes; i++) {
      testData[i] = (byte) ('a' + (i % 26));
    }
    ByteArrayInputStream is = new ByteArrayInputStream(testData);
    InputStreamReader reader = new InputStreamReader(is, UTF_8);

    StringBuilder actual = new StringBuilder();
    char[] buf = new char[7]; // Odd prime size to force crossing all buffer boundaries
    int read;
    while ((read = reader.read(buf, 0, buf.length)) != -1) {
      actual.append(buf, 0, read);
    }
    assertEquals(totalBytes, actual.length());
    assertEquals(new String(testData, UTF_8), actual.toString());
  }

  /**
   * Tests single-character read() calls on multi-byte UTF-8 sequences and surrogate pairs (emojis).
   */
  @Test
  public void testInputStreamReader_singleCharacterReads_withSurrogatePairsAndMultibyteChars()
      throws Exception {
    String testString = "Hello 🚀 World! 🌍 \u20ac \u00e9\u00e8\u00e0 \ud83d\ude00 End";
    byte[] utf8Bytes = testString.getBytes(UTF_8);
    ByteArrayInputStream is = new ByteArrayInputStream(utf8Bytes);
    InputStreamReader reader = new InputStreamReader(is, UTF_8);

    StringBuilder actual = new StringBuilder();
    int ch;
    while ((ch = reader.read()) != -1) {
      actual.append((char) ch);
    }
    assertEquals(testString, actual.toString());
  }

  /**
   * Tests that reading with a 1-char buffer across surrogate pairs (emojis) correctly preserves
   * both halves of the surrogate pair without corrupting or skipping.
   */
  @Test
  public void testInputStreamReader_charBufferOfLengthOne_withSurrogatePairs() throws Exception {
    String emojiString = "\ud83d\ude80\ud83d\ude0a\ud83c\udf89";
    byte[] utf8Bytes = emojiString.getBytes(UTF_8);
    ByteArrayInputStream is = new ByteArrayInputStream(utf8Bytes);
    InputStreamReader reader = new InputStreamReader(is, UTF_8);

    StringBuilder actual = new StringBuilder();
    char[] buf = new char[1];
    int read;
    while ((read = reader.read(buf, 0, 1)) != -1) {
      actual.append(buf[0]);
    }
    assertEquals(emojiString, actual.toString());
  }

  @Test
  public void testInputStreamReaderSplitMultiByte() throws Exception {
    // 4-byte emoji 🚀 (\uD83D\uDE80) repeated across 1-byte deliveries
    String emojiString = "\ud83d\ude80\ud83d\ude80\ud83d\ude80";
    byte[] bytes = emojiString.getBytes(UTF_8);

    InputStream oneByteStream =
        new InputStream() {
          private int pos = 0;

          @Override
          public int read() {
            if (pos >= bytes.length) {
              return -1;
            }
            return bytes[pos++] & 0xFF;
          }

          @Override
          public int read(byte[] b, int off, int len) {
            if (pos >= bytes.length) {
              return -1;
            }
            b[off] = bytes[pos++];
            return 1;
          }
        };

    InputStreamReader reader = new InputStreamReader(oneByteStream, UTF_8);
    StringBuilder actual = new StringBuilder();
    char[] buf = new char[2];
    int read;
    while ((read = reader.read(buf, 0, buf.length)) != -1) {
      actual.append(buf, 0, read);
    }
    assertEquals(emojiString, actual.toString());
  }

  /**
   * Tests mixed read operations (single-char read(), 1-char buffer read, and multi-char buffer
   * reads) over a stream containing surrogate pairs to ensure residual character buffering works
   * consistently across different read methods.
   */
  @Test
  public void testInputStreamReader_mixedReadSizes_withSurrogatePairs() throws Exception {
    String text = "A \ud83d\ude80 BC \ud83d\ude00 DEF \ud83c\udf89 GHI";
    byte[] bytes = text.getBytes(UTF_8);
    ByteArrayInputStream is = new ByteArrayInputStream(bytes);
    InputStreamReader reader = new InputStreamReader(is, UTF_8);

    StringBuilder actual = new StringBuilder();
    // 1. Single character read()
    int c = reader.read();
    assertEquals('A', c);
    actual.append((char) c);

    // 2. read(buf, 0, 1) for space
    char[] buf1 = new char[1];
    assertEquals(1, reader.read(buf1, 0, 1));
    actual.append(buf1[0]);

    // 3. read(buf, 0, 1) for high surrogate of rocket emoji
    assertEquals(1, reader.read(buf1, 0, 1));
    actual.append(buf1[0]);

    // 4. read() for low surrogate of rocket emoji (from residual)
    c = reader.read();
    actual.append((char) c);

    // 5. Read remaining with 3-char buffer
    char[] buf3 = new char[3];
    int read;
    while ((read = reader.read(buf3, 0, buf3.length)) != -1) {
      actual.append(buf3, 0, read);
    }
    assertEquals(text, actual.toString());
  }

  /** Tests ready() method behavior when residual is buffered. */
  @Test
  public void testInputStreamReader_ready_reflectsResidualAndBufferedState() throws Exception {
    String emojiString = "\ud83d\ude80"; // 🚀 high surrogate + low surrogate
    ByteArrayInputStream is = new ByteArrayInputStream(emojiString.getBytes(UTF_8));
    InputStreamReader reader = new InputStreamReader(is, UTF_8);

    assertTrue(reader.ready());
    // Read 1 char into 1-slot buffer -> consumes high surrogate and leaves low surrogate in
    // residual
    char[] buf = new char[1];
    assertEquals(1, reader.read(buf, 0, 1));
    assertEquals(emojiString.charAt(0), buf[0]);

    // reader.ready() must return true because residual is available
    assertTrue(reader.ready());

    // Read low surrogate
    assertEquals(1, reader.read(buf, 0, 1));
    assertEquals(emojiString.charAt(1), buf[0]);
  }

  @Test
  public void testUsAsciiDecoder() throws Exception {
    ByteArrayInputStream is = new ByteArrayInputStream("Hello ASCII".getBytes(US_ASCII));
    InputStreamReader reader = new InputStreamReader(is, US_ASCII);
    char[] buf = new char[64];
    int count = reader.read(buf);
    assertEquals(11, count);
    assertEquals("Hello ASCII", String.valueOf(buf, 0, count));
  }

  @Test
  public void testIso88591Decoder() throws Exception {
    byte[] latin1Bytes = new byte[] {'H', 'i', ' ', (byte) 0xC4};
    ByteArrayInputStream is = new ByteArrayInputStream(latin1Bytes);
    InputStreamReader reader = new InputStreamReader(is, ISO_8859_1);
    char[] buf = new char[64];
    int count = reader.read(buf);
    assertEquals(4, count);
    assertEquals("Hi Ä", String.valueOf(buf, 0, count));
  }

  @Test
  public void testPrintStream() {
    {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      PrintStream ps = new PrintStream(os);
      ps.print("hello, ");
      ps.append("world");
      ps.flush();
      assertEquals("hello, world", os.toString());
    }
    {
      // Test usage with Formatter
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      PrintStream ps = new PrintStream(os);
      Formatter fmt = new Formatter(ps);
      fmt.format("hello, %d world", 1234);
      ps.flush();
      assertEquals("hello, 1234 world", os.toString());
    }
  }

  @Test
  public void testPrintWriter() {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    PrintWriter writer = new PrintWriter(os);
    writer.printf("%d %s", 1234, null);
    writer.flush();
    assertEquals("1234 null", os.toString());
  }



  @Test
  public void testFileStreams() throws IOException {
    File testFile = new File(System.getProperty("java.io.tmpdir") + "/" + new Random().nextInt());

    testFile.createNewFile();

    try (FileInputStream fileInputStream = new FileInputStream(testFile)) {
      assertEquals(-1, fileInputStream.read());
    }

    try (FileOutputStream fileOutputStream = new FileOutputStream(testFile)) {
      fileOutputStream.write(42);
    }

    try (FileInputStream fileInputStream = new FileInputStream(testFile)) {
      assertEquals(42, fileInputStream.read());
      assertEquals(-1, fileInputStream.read());
    }

    testFile.delete();

    try (FileOutputStream fileOutputStream = new FileOutputStream(testFile)) {
      fileOutputStream.write(42);
    }

    try (FileInputStream fileInputStream = new FileInputStream(testFile)) {
      assertEquals(42, fileInputStream.read());
      assertEquals(-1, fileInputStream.read());
    }

    // Overwrite
    try (FileOutputStream fileOutputStream = new FileOutputStream(testFile)) {
      fileOutputStream.write(52);
    }

    try (FileInputStream fileInputStream = new FileInputStream(testFile)) {
      assertEquals(52, fileInputStream.read());
      assertEquals(-1, fileInputStream.read());
    }

    // Append
    try (FileOutputStream fileOutputStream = new FileOutputStream(testFile, true)) {
      fileOutputStream.write(53);
    }

    try (FileInputStream fileInputStream = new FileInputStream(testFile)) {
      assertEquals(52, fileInputStream.read());
      assertEquals(53, fileInputStream.read());
      assertEquals(-1, fileInputStream.read());
    }
  }

  @Test
  public void testFileStreamsWithByteArrays() throws IOException {
    File testFile = new File(System.getProperty("java.io.tmpdir") + "/" + new Random().nextInt());

    testFile.createNewFile();

    byte[] data = new byte[] {0, 1, 2, 3};

    try (FileOutputStream fileOutputStream = new FileOutputStream(testFile)) {
      fileOutputStream.write(data);
      fileOutputStream.write(data, 1, 2);
    }

    byte[] result = new byte[6];
    try (FileInputStream fileInputStream = new FileInputStream(testFile)) {
      int pos = 0;
      while (pos < 6) {
        pos += fileInputStream.read(result, pos, result.length - pos);
      }
    }

    assertEquals(0, result[0]);
    assertEquals(1, result[1]);
    assertEquals(2, result[2]);
    assertEquals(3, result[3]);
    assertEquals(1, result[4]);
    assertEquals(2, result[5]);

    try (FileInputStream fileInputStream = new FileInputStream(testFile)) {
      result = fileInputStream.readAllBytes();
    }
    assertEquals(6, result.length);
    assertEquals(0, result[0]);
    assertEquals(2, result[5]);
  }

  @Test
  public void testBufferedInputStream() throws IOException {
    ByteArrayInputStream is = new ByteArrayInputStream(new byte[] {1, 2, 3, 4, 5, 6, 7, 8});
    BufferedInputStream bis = new BufferedInputStream(is);
    assertEquals(8, is.available());
    assertEquals(8, bis.available());

    byte[] buf;

    buf = new byte[4];
    int size = bis.read(buf, 0, 2);
    assertEquals(2, size);
    assertArrayEquals(new byte[] {1, 2, 0, 0}, buf);
    assertEquals(0, is.available());
    assertEquals(6, bis.available());

    buf = new byte[4];
    size = bis.read(buf);
    assertEquals(4, size);
    assertArrayEquals(new byte[] {3, 4, 5, 6}, buf);
    assertEquals(0, is.available());
    assertEquals(2, bis.available());

    buf = new byte[4];
    size = bis.read(buf);
    assertEquals(2, size);
    assertArrayEquals(new byte[] {7, 8, 0, 0}, buf);
    assertEquals(0, is.available());
    assertEquals(0, bis.available());

    size = bis.read(buf);
    assertEquals(-1, size);
  }

  @Test
  public void testBufferedOutputStream() throws IOException {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    BufferedOutputStream bos = new BufferedOutputStream(os, 4);

    bos.write(1);
    bos.write(new byte[] {2, 3});
    assertEquals(0, os.size()); // Should be buffered

    bos.write(4);
    assertEquals(0, os.size()); // Should still be buffered (buffer is now full)

    bos.write(5);
    assertEquals(
        4, os.size()); // bos.write(5) triggers flush of the first 4 bytes, then buffers '5'
    assertArrayEquals(
        new byte[] {1, 2, 3, 4}, os.toByteArray().length == 4 ? os.toByteArray() : new byte[4]);
    assertEquals(4, os.toByteArray().length);

    bos.write(
        new byte[] {
          6, 7, 8, 9, 10
        }); // len(5) >= buf.length(4), flushes '5' then writes 5 bytes directly
    assertEquals(10, os.size());
    assertArrayEquals(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, os.toByteArray());
  }

  @Test
  public void testBufferedOutputStream_writeWithOffsetAndLen() throws IOException {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    BufferedOutputStream bos = new BufferedOutputStream(os, 10);

    byte[] data = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    bos.write(data, 2, 5); // Should buffer {2, 3, 4, 5, 6}
    assertEquals(0, os.size());

    bos.write(
        data, 7, 3); // Should buffer {7, 8, 9}, buffer is now {2, 3, 4, 5, 6, 7, 8, 9} (count=8)
    assertEquals(0, os.size());

    bos.write(data, 0, 3); // len(3) > 10 - 8 (2). Should flushBuffer() then buffer {0, 1, 2}
    assertEquals(8, os.size());
    assertArrayEquals(new byte[] {2, 3, 4, 5, 6, 7, 8, 9}, os.toByteArray());

    bos.flush();
    assertEquals(11, os.size());
    assertArrayEquals(new byte[] {2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2}, os.toByteArray());
  }

  @Test
  public void testSequenceInputStream() throws IOException {
    ByteArrayInputStream is1 = new ByteArrayInputStream(new byte[] {1, 2, 3});
    ByteArrayInputStream is2 = new ByteArrayInputStream(new byte[] {4, 5});
    ByteArrayInputStream is3 = new ByteArrayInputStream(new byte[] {6});

    Enumeration<? extends InputStream> e =
        Collections.enumeration(java.util.Arrays.asList(is1, is2, is3));
    try (SequenceInputStream sis = new SequenceInputStream(e)) {
      byte[] result = sis.readAllBytes();
      assertArrayEquals(new byte[] {1, 2, 3, 4, 5, 6}, result);
    }
  }

  @Test
  public void testPushbackInputStream() throws IOException {
    byte[] data = new byte[] {1, 2, 3, 4, 5};
    ByteArrayInputStream is = new ByteArrayInputStream(data);
    try (PushbackInputStream pis = new PushbackInputStream(is, 5)) {
      assertEquals(1, pis.read());
      pis.unread(1);
      assertEquals(1, pis.read());

      pis.unread(new byte[] {10, 11});
      assertEquals(10, pis.read());
      assertEquals(11, pis.read());
      assertEquals(2, pis.read());

      byte[] buf = new byte[2];
      pis.unread(new byte[] {20, 21, 22}, 1, 2); // 21, 22
      assertEquals(2, pis.read(buf));
      assertArrayEquals(new byte[] {21, 22}, buf);

      assertEquals(3, pis.available());

      assertEquals(1, pis.skip(1)); // Skips 3
      assertEquals(4, pis.read());
    }
  }

  @Test
  public void testStreamTokenizer() throws IOException {
    String input = "hello Greetings 123 world 0.456 ! \'walking on the moon\'";
    StreamTokenizer st = new StreamTokenizer(new StringReader(input));

    assertEquals(StreamTokenizer.TT_WORD, st.nextToken());
    assertEquals("hello", st.sval);
    assertEquals(StreamTokenizer.TT_WORD, st.nextToken());
    assertEquals("Greetings", st.sval);
    assertEquals(StreamTokenizer.TT_NUMBER, st.nextToken());
    assertEquals(123.0, st.nval, 0.1);
    assertEquals(StreamTokenizer.TT_WORD, st.nextToken());
    assertEquals("world", st.sval);
    assertEquals(StreamTokenizer.TT_NUMBER, st.nextToken());
    assertEquals(0.456, st.nval, 0.1);
    assertEquals('!', st.nextToken());
    assertEquals('\'', st.nextToken());
    assertEquals("walking on the moon", st.sval);
    assertEquals(StreamTokenizer.TT_EOF, st.nextToken());
  }
}
