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

/** java.lang.Long static method emulations */
object Long {
  operator fun invoke(l: kotlin.Long): kotlin.Long = l

  operator fun invoke(s: kotlin.String): kotlin.Long = s.toLong()

  const val MAX_VALUE = kotlin.Long.MAX_VALUE

  const val MIN_VALUE = kotlin.Long.MIN_VALUE

  const val SIZE = kotlin.Long.SIZE_BITS

  const val BYTES = kotlin.Long.SIZE_BYTES

  val TYPE: Class<kotlin.Long> = kotlin.Long::class.javaPrimitiveType!!

  fun valueOf(l: kotlin.Long): kotlin.Long = l

  fun valueOf(s: kotlin.String): kotlin.Long = s.toLong()

  fun valueOf(s: kotlin.String, radix: Int): kotlin.Long = s.toLong(radix)

  fun compare(l1: kotlin.Long, l2: kotlin.Long): Int = l1.compareTo(l2)

  fun decode(s: kotlin.String): kotlin.Long {
    val (radix, payload) = decodeNumberString(s)
    return payload.toLong(radix)
  }

  fun toString(l: kotlin.Long): kotlin.String = l.toString()

  fun toString(l: kotlin.Long, radix: Int): kotlin.String = l.toString(radix)

  fun toBinaryString(l: kotlin.Long): kotlin.String = l.toULong().toString(2)

  fun toOctalString(l: kotlin.Long): kotlin.String = l.toULong().toString(8)

  fun toHexString(l: kotlin.Long): kotlin.String = l.toULong().toString(16)

  fun toUnsignedString(l: kotlin.Long): kotlin.String = l.toULong().toString()

  fun toUnsignedString(l: kotlin.Long, radix: Int): kotlin.String = l.toULong().toString(radix)

  fun parseLong(s: kotlin.String): kotlin.Long = s.toLong()

  fun parseLong(s: kotlin.String, radix: Int): kotlin.Long = s.toLong(radix)

  fun rotateLeft(l: kotlin.Long, bitCount: Int): kotlin.Long = l.rotateLeft(bitCount)

  fun rotateRight(l: kotlin.Long, bitCount: Int): kotlin.Long = l.rotateRight(bitCount)

  fun hashCode(l: kotlin.Long): Int = l.hashCode()

  fun numberOfLeadingZeros(l: kotlin.Long): Int = l.countLeadingZeroBits()

  fun numberOfTrailingZeros(l: kotlin.Long): Int = l.countTrailingZeroBits()

  fun highestOneBit(l: kotlin.Long): kotlin.Long = l.takeHighestOneBit()

  fun lowestOneBit(l: kotlin.Long): kotlin.Long = l.takeLowestOneBit()

  fun bitCount(l: kotlin.Long): Int = l.countOneBits()

  fun signum(l: kotlin.Long): Int = l.sign

  fun min(l1: kotlin.Long, l2: kotlin.Long): kotlin.Long = kotlin.math.min(l1, l2)

  fun max(l1: kotlin.Long, l2: kotlin.Long): kotlin.Long = kotlin.math.max(l1, l2)

  fun sum(l1: kotlin.Long, l2: kotlin.Long): kotlin.Long = l1 + l2

  fun reverse(l: kotlin.Long): kotlin.Long =
    (Integer.reverse((l shr 32).toInt()).toUInt().toLong()) or
      (Integer.reverse(l.toInt()).toLong() shl 32)

  fun reverseBytes(l: kotlin.Long): kotlin.Long =
    (Integer.reverseBytes((l shr 32).toInt()).toUInt().toLong()) or
      (Integer.reverseBytes(l.toInt()).toLong() shl 32)
}
