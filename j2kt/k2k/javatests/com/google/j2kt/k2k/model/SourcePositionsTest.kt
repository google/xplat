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

import com.google.common.truth.Truth.assertThat
import com.google.debugging.sourcemap.FilePosition
import com.google.debugging.sourcemap.SourceMapConsumerV3
import com.google.j2kt.k2k.frontend.Frontend
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.psi.psiUtil.findDescendantOfType
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class SourcePositionsTest {
  @get:Rule val tempFolder = TemporaryFolder()

  @Test
  fun captureOriginalSourcePositions_skipsKDocAndComments() {
    val source =
      """
      /** Class KDoc. */
      class Foo {
        /** Property KDoc. */
        val x: Int = 1 // trailing comment

        /**
         * Multiline function KDoc.
         * @param a parameter doc
         */
        /* block comment */
        // line comment
        fun bar(a: Int) {
          /* leading comment */
          println(a) /* trailing comment */
        }
      }
      """
        .trimIndent()

    withParsedFile(source) { ktFile ->
      val tracked = captureOriginalSourcePositions(ktFile)
      val slices = tracked.map { extractText(source, it.original.start, it.original.end) }

      assertThat(slices).doesNotContain(source)
      assertThat(slices.none { it.startsWith("/**") || it.startsWith("/*") || it.startsWith("//") })
        .isTrue()
      assertThat(slices.any { it.startsWith("class Foo") }).isTrue()
      assertThat(slices).contains("val x: Int = 1")
      assertThat(slices.any { it.startsWith("fun bar(a: Int)") }).isTrue()
      assertThat(slices).contains("println(a)")
    }
  }

  @Test
  fun resolveSourceMappings_deduplicatesIdenticalSpansKeepingInnermost() {
    val source =
      """
      enum class Color {
        RED,
      }

      fun makeObject() {
        object : Any() {}
      }
      """
        .trimIndent()

    withParsedFile(source) { ktFile ->
      val tracked = captureOriginalSourcePositions(ktFile)
      val mappings = resolveSourceMappings(ktFile, tracked)

      val redMappings = mappings.filter { extractText(source, it.start, it.end) == "RED" }
      assertThat(redMappings).hasSize(1)
      assertThat(redMappings.single().original.name).isEqualTo("RED")

      val objectLiteralMappings = mappings.filter {
        extractText(source, it.start, it.end) == "object : Any() {}"
      }
      assertThat(objectLiteralMappings).hasSize(1)
    }
  }

  @Test
  fun resolveSourceMappings_skipsDetachedNodes() {
    val source =
      """
      fun f() {
        val kept = 2
      }
      """
        .trimIndent()

    withParsedFile(source) { ktFile ->
      val tracked = captureOriginalSourcePositions(ktFile)
      val detachedNode = KtPsiFactory(ktFile.project).createProperty("val detached = 1").node
      val detachedTracked =
        TrackedSourceNode(
          node = detachedNode,
          original = SourceSpan(FilePosition(0, 0), FilePosition(0, 16), name = "detached"),
        )

      val mappings = resolveSourceMappings(ktFile, tracked + detachedTracked)

      assertThat(mappings.mapNotNull { it.original.name }).doesNotContain("detached")
      val keptNameMapping = mappings.single { it.original.name == "kept" }
      assertThat(extractText(source, keptNameMapping.start, keptNameMapping.end)).isEqualTo("kept")
    }
  }

  @Test
  fun resolveSourceMappings_respectsResolveNodeNullAndRemapping() {
    val source =
      """
      fun f() {
        val dropped = 1
        val original = 2
        val target = 3
      }
      """
        .trimIndent()

    withParsedFile(source) { ktFile ->
      val tracked = captureOriginalSourcePositions(ktFile)
      val droppedNode =
        checkNotNull(ktFile.findDescendantOfType<KtProperty> { it.name == "dropped" }).node
      val originalNode =
        checkNotNull(ktFile.findDescendantOfType<KtProperty> { it.name == "original" }).node
      val targetNode =
        checkNotNull(ktFile.findDescendantOfType<KtProperty> { it.name == "target" }).node

      val mappings =
        resolveSourceMappings(ktFile, tracked) { node ->
          when {
            node === droppedNode -> null
            node === originalNode -> targetNode
            node === targetNode -> null
            else -> node
          }
        }

      assertThat(
          mappings.none {
            extractText(source, it.original.start, it.original.end) == "val dropped = 1"
          }
        )
        .isTrue()
      val remapped = mappings.single {
        extractText(source, it.original.start, it.original.end) == "val original = 2"
      }
      assertThat(extractText(source, remapped.start, remapped.end)).isEqualTo("val target = 3")
    }
  }

  @Test
  fun captureOriginalSourcePositions_emptyFile_returnsEmpty() {
    withParsedFile("") { ktFile -> assertThat(captureOriginalSourcePositions(ktFile)).isEmpty() }
  }

  @Test
  fun captureOriginalSourcePositions_commentOnlyFile_returnsEmpty() {
    val commentsOnly =
      """
      /** Doc comment */
      // Line comment
      /* Block comment */
      """
        .trimIndent()
    withParsedFile(commentsOnly) { ktFile ->
      assertThat(captureOriginalSourcePositions(ktFile)).isEmpty()
    }
  }

  @Test
  fun captureOriginalSourcePositions_tracksConstructorsInitAccessorsAndControlFlow() {
    val source =
      """
      class Sample constructor(val a: Int) {
        init {
          println(a)
        }

        constructor() : this(0)

        var prop: Int
          get() = a
          set(value) {
            println(value)
          }

        fun expr(x: Int): Int = x + 1

        fun flow(pair: Pair<Int, Int>, x: Int): Int {
          val (left, right) = pair
          outer@ while (x > 0) {
            break@outer
          }
          if (x > 0) return left else return right
        }

        fun whenAndTry(x: Int): Int {
          val y = when (x) {
            1 -> 10
            else -> 20
          }
          try {
            return y
          } catch (e: Exception) {
            return -1
          } finally {
            println(x)
          }
        }
      }
      """
        .trimIndent()

    withParsedFile(source) { ktFile ->
      val tracked = captureOriginalSourcePositions(ktFile)
      val slices = tracked.map { extractText(source, it.original.start, it.original.end) }
      val names = tracked.mapNotNull { it.original.name }

      assertThat(slices)
        .containsAtLeast(
          "constructor",
          "init",
          "constructor() : this(0)",
          "this(0)",
          "get() = a",
          "a",
          "x + 1",
          "while (x > 0) {\n      break@outer\n    }",
          "return left",
          "return right",
          "10",
          "20",
          "{\n      return -1\n    }",
          "{\n      println(x)\n    }",
        )
      assertThat(names)
        .containsAtLeast("Sample", "a", "prop", "value", "expr", "left", "right", "e")

      // Ensure the expression-bodied function itself is tracked distinctly from its body.
      val exprFunc =
        checkNotNull(ktFile.findDescendantOfType<KtNamedFunction> { it.name == "expr" })
      assertThat(tracked.any { it.node === exprFunc.node }).isTrue()
    }
  }

  @Test
  fun compilationUnit_generateSourceMap_producesValidSourceMapV3() {
    val source =
      """
      class Greeting {
        fun hello(name: String) {
          println(name)
        }
      }
      """
        .trimIndent()

    val file = tempFolder.newFile("Greeting.kt")
    file.writeText(source)
    val sourceMapJson =
      Frontend.parseLibrary(listOf(FileInfo(file, file))).use { library ->
        library.compilationUnits.single().generateSourceMap()
      }

    val consumer = SourceMapConsumerV3()
    consumer.parse(sourceMapJson)

    assertThat(consumer.file).isEqualTo("Greeting.kt")
    assertThat(consumer.originalSources).containsExactly("Greeting.kt")
    assertThat(consumer.originalNames).containsExactly("Greeting", "hello", "name").inOrder()

    // Line 1 (1-based), col 7 -> "Greeting"
    val classNameMapping = checkNotNull(consumer.getMappingForLine(1, 7))
    assertThat(classNameMapping.originalFile).isEqualTo("Greeting.kt")
    assertThat(classNameMapping.lineNumber).isEqualTo(1)
    assertThat(classNameMapping.columnPosition).isEqualTo(7)
    assertThat(classNameMapping.identifier).isEqualTo("Greeting")

    // Line 3 (1-based), col 5 -> "println(name)"
    val stmtMapping = checkNotNull(consumer.getMappingForLine(3, 5))
    assertThat(stmtMapping.lineNumber).isEqualTo(3)
    assertThat(stmtMapping.columnPosition).isEqualTo(5)
    assertThat(stmtMapping.identifier).isEmpty()
  }

  private fun withParsedFile(source: String, block: (KtFile) -> Unit) {
    val file = tempFolder.newFile("Test.kt")
    file.writeText(source)
    Frontend.parseLibrary(listOf(FileInfo(file, file))).use { library ->
      block(library.compilationUnits.single().ktFile)
    }
  }

  private fun extractText(text: String, start: FilePosition, end: FilePosition): String {
    val lineStarts = mutableListOf(0)
    for (i in text.indices) {
      if (text[i] == '\n') {
        lineStarts.add(i + 1)
      }
    }
    val startOffset = lineStarts[start.line] + start.column
    val endOffset = lineStarts[end.line] + end.column
    return text.substring(startOffset, endOffset)
  }
}
