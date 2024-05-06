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
