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
package java.security;

import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;

/** Java shim for SHA1Digest in J2KT, mapped to Kotlin implementation. */
@KtNative
@NullMarked
public class SHA1Digest extends MessageDigest {

  public SHA1Digest() {
    super("SHA-1");
  }

  @Override
  protected native void engineUpdate(byte input);

  @Override
  protected native void engineUpdate(byte[] input, int offset, int len);

  @Override
  protected native byte[] engineDigest();

  @Override
  protected native void engineReset();

  @Override
  protected native int engineGetDigestLength();
}
