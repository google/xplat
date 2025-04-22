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
import kotlinx.cinterop.BooleanVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSOpenStepRootDirectory

/** Minimal File emulation currently only suitable for pass-through purposes. */
@OptIn(kotlin.experimental.ExperimentalObjCName::class)
@ObjCName("J2ktJavaIoFile")
class File(pathname: String) {

  constructor(
    parent: File?,
    child: String,
  ) : this(
    buildString {
      if (parent != null) {
        val path = parent.getPath()
        if (path.isEmpty()) {
          append(NSOpenStepRootDirectory())
        } else {
          append(path)
        }
        append(separator)
      }
      append(child)
    }
  )

  constructor(
    parent: String?,
    child: String,
  ) : this(
    buildString {
      if (parent != null) {
        if (parent.isEmpty()) {
          append(NSOpenStepRootDirectory())
        } else {
          append(parent)
        }
        append(separator)
      }
      append(child)
    }
  )

  private val path = fixSlashes(pathname)

  /** Note: In contrast to the Java specificiation, creation and existence checks are not atomic. */
  fun createNewFile(): Boolean {
    if (exists()) {
      return false
    }
    return NSFileManager.defaultManager().createFileAtPath(path, NSData(), null)
  }

  fun delete() = NSFileManager.defaultManager().removeItemAtPath(path, null)

  fun exists() = NSFileManager.defaultManager().fileExistsAtPath(path)

  fun getAbsolutePath() =
    if (path.startsWith(separatorChar)) path
    else fixSlashes(NSFileManager.defaultManager().currentDirectoryPath() + separator + path)

  fun getPath() = path

  fun isDirectory(): Boolean {
    memScoped {
      val isDirectory = alloc<BooleanVar>()
      NSFileManager.defaultManager().fileExistsAtPath(path, isDirectory.ptr)
      return isDirectory.value
    }
  }

  fun listFiles(): Array<File>? {
    return NSFileManager.defaultManager()
      .contentsOfDirectoryAtPath(path, null)
      ?.map { File(path + separator + it) }
      ?.toTypedArray()
  }

  fun mkdir() = NSFileManager.defaultManager().createDirectoryAtPath(path, false, null, null)

  fun mkdirs() = NSFileManager.defaultManager().createDirectoryAtPath(path, true, null, null)

  fun renameTo(dest: File) = NSFileManager.defaultManager().moveItemAtPath(path, dest.path, null)

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
