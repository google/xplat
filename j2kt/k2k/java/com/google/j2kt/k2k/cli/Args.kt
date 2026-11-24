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

import java.io.File
import org.kohsuke.args4j.Argument
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option

internal class Args {
  @Argument(metaVar = "<source files>", required = true)
  var sources: MutableList<File> = mutableListOf()

  @Option(name = "-output", required = true, metaVar = "<path>") lateinit var output: File

  @Option(name = "-classpath", metaVar = "<path>") var classpath: String = ""

  @Option(name = "-readablesourcemaps") var readableSourceMaps: Boolean = false

  companion object {
    fun parse(rawArgs: Array<String>): Args =
      Args().apply { CmdLineParser(this).parseArgument(expandFlagFile(rawArgs)) }

    private fun expandFlagFile(args: Array<String>): List<String> = args.flatMap { arg ->
      if (arg.startsWith("@")) {
        File(arg.substring(1)).readLines().filter { it.isNotEmpty() }
      } else {
        listOf(arg)
      }
    }
  }
}
