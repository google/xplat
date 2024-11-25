/*
 * Copyright 2022 Google Inc.
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
@file:OptIn(ExperimentalObjCName::class)

package javaemul.lang

import java.util.Spliterator
import java.util.function.UnaryOperator
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@ObjCName("JavaemulLangJavaList", exact = true)
interface JavaList<E> : MutableList<E>, JavaCollection<E> {
  fun replaceAll(operator: UnaryOperator<E>) = default_replaceAll(operator)

  override fun spliterator(): Spliterator<E> = super<JavaCollection>.spliterator()

  fun sort(c: Comparator<in E>?) = if (c == null) sortBy { it as Comparable<Any> } else sortWith(c)
}

@Suppress("UNCHECKED_CAST")
fun <E> MutableList<E>.sort(c: Comparator<in E>?) {
  if (this is JavaList) {
    val list = this as JavaList<E> // kotlinc type inference fails without the temp variable
    list.sort(c)
  } else if (c != null) {
    sortWith(c)
  } else {
    sortBy { it as Comparable<Any> }
  }
}

internal fun <E> MutableList<E>.default_replaceAll(operator: UnaryOperator<E>) {
  val iterator = listIterator()
  while (iterator.hasNext()) {
    iterator.set(operator.apply(iterator.next()))
  }
}
