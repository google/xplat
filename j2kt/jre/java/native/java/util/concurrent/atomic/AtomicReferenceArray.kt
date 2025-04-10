// CHECKSTYLE_OFF: Copyrighted to Guava Authors.
/*
 * Copyright (C) 2015 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// CHECKSTYLE_ON
@file:OptIn(ExperimentalObjCName::class)

package java.util.concurrent.atomic

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic

/**
 * J2KT Native emulated version of `AtomicReferenceArray`.
 *
 * @param V the element type.
 */
@ObjCName("J2ktJavaUtilConcurrentAtomicReferenceArray", exact = true)
class AtomicReferenceArray<V> private constructor(private val array: Array<AtomicRef<V>>) {

  constructor(array: Array<V>) : this(Array(array.size) { atomic<V>(array[it]) })

  fun compareAndSet(i: Int, expect: V, update: V) = array[i].compareAndSet(expect, update)

  fun get(i: Int) = array[i].value

  fun getAndSet(i: Int, x: V) = array[i].getAndSet(x)

  fun lazySet(i: Int, x: V) = array[i].lazySet(x)

  fun length(): Int = array.size

  fun set(i: Int, x: V) {
    array[i].value = x
  }

  fun weakCompareAndSet(i: Int, expect: V, update: V): Boolean = compareAndSet(i, expect, update)

  override fun toString(): String = array.toString()

  companion object {
    operator fun <V> invoke(length: Int): AtomicReferenceArray<V?> =
      AtomicReferenceArray<V?>(Array(length) { atomic<V?>(null) })
  }
}
