@file:OptIn(ExperimentalObjCRefinement::class)

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

import java.lang.Character
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

// See regular JRE API documentation for other methods in this file.

@HiddenFromObjC
fun StringBuilder.appendCodePoint(codePoint: Int) =
  if (Character.charCount(codePoint) == 1) append(codePoint.toChar())
  else append(Character.highSurrogate(codePoint)).append(Character.lowSurrogate(codePoint))

// Note: The offset+len methods are prefixed with java_ because it's an error to use the original
// JRE methods in Kotlin JVM and Kotlin Native inherits those bans.
@HiddenFromObjC
fun StringBuilder.java_append(str: CharArray, offset: Int, len: Int) =
  this.appendRange(str, offset, offset + len)

@HiddenFromObjC
fun StringBuilder.java_insert(offset: Int, str: CharArray, strOffset: Int, strLen: Int) =
  this.insertRange(offset, str, strOffset, strOffset + strLen)

@HiddenFromObjC
fun StringBuilder.insert(offset: Int, s: CharSequence?, strOffset: Int, strLen: Int) =
  this.insertRange(offset, s ?: s.toString(), strOffset, strLen)
