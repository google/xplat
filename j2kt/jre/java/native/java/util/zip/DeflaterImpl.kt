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
import kotlin.math.min
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.UByteVarOf
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import kotlinx.cinterop.toLong
import kotlinx.cinterop.usePinned
import platform.zlib.MAX_MEM_LEVEL
import platform.zlib.MAX_WBITS
import platform.zlib.Z_BUF_ERROR
import platform.zlib.Z_DEFLATED
import platform.zlib.Z_OK
import platform.zlib.Z_STREAM_END
import platform.zlib.deflate as deflateNative
import platform.zlib.deflateEnd
import platform.zlib.deflateInit2
import platform.zlib.deflateParams
import platform.zlib.deflateReset

// See zutil.h
private const val DEF_WBITS = MAX_WBITS
private val DEF_MEM_LEVEL = min(MAX_MEM_LEVEL, 8)

// Port of libcore/luni/src/main/native/java_util_zip_Deflater.cpp
internal object DeflaterImpl {
  fun deflate(
    buf: ByteArray,
    offset: Int,
    byteCount: Int,
    nativeZipStream: NativeZipStream,
    flushStyle: Int,
    streamEndCallback: Runnable,
    bytesReadConsumer: Consumer<Int>,
  ): Int =
    buf.usePinned { pinnedBuf ->
      @Suppress("UNCHECKED_CAST")
      nativeZipStream.stream.next_out = pinnedBuf.addressOf(offset) as CPointer<UByteVarOf<UByte>>
      nativeZipStream.stream.avail_out = byteCount.toUInt()

      val initialNextIn = nativeZipStream.stream.next_in
      val initialNextOut = nativeZipStream.stream.next_out

      val err = deflateNative(nativeZipStream.stream.ptr, flushStyle)
      when (err) {
        Z_OK -> {}
        Z_STREAM_END -> {
          streamEndCallback.run()
        }
        Z_BUF_ERROR -> {
          // zlib reports this "if no progress is possible (for example avail_in or avail_out was
          // zero) ... Z_BUF_ERROR is not fatal, and deflate() can be called again with more
          // input and more output space to continue compressing".
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
      bytesWritten
    }

  fun end(nativeZipStream: NativeZipStream) = deflateEnd(nativeZipStream.stream.ptr)

  fun getAdler(nativeZipStream: NativeZipStream) = nativeZipStream.stream.adler.toInt()

  fun getTotalIn(nativeZipStream: NativeZipStream) = nativeZipStream.totalIn

  fun getTotalOut(nativeZipStream: NativeZipStream) = nativeZipStream.totalOut

  fun reset(nativeZipStream: NativeZipStream) {
    nativeZipStream.totalIn = 0
    nativeZipStream.totalOut = 0
    val err = deflateReset(nativeZipStream.stream.ptr)
    if (err != Z_OK) {
      throw IllegalArgumentException(nativeZipStream.stream.msg?.toKString())
    }
  }

  fun setDictionary(
    dict: ByteArray,
    offset: Int,
    byteCount: Int,
    nativeZipStream: NativeZipStream,
  ) = nativeZipStream.setDictionary(dict, offset, byteCount, inflate = false)

  fun setLevels(level: Int, strategy: Int, nativeZipStream: NativeZipStream) {
    nativeZipStream.stream.next_out = null
    nativeZipStream.stream.avail_out = 0U
    val err = deflateParams(nativeZipStream.stream.ptr, level, strategy)
    if (err != Z_OK) {
      throw IllegalStateException(nativeZipStream.stream.msg?.toKString())
    }
  }

  fun setInput(buf: ByteArray, offset: Int, byteCount: Int, nativeZipStream: NativeZipStream) =
    nativeZipStream.setInput(buf, offset, byteCount)

  fun createStream(level: Int, strategy: Int, noHeader: Boolean): NativeZipStream {
    val nativeZipStream = NativeZipStream()

    /*
     * See zlib.h for documentation of the deflateInit2 windowBits and memLevel parameters.
     *
     * zconf.h says the "requirements for deflate are (in bytes):
     *         (1 << (windowBits+2)) +  (1 << (memLevel+9))
     * that is: 128K for windowBits=15  +  128K for memLevel = 8  (default values) plus a few
     * kilobytes for small objects."
     */
    val windowBits: Int = if (noHeader) -DEF_WBITS else DEF_WBITS
    val memLevel: Int = DEF_MEM_LEVEL
    val err =
      deflateInit2(nativeZipStream.stream.ptr, level, Z_DEFLATED, windowBits, memLevel, strategy)
    if (err != Z_OK) {
      throw IllegalStateException(nativeZipStream.stream.msg?.toKString())
    }
    return nativeZipStream
  }
}
