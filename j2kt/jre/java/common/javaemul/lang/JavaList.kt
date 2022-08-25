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

interface JavaList<E> : MutableList<E>, JavaCollection<E> {
  override fun addAll(c: Collection<E>): Boolean = super<JavaCollection>.addAll(c)

  override fun containsAll(c: Collection<E>): Boolean = super<JavaCollection>.containsAll(c)

  override fun removeAll(c: Collection<E>): Boolean = super<JavaCollection>.removeAll(c)

  override fun retainAll(c: Collection<E>): Boolean = super<JavaCollection>.retainAll(c)

  abstract fun java_addAll(index: Int, c: MutableCollection<E>): Boolean

  // TODO(b/243046587): Rewrite to handle case in which c is not mutable
  override fun addAll(index: Int, c: Collection<E>): Boolean {
    return java_addAll(index, c as MutableCollection<E>)
  }
}

fun <E> MutableList<E>.java_addAll(index: Int, c: MutableCollection<out E>): Boolean {
  if (this is JavaList) return java_addAll(index, c) else return addAll(index, c)
}
