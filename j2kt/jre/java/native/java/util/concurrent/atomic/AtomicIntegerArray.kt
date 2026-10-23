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
@file:OptIn(ExperimentalAtomicApi::class)

package java.util.concurrent.atomic

import java.util.function.IntBinaryOperator
import java.util.function.IntUnaryOperator
import kotlin.concurrent.atomics.AtomicIntArray
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.fetchAndUpdateAt
import kotlin.concurrent.atomics.updateAndFetchAt

class AtomicIntegerArray private constructor(private val array: AtomicIntArray) {

  constructor(array: IntArray) : this(AtomicIntArray(array))

  constructor(length: Int) : this(AtomicIntArray(length))

  fun get(i: Int): Int = array.loadAt(i)

  fun getAndAccumulate(i: Int, x: Int, accumulatorFunction: IntBinaryOperator): Int =
    array.fetchAndUpdateAt(i) { accumulatorFunction.applyAsInt(it, x) }

  fun getAndAdd(i: Int, delta: Int): Int = array.fetchAndAddAt(i, delta)

  fun getAndIncrement(i: Int): Int = array.fetchAndAddAt(i, 1)

  fun getAndDecrement(i: Int): Int = array.fetchAndAddAt(i, -1)

  fun getAndSet(i: Int, newValue: Int): Int = array.exchangeAt(i, newValue)

  fun getAndUpdate(i: Int, updateFunction: IntUnaryOperator): Int =
    array.fetchAndUpdateAt(i) { updateFunction.applyAsInt(it) }

  fun accumulateAndGet(i: Int, x: Int, accumulatorFunction: IntBinaryOperator): Int =
    array.updateAndFetchAt(i) { accumulatorFunction.applyAsInt(it, x) }

  fun addAndGet(i: Int, delta: Int): Int = array.addAndFetchAt(i, delta)

  fun incrementAndGet(i: Int): Int = array.addAndFetchAt(i, 1)

  fun decrementAndGet(i: Int): Int = array.addAndFetchAt(i, -1)

  fun updateAndGet(i: Int, updateFunction: IntUnaryOperator): Int =
    array.updateAndFetchAt(i) { updateFunction.applyAsInt(it) }

  fun compareAndSet(i: Int, expect: Int, update: Int): Boolean =
    array.compareAndSetAt(i, expect, update)

  fun weakCompareAndSet(i: Int, expect: Int, update: Int): Boolean =
    compareAndSet(i, expect, update)

  fun set(i: Int, newValue: Int) {
    array.storeAt(i, newValue)
  }

  fun lazySet(i: Int, newValue: Int) {
    array.storeAt(i, newValue)
  }

  fun length(): Int = array.size

  override fun toString(): String = array.toString()
}
