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

/** java.lang.Float static method emulations */
class Float {
  companion object {
    operator fun invoke(f: kotlin.Float): kotlin.Float = f

    operator fun invoke(s: kotlin.String): kotlin.Float = s.toFloat()

    const val MAX_VALUE = kotlin.Float.MAX_VALUE

    const val MIN_VALUE = kotlin.Float.MIN_VALUE

    const val NaN = kotlin.Float.NaN

    const val POSITIVE_INFINITY = kotlin.Float.POSITIVE_INFINITY

    const val NEGATIVE_INFINITY = kotlin.Float.NEGATIVE_INFINITY

    const val MIN_NORMAL: kotlin.Float = 1.1754943508222875e-38F // 0x1.0p-126f

    const val MAX_EXPONENT = 127

    const val MIN_EXPONENT = -126

    const val SIZE = kotlin.Float.SIZE_BITS

    const val BYTES = kotlin.Float.SIZE_BYTES

    val TYPE: Class<kotlin.Float> = kotlin.Float::class.javaPrimitiveType!!

    fun valueOf(f: kotlin.Float): kotlin.Float = f

    fun valueOf(s: kotlin.String): kotlin.Float = s.toFloat()

    fun compare(f1: kotlin.Float, f2: kotlin.Float): Int = f1.compareTo(f2)

    fun toString(f: kotlin.Float): kotlin.String = f.toString()

    fun parseFloat(s: kotlin.String): kotlin.Float = s.toFloat()

    fun hashCode(f: kotlin.Float): Int = f.hashCode()

    fun isNaN(f: kotlin.Float): kotlin.Boolean = f.isNaN()

    fun isInfinite(f: kotlin.Float): kotlin.Boolean = f.isInfinite()

    fun isFinite(f: kotlin.Float): kotlin.Boolean = f.isFinite()

    fun floatToIntBits(f: kotlin.Float): Int = f.toBits()

    fun floatToRawIntBits(f: kotlin.Float): Int = f.toRawBits()

    fun intBitsToFloat(bits: Int): kotlin.Float = kotlin.Float.fromBits(bits)

    fun min(f1: kotlin.Float, f2: kotlin.Float): kotlin.Float = kotlin.math.min(f1, f2)

    fun max(f1: kotlin.Float, f2: kotlin.Float): kotlin.Float = kotlin.math.max(f1, f2)

    fun sum(f1: kotlin.Float, f2: kotlin.Float): kotlin.Float = f1 + f2
  }
}
