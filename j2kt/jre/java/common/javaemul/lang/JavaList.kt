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

import java.util.function.UnaryOperator
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@ObjCName("JavaemulLangJavaList", exact = true)
interface JavaList<E> : MutableList<E>, JavaCollection<E> {
  override fun addAll(c: Collection<E>): Boolean = super<JavaCollection>.addAll(c)

  // TODO(b/243046587): Rewrite to handle case in which c is not mutable
  override fun addAll(index: Int, c: Collection<E>): Boolean {
    return java_addAll(index, c as MutableCollection<E>)
  }

  override fun contains(e: E): Boolean = super<JavaCollection>.contains(e)

  override fun containsAll(c: Collection<E>): Boolean = super<JavaCollection>.containsAll(c)

  override fun indexOf(e: E): Int = java_indexOf(e)

  override fun lastIndexOf(e: E): Int = java_lastIndexOf(e)

  override fun remove(e: E): Boolean = super<JavaCollection>.remove(e)

  override fun removeAll(c: Collection<E>): Boolean = super<JavaCollection>.removeAll(c)

  override fun retainAll(c: Collection<E>): Boolean = super<JavaCollection>.retainAll(c)

  // TODO(b/275568193): Consider allowing c == null
  fun sort(c: Comparator<in E>) = sortWith(c)

  fun java_addAll(index: Int, c: MutableCollection<out E>): Boolean

  fun java_indexOf(a: Any?): Int

  fun java_lastIndexOf(a: Any?): Int

  fun java_replaceAll(operator: UnaryOperator<E>) = default_replaceAll(operator)
}

// TODO(b/275568193): Consider allowing c == null
@Suppress("UNCHECKED_CAST")
fun <E> MutableList<E>.sort(c: Comparator<in E>) {
  if (this is JavaList) {
    val list = this as JavaList<E> // kotlinc type inference fails without the temp variable
    list.sort(c)
  } else {
    sortWith(c)
  }
}

@Suppress("UNCHECKED_CAST")
fun <E> MutableList<E>.java_addAll(index: Int, c: MutableCollection<out E>): Boolean =
  if (this is JavaList) java_addAll(index, c) else addAll(index, c as MutableCollection<E>)

@Suppress("UNCHECKED_CAST")
fun <E> MutableList<E>.java_indexOf(a: Any?): Int =
  if (this is JavaList) java_indexOf(a) else indexOf(a as E)

@Suppress("UNCHECKED_CAST")
fun <E> MutableList<E>.java_lastIndexOf(a: Any?): Int =
  if (this is JavaList) java_lastIndexOf(a) else lastIndexOf(a as E)

fun <E> MutableList<E>.java_replaceAll(operator: UnaryOperator<E>) =
  if (this is JavaList) java_replaceAll(operator) else default_replaceAll(operator)

private fun <E> MutableList<E>.default_replaceAll(operator: UnaryOperator<E>) {
  val itr: MutableListIterator<E> = this.listIterator()
  while (itr.hasNext()) {
    itr.set(operator.apply(itr.next()))
  }
}
