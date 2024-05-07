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
import java.util.Locale

// The CharArray constructors are deliberately using nullable CharArray parameters to avoid
// triggering a compiler diagnostic that forbids use of these deprecated constructors in Kotlin 🤞.
/**
 * Pseudo-constructor for emulated java.lang.String.
 *
 * We assume that the platform encoding is UTF-8 here (like J2Objc and Android do).
 *
 * See regular JRE API documentation for other methods in this file.
 */
operator fun String.Companion.invoke(a: CharArray?): String = java.lang.String(a) as String

operator fun String.Companion.invoke(a: CharArray?, offset: Int, len: Int): String =
  java.lang.String(a, offset, len) as String

operator fun String.Companion.invoke(a: ByteArray) = java.lang.String(a) as String

operator fun String.Companion.invoke(a: ByteArray, offset: Int, len: Int) =
  java.lang.String(a, offset, len) as String

operator fun String.Companion.invoke(a: IntArray, offset: Int, len: Int): String =
  java.lang.String(a, offset, len) as String

operator fun String.Companion.invoke(
  a: ByteArray,
  offset: Int,
  len: Int,
  charset: Charset,
): String = java.lang.String(a, offset, len, charset) as String

operator fun String.Companion.invoke(a: ByteArray, charset: Charset) =
  java.lang.String(a, charset) as String

operator fun String.Companion.invoke(a: ByteArray, charsetName: String) =
  java.lang.String(a, charsetName) as String

operator fun String.Companion.invoke(a: ByteArray, offset: Int, len: Int, charsetName: String) =
  java.lang.String(a, offset, len, charsetName) as String

operator fun String.Companion.invoke(s: String) = java.lang.String(s) as String

fun String.Companion.join(delimiter: CharSequence, elements: Iterable<CharSequence>): String =
  java.lang.String.join(delimiter, elements)

fun String.Companion.join(delimiter: CharSequence, vararg elements: CharSequence): String =
  java.lang.String.join(delimiter, *elements)

fun String.Companion.valueOf(c: Char): String = java.lang.String.valueOf(c)

fun String.Companion.valueOf(a: Any?): String = java.lang.String.valueOf(a)

fun String.Companion.valueOf(data: CharArray): String = java.lang.String.valueOf(data)

fun String.Companion.valueOf(data: CharArray, offset: Int, count: Int) =
  java.lang.String.valueOf(data, offset, count)

fun String.Companion.copyValueOf(data: CharArray) = java.lang.String.copyValueOf(data)

fun String.Companion.copyValueOf(data: CharArray, offset: Int, count: Int): String =
  java.lang.String.copyValueOf(data, offset, count)

fun String.equalsIgnoreCase(str: String?) = asJavaString().equalsIgnoreCase(str)

fun String.codePointAt(index: Int) = asJavaString().codePointAt(index)

fun String.compareToIgnoreCase(str: String): Int = asJavaString().compareToIgnoreCase(str)

fun String.getBytes(): ByteArray = asJavaString().getBytes()

fun String.getBytes(charsetName: String): ByteArray = asJavaString().getBytes(charsetName)

fun String.getBytes(charset: Charset): ByteArray = asJavaString().getBytes(charset)

fun String.toUpperCase(locale: Locale): String = asJavaString().toUpperCase(locale)

fun String.toLowerCase(locale: Locale): String = asJavaString().toLowerCase(locale)

fun String.getChars(start: Int, end: Int, buffer: CharArray, index: Int) =
  asJavaString().getChars(start, end, buffer, index)

fun String.indexOf(codePoint: Int, fromIndex: Int = 0): Int =
  asJavaString().indexOf(codePoint, fromIndex)

fun String.lastIndexOf(codePoint: Int, fromIndex: Int = Int.MAX_VALUE): Int =
  asJavaString().lastIndexOf(codePoint, fromIndex)

fun String.replaceAll(regex: String, replacement: String): String =
  asJavaString().replaceAll(regex, replacement)

fun String.regionMatches(
  ignoreCase: Boolean,
  thisOffset: Int,
  other: String,
  otherOffset: Int,
  length: Int,
): Boolean = asJavaString().regionMatches(ignoreCase, thisOffset, other, otherOffset, length)

fun String.java_trim(): String = asJavaString().trim()

fun String.java_matches(regex: String) = asJavaString().matches(regex)

fun String.split(regex: String): Array<String> = asJavaString().split(regex)

fun String.split(regex: String, limit: Int): Array<String> = asJavaString().split(regex, limit)

fun String.java_replace(target: CharSequence, replacement: CharSequence): String =
  asJavaString().replace(target, replacement)

private fun String.asJavaString(): java.lang.String = this as java.lang.String
