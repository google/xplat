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

import java.lang.Character as JavaCharacter

/**
 * Pseudo-constructor for emulated java.lang.Character.
 *
 * See regular JRE API documentation for other methods in this file.
 */
operator fun Char.Companion.invoke(c: Char): Char = JavaCharacter(c) as Char

fun Char.shr(pos: Int): Int = toInt().shr(pos)

fun Char.ushr(pos: Int): Int = toInt().ushr(pos)

operator fun Char.unaryMinus(): Int = toInt().unaryMinus()
