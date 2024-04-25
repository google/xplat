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
import javaemul.internal.decodeNumberString
import kotlin.jvm.javaPrimitiveType
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

/**
 * Pseudo-constructor for emulated java.lang.Int.
 *
 * See regular JRE API documentation for other methods in this file.
 */
operator fun Int.Companion.invoke(i: Int): Int = i

operator fun Int.Companion.invoke(s: String): Int = s.toInt()

val Int.Companion.TYPE: Class<Int>
  get() = Int::class.javaPrimitiveType!!

fun Int.Companion.valueOf(i: Int): Int = i

fun Int.Companion.valueOf(s: String): Int = s.toInt()

fun Int.Companion.valueOf(s: String, radix: Int): Int = s.toInt(radix)

fun Int.Companion.compare(i1: Int, i2: Int): Int = i1.compareTo(i2)

fun Int.Companion.decode(s: String): Int {
  val (radix, payload) = decodeNumberString(s)
  return payload.toInt(radix)
}

fun Int.Companion.toString(i: Int) =
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

fun Int.Companion.toString(i: Int, radix: Int): String = i.toString(radix)

fun Int.Companion.toHexString(i: Int): String = i.toUInt().toString(16)

fun Int.Companion.toUnsignedString(l: Int): String = l.toUInt().toString()

fun Int.Companion.toUnsignedString(l: Int, radix: Int): String = l.toUInt().toString(radix)

fun Int.Companion.toOctalString(i: Int): String = i.toUInt().toString(8)

fun Int.Companion.toBinaryString(i: Int): String = i.toUInt().toString(2)

fun Int.Companion.parseInt(s: String): Int = s.toInt()

fun Int.Companion.parseInt(s: String, radix: Int): Int = s.toInt(radix)

fun Int.Companion.rotateLeft(i: Int, bitCount: Int): Int = i.rotateLeft(bitCount)

fun Int.Companion.rotateRight(i: Int, bitCount: Int): Int = i.rotateRight(bitCount)

fun Int.Companion.hashCode(i: Int): Int = i.hashCode()

fun Int.Companion.numberOfLeadingZeros(i: Int): Int = i.countLeadingZeroBits()

fun Int.Companion.numberOfTrailingZeros(i: Int): Int = i.countTrailingZeroBits()

fun Int.Companion.highestOneBit(i: Int): Int = i.takeHighestOneBit()

fun Int.Companion.lowestOneBit(i: Int): Int = i.takeLowestOneBit()

fun Int.Companion.bitCount(i: Int): Int = i.countOneBits()

fun Int.Companion.signum(i: Int): Int = i.sign

fun Int.Companion.min(i1: Int, i2: Int): Int = min(i1, i2)

fun Int.Companion.max(i1: Int, i2: Int): Int = max(i1, i2)

fun Int.Companion.sum(i1: Int, i2: Int): Int = i1 + i2

fun Int.Companion.reverse(i: Int): Int =
  reverseNibble(i shr 28) or
    (reverseNibble(i shr 24) shl 4) or
    (reverseNibble(i shr 20) shl 8) or
    (reverseNibble(i shr 16) shl 12) or
    (reverseNibble(i shr 12) shl 16) or
    (reverseNibble(i shr 8) shl 20) or
    (reverseNibble(i shr 4) shl 24) or
    (reverseNibble(i) shl 28)

fun Int.Companion.reverseBytes(i: Int) =
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
