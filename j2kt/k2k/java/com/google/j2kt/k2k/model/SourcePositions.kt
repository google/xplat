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

import com.google.debugging.sourcemap.FilePosition
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import java.util.Arrays
import java.util.Collections
import java.util.IdentityHashMap
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtCatchClause
import org.jetbrains.kotlin.psi.KtClassInitializer
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtDestructuringDeclarationEntry
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtFinallySection
import org.jetbrains.kotlin.psi.KtFunctionLiteral
import org.jetbrains.kotlin.psi.KtIfExpression
import org.jetbrains.kotlin.psi.KtLabeledExpression
import org.jetbrains.kotlin.psi.KtLoopExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtPrimaryConstructor
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtPropertyAccessor
import org.jetbrains.kotlin.psi.KtSecondaryConstructor
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid
import org.jetbrains.kotlin.psi.KtWhenEntry
import org.jetbrains.kotlin.psi.psiUtil.endOffset
import org.jetbrains.kotlin.psi.psiUtil.isAncestor
import org.jetbrains.kotlin.psi.psiUtil.siblings
import org.jetbrains.kotlin.psi.psiUtil.startOffset

/** Original source span captured from a Kotlin [ASTNode] prior to cleanup transformations. */
internal data class SourceSpan(
  val start: FilePosition,
  val end: FilePosition,
  val name: String? = null,
)

/** Association between an initial [ASTNode] and its pre-cleanup [SourceSpan]. */
internal data class TrackedSourceNode(val node: ASTNode, val original: SourceSpan)

/** Mapping between an original source span and its current span in the cleaned Kotlin file. */
internal data class SourceMapping(
  val original: SourceSpan,
  val start: FilePosition,
  val end: FilePosition,
)

/**
 * Captures pre-cleanup [SourceSpan]s for J2KT-relevant PSI nodes in [ktFile] in pre-order without
 * attaching user data to the AST.
 */
internal fun captureOriginalSourcePositions(ktFile: KtFile): List<TrackedSourceNode> {
  val lineStarts = buildLineStarts(ktFile.text)
  val seenNodes: MutableSet<ASTNode> = Collections.newSetFromMap(IdentityHashMap())

  return buildList {
    fun record(element: PsiElement?, name: String? = null) {
      val node = element?.node ?: return
      if (!seenNodes.add(node)) return
      val range = element.trimmedCodeRange() ?: return
      add(
        TrackedSourceNode(
          node = node,
          original =
            SourceSpan(
              start = offsetToFilePosition(lineStarts, range.startOffset),
              end = offsetToFilePosition(lineStarts, range.endOffset),
              name = name,
            ),
        )
      )
    }

    ktFile.accept(
      object : KtTreeVisitorVoid() {
        override fun visitElement(element: PsiElement) {
          when (element) {
            is KtClassOrObject -> {
              record(element)
              record(element.nameIdentifier, name = element.name)
            }
            is KtPrimaryConstructor -> record(element.getConstructorKeyword())
            is KtSecondaryConstructor -> {
              record(element)
              record(element.getConstructorKeyword())
              element.getDelegationCall().takeIf { !it.isImplicit }?.let(::record)
            }
            is KtClassInitializer -> record(element.initKeyword)
            is KtNamedFunction -> {
              record(element)
              record(element.nameIdentifier, name = element.name)
              if (!element.hasBlockBody()) {
                record(element.bodyExpression)
              }
            }
            is KtProperty -> {
              record(element)
              record(element.nameIdentifier, name = element.name)
            }
            is KtPropertyAccessor -> {
              record(element)
              if (!element.hasBlockBody()) {
                record(element.bodyExpression)
              }
            }
            is KtParameter -> record(element.nameIdentifier, name = element.name)
            is KtDestructuringDeclarationEntry ->
              record(element.nameIdentifier, name = element.name)
            is KtLabeledExpression -> record(element.baseExpression)
            is KtBlockExpression -> {
              if (
                element.parent !is KtNamedFunction &&
                  element.parent !is KtSecondaryConstructor &&
                  element.parent !is KtPropertyAccessor &&
                  element.parent !is KtFunctionLiteral
              ) {
                record(element)
              }
              for (statement in element.statements) {
                record(statement)
              }
            }
            is KtIfExpression -> {
              record(element.then)
              record(element.`else`)
            }
            is KtLoopExpression -> record(element.body)
            is KtWhenEntry -> record(element.expression)
            is KtCatchClause -> record(element.catchBody)
            is KtFinallySection -> record(element.finalExpression)
          }
          super.visitElement(element)
        }
      }
    )
  }
}

private data class IndexedMapping(val preOrderIndex: Int, val mapping: SourceMapping)

/**
 * Resolves [trackedNodes] against the current state of [ktFile] using [resolveNode] (which returns
 * the live node in [ktFile], or `null` if removed) and returns sorted [SourceMapping]s suitable for
 * SourceMap V3 generation.
 */
internal fun resolveSourceMappings(
  ktFile: KtFile,
  trackedNodes: List<TrackedSourceNode>,
  resolveNode: (ASTNode) -> ASTNode? = { node ->
    // Drop tracked nodes that are no longer attached to ktFile (e.g., removed by a cleanup pass).
    node.takeIf { it.psi != null && ktFile.isAncestor(it.psi) }
  },
): List<SourceMapping> {
  val lineStarts = buildLineStarts(ktFile.text)
  val indexed =
    buildList(trackedNodes.size) {
        for ((index, tracked) in trackedNodes.withIndex()) {
          val current = resolveNode(tracked.node) ?: continue
          val range = current.psi?.trimmedCodeRange() ?: continue
          val start = offsetToFilePosition(lineStarts, range.startOffset)
          val end = offsetToFilePosition(lineStarts, range.endOffset)
          add(IndexedMapping(index, SourceMapping(tracked.original, start, end)))
        }
      }
      // Order outermost-first when two spans share the same start position (required by
      // SourceMapGeneratorV3's nested mapping stack), and order by pre-order index when spans are
      // identical so the deduplication pass keeps the innermost (most specific) node.
      .sortedWith(
        compareBy<IndexedMapping>(
          { it.mapping.start.line },
          { it.mapping.start.column },
          { -it.mapping.end.line },
          { -it.mapping.end.column },
          { it.preOrderIndex },
        )
      )

  // Deduplicate mappings with identical (start, end) spans; because equal spans are ordered by
  // pre-order index, overwriting the previous entry keeps the innermost (most specific) node.
  return buildList(indexed.size) {
    for (entry in indexed) {
      val mapping = entry.mapping
      val last = lastOrNull()
      if (
        last != null &&
          last.start.samePositionAs(mapping.start) &&
          last.end.samePositionAs(mapping.end)
      ) {
        this[lastIndex] = mapping
      } else {
        add(mapping)
      }
    }
  }
}

private fun FilePosition.samePositionAs(other: FilePosition): Boolean =
  line == other.line && column == other.column

@Suppress("KotlinArray") // Avoid boxing per-line offsets in binary search lookup table.
private fun buildLineStarts(text: String): IntArray {
  val starts = IntArray(text.count { it == '\n' } + 1)
  var line = 1
  for (i in text.indices) {
    if (text[i] == '\n') {
      starts[line++] = i + 1
    }
  }
  return starts
}

@Suppress("KotlinArray") // Avoid boxing per-line offsets in binary search lookup table.
private fun offsetToFilePosition(lineStarts: IntArray, offset: Int): FilePosition {
  val idx = Arrays.binarySearch(lineStarts, offset)
  val line = (if (idx >= 0) idx else -idx - 2).coerceIn(0, lineStarts.lastIndex)
  val column = (offset - lineStarts[line]).coerceAtLeast(0)
  return FilePosition(line, column)
}

private fun PsiElement.trimmedCodeRange(): TextRange? {
  val startLeaf = firstCodeLeaf() ?: return null
  val endLeaf = lastCodeLeaf() ?: return null
  return TextRange(startLeaf.startOffset, endLeaf.endOffset).takeUnless { it.isEmpty }
}

private fun PsiElement.firstCodeLeaf(): PsiElement? =
  when {
    isIgnoredForSourceRange -> null
    firstChild == null -> this
    else ->
      firstChild.siblings(forward = true, withItself = true).firstNotNullOfOrNull {
        it.firstCodeLeaf()
      }
  }

private fun PsiElement.lastCodeLeaf(): PsiElement? =
  when {
    isIgnoredForSourceRange -> null
    firstChild == null -> this
    else ->
      lastChild.siblings(forward = false, withItself = true).firstNotNullOfOrNull {
        it.lastCodeLeaf()
      }
  }

private val PsiElement.isIgnoredForSourceRange: Boolean
  get() = textLength == 0 || this is PsiWhiteSpace || this is PsiComment
