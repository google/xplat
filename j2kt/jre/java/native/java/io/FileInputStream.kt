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

import kotlin.math.min
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointed
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.get
import kotlinx.cinterop.interpretCPointer
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.posix.memcpy

@OptIn(kotlin.experimental.ExperimentalObjCName::class)
@ObjCName("J2ktJavaIoFileInputStream")
class FileInputStream(file: File) : InputStream() {

  var data: NSData? // null after closing
  var position = 0UL

  constructor(path: String) : this(File(path))

  init {
    if (!file.exists()) {
      throw FileNotFoundException(file.getPath())
    }
    data = NSFileManager.defaultManager().contentsAtPath(file.getPath())
    if (data == null) {
      throw IOException()
    }
  }

  override fun available(): Int {
    val available = (data?.length ?: throw IOException()) - position
    return if (available > Int.MAX_VALUE.toULong()) Int.MAX_VALUE else available.toInt()
  }

  override fun close() {
    data = null
  }

  override fun read(): Int {
    val localData = data ?: throw IOException()
    if (position >= localData.length) {
      return -1
    }
    val bytes: CPointer<ByteVar> = localData.bytes!!.reinterpret<ByteVar>()
    val offset = (position++).toInt()
    return bytes[offset].toInt() and 255
  }

  override fun read(buffer: ByteArray, byteOffset: Int, byteCount: Int): Int {
    val localData = data ?: throw IOException()
    if (byteCount == 0) {
      return 0
    }
    if (byteOffset + byteCount > buffer.size) {
      throw IndexOutOfBoundsException()
    }
    if (position >= localData.length) {
      return -1
    }
    val count = min(localData.length - position, byteCount.toULong())
    val bytes: CPointer<ByteVar> = localData.bytes!!.reinterpret<ByteVar>()

    val fileOffsetPtr = localData.bytes!!.rawValue.plus(position.toLong())
    val sourcePointer = interpretCPointer<CPointed>(fileOffsetPtr)

    buffer.usePinned { memcpy(it.addressOf(byteOffset), sourcePointer, count) }
    position += count
    return count.toInt()
  }

  override fun skip(byteCount: Long): Long {
    val localData = data ?: throw IOException()
    val actualCount = min(byteCount.toULong(), localData.length - position)
    position += actualCount
    return actualCount.toLong()
  }
}
