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
package java.util.regex

import kotlin.text.Regex

/** Kotlin Pattern implementation backed by kotlin.text.Regex. */
class Pattern private constructor(private val pattern: String, private val flags: Int) {
  internal val regex: Regex

  init {
    if (flags and ALL_FLAGS.inv() != 0) {
      throw IllegalArgumentException("Unknown flag 0x${flags.toUInt().toString(16)}")
    }
    if (flags and UNICODE_CHARACTER_CLASS != 0) {
      throw IllegalArgumentException("UNICODE_CHARACTER_CLASS flag not supported")
    }
    try {
      val options = buildSet {
        if (flags and CASE_INSENSITIVE != 0) add(RegexOption.IGNORE_CASE)
        if (flags and MULTILINE != 0) add(RegexOption.MULTILINE)
        if (flags and LITERAL != 0) {
          add(RegexOption.LITERAL)
        } else {
          if (flags and CANON_EQ != 0) add(RegexOption.CANON_EQ)
          if (flags and COMMENTS != 0) add(RegexOption.COMMENTS)
          if (flags and DOTALL != 0) add(RegexOption.DOT_MATCHES_ALL)
          if (flags and UNIX_LINES != 0) add(RegexOption.UNIX_LINES)
        }
        // UNICODE_CASE is already the default for RegexOption.IGNORE_CASE in Kotlin/Native.
      }
      regex = Regex(pattern, options)
    } catch (e: RuntimeException) {
      throw PatternSyntaxException(e.message ?: "", pattern, -1)
    }
  }

  fun flags() = flags

  fun matcher(input: CharSequence): Matcher = Matcher(this, input)

  fun split(input: CharSequence, limit: Int = 0): Array<String> {
    if (limit == 1) {
      return arrayOf(input.toString())
    }
    var index = 0
    val matchLimited = limit > 0
    val matchList = ArrayList<String>()
    val m = matcher(input)

    while (m.find()) {
      if (!matchLimited || matchList.size < limit - 1) {
        if (index == 0 && index == m.start() && m.start() == m.end()) {
          // JDK 8+ omits the leading empty string for a zero-width match at index 0.
          continue
        }
        matchList.add(input.subSequence(index, m.start()).toString())
        index = m.end()
      } else if (matchList.size == limit - 1) {
        matchList.add(input.subSequence(index, input.length).toString())
        index = m.end()
        break
      }
    }

    if (index == 0) {
      return arrayOf(input.toString())
    }

    if (!matchLimited || matchList.size < limit) {
      matchList.add(input.subSequence(index, input.length).toString())
    }

    var resultSize = matchList.size
    if (limit == 0) {
      while (resultSize > 0 && matchList[resultSize - 1].isEmpty()) {
        resultSize--
      }
    }
    return matchList.subList(0, resultSize).toTypedArray()
  }

  fun pattern() = pattern

  override fun toString() = pattern

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

    private const val ALL_FLAGS =
      UNIX_LINES or
        CASE_INSENSITIVE or
        COMMENTS or
        MULTILINE or
        LITERAL or
        DOTALL or
        UNICODE_CASE or
        CANON_EQ or
        UNICODE_CHARACTER_CLASS

    fun compile(regex: String) = Pattern(regex, 0)

    fun compile(regex: String, flags: Int) = Pattern(regex, flags)

    fun matches(regex: String, input: CharSequence) = compile(regex).matcher(input).matches()

    fun quote(s: String) = Regex.escape(s)
  }
}
