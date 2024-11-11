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

import kotlin.native.ref.Cleaner
import kotlin.native.ref.createCleaner
import kotlinx.cinterop.CArrayPointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.free
import kotlinx.cinterop.get
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.set
import kotlinx.cinterop.toKString
import platform.zlib.Z_OK
import platform.zlib.deflateSetDictionary
import platform.zlib.inflateSetDictionary
import platform.zlib.z_stream_s

// Kotlin port of libcore/luni/src/main/native/ZipUtilities.cpp from Android 6.0.1 r81
internal class NativeZipStream {
  var inCap: Int = 0
  var input: CArrayPointer<UByteVar>? = null
  var inputCleaner: Cleaner? = null
  val stream: z_stream_s =
    nativeHeap.alloc<z_stream_s> {
      // Let zlib use its default allocator.
      opaque = null
      zalloc = null
      zfree = null
    }
  val streamCleaner = heapAllocCleaner(stream.ptr)
  var totalOut: Long = 0
  var totalIn: Long = 0

  private var dict: CArrayPointer<UByteVar>? = null
  private var dictCleaner: Cleaner? = null

  fun setDictionary(dict: ByteArray, offset: Int, byteCount: Int, inflate: Boolean) {
    val newDict = nativeHeap.allocArray<UByteVar>(byteCount)
    val newCleaner = heapAllocCleaner(newDict)
    for (i in 0 until byteCount) {
      newDict[i] = dict[offset + i].toUByte()
    }
    val err =
      if (inflate) {
        inflateSetDictionary(stream.ptr, newDict, byteCount.toUInt())
      } else {
        deflateSetDictionary(stream.ptr, newDict, byteCount.toUInt())
      }
    if (err != Z_OK) {
      throw IllegalArgumentException(stream.msg?.toKString())
    }
    this.dict = newDict
    this.dictCleaner = newCleaner // Let the old cleaner go out of scope and clean up
  }

  fun setInput(buf: ByteArray, offset: Int, byteCount: Int) {
    val newInput = nativeHeap.allocArray<UByteVar>(byteCount)
    val newCleaner = heapAllocCleaner(newInput)
    for (i in 0 until byteCount) {
      newInput[i] = buf[offset + i].toUByte()
    }
    stream.next_in = newInput
    stream.avail_in = byteCount.toUInt()
    inCap = byteCount
    this.input = newInput
    this.inputCleaner = newCleaner // Let the old cleaner go out of scope and clean up
  }

  private fun heapAllocCleaner(ptr: CPointer<*>): Cleaner {
    return createCleaner(ptr) { nativeHeap.free(it) }
  }
}
