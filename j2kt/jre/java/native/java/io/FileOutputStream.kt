/*
 * Copyright 2025 Google Inc.
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

package java.io

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ULongVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import platform.Foundation.NSData
import platform.Foundation.NSFileHandle
import platform.Foundation.dataWithBytesNoCopy
import platform.Foundation.fileHandleForWritingAtPath

class FileOutputStream(file: File, append: Boolean = false) : OutputStream() {

  constructor(path: String, append: Boolean = false) : this(File(path), append)

  var handle: NSFileHandle? // null after closing

  init {
    if (!file.exists()) {
      file.createNewFile()
    }
    val localHandle = NSFileHandle.fileHandleForWritingAtPath(file.getPath()) ?: throw IOException()
    handle = localHandle
    if (append) {
      memScoped {
        val pos = alloc<ULongVar>()
        if (!localHandle.seekToEndReturningOffset(pos.ptr, null)) {
          throw IOException()
        }
      }
    } else {
      if (!localHandle.truncateAtOffset(0UL, null)) {
        throw IOException()
      }
    }
  }

  override fun close() {
    val localHandle = handle
    if (localHandle != null) {
      handle = null
      if (!localHandle.closeAndReturnError(null)) {
        throw IOException()
      }
    }
  }

  override fun write(buffer: ByteArray, byteOffset: Int, byteCount: Int) {
    if (byteCount < 0 || byteOffset + byteCount > buffer.size) {
      throw IllegalArgumentException()
    }
    val localHandle = handle ?: throw IOException()
    if (byteCount == 0) {
      return
    }
    buffer.usePinned {
      val nsdata = NSData.dataWithBytesNoCopy(it.addressOf(byteOffset), byteCount.toULong())
      if (!localHandle.writeData(nsdata, null)) {
        throw IOException()
      }
    }
  }

  override fun write(oneByte: Int) {
    val localHandle = handle ?: throw IOException()
    memScoped {
      val byteVar = alloc<ByteVar>()
      byteVar.value = oneByte.toByte()
      val nsdata = NSData.dataWithBytesNoCopy(byteVar.ptr, 1UL)
      if (!localHandle.writeData(nsdata, null)) {
        throw IOException()
      }
    }
  }
}
