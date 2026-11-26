/*
 * Copyright 2010-2025 JetBrains s.r.o. and Kotlin Programming Language contributors.
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

import org.jetbrains.kotlin.cli.common.SessionConstructionUtils
import org.jetbrains.kotlin.cli.common.SessionWithSources
import org.jetbrains.kotlin.cli.jvm.compiler.VfsBasedProjectEnvironment
import org.jetbrains.kotlin.config.AnalysisFlags
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.JvmTarget
import org.jetbrains.kotlin.config.LanguageVersionSettings
import org.jetbrains.kotlin.config.jvmTarget
import org.jetbrains.kotlin.config.languageVersionSettings
import org.jetbrains.kotlin.fir.DependencyListForCliModule
import org.jetbrains.kotlin.fir.FirModuleData
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.SessionConfiguration
import org.jetbrains.kotlin.fir.checkers.registerJvmCheckers
import org.jetbrains.kotlin.fir.deserialization.ModuleDataProvider
import org.jetbrains.kotlin.fir.deserialization.SingleModuleDataProvider
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar
import org.jetbrains.kotlin.fir.java.FirJvmTargetProvider
import org.jetbrains.kotlin.fir.java.JavaSymbolProvider
import org.jetbrains.kotlin.fir.java.deserialization.FirJvmBuiltinsSymbolProvider
import org.jetbrains.kotlin.fir.java.deserialization.FirJvmClasspathBuiltinSymbolProvider
import org.jetbrains.kotlin.fir.java.deserialization.JvmClassFileBasedSymbolProvider
import org.jetbrains.kotlin.fir.java.deserialization.OptionalAnnotationClassesProvider
import org.jetbrains.kotlin.fir.languageVersionSettings
import org.jetbrains.kotlin.fir.moduleData
import org.jetbrains.kotlin.fir.resolve.providers.FirSymbolProvider
import org.jetbrains.kotlin.fir.resolve.providers.impl.FirCloneableSymbolProvider
import org.jetbrains.kotlin.fir.resolve.providers.impl.FirFallbackBuiltinSymbolProvider
import org.jetbrains.kotlin.fir.resolve.scopes.wrapScopeWithJvmMapped
import org.jetbrains.kotlin.fir.scopes.FirKotlinScopeProvider
import org.jetbrains.kotlin.fir.session.AbstractFirMetadataSessionFactory
import org.jetbrains.kotlin.fir.session.FirAbstractSessionFactory
import org.jetbrains.kotlin.fir.session.FirJvmIncrementalCompilationSymbolProviders
import org.jetbrains.kotlin.fir.session.FirSessionConfigurator
import org.jetbrains.kotlin.fir.session.FirSharableJavaComponents
import org.jetbrains.kotlin.fir.session.KlibBasedSymbolProvider
import org.jetbrains.kotlin.fir.session.KmpModuleKind
import org.jetbrains.kotlin.fir.session.environment.AbstractProjectEnvironment
import org.jetbrains.kotlin.fir.session.environment.AbstractProjectFileSearchScope
import org.jetbrains.kotlin.fir.session.firCachesFactoryForCliMode
import org.jetbrains.kotlin.fir.session.registerJavaComponents
import org.jetbrains.kotlin.library.KotlinLibrary
import org.jetbrains.kotlin.load.kotlin.PackagePartProvider
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.platform.jvm.JvmPlatforms
import org.jetbrains.kotlin.utils.addToStdlib.runIf
import org.jetbrains.kotlin.utils.addToStdlib.shouldNotBeCalled

// Copied from Kotlin 2.4.21:
// https://github.com/JetBrains/kotlin/blob/v2.4.21/compiler/cli/cli-jklib/src/org/jetbrains/kotlin/cli/jklib/FirJKlibSessionFactory.kt
//
// Locally modified only for package placement, `internal` visibility, explicit imports, and
// formatting. Kept as a copy because the upstream `prepareJKlibSessions` function in
// `jklib-compiler` is `internal` and cannot be called directly. Once K2K stabilizes and upstream
// visibility is opened up, remove this copy and depend on it directly.
@OptIn(SessionConfiguration::class)
internal object FirJKlibSessionFactory :
  FirAbstractSessionFactory<FirJKlibSessionFactory.Context>() {
  override fun createPlatformSpecificSharedProviders(
    session: FirSession,
    moduleData: FirModuleData,
    scopeProvider: FirKotlinScopeProvider,
    context: Context,
  ): List<FirSymbolProvider> {
    val kotlinClassFinder =
      context.projectEnvironment.getKotlinClassFinder(
        context.projectEnvironment.getSearchScopeForProjectLibraries()
      )
    return listOf(
      FirCloneableSymbolProvider(session, moduleData, scopeProvider),
      OptionalAnnotationClassesProvider(
        session,
        SingleModuleDataProvider(moduleData),
        scopeProvider,
        context.packagePartProvider,
      ),
      FirJvmBuiltinsSymbolProvider(
        session,
        FirFallbackBuiltinSymbolProvider(session, moduleData, scopeProvider),
      ) {
        kotlinClassFinder.findBuiltInsData(it)
      },
    )
  }

  // ==================================== Shared library session
  // ====================================

  /** See documentation to [FirAbstractSessionFactory.createSharedLibrarySession] */
  fun createSharedLibrarySession(
    mainModuleName: Name,
    projectEnvironment: AbstractProjectEnvironment,
    extensionRegistrars: List<FirExtensionRegistrar>,
    packagePartProvider: PackagePartProvider,
    languageVersionSettings: LanguageVersionSettings,
    predefinedJavaComponents: FirSharableJavaComponents?,
    jvmTarget: JvmTarget?,
  ): FirSession {
    val context =
      Context(predefinedJavaComponents, projectEnvironment, packagePartProvider, jvmTarget)
    return createSharedLibrarySession(
      mainModuleName,
      context,
      languageVersionSettings,
      extensionRegistrars,
    )
  }

  // ==================================== Library session ====================================

  /** See documentation to [FirAbstractSessionFactory.createLibrarySession] */
  fun createLibrarySession(
    resolvedLibraries: List<KotlinLibrary>,
    sharedLibrarySession: FirSession,
    moduleDataProvider: ModuleDataProvider,
    projectEnvironment: AbstractProjectEnvironment,
    extensionRegistrars: List<FirExtensionRegistrar>,
    scope: AbstractProjectFileSearchScope,
    packagePartProvider: PackagePartProvider,
    languageVersionSettings: LanguageVersionSettings,
    predefinedJavaComponents: FirSharableJavaComponents?,
    jvmTarget: JvmTarget?,
  ): FirSession {
    val kotlinClassFinder = projectEnvironment.getKotlinClassFinder(scope)
    val context =
      Context(predefinedJavaComponents, projectEnvironment, packagePartProvider, jvmTarget)
    return createLibrarySession(
      context,
      sharedLibrarySession,
      moduleDataProvider,
      languageVersionSettings,
      extensionRegistrars,
      createSeparateSharedProvidersInHmppCompilation = false,
      createProviders = { session, kotlinScopeProvider ->
        listOf(
          KlibBasedSymbolProvider(
            session,
            moduleDataProvider,
            kotlinScopeProvider,
            resolvedLibraries,
          ),
          JvmClassFileBasedSymbolProvider(
            session,
            moduleDataProvider,
            kotlinScopeProvider,
            packagePartProvider,
            kotlinClassFinder,
            projectEnvironment.getFirJavaFacade(
              session,
              moduleDataProvider.allModuleData.last(),
              scope,
            ),
          ),
        )
      },
    )
  }

  override fun createKotlinScopeProviderForLibrarySession(): FirKotlinScopeProvider {
    return FirKotlinScopeProvider(::wrapScopeWithJvmMapped)
  }

  override fun FirSession.registerLibrarySessionComponents(c: Context) {
    registerJavaComponents(c.projectEnvironment.getJavaModuleResolver(), c.predefinedJavaComponents)
  }

  // ==================================== Platform session ====================================

  /** See documentation to [FirAbstractSessionFactory.createSourceSession] */
  fun createSourceSession(
    moduleData: FirModuleData,
    javaSourcesScope: AbstractProjectFileSearchScope,
    projectEnvironment: AbstractProjectEnvironment,
    createIncrementalCompilationSymbolProviders:
      (FirSession) -> FirJvmIncrementalCompilationSymbolProviders?,
    extensionRegistrars: List<FirExtensionRegistrar>,
    configuration: CompilerConfiguration,
    predefinedJavaComponents: FirSharableJavaComponents?,
    needRegisterJavaElementFinder: Boolean,
    packagePartProvider: PackagePartProvider,
    kmpModuleKind: KmpModuleKind,
    init: FirSessionConfigurator.() -> Unit,
  ): FirSession {
    val context =
      Context(
        predefinedJavaComponents,
        projectEnvironment,
        packagePartProvider,
        jvmTarget = configuration.jvmTarget,
      )
    return createSourceSession(
        moduleData,
        context = context,
        extensionRegistrars,
        configuration,
        kmpModuleKind = kmpModuleKind,
        init,
        createProviders = { session, kotlinScopeProvider, symbolProvider, generatedSymbolsProvider
          ->
          val javaSymbolProvider =
            JavaSymbolProvider(
              session,
              projectEnvironment.getFirJavaFacade(session, moduleData, javaSourcesScope),
            )
          session.register(JavaSymbolProvider::class, javaSymbolProvider)

          val providers =
            listOfNotNull(
              symbolProvider,
              generatedSymbolsProvider,
              javaSymbolProvider,
              initializeForStdlibIfNeeded(projectEnvironment, session, kotlinScopeProvider),
            )

          val incrementalCompilationSymbolProviders =
            createIncrementalCompilationSymbolProviders(session)
          SourceProviders(
            providers,
            incrementalProvider =
              incrementalCompilationSymbolProviders
                ?.symbolProviderForBinariesFromIncrementalCompilation,
            additionalOptionalAnnotationsProvider =
              incrementalCompilationSymbolProviders
                ?.optionalAnnotationClassesProviderForBinariesFromIncrementalCompilation,
          )
        },
      )
      .also {
        if (needRegisterJavaElementFinder) {
          projectEnvironment.registerAsJavaElementFinder(it)
        }
      }
  }

  override fun createKotlinScopeProviderForSourceSession(
    moduleData: FirModuleData,
    languageVersionSettings: LanguageVersionSettings,
  ): FirKotlinScopeProvider {
    if (languageVersionSettings.getFlag(AnalysisFlags.stdlibCompilation) && moduleData.isCommon) {
      return FirKotlinScopeProvider()
    }
    return FirKotlinScopeProvider {
      klass,
      declaredScope,
      useSiteSession,
      scopeSession,
      memberRequiredPhase ->
      wrapScopeWithJvmMapped(
        klass,
        declaredScope,
        useSiteSession,
        scopeSession,
        memberRequiredPhase,
        filterOutJvmPlatformDeclarations =
          !languageVersionSettings.getFlag(AnalysisFlags.stdlibCompilation),
      )
    }
  }

  override fun FirSessionConfigurator.registerPlatformCheckers() {
    registerJvmCheckers()
  }

  override fun FirSessionConfigurator.registerExtraPlatformCheckers() {}

  override fun FirSession.registerSourceSessionComponents(c: Context) {
    registerLibrarySessionComponents(c)
    register(FirJvmTargetProvider::class, FirJvmTargetProvider(c.jvmTarget ?: JvmTarget.DEFAULT))
  }

  override val requiresSpecialSetupOfSourceProvidersInHmppCompilation: Boolean
    get() = false

  override val isFactoryForMetadataCompilation: Boolean
    get() = false

  // ==================================== Common parts ====================================

  // ==================================== Utilities ====================================

  class Context(
    val predefinedJavaComponents: FirSharableJavaComponents?,
    val projectEnvironment: AbstractProjectEnvironment,
    val packagePartProvider: PackagePartProvider,
    val jvmTarget: JvmTarget?,
  )

  private fun initializeForStdlibIfNeeded(
    projectEnvironment: AbstractProjectEnvironment,
    session: FirSession,
    kotlinScopeProvider: FirKotlinScopeProvider,
  ): FirSymbolProvider? {
    return runIf(
      session.languageVersionSettings.getFlag(AnalysisFlags.stdlibCompilation) &&
        !session.moduleData.isCommon &&
        session.moduleData.dependsOnDependencies.isEmpty()
    ) {
      val kotlinClassFinder =
        projectEnvironment.getKotlinClassFinder(
          projectEnvironment.getSearchScopeForProjectLibraries()
        )
      FirJvmClasspathBuiltinSymbolProvider(session, session.moduleData, kotlinScopeProvider) {
        kotlinClassFinder.findBuiltInsData(it)
      }
    }
  }
}

/**
 * Creates library session and sources session for klib compilation Number of created session
 * depends on mode of MPP:
 * - disabled
 * - legacy (one platform and one common module)
 * - HMPP (multiple number of modules)
 */
internal fun <F> prepareJKlibSessions(
  projectEnvironment: VfsBasedProjectEnvironment,
  files: List<F>,
  configuration: CompilerConfiguration,
  rootModuleName: Name,
  resolvedLibraries: List<KotlinLibrary>,
  libraryList: DependencyListForCliModule,
  extensionRegistrars: List<FirExtensionRegistrar>,
  metadataCompilationMode: Boolean,
  librariesScope: AbstractProjectFileSearchScope,
  isCommonSource: (F) -> Boolean,
  fileBelongsToModule: (F, String) -> Boolean,
): List<SessionWithSources<F>> {
  val predefinedJavaComponents = FirSharableJavaComponents(firCachesFactoryForCliMode)
  val packagePartProviderForLibraries = projectEnvironment.getPackagePartProvider(librariesScope)

  return SessionConstructionUtils.prepareSessions(
    files,
    configuration,
    rootModuleName,
    JvmPlatforms.unspecifiedJvmPlatform,
    metadataCompilationMode,
    libraryList,
    extensionRegistrars,
    isCommonSource,
    isScript = { false },
    fileBelongsToModule,
    createSharedLibrarySession = {
      FirJKlibSessionFactory.createSharedLibrarySession(
        rootModuleName,
        projectEnvironment,
        extensionRegistrars,
        packagePartProviderForLibraries,
        configuration.languageVersionSettings,
        predefinedJavaComponents = predefinedJavaComponents,
        jvmTarget = configuration.jvmTarget,
      )
    },
    createLibrarySession = { sharedLibrarySession ->
      FirJKlibSessionFactory.createLibrarySession(
        resolvedLibraries,
        sharedLibrarySession,
        libraryList.moduleDataProvider,
        projectEnvironment,
        extensionRegistrars,
        librariesScope,
        packagePartProviderForLibraries,
        configuration.languageVersionSettings,
        predefinedJavaComponents = predefinedJavaComponents,
        jvmTarget = configuration.jvmTarget,
      )
    },
    createSourceSession = { moduleData, kmpModuleKind, sessionConfigurator ->
      FirJKlibSessionFactory.createSourceSession(
        moduleData = moduleData,
        javaSourcesScope = projectEnvironment.getSearchScopeForProjectJavaSources(),
        projectEnvironment = projectEnvironment,
        createIncrementalCompilationSymbolProviders = { null },
        extensionRegistrars = extensionRegistrars,
        configuration = configuration,
        predefinedJavaComponents = predefinedJavaComponents,
        needRegisterJavaElementFinder = true,
        packagePartProvider = packagePartProviderForLibraries,
        init = sessionConfigurator,
        kmpModuleKind = kmpModuleKind,
      )
    },
    createMetadataSessionFactoryContextForHmppCommonLibrarySession = {
      AbstractFirMetadataSessionFactory.Context(
        createJvmContext = { shouldNotBeCalled() },
        createJsContext = { shouldNotBeCalled() },
      )
    },
  )
}
