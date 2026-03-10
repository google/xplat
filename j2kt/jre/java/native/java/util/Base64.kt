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

package java.util

import java.io.ByteArrayOutputStream
import java.io.OutputStream
import kotlin.io.encoding.Base64 as KBase64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
object Base64 {

  private val decoder = Decoder()
  private val encoder = Encoder()

  fun getDecoder(): Decoder = decoder

  fun getEncoder(): Encoder = encoder

  fun getUrlEncoder(): Encoder = Encoder(isUrl = true)

  fun getUrlDecoder(): Decoder = Decoder(isUrl = true)

  fun getMimeEncoder(): Encoder =
    Encoder(isMime = true, newline = CRLF, linemax = MIME_DEFAULT_LINE_MAX)

  fun getMimeDecoder(): Decoder = Decoder(isMime = true)

  fun getMimeEncoder(lineLength: Int, lineSeparator: ByteArray): Encoder {
    lineSeparator.forEach { b ->
      if (isBase64(b)) {
        throw IllegalArgumentException(
          "Illegal base64 line separator character 0x${b.toString(16)}"
        )
      }
    }
    // round down to nearest multiple of 4
    val actualLineLength = (lineLength / 4) * 4
    if (actualLineLength <= 0) return encoder
    return Encoder(isMime = true, newline = lineSeparator, linemax = actualLineLength)
  }

  private fun isBase64(b: Byte): Boolean {
    val c = (b.toInt() and 0xFF).toChar()
    return c in 'A'..'Z' ||
      c in 'a'..'z' ||
      c in '0'..'9' ||
      c == '+' ||
      c == '/' ||
      c == '-' ||
      c == '_' ||
      c == '='
  }

  class Decoder
  internal constructor(private val isUrl: Boolean = false, private val isMime: Boolean = false) {
    private val kbase64: KBase64 by lazy {
      when {
        isUrl -> KBase64.UrlSafe
        isMime -> KBase64.Mime
        else -> KBase64.Default
      }
    }

    fun decode(s: String): ByteArray = kbase64.decode(s)

    fun decode(b: ByteArray): ByteArray = kbase64.decode(b)
  }

  class Encoder
  internal constructor(
    private val isUrl: Boolean = false,
    private val isMime: Boolean = false,
    private val newline: ByteArray? = null,
    private val linemax: Int = -1,
    private val doPadding: Boolean = true,
  ) {
    private val kbase64: KBase64 by lazy {
      val base =
        when {
          isUrl -> KBase64.UrlSafe
          isMime && isStandardMime() -> KBase64.Mime
          else -> KBase64.Default
        }
      if (doPadding) base else base.withPadding(KBase64.PaddingOption.ABSENT)
    }

    private fun isStandardMime(): Boolean = linemax == MIME_DEFAULT_LINE_MAX && isCRLF(newline)

    private fun isCRLF(ba: ByteArray?): Boolean =
      ba != null && ba.size == 2 && ba[0] == '\r'.code.toByte() && ba[1] == '\n'.code.toByte()

    fun encodeToString(b: ByteArray): String {
      if (linemax <= 0 || isStandardMime()) {
        return kbase64.encode(b)
      }
      return encode(b).decodeToString()
    }

    fun encode(b: ByteArray): ByteArray {
      if (linemax <= 0 || isStandardMime()) {
        return kbase64.encodeToByteArray(b)
      }
      val baos = ByteArrayOutputStream()
      wrap(baos).use { it.write(b) }
      return baos.toByteArray()
    }

    fun wrap(os: OutputStream): OutputStream {
      return EncOutputStream(os, kbase64, newline, linemax)
    }

    fun withoutPadding(): Encoder =
      if (!doPadding) this else Encoder(isUrl, isMime, newline, linemax, false)
  }

  private const val MIME_DEFAULT_LINE_MAX = 76
  private val CRLF = byteArrayOf('\r'.code.toByte(), '\n'.code.toByte())

  private class EncOutputStream(
    private val out: OutputStream,
    private val kbase64: KBase64,
    private val newline: ByteArray?,
    private val linemax: Int,
  ) : OutputStream() {
    private var linepos = 0
    private val buffer = ByteArray(3)
    private var bufferLen = 0
    private val outBuffer = ByteArray(4)

    override fun write(b: Int) {
      buffer[bufferLen++] = b.toByte()
      if (bufferLen == 3) {
        flushBuffer()
      }
    }

    private fun flushBuffer() {
      if (bufferLen == 0) return
      if (linemax > 0 && linepos >= linemax && newline != null) {
        out.write(newline)
        linepos = 0
      }
      val encodedLen = kbase64.encodeIntoByteArray(buffer, outBuffer, 0, 0, bufferLen)
      out.write(outBuffer, 0, encodedLen)
      linepos += encodedLen
      bufferLen = 0
    }

    override fun close() {
      flushBuffer()
      out.close()
    }
  }
}
