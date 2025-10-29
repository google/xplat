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

import javaemul.lang.codePointAt
import kotlin.jvm.javaPrimitiveType

/** java.lang.Character static method emulations */
class Character {
  companion object {
    operator fun invoke(c: Char): Char = c

    const val MIN_VALUE: Char = Char.MIN_VALUE

    const val MAX_VALUE: Char = Char.MAX_VALUE

    const val MIN_RADIX: Int = 2

    const val MAX_RADIX: Int = 36

    val TYPE: Class<Char> = Char::class.javaPrimitiveType!!

    const val DIRECTIONALITY_ARABIC_NUMBER: kotlin.Byte = 6
    const val DIRECTIONALITY_BOUNDARY_NEUTRAL: kotlin.Byte = 9
    const val DIRECTIONALITY_COMMON_NUMBER_SEPARATOR: kotlin.Byte = 7
    const val DIRECTIONALITY_EUROPEAN_NUMBER: kotlin.Byte = 3
    const val DIRECTIONALITY_EUROPEAN_NUMBER_SEPARATOR: kotlin.Byte = 4
    const val DIRECTIONALITY_EUROPEAN_NUMBER_TERMINATOR: kotlin.Byte = 5
    const val DIRECTIONALITY_LEFT_TO_RIGHT: kotlin.Byte = 0
    const val DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING: kotlin.Byte = 14
    const val DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE: kotlin.Byte = 15
    const val DIRECTIONALITY_NONSPACING_MARK: kotlin.Byte = 8
    const val DIRECTIONALITY_OTHER_NEUTRALS: kotlin.Byte = 13
    const val DIRECTIONALITY_PARAGRAPH_SEPARATOR: kotlin.Byte = 10
    const val DIRECTIONALITY_POP_DIRECTIONAL_FORMAT: kotlin.Byte = 18
    const val DIRECTIONALITY_RIGHT_TO_LEFT: kotlin.Byte = 1
    const val DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC: kotlin.Byte = 2
    const val DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING: kotlin.Byte = 16
    const val DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE: kotlin.Byte = 17
    const val DIRECTIONALITY_SEGMENT_SEPARATOR: kotlin.Byte = 11
    const val DIRECTIONALITY_WHITESPACE: kotlin.Byte = 12
    const val DIRECTIONALITY_UNDEFINED: kotlin.Byte = -1

    const val MIN_HIGH_SURROGATE: Char = Char.MIN_HIGH_SURROGATE

    const val MAX_HIGH_SURROGATE: Char = Char.MAX_HIGH_SURROGATE

    const val MIN_LOW_SURROGATE: Char = Char.MIN_LOW_SURROGATE

    const val MAX_LOW_SURROGATE: Char = Char.MAX_LOW_SURROGATE

    const val MIN_SURROGATE: Char = Char.MIN_SURROGATE

    const val MAX_SURROGATE: Char = Char.MAX_SURROGATE

    const val MIN_SUPPLEMENTARY_CODE_POINT: Int = Char.MIN_SUPPLEMENTARY_CODE_POINT

    const val MIN_CODE_POINT: Int = Char.MIN_CODE_POINT

    const val MAX_CODE_POINT: Int = Char.MAX_CODE_POINT

    const val SIZE: Int = Char.SIZE_BITS

    fun valueOf(c: Char): Char = c

    fun compare(c1: Char, c2: Char): Int = c1.compareTo(c2)

    // TODO(b/367245215): Implement this method properly when Kotlin native supports it.
    fun getDirectionality(c: Char): kotlin.Byte = DIRECTIONALITY_UNDEFINED

    fun isDigit(c: Char): kotlin.Boolean = c.isDigit()

    // As Kotlin native doesn't seem to have support for digit checks for code points, we
    // assume false for code points outside of the 16 bit char range.
    fun isDigit(cp: Int): kotlin.Boolean = cp >= 0 && cp <= 0xffff && (cp as Char).isDigit()

    fun isISOControl(c: Char): kotlin.Boolean = c.isISOControl()

    fun isLetter(c: Char): kotlin.Boolean = c.isLetter()

    fun isLetterOrDigit(c: Char): kotlin.Boolean = c.isLetterOrDigit()

    fun isUpperCase(c: Char): kotlin.Boolean = c.isUpperCase()

    fun isLowerCase(c: Char): kotlin.Boolean = c.isLowerCase()

    fun isSpace(c: Char): kotlin.Boolean = "\t\n\u000C\r ".contains(c)

    // Exclude non-breaking spaces
    fun isWhitespace(c: Char): kotlin.Boolean =
      c.isWhitespace() && !"\u00a0\u2007\u202F".contains(c)

    fun isWhitespace(i: Int): kotlin.Boolean =
      if (i < Char.MAX_VALUE.code) isWhitespace(i.toChar())
      else toChars(i).concatToString().isBlank()

    // Exclude control characters
    fun isSpaceChar(c: Char): kotlin.Boolean =
      c.isWhitespace() && !"\t\n\u000B\u000C\r\u001C\u001D\u001E\u001F".contains(c)

    fun isSpaceChar(i: Int): kotlin.Boolean =
      if (i <= Char.MAX_VALUE.code) isSpaceChar(i.toChar())
      else toChars(i).concatToString().isBlank()

    fun isTitleCase(c: Char): kotlin.Boolean = c.isTitleCase()

    fun forDigit(digit: Int, radix: Int): Char = digit.digitToChar(radix).lowercaseChar()

    fun hashCode(c: Char): Int = c.hashCode()

    fun digit(ch: Char, radix: Int): Int = ch.digitToIntOrNull(radix) ?: -1

    fun charCount(codePoint: Int): Int = if (codePoint >= MIN_SUPPLEMENTARY_CODE_POINT) 2 else 1

    fun codePointAt(charArray: CharArray, index: Int, limit: Int = charArray.size): Int {
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

    fun codePointAt(charSequence: CharSequence, index: Int): Int {
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

    fun codePointBefore(cs: CharSequence, index: Int): Int {
      val loSurrogate = cs[index - 1]
      return if (loSurrogate.isLowSurrogate() && index > 1 && cs[index - 2].isHighSurrogate())
        toCodePoint(cs[index - 2], loSurrogate)
      else loSurrogate.code
    }

    fun codePointBefore(ca: CharArray, index: Int, start: Int = 0): Int {
      val loSurrogate = ca[index - 1]
      return if (
        loSurrogate.isLowSurrogate() && index - 2 >= start && ca[index - 2].isHighSurrogate()
      )
        toCodePoint(ca[index - 2], loSurrogate)
      else loSurrogate.code
    }

    fun codePointCount(seq: CharSequence, beginIndex: Int, endIndex: Int): Int {
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

    fun codePointCount(ca: CharArray, offset: Int, count: Int): Int {
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

    fun isValidCodePoint(codePoint: Int): kotlin.Boolean =
      MIN_CODE_POINT <= codePoint && codePoint <= MAX_CODE_POINT

    fun isSurrogatePair(high: Char, low: Char): kotlin.Boolean =
      high.isHighSurrogate() && low.isLowSurrogate()

    fun isLowSurrogate(ch: Char): kotlin.Boolean = ch.isLowSurrogate()

    fun isHighSurrogate(ch: Char): kotlin.Boolean = ch.isHighSurrogate()

    fun isSurrogate(ch: Char): kotlin.Boolean = ch.isSurrogate()

    fun isSupplementaryCodePoint(codePoint: Int) =
      codePoint >= MIN_SUPPLEMENTARY_CODE_POINT && codePoint <= MAX_CODE_POINT

    fun offsetByCodePoints(seq: CharSequence, index: Int, codePointOffset: Int): Int {
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

    fun offsetByCodePoints(
      ca: CharArray,
      offset: Int,
      count: Int,
      index: Int,
      codePointOffset: Int,
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

    fun toCodePoint(high: Char, low: Char): Int =
      (((high - MIN_HIGH_SURROGATE) shl 10) or (low - MIN_LOW_SURROGATE)) +
        MIN_SUPPLEMENTARY_CODE_POINT

    fun toChars(codePoint: Int): CharArray =
      if (codePoint >= MIN_SUPPLEMENTARY_CODE_POINT)
        charArrayOf(highSurrogate(codePoint), lowSurrogate(codePoint))
      else charArrayOf(codePoint.toChar())

    fun toChars(codePoint: Int, dst: CharArray, dstIndex: Int): Int {
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

    fun toString(c: Char): kotlin.String = c.toString()

    fun toString(c: Int): kotlin.String = toChars(c).concatToString()

    fun toUpperCase(c: Char) = c.uppercaseChar()

    fun toUpperCase(c: Int): Int = toString(c).uppercase().codePointAt(0)

    fun toLowerCase(c: Char) = c.lowercaseChar()

    fun toLowerCase(c: Int): Int = toString(c).lowercase().codePointAt(0)

    fun toTitleCase(c: Char) = c.titlecaseChar()

    fun lowSurrogate(codePoint: Int) =
      (MIN_LOW_SURROGATE + ((codePoint - MIN_SUPPLEMENTARY_CODE_POINT) and 1023)).toChar()

    fun highSurrogate(codePoint: Int) =
      (MIN_HIGH_SURROGATE + (((codePoint - MIN_SUPPLEMENTARY_CODE_POINT) shr 10) and 1023)).toChar()
  }
}
