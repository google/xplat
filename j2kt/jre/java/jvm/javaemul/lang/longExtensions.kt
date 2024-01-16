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
import kotlin.math.max
import kotlin.math.min

operator fun Long.Companion.invoke(l: Long): Long = java.lang.Long(l) as Long

val Long.Companion.TYPE: Class<Long>
  get() = java.lang.Long.TYPE

fun Long.Companion.valueOf(l: Long): Long = java.lang.Long.valueOf(l)

fun Long.Companion.valueOf(s: String): Long = java.lang.Long.valueOf(s)

fun Long.Companion.valueOf(s: String, radix: Int): Long = java.lang.Long.valueOf(s, radix)

fun Long.Companion.compare(l1: Long, l2: Long): Int = java.lang.Long.compare(l1, l2)

fun Long.Companion.decode(s: String): Long = java.lang.Long.decode(s)

fun Long.Companion.toString(l: Long): String = java.lang.Long.toString(l)

fun Long.Companion.toString(l: Long, radix: Int): String = java.lang.Long.toString(l, radix)

fun Long.Companion.toOctalString(l: Long): String = java.lang.Long.toOctalString(l)

fun Long.Companion.toHexString(l: Long): String = java.lang.Long.toHexString(l)

fun Long.Companion.toUnsignedString(l: Long): String = l.toULong().toString()

// java.lang.Long.toUnsignedString(l) not available on J2CL

fun Long.Companion.toUnsignedString(l: Long, radix: Int): String = l.toULong().toString(radix)

// java.lang.Long.toUnsignedString(l, radix) not available on J2CL

fun Long.Companion.parseLong(s: String): Long = java.lang.Long.parseLong(s)

fun Long.Companion.parseLong(s: String, radix: Int): Long = java.lang.Long.parseLong(s, radix)

fun Long.Companion.rotateLeft(l: Long, bitCount: Int): Long = java.lang.Long.rotateLeft(l, bitCount)

fun Long.Companion.rotateRight(l: Long, bitCount: Int): Long =
  java.lang.Long.rotateRight(l, bitCount)

fun Long.Companion.hashCode(l: Long): Int = java.lang.Long.hashCode(l)

fun Long.Companion.numberOfLeadingZeros(l: Long): Int = java.lang.Long.numberOfLeadingZeros(l)

fun Long.Companion.numberOfTrailingZeros(l: Long): Int = java.lang.Long.numberOfTrailingZeros(l)

fun Long.Companion.bitCount(l: Long): Int = java.lang.Long.bitCount(l)

fun Long.Companion.signum(l: Long): Int = java.lang.Long.signum(l)

fun Long.Companion.min(l1: Long, l2: Long): Long = min(l1, l2)

fun Long.Companion.max(l1: Long, l2: Long): Long = max(l1, l2)

fun Long.Companion.sum(l1: Long, l2: Long): Long = l1 + l2
