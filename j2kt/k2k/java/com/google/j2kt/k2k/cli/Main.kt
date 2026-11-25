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
package com.google.j2kt.k2k.cli

import com.google.j2kt.k2k.Cleaner
import com.google.j2kt.k2k.model.FileInfo
import java.io.File

object Main {
  @JvmStatic
  fun main(rawArgs: Array<String>) {
    val args = Args.parse(rawArgs)
    val sources =
      args.sources.map { inFile ->
        FileInfo(inputPath = inFile, outputPath = File(args.output, inFile.path))
      }
    val classpath = args.classpath.split(File.pathSeparator).filter { it.isNotEmpty() }.map(::File)
    Cleaner.clean(
      sources = sources,
      classpath = classpath,
      kotlincOptions = args.kotlincOptions,
      readableSourceMaps = args.readableSourceMaps,
    )
  }
}
