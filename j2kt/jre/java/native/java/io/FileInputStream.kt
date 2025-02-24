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

import kotlinx.io.Source
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray

@OptIn(kotlin.experimental.ExperimentalObjCName::class)
@ObjCName("J2ktJavaIoFileInputStream")
class FileInputStream(path: String) : InputStream() {

  constructor(file: File) : this(file.getPath())

  val source: Source = SystemFileSystem.source(Path(path)).buffered()

  override fun close() {
    source.close()
  }

  override fun read(): Int {
    try {
      if (source.exhausted()) {
        return -1
      }
      return source.readByte().toInt() and 255
    } catch (e: IllegalStateException) {
      throw IOException(e)
    }
  }

  override fun read(buffer: ByteArray, byteOffset: Int, byteCount: Int): Int {
    try {
      return source.readAtMostTo(buffer, byteOffset, byteOffset + byteCount)
    } catch (e: IllegalStateException) {
      throw IOException(e)
    }
  }

  override fun readAllBytes(): ByteArray {
    try {
      return source.readByteArray()
    } catch (e: IllegalStateException) {
      throw IOException(e)
    }
  }
}
