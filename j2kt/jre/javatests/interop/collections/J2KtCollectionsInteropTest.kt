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

import javaemul.lang.asMutableList
import kotlin.test.Test

/** Tests interop between Kotlin and J2KT-generated code. */
class J2KtCollectionsInteropTest {

  // At this moment, J2KT collections map to mutable collections in Kotlin, so read-only collections
  // needs to be wrapped as mutable.
  @Test
  fun j2kt_acceptsKotlinListAsMutableList() {
    J2KtCollections.accept(listOf("foo", "bar").asMutableList())
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
    val unused: MutableList<String> = J2KtCollections.newMutableList()
  }

  @Test
  fun j2ktMutableList_kotlinMutationSupported() {
    val list = J2KtCollections.newMutableList<String>()
    list.add("foo")
    list.addAll(listOf("foo", "bar"))
  }

  @Test
  fun j2ktReadonlyList_assignsToKotlinList() {
    val unused: List<String> = J2KtCollections.newReadonlyList()
  }

  // At this moment, collections from J2KT can be assigned to mutable collections in Kotlin.
  @Test
  fun j2ktReadonlyList_assignsToKotlinMutableList() {
    val unused: MutableList<String> = J2KtCollections.newReadonlyList()
  }

  // At this moment, collections from J2KT are mutable, so mutation is allowed but unsupported.
  @Test
  fun j2ktReadonlyList_kotlinMutationUnsupported() {
    val list: MutableList<String> = J2KtCollections.newReadonlyList()
    try {
      list.add("foo")
    } catch (e: UnsupportedOperationException) {
      // Mutation is unsupported
    }
  }
}
