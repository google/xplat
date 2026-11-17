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
import org.jetbrains.kotlin.cli.extensionsStorage
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration

/** Factory for creating a standalone [KotlinCoreEnvironment]. */
internal object FrontendKotlinCoreEnvironment {
  fun create(disposable: Disposable): KotlinCoreEnvironment {
    // CompilerConfiguration(), MESSAGE_COLLECTOR_KEY, and ExtensionStorage() are guarded by
    // internal @RequiresOptIn annotations in kotlin-compiler for standalone CLI setup.
    @Suppress("OPT_IN_USAGE_ERROR")
    val configuration =
      CompilerConfiguration().apply {
        put(CommonConfigurationKeys.MESSAGE_COLLECTOR_KEY, FrontendMessageCollector)
        extensionsStorage = CompilerPluginRegistrar.ExtensionStorage()
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
