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

import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.cli.jvm.compiler.toVfsBasedProjectEnvironment
import org.jetbrains.kotlin.cli.pipeline.jvm.JvmFrontendPipelinePhase
import org.jetbrains.kotlin.config.moduleName
import org.jetbrains.kotlin.fir.DependencyListForCliModule
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.KtFile

internal object FrontendFirSession {
  fun create(environment: KotlinCoreEnvironment, ktFiles: List<KtFile>): FirSession {
    val rootModuleName = Name.special("<${environment.configuration.moduleName ?: "k2k"}>")
    val projectEnvironment = environment.toVfsBasedProjectEnvironment()
    val librariesScope = projectEnvironment.getSearchScopeForProjectLibraries()
    val sessionsWithSources =
      JvmFrontendPipelinePhase.prepareJvmSessions(
        files = ktFiles,
        rootModuleName = rootModuleName,
        configuration = environment.configuration,
        projectEnvironment = projectEnvironment,
        librariesScope = librariesScope,
        libraryList = DependencyListForCliModule.build(rootModuleName) {},
        isCommonSource = { false },
        isScript = { false },
        fileBelongsToModule = { _, _ -> true },
        incrementalCompilationContext = null,
      )
    return sessionsWithSources.single().session
  }
}
