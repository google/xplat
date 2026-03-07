/*
 * Copyright 2022 Google Inc.
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
package java.nio.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Locale;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/** Partial emulation of the corresponding JRE-Class */
@NullMarked
public abstract class Charset {

  private String canonicalName;

  protected Charset(String canonicalName, String @Nullable [] aliases) {
    this.canonicalName = canonicalName;
  }

  public String name() {
    return canonicalName;
  }

  public String displayName() {
    return canonicalName;
  }

  public static Charset forName(String name)
      throws UnsupportedCharsetException, IllegalArgumentException {
    switch (name.toUpperCase(Locale.ROOT)) {
      case "ASCII":
        return StandardCharsets.US_ASCII;
      case "ISO-8859-1":
        return StandardCharsets.ISO_8859_1;
      case "UTF-8":
        return StandardCharsets.UTF_8;
      default:
        throw new UnsupportedCharsetException(name);
    }
  }

  public abstract boolean contains(Charset cs);

  public abstract CharsetDecoder newDecoder();

  public abstract CharsetEncoder newEncoder();

  public final CharBuffer decode(ByteBuffer buffer) {
    try {
      return newDecoder()
          .onMalformedInput(CodingErrorAction.REPLACE)
          .onUnmappableCharacter(CodingErrorAction.REPLACE)
          .decode(buffer);
    } catch (CharacterCodingException ex) {
      // This method always replaces malformed-input and unmappable-character sequences with this
      // charset's default replacement byte array. CharacterCodingException cannot occur here as a
      // result so it is wrapped in an Error.
      throw new Error(ex.getMessage(), ex);
    }
  }

  /**
   * Returns a new {@code ByteBuffer} containing the bytes encoding the characters from {@code
   * buffer}. This method uses {@code CodingErrorAction.REPLACE}.
   *
   * <p>Applications should generally create a {@link CharsetEncoder} using {@link #newEncoder} for
   * performance.
   *
   * @param buffer the character buffer containing the content to be encoded.
   * @return the result of the encoding.
   */
  public final ByteBuffer encode(CharBuffer buffer) {
    try {
      return newEncoder()
          .onMalformedInput(CodingErrorAction.REPLACE)
          .onUnmappableCharacter(CodingErrorAction.REPLACE)
          .encode(buffer);
    } catch (CharacterCodingException ex) {
      throw new Error(ex.getMessage(), ex);
    }
  }

  /**
   * Returns a new {@code ByteBuffer} containing the bytes encoding the characters from {@code s}.
   * This method uses {@code CodingErrorAction.REPLACE}.
   *
   * <p>Applications should generally create a {@link CharsetEncoder} using {@link #newEncoder} for
   * performance.
   *
   * @param s the string to be encoded.
   * @return the result of the encoding.
   */
  public final ByteBuffer encode(String s) {
    return encode(CharBuffer.wrap(s));
  }

  public static Charset defaultCharset() {
    return StandardCharsets.UTF_8;
  }

  @Override
  public String toString() {
    return canonicalName;
  }
}
