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
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Tests interop between Kotlin and J2KT-generated code. */
class J2KtCollectionsInteropTest {

  @Test
  fun j2kt_readsKotlinReadonlyList() {
    assertEquals("foo", J2KtCollections.getFirstOrNull(listOf("foo", "bar")))
    assertNull(J2KtCollections.getFirstOrNull(emptyList<String>()))
  }

  @Test
  fun j2kt_readsKotlinMutableList() {
    assertEquals("foo", J2KtCollections.getFirstOrNull(mutableListOf("foo", "bar")))
  }

  @Test
  fun j2ktAddTo_withKotlinMutableList_addsElement() {
    val list = mutableListOf("foo")
    J2KtCollections.addTo(list, "bar")
    assertEquals(listOf("foo", "bar"), list)
  }

  // Base Java collection parameters map to read-only Kotlin collections, so Kotlin callers can pass
  // read-only stdlib collections; J2KT's runtime asMutableList() check throws
  // UnsupportedOperationException on mutation.
  @Test
  fun j2ktAddTo_withKotlinReadonlyList_throwsUnsupportedOperationException() {
    assertFailsWith<UnsupportedOperationException> {
      J2KtCollections.addTo(listOf("foo", "bar"), "baz")
    }
    assertFailsWith<UnsupportedOperationException> { J2KtCollections.addTo(emptyList(), "baz") }
  }

  // Lower-bounded Java collection parameters (List<? super T>) map to MutableList<in T> in Kotlin.
  @Test
  fun j2ktAddToLowerBounded_acceptsContravariantMutableList() {
    val list: MutableList<Any> = mutableListOf("foo")
    J2KtCollections.addToLowerBounded(list, "bar")
    assertEquals(listOf<Any>("foo", "bar"), list)
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

  // Transpiled collection subtypes (such as AbstractList or ArrayList) implement MutableList in
  // Kotlin and are directly assignable to both List and MutableList without casting.
  @Test
  fun j2ktMutableListSubtype_assignsToListAndMutableList() {
    val asList: List<String> = J2KtCollections.newMutableListSubtype()
    val asMutableList: MutableList<String> = J2KtCollections.newMutableListSubtype()
    asMutableList.add("foo")
    assertEquals(emptyList(), asList)
    assertEquals(listOf("foo"), asMutableList)
  }

  // Base Java List return types map to read-only Kotlin List, even when backed by a mutable Java
  // collection at runtime; casting to MutableList in Kotlin succeeds and allows mutation.
  @Test
  fun j2ktMutableListAsList_assignsToListAndSupportsMutationAfterCast() {
    val list: List<String> = J2KtCollections.newMutableListAsList()
    val mutableList = list as MutableList<String>
    mutableList.add("foo")
    assertEquals(listOf("foo"), list)
  }

  // Transpiled unmodifiable Java collections still implement MutableList at runtime (so casting to
  // MutableList succeeds), and throw UnsupportedOperationException when mutated.
  @Test
  fun j2ktUnmodifiableList_assignsToListAndThrowsOnMutationAfterCast() {
    val list: List<String> = J2KtCollections.newUnmodifiableList()
    val mutableList = list as MutableList<String>
    assertFailsWith<UnsupportedOperationException> { mutableList.add("foo") }
    assertEquals(emptyList(), list)
  }
}
