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
import kotlin.jvm.javaPrimitiveType
import kotlin.math.max
import kotlin.math.min

/**
 * Pseudo-constructor for emulated java.lang.Float.
 *
 * See regular JRE API documentation for other methods in this file.
 */
operator fun Float.Companion.invoke(f: Float): Float = f

operator fun Float.Companion.invoke(s: String): Float = s.toFloat()

val Float.Companion.TYPE: Class<Float>
  get() = Float::class.javaPrimitiveType!!

val Float.Companion.MAX_EXPONENT: Int
  get() = 127

val Float.Companion.MIN_EXPONENT: Int
  get() = -126

val Float.Companion.MIN_NORMAL: Float
  get() = 1.1754943508222875e-38F // 0x1.0p-126f

fun Float.Companion.valueOf(f: Float): Float = f

fun Float.Companion.valueOf(s: String): Float = s.toFloat()

fun Float.Companion.compare(f1: Float, f2: Float): Int = f1.compareTo(f2)

fun Float.Companion.toString(f: Float): String = f.toString()

fun Float.Companion.parseFloat(s: String): Float = s.toFloat()

fun Float.Companion.hashCode(f: Float): Int = f.hashCode()

fun Float.Companion.isNaN(f: Float): Boolean = f.isNaN()

fun Float.Companion.isInfinite(f: Float): Boolean = f.isInfinite()

fun Float.Companion.isFinite(f: Float): Boolean = f.isFinite()

fun Float.Companion.floatToIntBits(f: Float): Int = f.toBits()

fun Float.Companion.floatToRawIntBits(f: Float): Int = f.toRawBits()

fun Float.Companion.min(f1: Float, f2: Float): Float = min(f1, f2)

fun Float.Companion.max(f1: Float, f2: Float): Float = max(f1, f2)

fun Float.Companion.sum(f1: Float, f2: Float): Float = f1 + f2

inline fun Float.toInt_toByte() = toInt().toByte()

inline fun Float.toInt_toShort() = toInt().toShort()
