/*
 * Copyright 2025 Google Inc.
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

package java.util.concurrent.atomic

import java.util.function.IntBinaryOperator
import java.util.function.IntUnaryOperator
import kotlin.collections.contentToString
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.getAndUpdate
import kotlinx.atomicfu.updateAndGet

@ObjCName("J2ktJavaUtilConcurrentAtomicIntegerArray", exact = true)
class AtomicIntegerArray private constructor(private val array: Array<kotlinx.atomicfu.AtomicInt>) {

  constructor(
    array: IntArray
  ) : this(Array<kotlinx.atomicfu.AtomicInt>(array.size) { kotlinx.atomicfu.atomic(array[it]) })

  constructor(
    length: Int
  ) : this(Array<kotlinx.atomicfu.AtomicInt>(length) { kotlinx.atomicfu.atomic(0) })

  fun get(i: Int): Int = array[i].value

  fun getAndAccumulate(i: Int, x: Int, accumulatorFunction: IntBinaryOperator): Int =
    array[i].getAndUpdate { accumulatorFunction.applyAsInt(x, it) }

  fun getAndAdd(i: Int, delta: Int): Int = array[i].getAndAdd(delta)

  fun getAndIncrement(i: Int): Int = array[i].getAndIncrement()

  fun getAndDecrement(i: Int): Int = array[i].getAndDecrement()

  fun getAndSet(i: Int, newValue: Int): Int = array[i].getAndSet(newValue)

  fun getAndUpdate(i: Int, updateFunction: IntUnaryOperator): Int =
    array[i].getAndUpdate { updateFunction.applyAsInt(it) }

  fun accumulateAndGet(i: Int, x: Int, accumulatorFunction: IntBinaryOperator): Int =
    array[i].updateAndGet { accumulatorFunction.applyAsInt(x, it) }

  fun addAndGet(i: Int, delta: Int): Int = array[i].addAndGet(delta)

  fun incrementAndGet(i: Int): Int = array[i].incrementAndGet()

  fun decrementAndGet(i: Int): Int = array[i].decrementAndGet()

  fun updateAndGet(i: Int, updateFunction: IntUnaryOperator): Int =
    array[i].updateAndGet { updateFunction.applyAsInt(it) }

  fun compareAndSet(i: Int, expect: Int, update: Int): Boolean =
    array[i].compareAndSet(expect, update)

  fun weakCompareAndSet(i: Int, expect: Int, update: Int): Boolean =
    compareAndSet(i, expect, update)

  fun set(i: Int, newValue: Int) {
    array[i].value = newValue
  }

  fun lazySet(i: Int, newValue: Int) = array[i].lazySet(newValue)

  fun length(): Int = array.size

  override fun toString(): String = array.contentToString()
}
