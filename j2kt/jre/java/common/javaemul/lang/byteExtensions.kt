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

import java.lang.Byte as JavaByte

/**
 * Pseudo-constructor for emulated java.lang.Byte.
 *
 * See regular JRE API documentation for other methods in this file.
 */
operator fun Byte.Companion.invoke(b: Byte): Byte =
  // In Kotlin JVM, java.lang.Byte(b) is not assignable to kotlin.Byte without the extra cast.
  JavaByte(b) as Byte

fun Byte.shl(pos: Int): Int {
  val intVal = this.toInt()
  return intVal.shl(pos)
}

fun Byte.shr(pos: Int): Int {
  val intVal = this.toInt()
  return intVal.shr(pos)
}

fun Byte.and(other: Byte): Int {
  val intVal = this.toInt()
  val otherIntVal = other.toInt()
  return intVal.and(otherIntVal)
}
