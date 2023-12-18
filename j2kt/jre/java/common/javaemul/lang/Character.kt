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

import java.lang.Class
import kotlin.jvm.javaPrimitiveType

/**
 * Pseudo-constructor for emulated java.lang.Character.
 *
 * See regular JRE API documentation for other methods in this file.
 */
operator fun Char.Companion.invoke(c: Char): Char = c

val Char.Companion.TYPE: Class<Char>
  get() = Char::class.javaPrimitiveType!!

// TODO(b/233944334): Duplicate method for JVM.
val Char.Companion.MIN_SUPPLEMENTARY_CODE_POINT: Int
  get() = 0x10000
val Char.Companion.MIN_CODE_POINT: Int
  get() = 0x0000
val Char.Companion.MAX_CODE_POINT: Int
  get() = 0x10FFFF

fun Char.Companion.valueOf(c: Char): Char = c

fun Char.Companion.compare(c1: Char, c2: Char): Int = c1.compareTo(c2)

fun Char.Companion.isDigit(c: Char): Boolean = c.isDigit()

// As Kotlin native doesn't seem to have support for digit checks for code points, we
// assume false for code points outside of the 16 bit char range.
fun Char.Companion.isDigit(cp: Int): Boolean = cp >= 0 && cp <= 0xffff && (cp as Char).isDigit()

fun Char.Companion.isLetter(c: Char): Boolean = c.isLetter()

fun Char.Companion.isLetterOrDigit(c: Char): Boolean = c.isLetterOrDigit()

fun Char.Companion.isUpperCase(c: Char): Boolean = c.isUpperCase()

fun Char.Companion.isLowerCase(c: Char): Boolean = c.isLowerCase()

fun Char.Companion.isSpace(c: Char): Boolean = "\t\n\u000C\r ".contains(c)

// Exclude non-breaking spaces
fun Char.Companion.isWhitespace(c: Char): Boolean =
  c.isWhitespace() && !"\u00a0\u2007\u202F".contains(c)

fun Char.Companion.isWhitespace(i: Int): Boolean =
  if (i < Char.MAX_VALUE.code) isWhitespace(i.toChar()) else toChars(i).concatToString().isBlank()

// Exclude control characters
fun Char.Companion.isSpaceChar(c: Char): Boolean =
  c.isWhitespace() && !"\t\n\u000B\u000C\r\u001C\u001D\u001E\u001F".contains(c)

fun Char.Companion.isSpaceChar(i: Int): Boolean =
  if (i <= Char.MAX_VALUE.code) isSpaceChar(i.toChar()) else toChars(i).concatToString().isBlank()

fun Char.Companion.isTitleCase(c: Char): Boolean = c.isTitleCase()

fun Char.Companion.forDigit(digit: Int, radix: Int): Char = digit.digitToChar(radix).lowercaseChar()

fun Char.Companion.hashCode(c: Char): Int = c.hashCode()

fun Char.Companion.digit(ch: Char, radix: Int): Int = ch.digitToIntOrNull(radix) ?: -1

fun Char.Companion.charCount(codePoint: Int): Int =
  if (codePoint >= MIN_SUPPLEMENTARY_CODE_POINT) 2 else 1

fun Char.Companion.codePointAt(charArray: CharArray, index: Int, limit: Int = charArray.size): Int {
  val hiSurrogate: Char = charArray[index]
  var loSurrogate: Char = '\u0000'
  return if (
    isHighSurrogate(hiSurrogate) &&
      index + 1 < limit &&
      isLowSurrogate(charArray[index + 1].also { loSurrogate = it })
  )
    toCodePoint(hiSurrogate, loSurrogate)
  else hiSurrogate.code
}

fun Char.Companion.codePointAt(
  charSequence: CharSequence,
  index: Int,
): Int {
  val hiSurrogate: Char = charSequence[index]
  var loSurrogate: Char = '\u0000'
  return if (
    isHighSurrogate(hiSurrogate) &&
      index + 1 < charSequence.length &&
      isLowSurrogate(charSequence[index + 1].also { loSurrogate = it })
  )
    toCodePoint(hiSurrogate, loSurrogate)
  else hiSurrogate.code
}

fun Char.Companion.codePointBefore(cs: CharSequence, index: Int): Int {
  val loSurrogate = cs[index - 1]
  return if (loSurrogate.isLowSurrogate() && index > 1 && cs[index - 2].isHighSurrogate())
    toCodePoint(cs[index - 2], loSurrogate)
  else loSurrogate.code
}

fun Char.Companion.codePointBefore(ca: CharArray, index: Int, start: Int = 0): Int {
  val loSurrogate = ca[index - 1]
  return if (loSurrogate.isLowSurrogate() && index - 2 >= start && ca[index - 2].isHighSurrogate())
    toCodePoint(ca[index - 2], loSurrogate)
  else loSurrogate.code
}

fun Char.Companion.codePointCount(seq: CharSequence, beginIndex: Int, endIndex: Int): Int {
  var count = 0
  var idx = beginIndex
  while (idx < endIndex) {
    val ch = seq[idx++]
    if (ch.isHighSurrogate() && idx < endIndex && seq[idx].isLowSurrogate()) {
      // skip the second char of surrogate pairs
      ++idx
    }
    ++count
  }
  return count
}

fun Char.Companion.codePointCount(ca: CharArray, offset: Int, count: Int): Int {
  val endIndex = offset + count
  var result = 0
  var idx = offset
  while (idx < endIndex) {
    val ch = ca[idx++]
    if (ch.isHighSurrogate() && idx < endIndex && ca[idx].isLowSurrogate()) {
      // skip the second char of surrogate pairs
      ++idx
    }
    ++result
  }
  return result
}

fun Char.Companion.isValidCodePoint(codePoint: Int): Boolean =
  MIN_CODE_POINT <= codePoint && codePoint <= MAX_CODE_POINT

fun Char.Companion.isSurrogatePair(high: Char, low: Char): Boolean =
  high.isHighSurrogate() && low.isLowSurrogate()

fun Char.Companion.isLowSurrogate(ch: Char): Boolean = ch.isLowSurrogate()

fun Char.Companion.isHighSurrogate(ch: Char): Boolean = ch.isHighSurrogate()

fun Char.Companion.isSurrogate(ch: Char): Boolean = ch.isSurrogate()

fun Char.Companion.offsetByCodePoints(seq: CharSequence, index: Int, codePointOffset: Int): Int {
  var codePointOffset = codePointOffset
  var index = index
  // move backwards
  while (codePointOffset < 0) {
    --index
    if (seq[index].isLowSurrogate() && seq[index - 1].isHighSurrogate()) {
      --index
    }
    ++codePointOffset
  }
  // move forwards
  while (codePointOffset > 0) {
    if (seq[index].isHighSurrogate() && seq[index + 1].isLowSurrogate()) {
      ++index
    }
    ++index
    --codePointOffset
  }
  return index
}

fun Char.Companion.offsetByCodePoints(
  ca: CharArray,
  offset: Int,
  count: Int,
  index: Int,
  codePointOffset: Int
): Int {
  var codePointOffset = codePointOffset
  var index = index
  // move backwards
  while (codePointOffset < 0 && index >= 0) {
    --index
    if (ca[offset + index].isLowSurrogate() && ca[offset + index - 1].isHighSurrogate()) {
      --index
    }
    ++codePointOffset
  }
  // move forwards
  while (codePointOffset > 0 && index <= offset + count) {
    if (ca[offset + index].isHighSurrogate() && ca[offset + index + 1].isLowSurrogate()) {
      ++index
    }
    ++index
    --codePointOffset
  }
  if (index < 0 || index > offset + count) {
    throw IndexOutOfBoundsException()
  }
  return index
}

// TODO(b/233944334): Duplicate method for JVM.
fun Char.Companion.toCodePoint(high: Char, low: Char): Int =
  (((high - MIN_HIGH_SURROGATE) shl 10) or (low - MIN_LOW_SURROGATE)) + MIN_SUPPLEMENTARY_CODE_POINT

fun Char.Companion.toChars(codePoint: Int): CharArray =
  if (codePoint >= MIN_SUPPLEMENTARY_CODE_POINT)
    charArrayOf(highSurrogate(codePoint), lowSurrogate(codePoint))
  else charArrayOf(codePoint.toChar())

fun Char.Companion.toChars(codePoint: Int, dst: CharArray, dstIndex: Int): Int {
  // TODO(b/228304843): Add InternalPreconditions for Kotlin.
  // checkCriticalArgument(Char.isValidCodePoint(codePoint));

  if (codePoint >= MIN_SUPPLEMENTARY_CODE_POINT) {
    dst[dstIndex] = highSurrogate(codePoint)
    dst[dstIndex + 1] = lowSurrogate(codePoint)
    return 2
  }
  dst[dstIndex] = codePoint.toChar()
  return 1
}

fun Char.Companion.toString(c: Char): String = c.toString()

fun Char.Companion.toString(c: Int): String = toChars(c).concatToString()

fun Char.Companion.toUpperCase(c: Char) = c.uppercaseChar()

fun Char.Companion.toUpperCase(c: Int): Int = toString(c).uppercase().codePointAt(0)

fun Char.Companion.toLowerCase(c: Char) = c.lowercaseChar()

fun Char.Companion.toLowerCase(c: Int): Int = toString(c).lowercase().codePointAt(0)

fun Char.Companion.toTitleCase(c: Char) = c.titlecaseChar()

fun Char.Companion.lowSurrogate(codePoint: Int) =
  (MIN_LOW_SURROGATE + ((codePoint - MIN_SUPPLEMENTARY_CODE_POINT) and 1023)).toChar()

fun Char.Companion.highSurrogate(codePoint: Int) =
  (MIN_HIGH_SURROGATE + (((codePoint - MIN_SUPPLEMENTARY_CODE_POINT) shr 10) and 1023)).toChar()

fun Char.shr(pos: Int): Int = toInt().shr(pos)

fun Char.ushr(pos: Int): Int = toInt().ushr(pos)

operator fun Char.unaryMinus(): Int = toInt().unaryMinus()
