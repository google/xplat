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

import kotlin.jvm.javaPrimitiveType

/** java.lang.Double static method emulations */
object Double {
  operator fun invoke(d: kotlin.Double): kotlin.Double = d

  operator fun invoke(s: kotlin.String): kotlin.Double = s.toDouble()

  const val MAX_VALUE = kotlin.Double.MAX_VALUE

  const val MIN_VALUE = kotlin.Double.MIN_VALUE

  const val NaN = kotlin.Double.NaN

  const val POSITIVE_INFINITY = kotlin.Double.POSITIVE_INFINITY

  const val NEGATIVE_INFINITY = kotlin.Double.NEGATIVE_INFINITY

  const val MIN_NORMAL = 2.2250738585072014e-308 // 0x1.0p-1022

  const val MAX_EXPONENT = 1023

  const val MIN_EXPONENT = -1022

  const val SIZE = kotlin.Double.SIZE_BITS

  const val BYTES = kotlin.Double.SIZE_BYTES

  val TYPE: Class<kotlin.Double> = kotlin.Double::class.javaPrimitiveType!!

  fun valueOf(d: kotlin.Double): kotlin.Double = d

  fun valueOf(s: kotlin.String): kotlin.Double = s.toDouble()

  fun compare(d1: kotlin.Double, d2: kotlin.Double): Int = d1.compareTo(d2)

  fun toString(d: kotlin.Double): kotlin.String = d.toString()

  fun parseDouble(s: kotlin.String): kotlin.Double = s.toDouble()

  fun hashCode(d: kotlin.Double): Int = d.hashCode()

  fun isNaN(d: kotlin.Double): kotlin.Boolean = d.isNaN()

  fun isInfinite(d: kotlin.Double): kotlin.Boolean = d.isInfinite()

  fun isFinite(d: kotlin.Double): kotlin.Boolean = d.isFinite()

  fun doubleToLongBits(value: kotlin.Double): kotlin.Long = value.toBits()

  fun doubleToRawLongBits(value: kotlin.Double): kotlin.Long = value.toRawBits()

  fun longBitsToDouble(bits: kotlin.Long): kotlin.Double = kotlin.Double.fromBits(bits)

  fun min(d1: kotlin.Double, d2: kotlin.Double): kotlin.Double = kotlin.math.min(d1, d2)

  fun max(d1: kotlin.Double, d2: kotlin.Double): kotlin.Double = kotlin.math.max(d1, d2)

  fun sum(d1: kotlin.Double, d2: kotlin.Double): kotlin.Double = d1 + d2
}
