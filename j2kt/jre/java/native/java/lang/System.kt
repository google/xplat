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
@file:OptIn(ExperimentalObjCName::class)

package java.lang

import java.io.OutputStream
import java.io.PrintStream
import kotlin.Long
import kotlin.String
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName
import kotlin.native.identityHashCode
import kotlin.native.internal.GC
import kotlin.system.getTimeNanos
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.CoreFoundation.CFAbsoluteTimeGetCurrent
import platform.CoreFoundation.kCFAbsoluteTimeIntervalSince1970
import platform.posix.write as posixWrite

// TODO(b/224765929): Avoid this hack for InternalPreconditions.java and logging.
@ObjCName("J2ktJavaLangSystem", exact = true)
object System {
  fun getProperty(name: String?, def: String?): String? = getProperty(name) ?: def

  fun getProperty(name: String?): String? =
    when (name) {
      "jre.checks.api",
      "jre.checks.bounds",
      "jre.checks.numeric",
      "jre.checks.type" -> "AUTO"
      "jre.checks.checkLevel" -> "NORMAL"
      "jre.logging.logLevel" -> "INFO"
      "jre.logging.simpleConsoleHandler" -> "ENABLED"
      else -> null
    }

  fun arraycopy(src: Any?, srcOfs: Int, dest: Any?, destOfs: Int, len: Int) {
    when (src) {
      is ByteArray -> src.copyInto(dest as ByteArray, destOfs, srcOfs, srcOfs + len)
      is ShortArray -> src.copyInto(dest as ShortArray, destOfs, srcOfs, srcOfs + len)
      is IntArray -> src.copyInto(dest as IntArray, destOfs, srcOfs, srcOfs + len)
      is LongArray -> src.copyInto(dest as LongArray, destOfs, srcOfs, srcOfs + len)
      is FloatArray -> src.copyInto(dest as FloatArray, destOfs, srcOfs, srcOfs + len)
      is DoubleArray -> src.copyInto(dest as DoubleArray, destOfs, srcOfs, srcOfs + len)
      is BooleanArray -> src.copyInto(dest as BooleanArray, destOfs, srcOfs, srcOfs + len)
      is CharArray -> src.copyInto(dest as CharArray, destOfs, srcOfs, srcOfs + len)
      is Array<*> -> src.copyInto(dest as Array<Any?>, destOfs, srcOfs, srcOfs + len)
      else -> throw ArrayStoreException()
    }
  }

  fun currentTimeMillis(): Long =
    ((CFAbsoluteTimeGetCurrent() + kCFAbsoluteTimeIntervalSince1970) * 1000.0).toLong()

  fun nanoTime(): Long = getTimeNanos()

  fun gc(): Unit = GC.collect()

  @OptIn(kotlin.ExperimentalStdlibApi::class)
  fun identityHashCode(o: Any?): Int = o.identityHashCode()

  private fun stdOutputStream(fd: Int) =
    object : OutputStream() {
      override fun write(b: ByteArray, off: Int, len: Int) {
        b.usePinned { posixWrite(fd, it.addressOf(off), len.toULong()) }
      }

      override fun write(b: Int) {
        write(byteArrayOf(b.toByte()))
      }
    }

  var err = PrintStream(stdOutputStream(2))

  var out = PrintStream(stdOutputStream(1))

  fun setErr(err: PrintStream) {
    this.err = err
  }

  fun setOut(out: PrintStream) {
    this.out = out
  }

  fun lineSeparator(): String = "\n"
}
