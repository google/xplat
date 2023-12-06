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
@file:OptIn(ExperimentalObjCName::class)

package java.util.regex

import kotlin.experimental.ExperimentalObjCName
import kotlin.math.max
import kotlin.native.ObjCName
import kotlin.text.Regex

/** Kotlin Pattern implementation backed by kotlin.text.Regex. */
@ObjCName("J2ktJavaUtilRegexPattern", exact = true)
class Pattern private constructor(pattern: String, private val flags: Int) {
  internal val regex: Regex

  init {
    try {
      val expr = StringBuilder()
      val options = mutableSetOf<RegexOption>()
      var flag = 1
      while (flag <= 256) {
        when (flags and flag) {
          0 -> {} // Flag not set
          CASE_INSENSITIVE -> options.add(RegexOption.IGNORE_CASE)
          COMMENTS -> expr.append("(?x)")
          DOTALL -> expr.append("(?s)")
          MULTILINE -> options.add(RegexOption.MULTILINE)
          UNICODE_CASE -> expr.append("(?u)")
          UNICODE_CHARACTER_CLASS -> expr.append("(?U)")
          UNIX_LINES -> expr.append("(?d)")
          else -> throw IllegalStateException("Unsupported flag $flag")
        }
        flag *= 2
      }
      expr.append(pattern)
      regex = Regex(expr.toString(), options)
    } catch (e: RuntimeException) {
      throw PatternSyntaxException(e.message ?: "", pattern, -1)
    }
  }

  fun flags() = flags

  fun matcher(input: CharSequence): Matcher = Matcher(this, input)

  fun split(input: CharSequence): Array<String> = regex.split(input).toTypedArray()

  fun split(input: CharSequence, limit: Int): Array<String> {
    val split = regex.split(input, max(0, limit))
    if (limit != 0 || !split.last().isEmpty() || input.isEmpty()) {
      return split.toTypedArray()
    }
    // For limit = 0, trim trailing empty strings (unless the input was empty itself)
    var newSize = 0
    for (i in split.size - 1 downTo 0) {
      if (!split[i].isEmpty()) {
        newSize = i + 1
        break
      }
    }
    return split.subList(0, newSize).toTypedArray()
  }

  fun pattern() = regex.pattern

  override fun toString() = regex.toString()

  companion object {

    const val CANON_EQ = 128
    const val CASE_INSENSITIVE = 2
    const val COMMENTS = 4
    const val DOTALL = 32
    const val LITERAL = 16
    const val MULTILINE = 8
    const val UNICODE_CASE = 64
    const val UNICODE_CHARACTER_CLASS = 256
    const val UNIX_LINES = 1

    fun compile(regex: String) = Pattern(regex, 0)

    fun compile(regex: String, flags: Int) = Pattern(regex, flags)

    fun matches(regex: String, input: CharSequence) = Regex(regex).matches(input)

    fun quote(s: String) = Regex.escape(s)
  }
}
