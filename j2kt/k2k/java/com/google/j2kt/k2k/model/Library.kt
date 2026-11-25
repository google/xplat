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

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.fir.declarations.FirFile
import org.jetbrains.kotlin.fir.psi
import org.jetbrains.kotlin.fir.resolve.ScopeSession
import org.jetbrains.kotlin.psi.KtFile

class Library(
  val compilationUnits: List<CompilationUnit>,
  val environment: KotlinCoreEnvironment,
  private val disposable: Disposable,
) : AutoCloseable {
  val ktFiles: List<KtFile>
    get() = compilationUnits.map { it.ktFile }

  var scopeSession: ScopeSession? = null
    private set

  fun attachFir(scopeSession: ScopeSession, resolvedFirFiles: List<FirFile>) {
    this.scopeSession = scopeSession
    val firByKtFile = resolvedFirFiles.associateBy { it.psi as? KtFile }
    for (unit in compilationUnits) {
      unit.firFile = firByKtFile[unit.ktFile]
    }
  }

  override fun close() {
    Disposer.dispose(disposable)
  }
}
