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
package javaemul.lang

import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.charset.UnsupportedCharsetException
import java.util.Formatter
import java.util.Locale
import java.util.StringJoiner
import java.util.regex.Pattern
import kotlin.text.CharacterCodingException

// The CharArray constructors are deliberately using nullable CharArray parameters to avoid
// triggering a compiler diagnostic that forbids use of these deprecated constructors in Kotlin 🤞.
/**
 * Pseudo-constructor for emulated java.lang.String.
 *
 * We assume that the platform encoding is UTF-8 here (like J2Objc and Android do).
 *
 * See regular JRE API documentation for other methods in this file.
 */
operator fun String.Companion.invoke(a: CharArray?): String = valueOf(a!!)

operator fun String.Companion.invoke(a: CharArray?, offset: Int, len: Int): String =
  valueOf(a!!, offset, len)

operator fun String.Companion.invoke(a: ByteArray) =
  a.decodeToString(throwOnInvalidSequence = false)

operator fun String.Companion.invoke(a: ByteArray, offset: Int, len: Int) =
  a.decodeToString(offset, offset + len, throwOnInvalidSequence = false)

operator fun String.Companion.invoke(a: IntArray, offset: Int, len: Int): String {
  val sb = StringBuilder(len)
  for (i in offset until offset + len) {
    sb.appendCodePoint(a[i])
  }
  return sb.toString()
}

operator fun String.Companion.invoke(
  a: ByteArray,
  offset: Int,
  len: Int,
  charSet: Charset
): String =
  when (charSet) {
    StandardCharsets.UTF_8 -> a.decodeToString(offset, offset + len, throwOnInvalidSequence = false)
    StandardCharsets.US_ASCII -> a.decodeToStringUnmapped(offset, offset + len, ascii = true)
    StandardCharsets.ISO_8859_1 -> a.decodeToStringUnmapped(offset, offset + len, ascii = false)
    else -> throw UnsupportedCharsetException(charSet.name())
  }

operator fun String.Companion.invoke(a: ByteArray, charSet: Charset) = String(a, 0, a.size, charSet)

operator fun String.Companion.invoke(a: ByteArray, charSetName: String) =
  String(a, 0, a.size, charSetName)

operator fun String.Companion.invoke(
  a: ByteArray,
  offset: Int,
  len: Int,
  charsetName: String
): String {
  // In Java, UnsupportedCharsetException is an unchecked exception thrown by Charset.forName;
  // Methods taking a character set name here are expected to throw the checked
  // UnsupportedEncodingException instead.
  try {
    return String(a, offset, len, Charset.forName(charsetName))
  } catch (e: UnsupportedCharsetException) {
    throw UnsupportedEncodingException(charsetName)
  }
}

operator fun String.Companion.invoke(s: String) = s + ""

fun String.Companion.format(format: String, vararg values: Any?): String =
  Formatter().format(format, *values).toString()

fun String.Companion.format(locale: Locale?, format: String, vararg values: Any?): String =
  Formatter().format(locale, format, *values).toString()

fun String.Companion.join(delimiter: CharSequence, elements: Iterable<CharSequence>): String {
  val joiner = StringJoiner(delimiter)
  for (element in elements) {
    joiner.add(element)
  }
  return joiner.toString()
}

fun String.Companion.join(delimiter: CharSequence, vararg elements: CharSequence): String =
  join(delimiter, elements.asList())

fun String.Companion.valueOf(c: Char): String = c.toString()

fun String.Companion.valueOf(a: Any?): String = a.toString()

fun String.Companion.valueOf(data: CharArray): String = data.concatToString()

fun String.Companion.valueOf(data: CharArray, offset: Int, count: Int) =
  data.concatToString(offset, offset + count)

fun String.Companion.copyValueOf(data: CharArray) = valueOf(data)

fun String.Companion.copyValueOf(data: CharArray, offset: Int, count: Int): String =
  valueOf(data, offset, count)

fun String.equalsIgnoreCase(str: String?) = this.equals(str, ignoreCase = true)

internal fun String.Companion.fromCodePoint(codePoint: Int): String {
  if (codePoint >= Char.MIN_SUPPLEMENTARY_CODE_POINT) {
    val chars = CharArray(2)
    Char.toChars(codePoint, chars, 0)
    return chars.concatToString()
  } else {
    return codePoint.toChar().toString()
  }
}

fun String.codePointAt(index: Int) = Char.codePointAt(this, index)

fun String.compareToIgnoreCase(str: String): Int = this.compareTo(str, ignoreCase = true)

fun String.getBytes(): ByteArray = encodeToByteArray()

fun String.getBytes(charsetName: String): ByteArray {
  try {
    return getBytes(Charset.forName(charsetName))
  } catch (e: UnsupportedCharsetException) {
    throw UnsupportedEncodingException(charsetName)
  }
}

fun String.getBytes(charset: Charset): ByteArray =
  when (charset) {
    StandardCharsets.US_ASCII -> encodeToByteArrayUnmapped(127)
    StandardCharsets.ISO_8859_1 -> encodeToByteArrayUnmapped(255)
    StandardCharsets.UTF_8 -> encodeToByteArray()
    else -> throw UnsupportedEncodingException(charset.name())
  }

// TODO(b/230671584): Add support for Locale on Kotlin Native
fun String.toUpperCase(locale: Locale): String = this.uppercase()

fun String.toLowerCase(locale: Locale): String = this.lowercase()

fun String.getChars(start: Int, end: Int, buffer: CharArray, index: Int) {
  var bufferIndex = index
  for (srcIndex in start until end) {
    buffer[bufferIndex++] = this[srcIndex]
  }
}

// TODO(b/233944334): This duplicates JRE code on the JVM
fun String.indexOf(codePoint: Int, fromIndex: Int = 0): Int {
  if (codePoint < Char.MIN_SUPPLEMENTARY_CODE_POINT) {
    return indexOf(codePoint.toChar(), fromIndex)
  } else {
    return indexOf(String.fromCodePoint(codePoint), fromIndex)
  }
}

// TODO(b/233944334): This duplicates JRE code on the JVM
fun String.lastIndexOf(codePoint: Int, fromIndex: Int = Int.MAX_VALUE): Int {
  if (codePoint < Char.MIN_SUPPLEMENTARY_CODE_POINT) {
    return lastIndexOf(codePoint.toChar(), fromIndex)
  } else {
    return lastIndexOf(String.fromCodePoint(codePoint), fromIndex)
  }
}

fun String.replaceAll(regex: String, replacement: String): String =
  Pattern.compile(regex).matcher(this).replaceAll(replacement)

fun String.regionMatches(
  ignoreCase: Boolean,
  thisOffset: Int,
  other: String,
  otherOffset: Int,
  length: Int
): Boolean = regionMatches(thisOffset, other, otherOffset, length, ignoreCase = ignoreCase)

fun String.java_matches(regex: String) = Regex(regex).matches(this)

fun String.java_split(regularExpression: String): Array<String> {
  val strList: List<String> = this.split(regularExpression.toRegex())
  return strList.toTypedArray()
}

fun String.java_split(regularExpression: String, limit: Int): Array<String> {
  val strList: List<String> = this.split(regularExpression.toRegex(), limit)
  return strList.toTypedArray()
}

fun String.java_replace(target: CharSequence, replacement: CharSequence): String {
  return this.replace(target.toString(), replacement.toString())
}

private fun ByteArray.decodeToStringUnmapped(offset: Int, end: Int, ascii: Boolean): String {
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

private fun String.encodeToByteArrayUnmapped(maxValue: Int): ByteArray {
  val result = ByteArray(length)
  for (i in 0 until length) {
    val c = this[i].toInt()
    if (c > maxValue) {
      throw CharacterCodingException()
    }
    result[i] = c.toByte()
  }
  return result
}
