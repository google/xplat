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

import java.io.File
import kotlin.io.path.absolutePathString
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.cli.jvm.compiler.toVfsBasedProjectEnvironment
import org.jetbrains.kotlin.cli.jvm.config.jvmClasspathRoots
import org.jetbrains.kotlin.cli.jvm.config.jvmModularRoots
import org.jetbrains.kotlin.config.friendPaths
import org.jetbrains.kotlin.config.moduleName
import org.jetbrains.kotlin.fir.DependencyListForCliModule
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.library.KotlinLibrary
import org.jetbrains.kotlin.library.loader.KlibLoader
import org.jetbrains.kotlin.library.loader.reportLoadingProblemsIfAny
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.KtFile

/**
 * Factory for creating a K2 [FirSession] supporting JVM classpath JARs, `.klib` archives, or both
 * via [prepareJKlibSessions].
 */
internal object FrontendFirSession {
  fun create(
    environment: KotlinCoreEnvironment,
    ktFiles: List<KtFile>,
    klibs: List<File>,
  ): FirSession {
    val configuration = environment.configuration
    val moduleName = configuration.moduleName ?: "k2k"
    val rootModuleName = Name.special("<$moduleName>")
    val projectEnvironment = environment.toVfsBasedProjectEnvironment()
    val librariesScope = projectEnvironment.getSearchScopeForProjectLibraries()
    val resolvedLibraries = loadKlibs(klibs)
    val libraryList =
      DependencyListForCliModule.build(Name.identifier(moduleName)) {
        dependencies(configuration.jvmClasspathRoots.map { it.absolutePath })
        dependencies(configuration.jvmModularRoots.map { it.absolutePath })
        dependencies(resolvedLibraries.map { it.path.absolutePathString() })
        friendDependencies(configuration.friendPaths)
      }
    val sessionsWithSources =
      prepareJKlibSessions(
        projectEnvironment = projectEnvironment,
        files = ktFiles,
        configuration = configuration,
        rootModuleName = rootModuleName,
        resolvedLibraries = resolvedLibraries,
        libraryList = libraryList,
        extensionRegistrars = emptyList(),
        metadataCompilationMode = false,
        librariesScope = librariesScope,
        isCommonSource = { false },
        fileBelongsToModule = { _, _ -> true },
      )
    // In multiplatform mode, prepareJKlibSessions returns common sessions before the platform
    // session; select the platform session, which owns all non-common source files.
    return sessionsWithSources
      .last()
      .also {
        check(it.files.size == ktFiles.size) {
          "Expected ${ktFiles.size} files in the last FIR session, got ${it.files.size}"
        }
      }
      .session
  }

  private fun loadKlibs(files: List<File>): List<KotlinLibrary> {
    if (files.isEmpty()) return emptyList()
    val result = KlibLoader { libraryPaths(files.map { it.path }) }.load()
    val errors = mutableListOf<String>()
    check(!result.reportLoadingProblemsIfAny { _, message -> errors.add(message) }) {
      errors.joinToString("\n")
    }
    return result.librariesStdlibFirst
  }
}
