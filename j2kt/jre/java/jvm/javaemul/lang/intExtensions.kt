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

/**
 * Pseudo-constructor for emulated java.lang.Int.
 *
 * See regular JRE API documentation for other methods in this file.
 */
operator fun Int.Companion.invoke(i: Int): Int = java.lang.Integer(i) as Int

val Int.Companion.TYPE: Class<Int>
  get() = java.lang.Integer.TYPE

fun Int.Companion.valueOf(i: Int): Int = java.lang.Integer.valueOf(i)

fun Int.Companion.valueOf(s: String): Int = java.lang.Integer.valueOf(s)

fun Int.Companion.valueOf(s: String, radix: Int): Int = java.lang.Integer.valueOf(s, radix)

fun Int.Companion.compare(i1: Int, i2: Int): Int = java.lang.Integer.compare(i1, i2)

fun Int.Companion.decode(s: String): Int = java.lang.Integer.decode(s)

fun Int.Companion.toString(i: Int): String = java.lang.Integer.toString(i)

fun Int.Companion.toString(i: Int, radix: Int): String = java.lang.Integer.toString(i, radix)

fun Int.Companion.toHexString(i: Int): String = java.lang.Integer.toHexString(i)

fun Int.Companion.parseInt(s: String): Int = java.lang.Integer.parseInt(s)

fun Int.Companion.parseInt(s: String, radix: Int): Int = java.lang.Integer.parseInt(s, radix)

fun Int.Companion.rotateLeft(i: Int, bitCount: Int): Int = java.lang.Integer.rotateLeft(i, bitCount)

fun Int.Companion.rotateRight(i: Int, bitCount: Int): Int =
  java.lang.Integer.rotateRight(i, bitCount)

fun Int.Companion.hashCode(i: Int): Int = java.lang.Integer.hashCode(i)

fun Int.Companion.numberOfLeadingZeros(i: Int): Int = java.lang.Integer.numberOfLeadingZeros(i)

fun Int.Companion.numberOfTrailingZeros(i: Int): Int = java.lang.Integer.numberOfTrailingZeros(i)

fun Int.Companion.highestOneBit(i: Int): Int = java.lang.Integer.highestOneBit(i)

fun Int.Companion.bitCount(i: Int): Int = java.lang.Integer.bitCount(i)

fun Int.Companion.signum(i: Int): Int = java.lang.Integer.signum(i)
