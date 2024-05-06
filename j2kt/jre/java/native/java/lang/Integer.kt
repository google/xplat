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

import javaemul.internal.decodeNumberString
import kotlin.jvm.javaPrimitiveType
import kotlin.math.sign

/** java.lang.Integer static method emulations */
object Integer {
  operator fun invoke(i: Int): Int = i

  operator fun invoke(s: kotlin.String): Int = s.toInt()

  const val MAX_VALUE = Int.MAX_VALUE

  const val MIN_VALUE = Int.MIN_VALUE

  const val SIZE = Int.SIZE_BITS

  const val BYTES = Int.SIZE_BYTES

  val TYPE: Class<Int> = Int::class.javaPrimitiveType!!

  fun valueOf(i: Int): Int = i

  fun valueOf(s: kotlin.String): Int = s.toInt()

  fun valueOf(s: kotlin.String, radix: Int): Int = s.toInt(radix)

  fun compare(i1: Int, i2: Int): Int = i1.compareTo(i2)

  fun decode(s: kotlin.String): Int {
    val (radix, payload) = decodeNumberString(s)
    return payload.toInt(radix)
  }

  fun toString(i: Int): kotlin.String =
    when (i) {
      0 -> "0"
      1 -> "1"
      2 -> "2"
      3 -> "3"
      4 -> "4"
      5 -> "5"
      6 -> "6"
      7 -> "7"
      8 -> "8"
      9 -> "9"
      10 -> "10"
      11 -> "11"
      12 -> "12"
      13 -> "13"
      14 -> "14"
      15 -> "15"
      16 -> "16"
      17 -> "17"
      18 -> "18"
      19 -> "19"
      20 -> "20"
      21 -> "21"
      22 -> "22"
      23 -> "23"
      else -> i.toString()
    }

  fun toString(i: Int, radix: Int): kotlin.String = i.toString(radix)

  fun toHexString(i: Int): kotlin.String = i.toUInt().toString(16)

  fun toUnsignedString(l: Int): kotlin.String = l.toUInt().toString()

  fun toUnsignedString(l: Int, radix: Int): kotlin.String = l.toUInt().toString(radix)

  fun toOctalString(i: Int): kotlin.String = i.toUInt().toString(8)

  fun toBinaryString(i: Int): kotlin.String = i.toUInt().toString(2)

  fun parseInt(s: kotlin.String): Int = s.toInt()

  fun parseInt(s: kotlin.String, radix: Int): Int = s.toInt(radix)

  fun rotateLeft(i: Int, bitCount: Int): Int = i.rotateLeft(bitCount)

  fun rotateRight(i: Int, bitCount: Int): Int = i.rotateRight(bitCount)

  fun hashCode(i: Int): Int = i.hashCode()

  fun numberOfLeadingZeros(i: Int): Int = i.countLeadingZeroBits()

  fun numberOfTrailingZeros(i: Int): Int = i.countTrailingZeroBits()

  fun highestOneBit(i: Int): Int = i.takeHighestOneBit()

  fun lowestOneBit(i: Int): Int = i.takeLowestOneBit()

  fun bitCount(i: Int): Int = i.countOneBits()

  fun signum(i: Int): Int = i.sign

  fun min(i1: Int, i2: Int): Int = kotlin.math.min(i1, i2)

  fun max(i1: Int, i2: Int): Int = kotlin.math.max(i1, i2)

  fun sum(i1: Int, i2: Int): Int = i1 + i2

  fun reverse(i: Int): Int =
    reverseNibble(i shr 28) or
      (reverseNibble(i shr 24) shl 4) or
      (reverseNibble(i shr 20) shl 8) or
      (reverseNibble(i shr 16) shl 12) or
      (reverseNibble(i shr 12) shl 16) or
      (reverseNibble(i shr 8) shl 20) or
      (reverseNibble(i shr 4) shl 24) or
      (reverseNibble(i) shl 28)

  fun reverseBytes(i: Int): Int =
    ((i and 0xff) shl 24) or
      ((i and 0xff00) shl 8) or
      ((i and 0xff0000) shr 8) or
      ((i shr 24) and 0xff)

  private fun reverseNibble(i: Int): Int =
    when (i and 0xf) {
      0 -> 0x0
      1 -> 0x8
      2 -> 0x4
      3 -> 0xc
      4 -> 0x2
      5 -> 0xa
      6 -> 0x6
      7 -> 0xe
      8 -> 0x1
      9 -> 0x9
      10 -> 0x5
      11 -> 0xd
      12 -> 0x3
      13 -> 0xb
      14 -> 0x7
      15 -> 0xf
      else -> throw IllegalArgumentException("Invalid nibble: $i")
    }
}
