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

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class Utf8DecoderTest {

  @Test
  public void testUtf8Decoder_splitMultiByteCharacter() throws Exception {
    CharsetDecoder decoder = UTF_8.newDecoder();

    // The character 'Ä' is encoded in UTF-8 as two bytes: 0xC3 0x84
    // Create a ByteBuffer that contains only the FIRST byte (0xC3), simulating a multi-byte split
    ByteBuffer in = ByteBuffer.allocate(1);
    in.put((byte) 0xC3);
    in.flip();

    CharBuffer out = CharBuffer.allocate(16);

    // Since the sequence is incomplete, the decoder should detect it and report UNDERFLOW safely
    CoderResult result = decoder.decode(in, out, false);

    assertEquals(CoderResult.UNDERFLOW, result);
    assertEquals(0, out.position()); // No characters should be emitted yet
    assertEquals(1, in.remaining()); // The split byte must remain unread in the input buffer

    // Now we provide the continuation byte (0x84)
    ByteBuffer in2 = ByteBuffer.allocate(2);
    in2.put((byte) 0xC3); // Carry over the unread byte
    in2.put((byte) 0x84); // Append the continuation byte
    in2.flip();

    result = decoder.decode(in2, out, true);

    assertEquals(CoderResult.UNDERFLOW, result);
    assertEquals(1, out.position()); // Now the full character 'Ä' must be successfully decoded
    assertEquals('Ä', out.get(0));
  }

  @Test
  public void testUtf8Decoder_overflowHandling() throws Exception {
    CharsetDecoder decoder = UTF_8.newDecoder();

    // Create input bytes containing "Hello" (5 characters)
    ByteBuffer in = ByteBuffer.wrap("Hello".getBytes(UTF_8));

    // Allocate an extremely small output buffer that can hold only 2 characters
    CharBuffer out = CharBuffer.allocate(2);

    // Attempt to decode
    CoderResult result = decoder.decode(in, out, false);

    // The decoder must report OVERFLOW because the destination buffer doesn't have capacity
    assertEquals(CoderResult.OVERFLOW, result);

    // Make sure exactly 2 characters were successfully placed in the output buffer
    assertEquals(2, out.position());
    assertEquals('H', out.get(0));
    assertEquals('e', out.get(1));

    // The remaining 3 bytes must remain unread in the source ByteBuffer
    assertEquals(3, in.remaining());
  }

  @Test
  public void testUtf8Decoder_split3ByteCharacter() throws Exception {
    CharsetDecoder decoder = UTF_8.newDecoder();

    // The character '日' (Japanese kanji for day/sun) is encoded in UTF-8 as three bytes: 0xE6 0x97
    // 0xA5
    // We simulate reading only the first 2 bytes
    ByteBuffer in = ByteBuffer.allocate(2);
    in.put((byte) 0xE6);
    in.put((byte) 0x97);
    in.flip();

    CharBuffer out = CharBuffer.allocate(16);

    CoderResult result = decoder.decode(in, out, false);

    // Must report UNDERFLOW and leave the 2 split bytes unread safely
    assertEquals(CoderResult.UNDERFLOW, result);
    assertEquals(0, out.position());
    assertEquals(2, in.remaining());

    // Provide the full 3-byte sequence
    ByteBuffer in2 = ByteBuffer.allocate(3);
    in2.put((byte) 0xE6);
    in2.put((byte) 0x97);
    in2.put((byte) 0xA5);
    in2.flip();

    result = decoder.decode(in2, out, true);

    assertEquals(CoderResult.UNDERFLOW, result);
    assertEquals(1, out.position());
    assertEquals('日', out.get(0));
  }

  @Test
  public void testUtf8Decoder_split4ByteCharacter() throws Exception {
    CharsetDecoder decoder = UTF_8.newDecoder();

    // The emoji '😀' (grinning face) is encoded in UTF-8 as four bytes: 0xF0 0x9F 0x98 0x80
    // We simulate reading only the first 3 bytes
    ByteBuffer in = ByteBuffer.allocate(3);
    in.put((byte) 0xF0);
    in.put((byte) 0x9F);
    in.put((byte) 0x98);
    in.flip();

    CharBuffer out = CharBuffer.allocate(16);

    CoderResult result = decoder.decode(in, out, false);

    // Must report UNDERFLOW and leave all 3 split bytes unread safely
    assertEquals(CoderResult.UNDERFLOW, result);
    assertEquals(0, out.position());
    assertEquals(3, in.remaining());

    // Provide the full 4-byte sequence
    ByteBuffer in2 = ByteBuffer.allocate(4);
    in2.put((byte) 0xF0);
    in2.put((byte) 0x9F);
    in2.put((byte) 0x98);
    in2.put((byte) 0x80);
    in2.flip();

    result = decoder.decode(in2, out, true);

    assertEquals(CoderResult.UNDERFLOW, result);

    // Since '😀' is beyond the Basic Multilingual Plane, Java represents it as a surrogate pair (2
    // chars)
    assertEquals(2, out.position());
    assertEquals("😀", out.flip().toString());
  }
}
