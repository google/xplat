@file:OptIn(ExperimentalObjCRefinement::class)

/*
 * Copyright 2026 Google Inc.
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

import java.lang.Character
import java.lang.StringBuffer
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
fun StringBuilder.appendCodePoint(codePoint: Int) =
  if (Character.charCount(codePoint) == 1) append(codePoint.toChar())
  else append(Character.highSurrogate(codePoint)).append(Character.lowSurrogate(codePoint))

@HiddenFromObjC
fun StringBuilder.append(b: StringBuffer?) =
  b?.internalAppendToStringBuilder_pp_java_lang(this) ?: this.append(null)

@HiddenFromObjC
fun StringBuilder.getChars(start: Int, end: Int, buffer: CharArray, index: Int) {
  toCharArray(buffer, index, start, end)
}

@HiddenFromObjC
fun StringBuilder.insert(offset: Int, s: CharSequence?, strOffset: Int, strLen: Int) =
  this.insertRange(offset, s ?: s.toString(), strOffset, strLen)
