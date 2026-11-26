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

import com.google.common.truth.Truth.assertThat
import com.google.j2kt.k2k.model.FileInfo
import java.io.File
import org.jetbrains.kotlin.KtIoFileSourceFile
import org.jetbrains.kotlin.backend.common.serialization.serializeModuleIntoKlib
import org.jetbrains.kotlin.cli.common.diagnosticsCollector
import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.config.languageVersionSettings
import org.jetbrains.kotlin.diagnostics.impl.deduplicating
import org.jetbrains.kotlin.fir.declarations.DirectDeclarationsAccess
import org.jetbrains.kotlin.fir.declarations.FirFile
import org.jetbrains.kotlin.fir.declarations.FirNamedFunction
import org.jetbrains.kotlin.fir.pipeline.Fir2KlibMetadataSerializer
import org.jetbrains.kotlin.fir.pipeline.SingleModuleFrontendOutput
import org.jetbrains.kotlin.fir.pipeline.buildFirViaLightTree
import org.jetbrains.kotlin.fir.pipeline.runResolution
import org.jetbrains.kotlin.fir.types.classId
import org.jetbrains.kotlin.fir.types.coneType
import org.jetbrains.kotlin.ir.KtDiagnosticReporterWithImplicitIrBasedContext
import org.jetbrains.kotlin.library.KlibFormat
import org.jetbrains.kotlin.library.KotlinAbiVersion
import org.jetbrains.kotlin.library.KotlinLibraryVersioning
import org.jetbrains.kotlin.library.impl.BuiltInsPlatform
import org.jetbrains.kotlin.library.writer.KlibWriter
import org.jetbrains.kotlin.library.writer.includeMetadata
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.util.metadataVersion
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class FrontendFirSessionTest {
  @get:Rule val tempFolder = TemporaryFolder()

  private val javaDepJar: File
    get() = File(checkNotNull(System.getProperty("java_dep_jar")))

  @Test
  fun resolveFir_jvmClasspathOnly_resolvesClasspathSymbols() {
    val firFile =
      resolveSource(
        source =
          """
          package consumer
          import test.javadep.JavaDep
          fun useJava(dep: JavaDep) = dep.value()
          """
            .trimIndent(),
        classpath = listOf(javaDepJar),
        klibs = emptyList(),
      )

    val useJava = firFile.findFunction("useJava")
    assertThat(useJava.valueParameters.single().returnTypeRef.coneType.classId)
      .isEqualTo(JAVA_DEP_CLASS_ID)
    assertThat(useJava.returnTypeRef.coneType.classId).isEqualTo(INT_CLASS_ID)
  }

  @Test
  fun resolveFir_klibsOnly_resolvesKlibSymbols() {
    val klib = buildHeaderKlib("klib_dep_native", BuiltInsPlatform.NATIVE, KLIB_DEP_SOURCE)

    val firFile =
      resolveSource(
        source =
          """
          package consumer
          import test.klibdep.KlibDep
          fun useKlib(dep: KlibDep) = dep.number()
          """
            .trimIndent(),
        classpath = emptyList(),
        klibs = listOf(klib),
      )

    val useKlib = firFile.findFunction("useKlib")
    assertThat(useKlib.valueParameters.single().returnTypeRef.coneType.classId)
      .isEqualTo(KLIB_DEP_CLASS_ID)
    assertThat(useKlib.returnTypeRef.coneType.classId).isEqualTo(INT_CLASS_ID)
  }

  @Test
  fun resolveFir_hybridJvmAndKlib_resolvesBothKlibAndClasspathSymbols() {
    val klib = buildHeaderKlib("klib_dep_jklib", BuiltInsPlatform.JKLIB, KLIB_DEP_SOURCE)

    val firFile =
      resolveSource(
        source =
          """
          package consumer
          import test.javadep.JavaDep
          import test.klibdep.KlibDep
          fun useKlib(dep: KlibDep) = dep.number()
          fun useJava(dep: JavaDep) = dep.value()
          """
            .trimIndent(),
        classpath = listOf(javaDepJar),
        klibs = listOf(klib),
      )

    val useKlib = firFile.findFunction("useKlib")
    assertThat(useKlib.valueParameters.single().returnTypeRef.coneType.classId)
      .isEqualTo(KLIB_DEP_CLASS_ID)
    assertThat(useKlib.returnTypeRef.coneType.classId).isEqualTo(INT_CLASS_ID)

    val useJava = firFile.findFunction("useJava")
    assertThat(useJava.valueParameters.single().returnTypeRef.coneType.classId)
      .isEqualTo(JAVA_DEP_CLASS_ID)
    assertThat(useJava.returnTypeRef.coneType.classId).isEqualTo(INT_CLASS_ID)
  }

  @Test
  fun resolveFir_multiplatformOption_resolvesKlibAndClasspathSymbols() {
    val klib = buildHeaderKlib("klib_dep_mpp", BuiltInsPlatform.JKLIB, KLIB_DEP_SOURCE)

    val firFile =
      resolveSource(
        source =
          """
          package consumer
          import test.javadep.JavaDep
          import test.klibdep.KlibDep
          fun useKlib(dep: KlibDep) = dep.number()
          fun useJava(dep: JavaDep) = dep.value()
          """
            .trimIndent(),
        classpath = listOf(javaDepJar),
        klibs = listOf(klib),
        kotlincOptions = listOf("-Xmulti-platform"),
      )

    val useKlib = firFile.findFunction("useKlib")
    assertThat(useKlib.valueParameters.single().returnTypeRef.coneType.classId)
      .isEqualTo(KLIB_DEP_CLASS_ID)
    assertThat(useKlib.returnTypeRef.coneType.classId).isEqualTo(INT_CLASS_ID)

    val useJava = firFile.findFunction("useJava")
    assertThat(useJava.valueParameters.single().returnTypeRef.coneType.classId)
      .isEqualTo(JAVA_DEP_CLASS_ID)
    assertThat(useJava.returnTypeRef.coneType.classId).isEqualTo(INT_CLASS_ID)
  }

  private fun resolveSource(
    source: String,
    classpath: List<File>,
    klibs: List<File>,
    kotlincOptions: List<String> = emptyList(),
  ): FirFile {
    val srcFile = tempFolder.newFile("Consumer.kt").apply { writeText(source) }
    return Frontend.parseLibrary(
        sources = listOf(FileInfo(srcFile, srcFile)),
        classpath = classpath,
        kotlincOptions = kotlincOptions,
        captureOriginalText = false,
      )
      .use { library ->
        Frontend.resolveFir(library, klibs)
        checkNotNull(library.compilationUnits.single().firFile)
      }
  }

  private fun buildHeaderKlib(
    moduleName: String,
    platform: BuiltInsPlatform,
    source: String,
  ): File {
    val srcFile = tempFolder.newFile("$moduleName.kt").apply { writeText(source) }
    val klibFile = File(tempFolder.root, "$moduleName.klib")
    Frontend.parseLibrary(
        sources = emptyList(),
        classpath = emptyList(),
        kotlincOptions = emptyList(),
        captureOriginalText = false,
      )
      .use { library ->
        val configuration = library.environment.configuration
        val session = FrontendFirSession.create(library.environment, library.ktFiles, emptyList())
        val rawFirFiles =
          session.buildFirViaLightTree(
            listOf(KtIoFileSourceFile(srcFile)),
            configuration.diagnosticsCollector,
            reportFilesAndLines = null,
          )
        val (scopeSession, resolvedFirFiles) = session.runResolution(rawFirFiles)
        val frontendOutput = SingleModuleFrontendOutput(session, scopeSession, resolvedFirFiles)
        val serializerOutput =
          serializeModuleIntoKlib(
            moduleName = moduleName,
            irModuleFragment = null,
            configuration = configuration,
            diagnosticReporter =
              KtDiagnosticReporterWithImplicitIrBasedContext(
                configuration.diagnosticsCollector.deduplicating(),
                configuration.languageVersionSettings,
              ),
            cleanFiles = emptyList(),
            dependencies = emptyList(),
            createModuleSerializer = { error("IR serialization is not expected for header klib") },
            metadataSerializer =
              Fir2KlibMetadataSerializer(
                configuration,
                listOf(frontendOutput),
                fir2IrActualizedResult = null,
                exportKDoc = false,
                produceHeaderKlib = true,
              ),
          )
        KlibWriter {
            format(KlibFormat.ZipArchive)
            manifest {
              moduleName(moduleName)
              versions(
                KotlinLibraryVersioning(
                  abiVersion = KotlinAbiVersion.CURRENT,
                  compilerVersion = KotlinCompilerVersion.getVersion(),
                  metadataVersion = configuration.metadataVersion(),
                )
              )
              platformAndTargets(platform, emptyList())
            }
            includeMetadata(checkNotNull(serializerOutput.serializedMetadata))
          }
          .writeTo(klibFile.toPath())
      }
    return klibFile
  }

  @OptIn(DirectDeclarationsAccess::class)
  private fun FirFile.findFunction(name: String): FirNamedFunction =
    declarations.filterIsInstance<FirNamedFunction>().single { it.name.asString() == name }

  private companion object {
    val JAVA_DEP_CLASS_ID = ClassId.topLevel(FqName("test.javadep.JavaDep"))
    val KLIB_DEP_CLASS_ID = ClassId.topLevel(FqName("test.klibdep.KlibDep"))
    val INT_CLASS_ID = ClassId.topLevel(FqName("kotlin.Int"))

    val KLIB_DEP_SOURCE =
      """
      package test.klibdep
      class KlibDep {
        fun number(): Int = 7
      }
      """
        .trimIndent()
  }
}
