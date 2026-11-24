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
package com.google.j2kt.k2k

import com.google.j2kt.k2k.frontend.Frontend
import com.google.j2kt.k2k.model.CompilationUnit
import com.google.j2kt.k2k.model.FileInfo
import com.google.j2kt.k2k.model.Library
import java.io.File

object Cleaner {
  fun clean(
    sources: List<FileInfo>,
    classpath: List<File> = emptyList(),
    readableSourceMaps: Boolean = false,
  ) {
    if (sources.isEmpty()) return
    Frontend.parseLibrary(sources, classpath, captureOriginalText = readableSourceMaps).use {
      library ->
      library.writeOutputs(readableSourceMaps)
    }
  }
}

private fun Library.writeOutputs(readableSourceMaps: Boolean) {
  for (unit in compilationUnits) {
    unit.writeOutputs(readableSourceMaps)
  }
}

private fun CompilationUnit.writeOutputs(readableSourceMaps: Boolean) {
  fileInfo.outputPath.writeOutput(ktFile.text)
  File("${fileInfo.outputPath.path}.map").writeOutput(generateSourceMap())
  if (readableSourceMaps) {
    File("${fileInfo.outputPath.path}.mappings").writeOutput(generateReadableSourceMap())
  }
}

private fun File.writeOutput(content: String) {
  parentFile?.mkdirs()
  writeText(content)
}
