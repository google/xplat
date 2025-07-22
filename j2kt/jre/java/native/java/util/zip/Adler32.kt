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
package java.util.zip

import javaemul.internal.InternalPreconditions.Companion.checkCriticalArrayBounds
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.zlib.adler32

/**
 * The Adler-32 class is used to compute the `Adler32` checksum from a set of data. Compared to
 * [CRC32] it trades reliability for speed. Refer to RFC 1950 for the specification.
 */
class Adler32 : Checksum {
  private var adler: Long = 1

  /**
   * Returns the `Adler32` checksum for all input received.
   *
   * @return The checksum for this instance.
   */
  override fun getValue(): Long {
    return adler
  }

  /** Reset this instance to its initial checksum. */
  override fun reset() {
    adler = 1
  }

  /**
   * Update this `Adler32` checksum with the single byte provided as argument.
   *
   * @param i the byte to update checksum with.
   */
  override fun update(i: Int) {
    adler = updateImpl(byteArrayOf(i.toByte()), 0, 1, adler)
  }

  /**
   * Update this `Adler32` checksum using the contents of `buf`.
   *
   * @param buf bytes to update checksum with.
   */
  fun update(buf: ByteArray) {
    update(buf, 0, buf.size)
  }

  /**
   * Update this `Adler32` checksum with the contents of `buf`, starting from `offset` and reading
   * `byteCount` bytes of data.
   */
  override fun update(buf: ByteArray, offset: Int, byteCount: Int) {
    checkCriticalArrayBounds(offset, offset + byteCount, buf.size)
    adler = updateImpl(buf, offset, byteCount, adler)
  }

  private fun updateImpl(buf: ByteArray, offset: Int, byteCount: Int, adler: Long): Long {
    buf.usePinned { pinnedBuf ->
      @Suppress("UNCHECKED_CAST")
      return adler32(
          adler.toULong(),
          pinnedBuf.addressOf(offset) as CPointer<UByteVar>,
          byteCount.toUInt(),
        )
        .toLong()
    }
  }
}
