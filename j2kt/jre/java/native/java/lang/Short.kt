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

/** java.lang.Short static method emulations */
object Short {
  operator fun invoke(s: kotlin.Short): kotlin.Short = s

  operator fun invoke(s: kotlin.String): kotlin.Short = s.toShort()

  const val MAX_VALUE = kotlin.Short.MAX_VALUE

  const val MIN_VALUE = kotlin.Short.MIN_VALUE

  const val SIZE = kotlin.Short.SIZE_BITS

  const val BYTES = kotlin.Short.SIZE_BYTES

  val TYPE: Class<kotlin.Short> = kotlin.Short::class.javaPrimitiveType!!

  fun valueOf(s: kotlin.Short): kotlin.Short = s

  fun valueOf(str: kotlin.String): kotlin.Short = str.toShort()

  fun valueOf(s: kotlin.String, radix: Int): kotlin.Short = s.toShort(radix)

  fun compare(s1: kotlin.Short, s2: kotlin.Short): Int = s1.compareTo(s2)

  fun decode(s: kotlin.String): kotlin.Short {
    val (radix, payload) = decodeNumberString(s)
    return payload.toShort(radix)
  }

  fun toString(s: kotlin.Short): kotlin.String = s.toString()

  fun parseShort(str: kotlin.String): kotlin.Short = str.toShort()

  fun parseShort(s: kotlin.String, radix: Int): kotlin.Short = s.toShort(radix)

  fun hashCode(s: kotlin.Short): Int = s.hashCode()

  fun reverseBytes(s: kotlin.Short): kotlin.Short =
    (((s.toInt() shr 8) and 0xff) or ((s.toInt() and 0xff) shl 8)).toShort()
}
