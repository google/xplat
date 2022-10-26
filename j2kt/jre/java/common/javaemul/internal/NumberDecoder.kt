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
package javaemul.internal

internal data class Decode(val radix: Int, val payload: String)

/**
 * This function will determine the radix that the string is expressed in based on the parsing rules
 * defined in the Javadocs for [Integer.decode()]
 */
internal fun decodeNumberString(s: String): Decode {
  var s = s
  val negative: Boolean
  if (s.startsWith("-")) {
    negative = true
    s = s.substring(1)
  } else {
    negative = false
    if (s.startsWith("+")) {
      s = s.substring(1)
    }
  }
  val radix: Int
  if (s.startsWith("0x") || s.startsWith("0X")) {
    s = s.substring(2)
    radix = 16
  } else if (s.startsWith("#")) {
    s = s.substring(1)
    radix = 16
  } else if (s.startsWith("0")) {
    radix = 8
  } else {
    radix = 10
  }
  if (negative) {
    s = "-$s"
  }
  return Decode(radix, s)
}
