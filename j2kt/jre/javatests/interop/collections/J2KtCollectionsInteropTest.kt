/*
 * Copyright 2023 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package interop.collections

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Tests interop between Kotlin and J2KT-generated code. */
class J2KtCollectionsInteropTest {

  @Test
  fun j2kt_acceptsKotlinList() {
    J2KtCollections.accept(listOf("foo", "bar"))
  }

  @Test
  fun j2kt_acceptsKotlinMutableList() {
    J2KtCollections.accept(mutableListOf("foo", "bar"))
  }

  @Test
  fun j2ktMutableList_assignsToList() {
    val unused: List<String> = J2KtCollections.newMutableList()
  }

  // At this moment, collections from J2KT can be assigned to mutable collections in Kotlin.
  @Test
  fun j2ktMutableList_assignsToMutableList() {
    val unused: List<String> = J2KtCollections.newMutableList()
  }

  @Test
  fun j2ktMutableList_kotlinMutationSupported() {
    val list = J2KtCollections.newMutableList<String>()
    list.add("foo")
  }

  @Test
  fun j2ktReadonlyList_assignsToKotlinList() {
    val unused: List<String> = J2KtCollections.newReadonlyList()
  }

  // Collections from J2KT are mutable at runtime, but mutation may fail.
  @Test
  fun j2ktReadonlyList_kotlinMutationUnsupported() {
    val list: MutableList<String> = J2KtCollections.newReadonlyList<String>() as MutableList<String>
    try {
      list.add("foo")
    } catch (e: UnsupportedOperationException) {
      // Mutation is unsupported
    }
  }

  @Test
  fun j2ktRemoveAll_acceptsReadonlyKotlinCollections() {
    // The transpiled `from` parameter is typed as read-only `Collection`, but we explicitly pass a
    // `MutableList` here because `removeAll` mutates it via `asMutableCollection()` (while
    // `elements` exercises passing read-only Kotlin collections).
    val target: MutableList<String> = mutableListOf("foo", "bar", "baz")
    assertTrue(J2KtCollections.removeAll(from = target, elements = listOf("foo", "baz")))
    assertEquals(listOf("bar"), target)
    assertFalse(J2KtCollections.removeAll(from = target, elements = emptyList<String>()))
    assertFalse(J2KtCollections.removeAll(from = target, elements = emptySet<String>()))
    assertEquals(listOf("bar"), target)
  }
}
