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
package com.google.j2kt.k2k.frontend

import com.intellij.openapi.Disposable
import java.io.File
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.arguments.parseCommandLineArguments
import org.jetbrains.kotlin.cli.common.arguments.validateArguments
import org.jetbrains.kotlin.cli.common.diagnosticsCollector
import org.jetbrains.kotlin.cli.common.setupCommonArguments
import org.jetbrains.kotlin.cli.create
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoots
import org.jetbrains.kotlin.cli.jvm.setupJvmSpecificArguments
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.moduleName
import org.jetbrains.kotlin.metadata.deserialization.MetadataVersion

/** Factory for creating a standalone [KotlinCoreEnvironment] configured for K2 FIR analysis. */
internal object FrontendKotlinCoreEnvironment {
  fun create(
    disposable: Disposable,
    classpath: List<File> = emptyList(),
    kotlincOptions: List<String> = emptyList(),
  ): KotlinCoreEnvironment {
    val configuration =
      CompilerConfiguration.create(messageCollector = FrontendMessageCollector).apply {
        if (kotlincOptions.isNotEmpty()) {
          val arguments = K2JVMCompilerArguments()
          parseCommandLineArguments(kotlincOptions, arguments)
          validateArguments(arguments.errors)?.let { error(it) }
          setupCommonArguments(arguments) { versionArray -> MetadataVersion(*versionArray) }
          setupJvmSpecificArguments(arguments)
          arguments.moduleName?.let { moduleName = it }
          check(!diagnosticsCollector.hasErrors) {
            diagnosticsCollector.diagnostics
              .filter { it.severity.isError }
              .joinToString("\n") { it.renderMessage() }
          }
        }
        addJvmClasspathRoots(classpath)
      }

    // KotlinCoreEnvironment.createForProduction is guarded by an internal @RequiresOptIn annotation
    // in kotlin-compiler, but remains required to initialize the standalone PSI environment.
    @Suppress("OPT_IN_USAGE_ERROR")
    return KotlinCoreEnvironment.createForProduction(
      disposable,
      configuration,
      EnvironmentConfigFiles.JVM_CONFIG_FILES,
    )
  }
}
