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
 * Pseudo-constructor for emulated java.lang.Byte.
 *
 * See regular JRE API documentation for other methods in this file.
 */
operator fun Byte.Companion.invoke(b: Byte): Byte = java.lang.Byte(b) as Byte

val Byte.Companion.TYPE: Class<Byte>
  get() = java.lang.Byte.TYPE

fun Byte.Companion.valueOf(b: Byte): Byte = java.lang.Byte.valueOf(b)

fun Byte.Companion.valueOf(s: String): Byte = java.lang.Byte.valueOf(s)

fun Byte.Companion.valueOf(s: String, radix: Int): Byte = java.lang.Byte.valueOf(s, radix)

fun Byte.Companion.compare(b1: Byte, b2: Byte): Int = java.lang.Byte.compare(b1, b2)

fun Byte.Companion.decode(s: String): Byte = java.lang.Byte.decode(s)

fun Byte.Companion.toString(b: Byte): String = java.lang.Byte.toString(b)

fun Byte.Companion.parseByte(s: String): Byte = java.lang.Byte.parseByte(s)

fun Byte.Companion.parseByte(s: String, radix: Int): Byte = java.lang.Byte.parseByte(s, radix)

fun Byte.Companion.hashCode(b: Byte): Int = java.lang.Byte.hashCode(b)
