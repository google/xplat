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
package javaemul.internal;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import org.jspecify.nullness.NullMarked;

/** Provides Charset implementations (currently UTF-8 only) */
@NullMarked
public class EmulatedCharset extends Charset {

  public static final EmulatedCharset UTF_8 = new EmulatedCharset("UTF-8");
  public static final EmulatedCharset ISO_8859_1 = new EmulatedCharset("ISO-8859-1");
  public static final EmulatedCharset US_ASCII = new EmulatedCharset("ASCII");

  private EmulatedCharset(String name) {
    super(name, new String[0]);
  }

  public byte[] getBytes(char[] buffer, int offset, int count) {
    char[] replaced = null;
    int copiedTo = 0;

    // Replace invalid surrogate characters with '?'.
    // We might want to insert this into the String constructor instead.
    for (int i = 0; i < count; i++) {
      char c = buffer[i + offset];
      if ((Character.isHighSurrogate(c)
              && (i == count - 1 || !Character.isLowSurrogate(buffer[i + offset + 1])))
          || Character.isLowSurrogate(c)
              && (i == 0 || !Character.isHighSurrogate(buffer[i + offset - 1]))) {
        if (replaced == null) {
          replaced = new char[count];
        }
        System.arraycopy(buffer, copiedTo + offset, replaced, copiedTo, i - copiedTo);
        replaced[i] = '?';
        copiedTo = i + 1;
      }
    }

    if (replaced != null) {
      if (copiedTo < count) {
        System.arraycopy(buffer, copiedTo + offset, replaced, copiedTo, count - copiedTo);
      }
      return new String(replaced).getBytes(this);
    }

    return new String(buffer, offset, count).getBytes(this);
  }

  @Override
  public boolean contains(Charset cs) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CharsetDecoder newDecoder() {
    throw new UnsupportedOperationException();
  }

  @Override
  public CharsetEncoder newEncoder() {
    throw new UnsupportedOperationException();
  }
}
