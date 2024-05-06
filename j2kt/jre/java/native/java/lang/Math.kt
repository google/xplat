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
@file:OptIn(ExperimentalObjCName::class)

package java.lang

import javaemul.internal.InternalPreconditions.Companion.checkCriticalArithmetic
import kotlin.experimental.ExperimentalObjCName
import kotlin.math.IEEErem
import kotlin.math.nextTowards
import kotlin.math.nextUp
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.math.ulp
import kotlin.math.withSign
import kotlin.native.ObjCName

/**
 * Implementation of java.lang.Math for Kotlin Native.
 *
 * This is only meant to be used in generated code. See regular JRE documentation for javadoc.
 */
@ObjCName("J2ktJavaLangMath", exact = true)
object Math {
  private const val FLOAT_MANTISSA_BITS: Int = 23
  private const val FLOAT_EXPONENT_BIAS: Int = 127
  private const val FLOAT_EXPONENT_MASK: Int = 0x7f800000
  private const val DOUBLE_MANTISSA_BITS: Int = 52
  private const val DOUBLE_EXPONENT_BIAS: Int = 1023
  private const val DOUBLE_EXPONENT_MASK: kotlin.Long = 0x7ff0000000000000L

  const val E: kotlin.Double = kotlin.math.E

  const val PI: kotlin.Double = kotlin.math.PI

  private const val MINUS_ZERO_LONG_BITS = Long.MIN_VALUE // 0x8000000000000000

  private const val PI_OVER_180 = PI / 180.0

  private const val PI_UNDER_180 = 180.0 / PI

  fun abs(x: kotlin.Double): kotlin.Double = kotlin.math.abs(x)

  fun abs(x: kotlin.Float): kotlin.Float = kotlin.math.abs(x)

  fun abs(x: Int): Int = kotlin.math.abs(x)

  fun abs(x: kotlin.Long): kotlin.Long = kotlin.math.abs(x)

  fun addExact(x: Int, y: Int): Int {
    val r = x + y
    // "Hacker's Delight" 2-12 Overflow if both arguments have the opposite sign of the result
    checkCriticalArithmetic((x xor r) and (y xor r) >= 0)
    return r
  }

  fun addExact(x: kotlin.Long, y: kotlin.Long): kotlin.Long {
    val r = x + y
    // "Hacker's Delight" 2-12 Overflow if both arguments have the opposite sign of the result
    checkCriticalArithmetic((x xor r) and (y xor r) >= 0)
    return r
  }

  fun acos(d: kotlin.Double): kotlin.Double = kotlin.math.acos(d)

  fun asin(d: kotlin.Double): kotlin.Double = kotlin.math.asin(d)

  fun atan(d: kotlin.Double): kotlin.Double = kotlin.math.atan(d)

  fun atan2(y: kotlin.Double, x: kotlin.Double) = kotlin.math.atan2(y, x)

  fun cbrt(d: kotlin.Double): kotlin.Double {
    return if (d == 0.0 || !d.isFinite()) d else d.pow(1.0 / 3.0)
  }

  fun ceil(d: kotlin.Double): kotlin.Double = kotlin.math.ceil(d)

  fun cos(d: kotlin.Double): kotlin.Double = kotlin.math.cos(d)

  fun cosh(d: kotlin.Double): kotlin.Double = kotlin.math.cosh(d)

  fun decrementExact(i: Int): Int {
    checkCriticalArithmetic(i != Int.MIN_VALUE)
    return i - 1
  }

  fun decrementExact(l: kotlin.Long): kotlin.Long {
    checkCriticalArithmetic(l != kotlin.Long.MIN_VALUE)
    return l - 1
  }

  fun exp(d: kotlin.Double): kotlin.Double = kotlin.math.exp(d)

  fun expm1(d: kotlin.Double): kotlin.Double = kotlin.math.expm1(d)

  fun floor(x: kotlin.Double) = kotlin.math.floor(x)

  fun floorDiv(dividend: Int, divisor: Int): Int = dividend.floorDiv(divisor)

  fun floorDiv(dividend: kotlin.Long, divisor: kotlin.Long): kotlin.Long =
    dividend.floorDiv(divisor)

  fun floorMod(dividend: Int, divisor: Int): Int = dividend.mod(divisor)

  fun floorMod(dividend: kotlin.Long, divisor: kotlin.Long): kotlin.Long = dividend.mod(divisor)

  fun hypot(x: kotlin.Double, y: kotlin.Double): kotlin.Double = kotlin.math.hypot(x, y)

  fun IEEEremainder(x: kotlin.Double, y: kotlin.Double): kotlin.Double = x.IEEErem(y)

  fun incrementExact(i: Int): Int {
    checkCriticalArithmetic(i != Int.MAX_VALUE)
    return i + 1
  }

  fun incrementExact(l: kotlin.Long): kotlin.Long {
    checkCriticalArithmetic(l != kotlin.Long.MAX_VALUE)
    return l + 1
  }

  fun log(d: kotlin.Double): kotlin.Double = kotlin.math.log(d, kotlin.math.E)

  fun log10(d: kotlin.Double): kotlin.Double = kotlin.math.log10(d)

  fun log1p(d: kotlin.Double): kotlin.Double =
    if (d.toBits() == MINUS_ZERO_LONG_BITS) -0.0 else kotlin.math.log(d + 1.0, kotlin.math.E)

  fun max(x: kotlin.Double, y: kotlin.Double): kotlin.Double = kotlin.math.max(x, y)

  fun max(x: kotlin.Float, y: kotlin.Float): kotlin.Float = kotlin.math.max(x, y)

  fun max(x: Int, y: Int): Int = kotlin.math.max(x, y)

  fun max(x: kotlin.Long, y: kotlin.Long): kotlin.Long = kotlin.math.max(x, y)

  fun min(x: kotlin.Double, y: kotlin.Double): kotlin.Double = kotlin.math.min(x, y)

  fun min(x: kotlin.Float, y: kotlin.Float): kotlin.Float = kotlin.math.min(x, y)

  fun min(x: Int, y: Int): Int = kotlin.math.min(x, y)

  fun min(x: kotlin.Long, y: kotlin.Long): kotlin.Long = kotlin.math.min(x, y)

  fun multiplyExact(x: Int, y: Int): Int {
    val lr = x.toLong() * y.toLong()
    val r = lr.toInt()
    checkCriticalArithmetic(r.toLong() == lr)
    return r
  }

  fun multiplyExact(x: kotlin.Long, y: kotlin.Long): kotlin.Long {
    if (y == -1L) {
      return negateExact(x)
    }
    if (y == 0L) {
      return 0
    }
    val r: kotlin.Long = x * y
    checkCriticalArithmetic(r / y == x)
    return r
  }

  fun negateExact(x: Int): Int {
    checkCriticalArithmetic(x != Int.MIN_VALUE)
    return -x
  }

  fun negateExact(x: kotlin.Long): kotlin.Long {
    checkCriticalArithmetic(x != kotlin.Long.MIN_VALUE)
    return -x
  }

  fun pow(x: kotlin.Double, y: kotlin.Double) = if (y.isNaN()) kotlin.Double.NaN else x.pow(y)

  fun rint(d: kotlin.Double): kotlin.Double = kotlin.math.round(d)

  fun round(d: kotlin.Double): kotlin.Long = if (d.isNaN()) 0L else d.roundToLong()

  fun round(f: kotlin.Float): Int = if (f.isNaN()) 0 else f.roundToInt()

  fun signum(d: kotlin.Double): kotlin.Double = kotlin.math.sign(d)

  fun signum(f: kotlin.Float): kotlin.Float = kotlin.math.sign(f)

  fun sin(d: kotlin.Double): kotlin.Double = kotlin.math.sin(d)

  fun sinh(d: kotlin.Double): kotlin.Double = kotlin.math.sinh(d)

  fun sqrt(d: kotlin.Double): kotlin.Double = kotlin.math.sqrt(d)

  fun subtractExact(x: Int, y: Int): Int {
    val r = x - y
    // "Hacker's Delight" Overflow if the arguments have different signs and
    // the sign of the result is different than the sign of x
    checkCriticalArithmetic((x xor y) and (x xor r) >= 0)
    return r
  }

  fun subtractExact(x: kotlin.Long, y: kotlin.Long): kotlin.Long {
    val r = x - y
    // "Hacker's Delight" Overflow if the arguments have different signs and
    // the sign of the result is different than the sign of x
    checkCriticalArithmetic((x xor y) and (x xor r) >= 0)
    return r
  }

  fun tan(d: kotlin.Double): kotlin.Double = kotlin.math.tan(d)

  fun tanh(d: kotlin.Double): kotlin.Double = kotlin.math.tanh(d)

  fun random(): kotlin.Double = kotlin.random.Random.nextDouble()

  fun toRadians(angdeg: kotlin.Double): kotlin.Double = angdeg * PI_OVER_180

  fun toDegrees(angrad: kotlin.Double): kotlin.Double = angrad * PI_UNDER_180

  fun toIntExact(x: kotlin.Long): Int {
    val ix = x.toInt()
    checkCriticalArithmetic(ix.toLong() == x)
    return ix
  }

  fun ulp(d: kotlin.Double): kotlin.Double = d.ulp

  fun ulp(f: kotlin.Float): kotlin.Float = f.ulp

  fun copySign(magnitude: kotlin.Double, sign: kotlin.Double): kotlin.Double =
    magnitude.withSign(sign)

  fun copySign(magnitude: kotlin.Float, sign: kotlin.Float): kotlin.Float = magnitude.withSign(sign)

  fun getExponent(d: kotlin.Double): Int {
    var bits = d.toRawBits() and DOUBLE_EXPONENT_MASK shr DOUBLE_MANTISSA_BITS
    return bits.toInt() - DOUBLE_EXPONENT_BIAS
  }

  fun getExponent(f: kotlin.Float): Int {
    var bits = f.toRawBits() and FLOAT_EXPONENT_MASK shr FLOAT_MANTISSA_BITS
    return bits - FLOAT_EXPONENT_BIAS
  }

  fun nextAfter(start: kotlin.Double, direction: kotlin.Double): kotlin.Double =
    start.nextTowards(direction)

  fun nextAfter(start: kotlin.Float, direction: kotlin.Float): kotlin.Float =
    start.nextTowards(direction)

  fun nextUp(d: kotlin.Double): kotlin.Double = d.nextUp()

  fun nextUp(f: kotlin.Float): kotlin.Float = f.nextUp()

  fun scalb(d: kotlin.Double, scaleFactor: Int): kotlin.Double {
    return if (scaleFactor >= 31 || scaleFactor <= -31) {
      d * (2.0).pow(scaleFactor.toDouble())
    } else if (scaleFactor > 0) {
      d * (1 shl scaleFactor)
    } else if (scaleFactor == 0) {
      d
    } else {
      d / (1 shl -scaleFactor)
    }
  }

  fun scalb(f: kotlin.Float, scaleFactor: Int): kotlin.Float =
    scalb(f.toDouble(), scaleFactor).toFloat()
}
