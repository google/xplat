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

import com.google.j2kt.k2k.model.CompilationUnit
import com.google.j2kt.k2k.model.FileInfo
import com.google.j2kt.k2k.model.Library
import com.intellij.openapi.util.Disposer
import java.io.File
import org.jetbrains.kotlin.psi.KtPsiFactory

internal object Frontend {
  fun parseLibrary(sources: List<FileInfo>, classpath: List<File> = emptyList()): Library {
    val disposable = Disposer.newDisposable()
    try {
      val environment = FrontendKotlinCoreEnvironment.create(disposable, classpath)
      val psiFactory = KtPsiFactory(environment.project)
      val compilationUnits = sources.map { fileInfo ->
        val content = fileInfo.inputPath.readText()
        CompilationUnit(
          fileInfo = fileInfo,
          ktFile = psiFactory.createFile(fileInfo.inputPath.path, content),
        )
      }
      return Library(compilationUnits, disposable)
    } catch (t: Throwable) {
      Disposer.dispose(disposable)
      throw t
    }
  }
}
