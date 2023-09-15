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
 * Pseudo-constructor for emulated java.lang.Short.
 *
 * See regular JRE API documentation for other methods in this file.
 */
operator fun Short.Companion.invoke(s: Short): Short = java.lang.Short(s) as Short

val Short.Companion.TYPE: Class<Short>
  get() = java.lang.Short.TYPE

fun Short.Companion.valueOf(s: Short): Short = java.lang.Short.valueOf(s)

fun Short.Companion.valueOf(str: String): Short = java.lang.Short.valueOf(str)

fun Short.Companion.valueOf(s: String, radix: Int): Short = java.lang.Short.valueOf(s, radix)

fun Short.Companion.compare(s1: Short, s2: Short): Int = java.lang.Short.compare(s1, s2)

fun Short.Companion.decode(s: String): Short = java.lang.Short.decode(s)

fun Short.Companion.toString(s: Short): String = java.lang.Short.toString(s)

fun Short.Companion.parseShort(str: String): Short = java.lang.Short.parseShort(str)

fun Short.Companion.parseShort(s: String, radix: Int): Short = java.lang.Short.parseShort(s, radix)

fun Short.Companion.hashCode(s: Short): Int = java.lang.Short.hashCode(s)
