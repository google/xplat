/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
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

package java.util.zip;

import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;

/**
 * The CRC32 class is used to compute a CRC32 checksum from data provided as input value. See also
 * {@link Adler32} which is almost as good, but cheaper.
 */
@KtNative
@NullMarked
public class CRC32 implements Checksum {

  // Visibility is `internal` and enforced by kotlinc. Declared as public to avoid mangling.
  public long tbytes = 0;

  /**
   * Returns the CRC32 checksum for all input received.
   *
   * @return The checksum for this instance.
   */
  @Override
  public native long getValue();

  /** Resets the CRC32 checksum to it initial state. */
  @Override
  public native void reset();

  /**
   * Updates this checksum with the byte value provided as integer.
   *
   * @param val represents the byte to update the checksum.
   */
  @Override
  public native void update(int val);

  /**
   * Updates this checksum with the bytes contained in buffer {@code buf}.
   *
   * @param buf the buffer holding the data to update the checksum with.
   */
  public native void update(byte[] buf);

  /**
   * Update this {@code CRC32} checksum with the contents of {@code buf}, starting from {@code
   * offset} and reading {@code byteCount} bytes of data.
   */
  @Override
  public native void update(byte[] buf, int offset, int byteCount);
}
