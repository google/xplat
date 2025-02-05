/*
 * Copyright 2022 Google Inc.
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

import java.lang.System
import platform.Foundation.NSFileManager

/** Minimal File emulation currently only suitable for pass-through purposes. */
@OptIn(kotlin.experimental.ExperimentalObjCName::class)
@ObjCName("J2ktJavaIoFile")
class File(path: String) {

  private val path = fixSlashes(path)

  fun exists() = NSFileManager.defaultManager().fileExistsAtPath(path)

  fun getPath() = path

  override fun toString() = getPath()

  companion object {
    val separatorChar = System.getProperty("file.separator", "/")!![0]

    val separator = separatorChar.toString()

    val pathSeparatorChar = System.getProperty("path.separator", ":")!![0]

    val pathSeparator = pathSeparatorChar.toString()

    // Removes duplicate adjacent slashes and any trailing slash.
    private fun fixSlashes(origPath: String): String {
      var lastWasSlash = false
      var newPath = origPath.toCharArray()
      val length = newPath.size
      var newLength = 0
      for (i in 0 until length) {
        val ch = newPath[i]
        if (ch == '/') {
          if (!lastWasSlash) {
            newPath[newLength++] = separatorChar
            lastWasSlash = true
          }
        } else {
          newPath[newLength++] = ch
          lastWasSlash = false
        }
      }
      // Remove any trailing slash (unless this is the root of the file system).
      if (lastWasSlash && newLength > 1) {
        newLength--
      }
      // Reuse the original string if possible.
      return if (newLength != length) newPath.concatToString(0, newLength) else origPath
    }
  }
}
