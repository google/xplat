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
package javaemul.lang

import java.lang.reflect.Array as JavaLangReflectArray
import kotlin.jvm.javaObjectType

/** Bridge class for java.util.Collection. */
interface JavaCollection<E> : MutableCollection<E>, JavaIterable<E> {
  // TODO(b/243046587): Rewrite to handle case in which c is not mutable
  override fun addAll(c: Collection<E>): Boolean = java_addAll(c as MutableCollection<E>)

  override fun containsAll(c: Collection<E>): Boolean = java_containsAll(c as MutableCollection<E>)

  override fun removeAll(c: Collection<E>): Boolean = java_removeAll(c as MutableCollection<E>)

  override fun retainAll(c: Collection<E>): Boolean = java_retainAll(c as MutableCollection<E>)

  abstract fun java_addAll(c: MutableCollection<E>): Boolean

  abstract fun java_containsAll(c: MutableCollection<E>): Boolean

  abstract fun java_removeAll(c: MutableCollection<E>): Boolean

  abstract fun java_retainAll(c: MutableCollection<E>): Boolean

  fun java_toArray(): Array<Any?> = default_toArray()

  fun <T> java_toArray(a: Array<T>?): Array<T> = default_toArray(a)
}

fun <E> MutableCollection<E>.java_addAll(c: MutableCollection<out E>): Boolean {
  if (this is JavaCollection) return java_addAll(c) else return addAll(c)
}

fun <E> MutableCollection<E>.java_containsAll(c: MutableCollection<out E>): Boolean {
  if (this is JavaCollection) return java_containsAll(c) else return containsAll(c)
}

fun <E> MutableCollection<E>.java_removeAll(c: MutableCollection<out E>): Boolean {
  if (this is JavaCollection) return removeAll(c) else return containsAll(c)
}

fun <E> MutableCollection<E>.java_retainAll(c: MutableCollection<out E>): Boolean {
  if (this is JavaCollection) return java_retainAll(c) else return containsAll(c)
}

fun MutableCollection<*>.java_toArray(): Array<Any?> =
  if (this is JavaCollection) java_toArray() else default_toArray()

fun <T> MutableCollection<*>.java_toArray(a: Array<T?>?): Array<T?> =
  if (this is JavaCollection) java_toArray(a!!) else default_toArray(a!!)

private fun MutableCollection<*>.default_toArray(): Array<Any?> {
  val emptyArray: Array<Any?> = emptyArray<Any?>()
  return default_toArray(emptyArray)
}

private fun <T> MutableCollection<*>.default_toArray(a: Array<T>?): Array<T> {
  a!!
  if (this.size > a.size) {
    return default_toArray(
      JavaLangReflectArray.newInstance(a::class.javaObjectType.getComponentType(), size) as Array<T>
    )
  } else {
    val iterator = iterator()
    var index = 0
    while (iterator.hasNext()) {
      a[index++] = iterator.next() as T
    }
    if (index < a.size) {
      a[index] = null as T
    }
    return a
  }
}
