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

/** java.lang.Byte static method emulations */
class Byte {
  companion object {
    operator fun invoke(b: kotlin.Byte): kotlin.Byte = b

    const val MAX_VALUE = kotlin.Byte.MAX_VALUE

    const val MIN_VALUE = kotlin.Byte.MIN_VALUE

    const val SIZE: Int = kotlin.Byte.SIZE_BITS

    const val BYTES: Int = kotlin.Byte.SIZE_BYTES

    val TYPE: Class<kotlin.Byte> = kotlin.Byte::class.javaPrimitiveType!!

    fun valueOf(b: kotlin.Byte): kotlin.Byte = b

    fun valueOf(s: kotlin.String): kotlin.Byte = s.toByte()

    fun valueOf(s: kotlin.String, radix: Int): kotlin.Byte = s.toByte(radix)

    fun compare(b1: kotlin.Byte, b2: kotlin.Byte): Int = b1.compareTo(b2)

    fun decode(s: kotlin.String): kotlin.Byte {
      val (radix, payload) = decodeNumberString(s)
      return payload.toByte(radix)
    }

    fun toString(b: kotlin.Byte): kotlin.String = b.toString()

    internal fun toHexString(b: kotlin.Byte, upperCase: kotlin.Boolean): kotlin.String =
      b.toUByte().toString(16).let { if (upperCase) it.uppercase() else it }

    fun parseByte(s: kotlin.String): kotlin.Byte = s.toByte()

    fun parseByte(s: kotlin.String, radix: Int): kotlin.Byte = s.toByte(radix)

    fun hashCode(b: kotlin.Byte): Int = b.hashCode()
  }
}
