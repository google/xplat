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
import java.lang.Character
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.charset.UnsupportedCharsetException
import java.util.Locale
import java.util.regex.Pattern
import kotlin.text.CharacterCodingException

fun String.equalsIgnoreCase(str: String?) = this.equals(str, ignoreCase = true)

internal fun String.Companion.fromCodePoint(codePoint: Int): String {
  if (codePoint >= Char.MIN_SUPPLEMENTARY_CODE_POINT) {
    val chars = CharArray(2)
    Character.toChars(codePoint, chars, 0)
    return chars.concatToString()
  } else {
    return codePoint.toChar().toString()
  }
}

fun String.codePointAt(index: Int) = Character.codePointAt(this, index)

fun String.codePointBefore(index: Int): Int = Character.codePointBefore(this, index)

fun String.codePointCount(beginIndex: Int, endIndex: Int): Int =
  Character.codePointCount(this, beginIndex, endIndex)

fun String.offsetByCodePoints(index: Int, codePointOffset: Int): Int =
  Character.offsetByCodePoints(this, index, codePointOffset)

fun String.compareToIgnoreCase(str: String): Int = this.compareTo(str, ignoreCase = true)

fun String.toByteArray(): ByteArray {
  var replaced: StringBuilder? = null
  var copiedTo = 0

  // Check for invalid surrogate characters and replace them with '?'.
  for (i in 0..<length) {
    val c = this[i]
    if (
      (c.isHighSurrogate() && (i == length - 1 || !this[i + 1].isLowSurrogate())) ||
        (c.isLowSurrogate() && (i == 0 || !this[i - 1].isHighSurrogate()))
    ) {
      if (replaced == null) {
        replaced = StringBuilder(length)
      }
      replaced.append(this, copiedTo, i).append('?')
      copiedTo = i + 1
    }
  }

  if (replaced != null) {
    if (copiedTo < length) {
      replaced.append(this, copiedTo, length)
    }
    return replaced.toString().encodeToByteArray()
  }

  return encodeToByteArray()
}

fun String.toByteArray(charsetName: String): ByteArray {
  try {
    return toByteArray(Charset.forName(charsetName))
  } catch (e: UnsupportedCharsetException) {
    throw UnsupportedEncodingException(charsetName)
  }
}

fun String.toByteArray(charset: Charset): ByteArray =
  when (charset) {
    StandardCharsets.US_ASCII -> encodeToByteArrayUnmapped(127)
    StandardCharsets.ISO_8859_1 -> encodeToByteArrayUnmapped(255)
    StandardCharsets.UTF_8 -> toByteArray()
    else -> throw UnsupportedEncodingException(charset.name())
  }

// TODO(b/230671584): Add support for Locale on Kotlin Native
fun String.uppercase(locale: Locale): String = this.uppercase()

fun String.lowercase(locale: Locale): String = this.lowercase()

fun String.getChars(start: Int, end: Int, buffer: CharArray, index: Int) {
  var bufferIndex = index
  for (srcIndex in start until end) {
    buffer[bufferIndex++] = this[srcIndex]
  }
}

fun String.indexOf(codePoint: Int, fromIndex: Int = 0): Int {
  if (codePoint < Char.MIN_SUPPLEMENTARY_CODE_POINT) {
    return indexOf(codePoint.toChar(), fromIndex)
  } else {
    return indexOf(String.fromCodePoint(codePoint), fromIndex)
  }
}

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
  len: Int,
): Boolean =
  if (len <= 0)
    thisOffset >= 0 &&
      otherOffset >= 0 &&
      thisOffset + len <= length &&
      otherOffset + len <= other.length
  else regionMatches(thisOffset, other, otherOffset, len, ignoreCase = ignoreCase)

fun String.java_matches(regex: String) = Regex(regex).matches(this)

fun String.split(regularExpression: String): Array<String> =
  Pattern.compile(regularExpression).split(this)

fun String.split(regularExpression: String, limit: Int): Array<String> =
  Pattern.compile(regularExpression).split(this, limit)

fun String.java_replace(target: CharSequence, replacement: CharSequence): String {
  return this.replace(target.toString(), replacement.toString())
}

fun String.java_trim(): String {
  var start = 0
  while (start < length && this[start] <= ' ') {
    start++
  }
  var end = length
  while (end > start && this[end - 1] <= ' ') {
    end--
  }
  return if (start > 0 || end < length) substring(start, end) else this
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
