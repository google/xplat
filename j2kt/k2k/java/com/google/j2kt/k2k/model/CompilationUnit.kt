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

import com.google.debugging.sourcemap.SourceMapGeneratorV3
import org.jetbrains.kotlin.fir.declarations.FirFile
import org.jetbrains.kotlin.psi.KtFile

/** A compilation unit consisting of a Kotlin file and its captured pre-cleanup source positions. */
class CompilationUnit(
  val fileInfo: FileInfo,
  val ktFile: KtFile,
  captureOriginalText: Boolean = false,
  var firFile: FirFile? = null,
) {
  private val originalText: String? = if (captureOriginalText) ktFile.text else null
  private val trackedNodes: List<TrackedSourceNode> = captureOriginalSourcePositions(ktFile)

  /** Generates a standalone SourceMap V3 JSON mapping original positions to the current AST. */
  fun generateSourceMap(): String {
    val generator = SourceMapGeneratorV3()
    for (mapping in resolveSourceMappings(ktFile, trackedNodes)) {
      generator.addMapping(
        fileInfo.inputPath.name,
        mapping.original.name,
        mapping.original.start,
        mapping.start,
        mapping.end,
      )
    }
    return buildString { generator.appendTo(this, fileInfo.outputPath.name) }
  }

  /** Generates J2CL-style human-readable `.kt.mappings` from original to current AST spans. */
  fun generateReadableSourceMap(): String =
    ReadableSourceMaps.generate(
      checkNotNull(originalText) { "Original text was not captured for ${fileInfo.inputPath}" },
      ktFile.text,
      resolveSourceMappings(ktFile, trackedNodes),
    )
}
