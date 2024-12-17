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
 * The Adler-32 class is used to compute the {@code Adler32} checksum from a set of data. Compared
 * to {@link CRC32} it trades reliability for speed. Refer to RFC 1950 for the specification.
 */
@KtNative
@NullMarked
public class Adler32 implements Checksum {

  /**
   * Returns the {@code Adler32} checksum for all input received.
   *
   * @return The checksum for this instance.
   */
  @Override
  public native long getValue();

  /** Reset this instance to its initial checksum. */
  @Override
  public native void reset();

  /**
   * Update this {@code Adler32} checksum with the single byte provided as argument.
   *
   * @param i the byte to update checksum with.
   */
  @Override
  public native void update(int i);

  /**
   * Update this {@code Adler32} checksum using the contents of {@code buf}.
   *
   * @param buf bytes to update checksum with.
   */
  public native void update(byte[] buf);

  /**
   * Update this {@code Adler32} checksum with the contents of {@code buf}, starting from {@code
   * offset} and reading {@code byteCount} bytes of data.
   */
  public native void update(byte[] buf, int offset, int byteCount);
}
