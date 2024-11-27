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

package java.util

import java.io.Serializable
import kotlin.Cloneable
import kotlin.collections.ArrayList as KtArrayList
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

private const val DEFAULT_CAPACITY = 10

@ObjCName("J2ktJavaUtilArrayList", exact = true)
open class ArrayList<T> private constructor(private val ktArrayList: KtArrayList<T>) :
  AbstractList<T>(), Cloneable, RandomAccess, Serializable, MutableList<T> by ktArrayList {

  constructor(initialCapacity: Int = DEFAULT_CAPACITY) : this(KtArrayList(initialCapacity))

  constructor(c: Collection<T>) : this(KtArrayList(c))

  fun trimToSize() = ktArrayList.trimToSize()

  fun ensureCapacity(capacity: Int) = ktArrayList.ensureCapacity(capacity)

  override fun clone(): Any = ArrayList<T>(KtArrayList<T>(ktArrayList))
}
