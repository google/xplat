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
 * Pseudo-constructor for emulated java.lang.Long.
 *
 * See regular JRE API documentation for other methods in this file.
 */
operator fun Long.Companion.invoke(l: Long): Long = l

operator fun Long.Companion.invoke(s: String): Long = s.toLong()

val Long.Companion.TYPE: Class<Long>
  get() = Long::class.javaPrimitiveType!!

fun Long.Companion.valueOf(l: Long): Long = l

fun Long.Companion.valueOf(s: String): Long = s.toLong()

fun Long.Companion.valueOf(s: String, radix: Int): Long = s.toLong(radix)

fun Long.Companion.compare(l1: Long, l2: Long): Int = l1.compareTo(l2)

fun Long.Companion.decode(s: String): Long {
  val (radix, payload) = decodeNumberString(s)
  return payload.toLong(radix)
}

fun Long.Companion.toString(l: Long): String = l.toString()

fun Long.Companion.toString(l: Long, radix: Int): String = l.toString(radix)

fun Long.Companion.toBinaryString(l: Long): String = l.toULong().toString(2)

fun Long.Companion.toOctalString(l: Long): String = l.toULong().toString(8)

fun Long.Companion.toHexString(l: Long): String = l.toULong().toString(16)

fun Long.Companion.toUnsignedString(l: Long): String = l.toULong().toString()

fun Long.Companion.toUnsignedString(l: Long, radix: Int): String = l.toULong().toString(radix)

fun Long.Companion.parseLong(s: String): Long = s.toLong()

fun Long.Companion.parseLong(s: String, radix: Int): Long = s.toLong(radix)

fun Long.Companion.rotateLeft(l: Long, bitCount: Int): Long = l.rotateLeft(bitCount)

fun Long.Companion.rotateRight(l: Long, bitCount: Int): Long = l.rotateRight(bitCount)

fun Long.Companion.hashCode(l: Long): Int = l.hashCode()

fun Long.Companion.numberOfLeadingZeros(l: Long): Int = l.countLeadingZeroBits()

fun Long.Companion.numberOfTrailingZeros(l: Long): Int = l.countTrailingZeroBits()

fun Long.Companion.highestOneBit(l: Long): Long = l.takeHighestOneBit()

fun Long.Companion.lowestOneBit(l: Long): Long = l.takeLowestOneBit()

fun Long.Companion.bitCount(l: Long): Int = l.countOneBits()

fun Long.Companion.signum(l: Long): Int = l.sign

fun Long.Companion.min(l1: Long, l2: Long): Long = min(l1, l2)

fun Long.Companion.max(l1: Long, l2: Long): Long = max(l1, l2)

fun Long.Companion.sum(l1: Long, l2: Long): Long = l1 + l2

fun Long.Companion.reverse(l: Long): Long =
  (Int.reverse((l shr 32).toInt()).toUInt().toLong()) or (Int.reverse(l.toInt()).toLong() shl 32)

fun Long.Companion.reverseBytes(l: Long): Long =
  (Int.reverseBytes((l shr 32).toInt()).toUInt().toLong()) or
    (Int.reverseBytes(l.toInt()).toLong() shl 32)
