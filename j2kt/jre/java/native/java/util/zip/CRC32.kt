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
import platform.zlib.crc32

/**
 * The CRC32 class is used to compute a CRC32 checksum from data provided as input value. See also
 * [Adler32] which is almost as good, but cheaper.
 */
class CRC32 : Checksum {
  private var crc = 0L
  var tbytes = 0L

  /**
   * Returns the CRC32 checksum for all input received.
   *
   * @return The checksum for this instance.
   */
  override fun getValue(): Long {
    return crc
  }

  /** Resets the CRC32 checksum to it initial state. */
  override fun reset() {
    crc = 0
    tbytes = crc
  }

  /**
   * Updates this checksum with the byte value provided as integer.
   *
   * @param `val` represents the byte to update the checksum.
   */
  override fun update(value: Int) {
    crc = updateImpl(byteArrayOf(value.toByte()), 0, 1, crc)
  }

  /**
   * Updates this checksum with the bytes contained in buffer `buf`.
   *
   * @param buf the buffer holding the data to update the checksum with.
   */
  fun update(buf: ByteArray) {
    update(buf, 0, buf.size)
  }

  /**
   * Update this `CRC32` checksum with the contents of `buf`, starting from `offset` and reading
   * `byteCount` bytes of data.
   */
  override fun update(buf: ByteArray, offset: Int, byteCount: Int) {
    checkCriticalArrayBounds(offset, offset + byteCount, buf.size)
    tbytes += byteCount.toLong()
    crc = updateImpl(buf, offset, byteCount, crc)
  }

  private fun updateImpl(buf: ByteArray, offset: Int, byteCount: Int, crc: Long): Long {
    buf.usePinned { pinnedBuf ->
      return crc32(
          crc.toULong(),
          pinnedBuf.addressOf(offset) as CPointer<UByteVar>,
          byteCount.toUInt(),
        )
        .toLong()
    }
  }
}
