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

import java.lang.Runnable
import java.util.function.Consumer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.UByteVarOf
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import kotlinx.cinterop.toLong
import kotlinx.cinterop.usePinned
import platform.zlib.MAX_WBITS
import platform.zlib.Z_NEED_DICT
import platform.zlib.Z_OK
import platform.zlib.Z_STREAM_END
import platform.zlib.Z_STREAM_ERROR
import platform.zlib.Z_SYNC_FLUSH
import platform.zlib.inflate as inflateNative
import platform.zlib.inflateEnd
import platform.zlib.inflateInit2
import platform.zlib.inflateReset

private const val DEF_WBITS = MAX_WBITS

internal object InflaterImpl {
  // Used instead of zero-length arrays to avoid overly conservative out-of-bounds errors. Readonly.
  private val singleByteArray = byteArrayOf(0)

  fun createStream(noHeader: Boolean): NativeZipStream {
    val nativeZipStream = NativeZipStream()

    nativeZipStream.stream.adler = 1U

    /*
     * See zlib.h for documentation of the inflateInit2 windowBits parameter.
     *
     * zconf.h says the "requirements for inflate are (in bytes) 1 << windowBits
     * that is, 32K for windowBits=15 (default value) plus a few kilobytes
     * for small objects." This means that we can happily use the default
     * here without worrying about memory consumption.
     */
    val err = inflateInit2(nativeZipStream.stream.ptr, if (noHeader) -DEF_WBITS else DEF_WBITS)
    if (err != Z_OK) {
      throw IllegalArgumentException(nativeZipStream.stream.msg?.toKString())
    }

    return nativeZipStream
  }

  fun end(nativeZipStream: NativeZipStream) {
    inflateEnd(nativeZipStream.stream.ptr)
  }

  fun getAdler(nativeZipStream: NativeZipStream): Int = nativeZipStream.stream.adler.toInt()

  fun getTotalIn(nativeZipStream: NativeZipStream): Long = nativeZipStream.totalIn

  fun getTotalOut(nativeZipStream: NativeZipStream): Long = nativeZipStream.totalOut

  fun inflate(
    buf: ByteArray,
    offset: Int,
    byteCount: Int,
    nativeZipStream: NativeZipStream,
    streamEndCallback: Runnable,
    bytesReadConsumer: Consumer<Int>,
    needDictCallback: Runnable,
  ): Int =
    (if (byteCount === 0) singleByteArray else buf).usePinned { pinnedBuf ->
      nativeZipStream.stream.next_out =
        pinnedBuf.addressOf(if (byteCount === 0) 0 else offset) as CPointer<UByteVarOf<UByte>>
      nativeZipStream.stream.avail_out = byteCount.toUInt()

      val initialNextIn = nativeZipStream.stream.next_in
      val initialNextOut = nativeZipStream.stream.next_out

      val err = inflateNative(nativeZipStream.stream.ptr, Z_SYNC_FLUSH)
      when (err) {
        Z_OK -> {}
        Z_NEED_DICT -> {
          needDictCallback.run()
        }
        Z_STREAM_END -> {
          streamEndCallback.run()
        }
        Z_STREAM_ERROR -> {
          return@usePinned 0
        }
        else -> {
          throw DataFormatException(nativeZipStream.stream.msg?.toKString())
        }
      }

      val bytesRead = (nativeZipStream.stream.next_in.toLong() - initialNextIn.toLong()).toInt()
      val bytesWritten =
        (nativeZipStream.stream.next_out.toLong() - initialNextOut.toLong()).toInt()

      nativeZipStream.totalIn += bytesRead
      nativeZipStream.totalOut += bytesWritten

      bytesReadConsumer.accept(bytesRead)

      return bytesWritten
    }

  fun reset(nativeZipStream: NativeZipStream) {
    nativeZipStream.totalIn = 0
    nativeZipStream.totalOut = 0
    val err = inflateReset(nativeZipStream.stream.ptr)
    if (err != Z_OK) {
      throw IllegalArgumentException(nativeZipStream.stream.msg?.toKString())
    }
  }

  fun setDictionary(
    dictionary: ByteArray,
    offset: Int,
    byteCount: Int,
    nativeZipStream: NativeZipStream,
  ) {
    nativeZipStream.setDictionary(dictionary, offset, byteCount, inflate = true)
  }

  fun setInput(buf: ByteArray, offset: Int, byteCount: Int, nativeZipStream: NativeZipStream) {
    nativeZipStream.setInput(buf, offset, byteCount)
  }
}
