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
package com.google.j2kt.k2k.model

import com.google.debugging.sourcemap.FilePosition

/**
 * Formats [SourceMapping]s into J2CL-style human-readable `.kt.mappings` (`[original] => [cleaned]
 * "name"`).
 */
internal object ReadableSourceMaps {
  fun generate(originalText: String, cleanedText: String, mappings: List<SourceMapping>): String {
    val originalLines = originalText.removeSuffix("\n").split("\n")
    val cleanedLines = cleanedText.removeSuffix("\n").split("\n")

    return buildString {
      for (mapping in mappings) {
        val name = mapping.original.name
        val condense = name != null
        append(extract(mapping.original.start, mapping.original.end, originalLines, condense))
          .append(" => ")
          .append(extract(mapping.start, mapping.end, cleanedLines, condense).trim())
        if (name != null) {
          append(" \"").append(name).append("\"")
        }
        append("\n")
      }
    }
  }

  private fun extract(
    startPos: FilePosition,
    endPos: FilePosition,
    lines: List<String>,
    condense: Boolean,
  ): String {
    if (lines.isEmpty()) return "[]"
    val startLine = startPos.line.coerceIn(0, lines.lastIndex)
    var endLine = endPos.line.coerceIn(startLine, lines.size)
    var fragment = lines[startLine]
    val startColumn = startPos.column.coerceIn(0, fragment.length)
    var endColumn = endPos.column

    if (endLine == lines.size && endColumn == 0) {
      endLine--
      endColumn = -1
    } else {
      endLine = endLine.coerceAtMost(lines.lastIndex)
    }

    if (endLine != startLine || endColumn == -1) {
      val content = buildString {
        append(fragment.substring(startColumn).trimEnd())
        if (condense && startLine + 3 < endLine) {
          append("\n")
            .append(lines[startLine + 1].trimEnd())
            .append("\n...\n")
            .append(lines[endLine - 1].trimEnd())
        } else {
          for (line in startLine + 1 until endLine) {
            append("\n").append(lines[line].trimEnd())
          }
        }
        fragment = lines[endLine]
        val resolvedEndCol =
          if (endColumn == -1) fragment.length else endColumn.coerceIn(0, fragment.length)
        append("\n").append(fragment.substring(0, resolvedEndCol).trimEnd())
      }
      return "[$content]"
    }

    val resolvedEndCol = endColumn.coerceIn(startColumn, fragment.length)
    return "[${fragment.substring(startColumn, resolvedEndCol)}]"
  }
}
