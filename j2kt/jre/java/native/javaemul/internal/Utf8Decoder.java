/*
 * Copyright 2026 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package javaemul.internal;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import org.jspecify.annotations.NullMarked;

/**
 * A safe, hybrid UTF-8 CharsetDecoder implementation for J2KT-Native.
 *
 * <p>Instead of implementing a complex low-level multi-byte state machine, this decoder leverages
 * the platform's highly optimized standard String constructor to perform the actual text
 * conversion. It ensures perfect multi-byte character safety across streaming buffer chunk
 * boundaries by only inspecting the very tail of the input buffer to detect and exclude any split
 * multi-byte sequences.
 */
@NullMarked
final class Utf8Decoder extends CharsetDecoder {

  Utf8Decoder() {
    super(EmulatedCharset.UTF_8, 1.0f, 1.0f);
  }

  @Override
  protected CoderResult decodeLoop(ByteBuffer in, CharBuffer out) {
    if (!in.hasRemaining()) {
      return CoderResult.UNDERFLOW;
    }

    int len = in.remaining();
    int safeLen = len;

    /*
     * Safeguard against multi-byte sequences split across buffer chunks:
     *
     * In standard UTF-8 encoding, ASCII characters require 1 byte, while non-ASCII characters
     * require up to 4 bytes. Because this method might receive an arbitrary slice (e.g., the first
     * 1024 bytes of a larger stream), a multi-byte character might start near the very end of the
     * buffer but require continuation bytes that are not yet present.
     *
     * We inspect up to the last 3 bytes in the buffer to see if we hit any multi-byte leading byte
     * headers:
     * - Leading bytes (headers) always have their two highest bits set (i.e., byte value >= 0xC0).
     * - Continuation bytes always have values between 0x80 and 0xBF.
     */
    for (int i = 1; i <= 3 && i <= len; i++) {
      int b = in.get(in.position() + len - i) & 0xFF;

      // If we find a leading byte header, it marks the start of a multi-byte character
      if (b >= 0xC0) {
        int requiredBytes;
        if (b >= 0xF0) {
          requiredBytes = 4; // 4-byte sequence header (11110xxx)
        } else if (b >= 0xE0) {
          requiredBytes = 3; // 3-byte sequence header (1110xxxx)
        } else {
          requiredBytes = 2; // 2-byte sequence header (110xxxxx)
        }

        // If the current buffer ends before we have all continuation bytes needed to form the full
        // character,
        // we exclude this incomplete sequence from the decoding scope so it can be appended safely
        // to the next chunk.
        if (i < requiredBytes) {
          safeLen = len - i;
        }

        // A character sequence can have only one leading byte, so we stop searching.
        break;
      }
    }

    // If the entire available buffer consists of the start of an incomplete character, we return
    // UNDERFLOW immediately.
    if (safeLen == 0) {
      return CoderResult.UNDERFLOW;
    }

    // Read only the safe bytes that form completely formed sequences
    byte[] bytes = new byte[safeLen];
    in.get(bytes);

    // Delegate the actual text conversion to the standard system runtime
    String decoded = new String(bytes, UTF_8);

    // Ensure the output CharBuffer has enough remaining capacity to receive the parsed string
    if (out.remaining() < decoded.length()) {
      in.position(in.position() - safeLen);
      return CoderResult.OVERFLOW;
    }

    out.put(decoded);

    // Inform the reader that all safe bytes were successfully processed
    return CoderResult.UNDERFLOW;
  }
}
