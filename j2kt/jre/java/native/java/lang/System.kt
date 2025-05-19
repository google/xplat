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
@file:OptIn(ExperimentalObjCName::class, NativeRuntimeApi::class)

package java.lang

import java.io.OutputStream
import java.io.PrintStream
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName
import kotlin.native.identityHashCode
import kotlin.native.runtime.GC
import kotlin.native.runtime.NativeRuntimeApi
import kotlin.time.TimeSource
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.CoreFoundation.CFAbsoluteTimeGetCurrent
import platform.CoreFoundation.kCFAbsoluteTimeIntervalSince1970
import platform.Foundation.NSTemporaryDirectory
import platform.posix.write as posixWrite

@ObjCName("J2ktJavaLangSystem", exact = true)
object System {
  private val timeReference = TimeSource.Monotonic.markNow()

  fun getProperty(name: kotlin.String?, def: kotlin.String?): kotlin.String? =
    getProperty(name) ?: def

  fun getProperty(name: kotlin.String?): kotlin.String? =
    when (name) {
      "file.separator" -> "/"
      "path.separator" -> ":"
      "java.io.tmpdir" -> NSTemporaryDirectory()
      // TODO(b/392585924): Avoid this hack for InternalPreconditions.java and logging.
      "jre.checks.api",
      "jre.checks.bounds",
      "jre.checks.numeric",
      "jre.checks.type" -> "AUTO"
      "jre.checks.checkLevel" -> "NORMAL"
      else -> null
    }

  fun arraycopy(src: Any?, srcOfs: Int, dest: Any?, destOfs: Int, len: Int) {
    when (src) {
      is ByteArray ->
        if (dest is ByteArray) src.copyInto(dest, destOfs, srcOfs, srcOfs + len)
        else throw ArrayStoreException()
      is ShortArray ->
        if (dest is ShortArray) src.copyInto(dest, destOfs, srcOfs, srcOfs + len)
        else throw ArrayStoreException()
      is IntArray ->
        if (dest is IntArray) src.copyInto(dest, destOfs, srcOfs, srcOfs + len)
        else throw ArrayStoreException()
      is LongArray ->
        if (dest is LongArray) src.copyInto(dest, destOfs, srcOfs, srcOfs + len)
        else throw ArrayStoreException()
      is FloatArray ->
        if (dest is FloatArray) src.copyInto(dest, destOfs, srcOfs, srcOfs + len)
        else throw ArrayStoreException()
      is DoubleArray ->
        if (dest is DoubleArray) src.copyInto(dest, destOfs, srcOfs, srcOfs + len)
        else throw ArrayStoreException()
      is BooleanArray ->
        if (dest is BooleanArray) src.copyInto(dest, destOfs, srcOfs, srcOfs + len)
        else throw ArrayStoreException()
      is CharArray ->
        if (dest is CharArray) src.copyInto(dest, destOfs, srcOfs, srcOfs + len)
        else throw ArrayStoreException()
      is Array<*> ->
        if (dest is Array<*>) src.copyInto(dest as Array<Any?>, destOfs, srcOfs, srcOfs + len)
        else throw ArrayStoreException()
      else -> throw ArrayStoreException()
    }
  }

  fun currentTimeMillis(): kotlin.Long =
    ((CFAbsoluteTimeGetCurrent() + kCFAbsoluteTimeIntervalSince1970) * 1000.0).toLong()

  fun nanoTime(): kotlin.Long = timeReference.elapsedNow().inWholeNanoseconds

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

  fun lineSeparator(): kotlin.String = "\n"
}
