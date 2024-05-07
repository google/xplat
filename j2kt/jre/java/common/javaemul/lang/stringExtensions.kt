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

import java.nio.charset.Charset

// The CharArray constructors are deliberately using nullable CharArray parameters to avoid
// triggering a compiler diagnostic that forbids use of these deprecated constructors in Kotlin 🤞.
/**
 * Pseudo-constructor for emulated java.lang.String.
 *
 * We assume that the platform encoding is UTF-8 here (like J2Objc and Android do).
 *
 * See regular JRE API documentation for other methods in this file.
 */
inline operator fun String.Companion.invoke(a: CharArray?): String = java.lang.String(a!!) as String

inline operator fun String.Companion.invoke(a: CharArray?, offset: Int, len: Int): String =
  java.lang.String(a!!, offset, len) as String

inline operator fun String.Companion.invoke(a: ByteArray) = java.lang.String(a) as String

inline operator fun String.Companion.invoke(a: ByteArray, offset: Int, len: Int) =
  java.lang.String(a, offset, len) as String

inline operator fun String.Companion.invoke(a: IntArray, offset: Int, len: Int): String =
  java.lang.String(a, offset, len) as String

inline operator fun String.Companion.invoke(
  a: ByteArray,
  offset: Int,
  len: Int,
  charset: Charset,
): String = java.lang.String(a, offset, len, charset) as String

inline operator fun String.Companion.invoke(a: ByteArray, charset: Charset) =
  java.lang.String(a, charset) as String

inline operator fun String.Companion.invoke(a: ByteArray, charsetName: String) =
  java.lang.String(a, charsetName) as String

inline operator fun String.Companion.invoke(
  a: ByteArray,
  offset: Int,
  len: Int,
  charsetName: String,
) = java.lang.String(a, offset, len, charsetName) as String

inline operator fun String.Companion.invoke(s: String) = java.lang.String(s) as String

inline operator fun String.Companion.invoke(sb: StringBuilder) = java.lang.String(sb) as String
