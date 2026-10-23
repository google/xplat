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

import java.util.function.LongBinaryOperator
import java.util.function.LongUnaryOperator
import kotlin.concurrent.atomics.AtomicLongArray as KotlinAtomicLongArray
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.fetchAndUpdateAt
import kotlin.concurrent.atomics.updateAndFetchAt

class AtomicLongArray private constructor(private val array: KotlinAtomicLongArray) {

  constructor(array: LongArray) : this(KotlinAtomicLongArray(array))

  constructor(length: Int) : this(KotlinAtomicLongArray(length))

  fun get(i: Int): Long = array.loadAt(i)

  fun getAndAccumulate(i: Int, x: Long, accumulatorFunction: LongBinaryOperator): Long =
    array.fetchAndUpdateAt(i) { accumulatorFunction.applyAsLong(it, x) }

  fun getAndAdd(i: Int, delta: Long): Long = array.fetchAndAddAt(i, delta)

  fun getAndIncrement(i: Int): Long = array.fetchAndAddAt(i, 1L)

  fun getAndDecrement(i: Int): Long = array.fetchAndAddAt(i, -1L)

  fun getAndSet(i: Int, newValue: Long): Long = array.exchangeAt(i, newValue)

  fun getAndUpdate(i: Int, updateFunction: LongUnaryOperator): Long =
    array.fetchAndUpdateAt(i) { updateFunction.applyAsLong(it) }

  fun accumulateAndGet(i: Int, x: Long, accumulatorFunction: LongBinaryOperator): Long =
    array.updateAndFetchAt(i) { accumulatorFunction.applyAsLong(it, x) }

  fun addAndGet(i: Int, delta: Long): Long = array.addAndFetchAt(i, delta)

  fun incrementAndGet(i: Int): Long = array.addAndFetchAt(i, 1L)

  fun decrementAndGet(i: Int): Long = array.addAndFetchAt(i, -1L)

  fun updateAndGet(i: Int, updateFunction: LongUnaryOperator): Long =
    array.updateAndFetchAt(i) { updateFunction.applyAsLong(it) }

  fun compareAndSet(i: Int, expect: Long, update: Long): Boolean =
    array.compareAndSetAt(i, expect, update)

  fun weakCompareAndSet(i: Int, expect: Long, update: Long): Boolean =
    compareAndSet(i, expect, update)

  fun set(i: Int, newValue: Long) {
    array.storeAt(i, newValue)
  }

  fun lazySet(i: Int, newValue: Long) {
    array.storeAt(i, newValue)
  }

  fun length(): Int = array.size

  override fun toString(): String = array.toString()
}
