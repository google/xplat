/*
 * Copyright 2024 Google Inc.
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

package java.util;

import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;

/** Partial J2cl emulation of Base64. */
@KtNative // Reimplemented in Kotlin, not transpiled.
@NullMarked
public final class Base64 {

  private Base64() {}

  public static native Decoder getDecoder();

  public static native Encoder getEncoder();

  @KtNative
  public static class Decoder {
    Decoder() {}

    public native byte[] decode(String s);
  }

  @KtNative
  public static class Encoder {
    Encoder() {}

    public native String encodeToString(byte[] b);
  }
}
