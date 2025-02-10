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
package java.lang

import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.charset.UnsupportedCharsetException
import java.util.Formatter
import java.util.Locale
import java.util.StringJoiner
import javaemul.lang.appendCodePoint
import kotlin.collections.Iterable as KotlinIterable

/**
 * Pseudo-constructor for emulated java.lang.String.
 *
 * We assume that the platform encoding is UTF-8 here (like J2Objc and Android do).
 *
 * See regular JRE API documentation for other methods in this file.
 */
object String {
  val CASE_INSENSITIVE_ORDER: Comparator<kotlin.String> = kotlin.String.CASE_INSENSITIVE_ORDER

  operator fun invoke(a: CharArray): kotlin.String = valueOf(a)

  operator fun invoke(sb: StringBuilder): kotlin.String = sb.toString()

  operator fun invoke(a: CharArray, offset: Int, len: Int): kotlin.String = valueOf(a, offset, len)

  operator fun invoke(a: ByteArray) = a.decodeToString(throwOnInvalidSequence = false)

  operator fun invoke(a: ByteArray, offset: Int, len: Int) =
    a.decodeToString(offset, offset + len, throwOnInvalidSequence = false)

  operator fun invoke(a: IntArray, offset: Int, len: Int): kotlin.String {
    val sb = StringBuilder(len)
    for (i in offset until offset + len) {
      sb.appendCodePoint(a[i])
    }
    return sb.toString()
  }

  operator fun invoke(a: ByteArray, offset: Int, len: Int, charSet: Charset): kotlin.String =
    when (charSet) {
      StandardCharsets.UTF_8 ->
        a.decodeToString(offset, offset + len, throwOnInvalidSequence = false)
      StandardCharsets.US_ASCII -> a.decodeToStringUnmapped(offset, offset + len, ascii = true)
      StandardCharsets.ISO_8859_1 -> a.decodeToStringUnmapped(offset, offset + len, ascii = false)
      else -> throw UnsupportedCharsetException(charSet.name())
    }

  operator fun invoke(a: ByteArray, charSet: Charset) = String(a, 0, a.size, charSet)

  operator fun invoke(a: ByteArray, charSetName: kotlin.String) = String(a, 0, a.size, charSetName)

  operator fun invoke(
    a: ByteArray,
    offset: Int,
    len: Int,
    charsetName: kotlin.String,
  ): kotlin.String {
    // In Java, UnsupportedCharsetException is an unchecked exception thrown by Charset.forName;
    // Methods taking a character set name here are expected to throw the checked
    // UnsupportedEncodingException instead.
    try {
      return String(a, offset, len, Charset.forName(charsetName))
    } catch (e: UnsupportedCharsetException) {
      throw UnsupportedEncodingException(charsetName)
    }
  }

  operator fun invoke(s: kotlin.String) = StringBuilder(s).toString()

  fun format(format: kotlin.String, vararg values: Any?): kotlin.String =
    Formatter().format(format, *values).toString()

  fun format(locale: Locale?, format: kotlin.String, vararg values: Any?): kotlin.String =
    Formatter().format(locale, format, *values).toString()

  fun join(delimiter: CharSequence, elements: KotlinIterable<CharSequence>): kotlin.String {
    val joiner = StringJoiner(delimiter)
    for (element in elements) {
      joiner.add(element)
    }
    return joiner.toString()
  }

  fun join(delimiter: CharSequence, vararg elements: CharSequence): kotlin.String =
    join(delimiter, elements.asList())

  fun valueOf(c: Char): kotlin.String = c.toString()

  fun valueOf(a: Any?) = if (a is Int) Integer.toString(a) else a.toString()

  fun valueOf(i: Int): kotlin.String = Integer.toString(i)

  fun valueOf(data: CharArray): kotlin.String = data.concatToString()

  fun valueOf(data: CharArray, offset: Int, count: Int) =
    data.concatToString(offset, offset + count)

  fun copyValueOf(data: CharArray) = valueOf(data)

  fun copyValueOf(data: CharArray, offset: Int, count: Int): kotlin.String =
    valueOf(data, offset, count)

  private fun ByteArray.decodeToStringUnmapped(
    offset: Int,
    end: Int,
    ascii: kotlin.Boolean,
  ): kotlin.String {
    val sb: StringBuilder = StringBuilder(end - offset)
    for (i in offset until end) {
      val c = this[i].toInt() and 255
      if (ascii && c > 127) {
        throw CharacterCodingException()
      }
      sb.append(c.toChar())
    }
    return sb.toString()
  }
}
