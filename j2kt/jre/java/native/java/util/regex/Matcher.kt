/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:OptIn(ExperimentalObjCRefinement::class)

package java.util.regex

import kotlin.experimental.ExperimentalObjCRefinement

/** The result of applying a `Pattern` to a given input. */
class Matcher(private var pattern: Pattern, private var input: CharSequence) {

  /**
   * Holds the start of the region, or 0 if the matching should start at the beginning of the text.
   */
  private var regionStart = 0

  /** Holds the active region of the input. */
  private var region: String = input.toString()

  /** Start index (within [region]) of the previous match, or -1 if no match is active. */
  private var lastMatchStart = -1

  /**
   * End index (within [region]) of the previous match, used as the starting position for find().
   */
  private var lastMatchEnd = 0

  private var matchResult: kotlin.text.MatchResult? = null

  private var appendPosition = 0

  /**
   * Implements a non-terminal append-and-replace step.
   *
   * This method performs the following actions:
   *
   * It reads characters from the input sequence, starting at the append position, and appends them
   * to the given string buffer. It stops after reading the last character preceding the previous
   * match, that is, the character at index start() - 1.
   *
   * It appends the given replacement string to the string buffer.
   *
   * It sets the append position of this matcher to the index of the last character matched, plus
   * one, that is, to end().
   */
  @HiddenFromObjC
  fun appendReplacement(sb: StringBuilder, replacement: String): Matcher {
    val matchStart = start()
    val matchEnd = end()
    sb.append(input.subSequence(appendPosition, matchStart))
    sb.append(replacement)
    appendPosition = matchEnd
    return this
  }

  /**
   * Implements a terminal append-and-replace step.
   *
   * This method reads characters from the input sequence, starting at the append position, and
   * appends them to the given string buffer. It is intended to be invoked after one or more
   * invocations of the appendReplacement method in order to copy the remainder of the input
   * sequence.
   */
  @HiddenFromObjC
  fun appendTail(sb: StringBuilder): StringBuilder {
    sb.append(input.subSequence(appendPosition, input.length))
    appendPosition = input.length
    return sb
  }

  /**
   * Resets the `Matcher`. This results in the region being set to the whole input. Results of a
   * previous find get lost. The next attempt to find an occurrence of the [Pattern] in the string
   * will start at the beginning of the input.
   *
   * @return the `Matcher` itself.
   */
  fun reset(): Matcher {
    return reset(input, 0, input.length)
  }

  /**
   * Provides a new input and resets the `Matcher`. This results in the region being set to the
   * whole input. Results of a previous find get lost. The next attempt to find an occurrence of the
   * [Pattern] in the string will start at the beginning of the input.
   *
   * @param input the new input sequence.
   * @return the `Matcher` itself.
   */
  fun reset(input: CharSequence): Matcher {
    return reset(input, 0, input.length)
  }

  /**
   * Resets the Matcher. A new input sequence and a new region can be specified. Results of a
   * previous find get lost. The next attempt to find an occurrence of the Pattern in the string
   * will start at the beginning of the region. This is the internal version of reset() to which the
   * several public versions delegate.
   *
   * @param input the input sequence.
   * @param start the start of the region.
   * @param end the end of the region.
   * @return the matcher itself.
   */
  private fun reset(input: CharSequence, start: Int, end: Int): Matcher {
    if (start < 0 || end < 0 || start > input.length || end > input.length || start > end) {
      throw IndexOutOfBoundsException()
    }
    this.input = input
    regionStart = start
    region = input.subSequence(start, end).toString()
    matchResult = null
    lastMatchStart = -1
    lastMatchEnd = 0
    appendPosition = 0
    return this
  }

  /**
   * Sets a new pattern for the `Matcher`. Results of a previous find get lost. The next attempt to
   * find an occurrence of the [Pattern] in the string will start at the beginning of the input.
   *
   * @param pattern the new `Pattern`.
   * @return the `Matcher` itself.
   */
  fun usePattern(pattern: Pattern): Matcher {
    this.pattern = pattern
    matchResult = null
    return this
  }

  /**
   * Resets this matcher and sets a region. Only characters inside the region are considered for a
   * match.
   *
   * @param start the first character of the region.
   * @param end the first character after the end of the region.
   * @return the `Matcher` itself.
   */
  fun region(start: Int, end: Int): Matcher {
    return reset(input, start, end)
  }

  /**
   * Returns the [Pattern] instance used inside this matcher.
   *
   * @return the `Pattern` instance.
   */
  fun pattern(): Pattern {
    return pattern
  }

  /**
   * Returns the text that matched a given group of the regular expression. Explicit capturing
   * groups in the pattern are numbered left to right in order of their *opening* parenthesis,
   * starting at 1. The special group 0 represents the entire match (as if the entire pattern is
   * surrounded by an implicit capturing group). For example, "a((b)c)" matching "abc" would give
   * the following groups: <pre> 0 "abc" 1 "bc" 2 "b" </pre> *
   *
   * An optional capturing group that failed to match as part of an overall successful match (for
   * example, "a(b)?c" matching "ac") returns null. A capturing group that matched the empty string
   * (for example, "a(b?)c" matching "ac") returns the empty string.
   *
   * Note: For j2kt-native specifically, this will also return null if the match was empty due to
   * differences in behavior between Java and Kotlin native regexes; see b/353710234.
   *
   * @throws IllegalStateException if no successful match has been made.
   */
  fun group(group: Int): String? = ensureMatch().groups[group]?.value

  /**
   * Returns the text that matched the whole regular expression.
   *
   * @return the text.
   * @throws IllegalStateException if no successful match has been made.
   */
  fun group(): String {
    return group(0)!! // group(0) can only throw or return non-null
  }

  /**
   * Resets this matcher and then attempts to find the next occurrence of the [Pattern] in the
   * input, starting at the specified index.
   *
   * @param start The index in the input at which the find operation is to begin.
   * @return true if (and only if) a match has been found.
   * @throws IndexOutOfBoundsException if [start] is less than zero or greater than the length of
   *   the input sequence.
   */
  fun find(start: Int): Boolean {
    if (start < 0 || start > input.length) {
      throw IndexOutOfBoundsException("Illegal start index")
    }
    reset()
    return op { it.find(region, start) }
  }

  /** Performs a kotlin regex operation and maps the result to the corresponding state here. */
  private fun op(op: (Regex) -> kotlin.text.MatchResult?): Boolean {
    val result = op(pattern.regex)
    matchResult = result
    return if (result != null) {
      lastMatchStart = result.range.first
      lastMatchEnd = result.range.last + 1
      true
    } else {
      lastMatchStart = -1
      false
    }
  }

  /**
   * Returns the next occurrence of the [Pattern] in the input. If a previous match was successful,
   * the method continues the search from the first character following that match in the input.
   * Otherwise it searches either from the region start (if one has been set), or from position 0.
   *
   * @return true if (and only if) a match has been found.
   */
  fun find(): Boolean {
    var nextSearchIndex = lastMatchEnd
    if (nextSearchIndex == lastMatchStart) {
      nextSearchIndex++
    }
    if (nextSearchIndex < 0) {
      nextSearchIndex = 0
    }
    if (nextSearchIndex > region.length) {
      matchResult = null
      return false
    }
    return op { it.find(region, nextSearchIndex) }
  }

  /**
   * Tries to match the [Pattern], starting from the beginning of the region (or the beginning of
   * the input, if no region has been set). Doesn't require the `Pattern` to match against the whole
   * region.
   *
   * @return true if (and only if) the `Pattern` matches.
   */
  fun lookingAt(): Boolean = op { it.matchAt(region, 0) }

  /**
   * Tries to match the [Pattern] against the entire region (or the entire input, if no region has
   * been set).
   *
   * @return true if (and only if) the `Pattern` matches the entire region.
   */
  fun matches(): Boolean = op { it.matchEntire(region) }

  /**
   * Returns the index of the first character of the text that matched a given group.
   *
   * @param group the group, ranging from 0 to groupCount() - 1, with 0 representing the whole
   *   pattern.
   * @return the character index.
   * @throws IllegalStateException if no successful match has been made.
   */
  fun start(group: Int): Int {
    val group = ensureMatch().groups[group]
    return if (group == null) -1 else (group.range.start + regionStart)
  }

  /** Returns the number of capturing groups in this matcher's pattern. */
  fun groupCount() = ensureMatch().groups.size - 1

  /**
   * Returns the index of the first character following the text that matched a given group.
   *
   * @param group the group, ranging from 0 to groupCount() - 1, with 0 representing the whole
   *   pattern.
   * @return the character index.
   * @throws IllegalStateException if no successful match has been made.
   */
  fun end(group: Int): Int {
    val group = ensureMatch().groups[group]
    return if (group == null) -1 else (group.range.endInclusive + 1 + regionStart)
  }

  /**
   * Returns the index of the first character of the text that matched the whole regular expression.
   *
   * @return the character index.
   * @throws IllegalStateException if no successful match has been made.
   */
  fun start(): Int {
    return start(0)
  }

  /**
   * Returns the index of the first character following the text that matched the whole regular
   * expression.
   *
   * @return the character index.
   * @throws IllegalStateException if no successful match has been made.
   */
  fun end(): Int {
    return end(0)
  }

  /**
   * Indicates whether this matcher has anchoring bounds enabled. When anchoring bounds are enabled,
   * the start and end of the input match the '^' and '$' meta-characters, otherwise not. Anchoring
   * bounds are enabled by default.
   *
   * @return true if (and only if) the `Matcher` uses anchoring bounds.
   */
  fun hasAnchoringBounds() = true

  /**
   * Makes sure that a successful match has been made. Is invoked internally from various places in
   * the class.
   *
   * @throws IllegalStateException if no successful match has been made.
   */
  private fun ensureMatch(): kotlin.text.MatchResult {
    val matchResult = matchResult
    if (matchResult == null) {
      throw IllegalStateException("No successful match so far")
    }
    return matchResult
  }

  /**
   * Indicates whether this matcher has transparent bounds enabled. When transparent bounds are
   * enabled, the parts of the input outside the region are subject to lookahead and lookbehind,
   * otherwise they are not. Transparent bounds are disabled by default.
   *
   * @return true if (and only if) the `Matcher` uses anchoring bounds.
   */
  fun hasTransparentBounds() = false

  /**
   * Returns this matcher's region start, that is, the first character that is considered for a
   * match.
   *
   * @return the start of the region.
   */
  fun regionStart() = regionStart

  /**
   * Returns this matcher's region end, that is, the first character that is not considered for a
   * match.
   *
   * @return the end of the region.
   */
  fun regionEnd() = regionStart + region.length

  /**
   * Replaces the first subsequence of the input sequence that matches the pattern with the given
   * replacement string.
   *
   * Note that we don't try to emulate undocumented side effects of the original methods here.
   */
  fun replaceFirst(replacement: String) = pattern.regex.replaceFirst(region, replacement)

  /**
   * Replaces all subsequence of the input sequence matching the pattern with the given replacement
   * string.
   *
   * Note that we don't try to emulate undocumented side effects of the original methods here.
   */
  fun replaceAll(replacement: String) = pattern.regex.replace(region, replacement)

  companion object {
    /**
     * Returns a replacement string for the given one that has all backslashes and dollar signs
     * escaped.
     *
     * @param s the input string.
     * @return the input string, with all backslashes and dollar signs having been escaped.
     */
    fun quoteReplacement(s: String) = Regex.escapeReplacement(s)
  }
}
