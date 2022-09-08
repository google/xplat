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
import org.jspecify.nullness.Nullable;

/** Partial emulation of the corresponding JRE-Class */
public abstract class Charset {

  private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

  private String canonicalName;

  protected Charset(String canonicalName, String @Nullable [] aliases) {
    this.canonicalName = canonicalName;
  }

  public String name() {
    return canonicalName;
  }

  public static Charset forName(String name)
      throws UnsupportedCharsetException, IllegalArgumentException {
    if (name == null) {
      throw new IllegalArgumentException();
    }
    switch (name) {
      case "UTF-8":
        return StandardCharsets.UTF_8;
      default:
        throw new UnsupportedCharsetException(name);
    }
  }

  public abstract CharsetDecoder newDecoder();

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

  public static Charset defaultCharset() {
    return DEFAULT_CHARSET;
  }
}
