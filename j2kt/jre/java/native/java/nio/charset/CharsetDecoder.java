/* Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package java.nio.charset;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * A converter that can convert a byte sequence from a charset into a 16-bit Unicode character
 * sequence.
 */
public abstract class CharsetDecoder {

  private static final int INIT = 0;

  private static final int ONGOING = 1;

  private static final int END = 2;

  private static final int FLUSH = 3;

  private float averChars;

  private float maxChars;

  private Charset cs;

  private CodingErrorAction malformAction;

  private CodingErrorAction unmapAction;

  private String replace;

  private int status;

  protected CharsetDecoder(Charset charset, float averageCharsPerByte, float maxCharsPerByte) {
    if (averageCharsPerByte <= 0 || maxCharsPerByte <= 0) {
      throw new IllegalArgumentException(
          "averageCharsPerByte and maxCharsPerByte must be positive");
    }
    if (averageCharsPerByte > maxCharsPerByte) {
      throw new IllegalArgumentException("averageCharsPerByte is greater than maxCharsPerByte");
    }
    averChars = averageCharsPerByte;
    maxChars = maxCharsPerByte;
    cs = charset;
    status = INIT;
    malformAction = CodingErrorAction.REPORT;
    unmapAction = CodingErrorAction.REPORT;
    replace = "\ufffd";
  }

  public final float averageCharsPerByte() {
    return averChars;
  }

  public final Charset charset() {
    return cs;
  }

  public final CharBuffer decode(ByteBuffer in) throws CharacterCodingException {
    reset();
    int length = (int) (in.remaining() * averChars);
    CharBuffer output = CharBuffer.allocate(length);
    CoderResult result = null;
    while (true) {
      result = decode(in, output, false);
      checkCoderResult(result);
      if (result.isUnderflow()) {
        break;
      } else if (result.isOverflow()) {
        output = allocateMore(output);
      }
    }
    result = decode(in, output, true);
    checkCoderResult(result);

    while (true) {
      result = flush(output);
      checkCoderResult(result);
      if (result.isOverflow()) {
        output = allocateMore(output);
      } else {
        break;
      }
    }

    output.flip();
    status = FLUSH;
    return output;
  }

  private void checkCoderResult(CoderResult result) throws CharacterCodingException {
    if (result.isMalformed() && malformAction == CodingErrorAction.REPORT) {
      throw new MalformedInputException(result.length());
    } else if (result.isUnmappable() && unmapAction == CodingErrorAction.REPORT) {
      throw new UnmappableCharacterException(result.length());
    }
  }

  private CharBuffer allocateMore(CharBuffer output) {
    if (output.capacity() == 0) {
      return CharBuffer.allocate(1);
    }
    CharBuffer result = CharBuffer.allocate(output.capacity() * 2);
    output.flip();
    result.put(output);
    return result;
  }

  public final CoderResult decode(ByteBuffer in, CharBuffer out, boolean endOfInput) {
    /*
     * status check
     */
    if ((status == FLUSH) || (!endOfInput && status == END)) {
      throw new IllegalStateException();
    }

    CoderResult result = null;

    // begin to decode
    while (true) {
      CodingErrorAction action = null;
      try {
        result = decodeLoop(in, out);
      } catch (BufferOverflowException ex) {
        // unexpected exception
        throw new CoderMalfunctionError(ex);
      } catch (BufferUnderflowException ex) {
        // unexpected exception
        throw new CoderMalfunctionError(ex);
      }

      /*
       * result handling
       */
      if (result.isUnderflow()) {
        int remaining = in.remaining();
        status = endOfInput ? END : ONGOING;
        if (endOfInput && remaining > 0) {
          result = CoderResult.malformedForLength(remaining);
        } else {
          return result;
        }
      }
      if (result.isOverflow()) {
        return result;
      }
      // set coding error handle action
      action = malformAction;
      if (result.isUnmappable()) {
        action = unmapAction;
      }
      // If the action is IGNORE or REPLACE, we should continue decoding.
      if (action == CodingErrorAction.REPLACE) {
        if (out.remaining() < replace.length()) {
          return CoderResult.OVERFLOW;
        }
        out.put(replace);
      } else {
        if (action != CodingErrorAction.IGNORE) return result;
      }
      in.position(in.position() + result.length());
    }
  }

  protected abstract CoderResult decodeLoop(ByteBuffer in, CharBuffer out);

  public Charset detectedCharset() {
    throw new UnsupportedOperationException();
  }

  public final CoderResult flush(CharBuffer out) {
    if (status != END && status != INIT) {
      throw new IllegalStateException();
    }
    CoderResult result = implFlush(out);
    if (result == CoderResult.UNDERFLOW) {
      status = FLUSH;
    }
    return result;
  }

  protected CoderResult implFlush(CharBuffer out) {
    return CoderResult.UNDERFLOW;
  }

  protected void implOnMalformedInput(CodingErrorAction newAction) {
    // default implementation is empty
  }

  protected void implOnUnmappableCharacter(CodingErrorAction newAction) {
    // default implementation is empty
  }

  protected void implReplaceWith(String newReplacement) {
    // default implementation is empty
  }

  protected void implReset() {
    // default implementation is empty
  }

  public boolean isAutoDetecting() {
    return false;
  }

  public boolean isCharsetDetected() {
    throw new UnsupportedOperationException();
  }

  public CodingErrorAction malformedInputAction() {
    return malformAction;
  }

  public final float maxCharsPerByte() {
    return maxChars;
  }

  public final CharsetDecoder onMalformedInput(CodingErrorAction newAction) {
    if (null == newAction) {
      throw new IllegalArgumentException();
    }
    malformAction = newAction;
    implOnMalformedInput(newAction);
    return this;
  }

  public final CharsetDecoder onUnmappableCharacter(CodingErrorAction newAction) {
    if (null == newAction) {
      throw new IllegalArgumentException();
    }
    unmapAction = newAction;
    implOnUnmappableCharacter(newAction);
    return this;
  }

  public final String replacement() {
    return replace;
  }

  public final CharsetDecoder replaceWith(String newReplacement) {
    if (newReplacement == null || newReplacement.isEmpty()) {
      throw new IllegalArgumentException("Replacement string cannot be null or empty");
    }
    if (newReplacement.length() > maxChars) {
      throw new IllegalArgumentException(
          "Replacement string cannot be longer than max characters per byte");
    }
    replace = newReplacement;
    implReplaceWith(newReplacement);
    return this;
  }

  public final CharsetDecoder reset() {
    status = INIT;
    implReset();
    return this;
  }

  public CodingErrorAction unmappableCharacterAction() {
    return unmapAction;
  }
}
