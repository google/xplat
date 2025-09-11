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
package java.util.concurrent.atomic

import java.util.function.LongBinaryOperator
import java.util.function.LongUnaryOperator
import kotlin.collections.contentToString

@OptIn(kotlin.concurrent.atomics.ExperimentalAtomicApi::class)
class AtomicLongArray
private constructor(private val array: Array<kotlin.concurrent.atomics.AtomicLong>) {

  constructor(
    array: LongArray
  ) : this(
    Array<kotlin.concurrent.atomics.AtomicLong>(array.size) {
      kotlin.concurrent.atomics.AtomicLong(array[it])
    }
  )

  constructor(
    length: Int
  ) : this(
    Array<kotlin.concurrent.atomics.AtomicLong>(length) { kotlin.concurrent.atomics.AtomicLong(0L) }
  )

  fun get(i: Int): Long = array[i].load()

  fun getAndAccumulate(i: Int, x: Long, accumulatorFunction: LongBinaryOperator): Long =
    array[i].fetchAndUpdate { accumulatorFunction.applyAsLong(x, it) }

  fun getAndAdd(i: Int, delta: Long): Long = array[i].fetchAndAdd(delta)

  fun getAndIncrement(i: Int): Long = array[i].fetchAndAdd(1)

  fun getAndDecrement(i: Int): Long = array[i].fetchAndAdd(-1)

  fun getAndSet(i: Int, newValue: Long): Long = array[i].exchange(newValue)

  fun getAndUpdate(i: Int, updateFunction: LongUnaryOperator): Long =
    array[i].fetchAndUpdate { updateFunction.applyAsLong(it) }

  fun accumulateAndGet(i: Int, x: Long, accumulatorFunction: LongBinaryOperator): Long =
    array[i].updateAndFetch { accumulatorFunction.applyAsLong(x, it) }

  fun addAndGet(i: Int, delta: Long): Long = array[i].addAndFetch(delta)

  fun incrementAndGet(i: Int): Long = array[i].addAndFetch(1)

  fun decrementAndGet(i: Int): Long = array[i].addAndFetch(-1)

  fun updateAndGet(i: Int, updateFunction: LongUnaryOperator): Long =
    array[i].updateAndFetch { updateFunction.applyAsLong(it) }

  fun compareAndSet(i: Int, expect: Long, update: Long): Boolean =
    array[i].compareAndSet(expect, update)

  fun weakCompareAndSet(i: Int, expect: Long, update: Long): Boolean =
    compareAndSet(i, expect, update)

  fun set(i: Int, newValue: Long) {
    array[i].store(newValue)
  }

  fun lazySet(i: Int, newValue: Long) {
    array[i].store(newValue)
  }

  fun length(): Int = array.size

  override fun toString(): String = array.contentToString()

  // TODO(b/444408156): Replace this with extension function imports.
  companion object {
    fun kotlin.concurrent.atomics.AtomicLong.fetchAndUpdate(transform: (Long) -> Long): Long {
      while (true) {
        val old = load()
        val newValue = transform(old)
        if (compareAndSet(old, newValue)) return old
      }
    }

    fun kotlin.concurrent.atomics.AtomicLong.updateAndFetch(transform: (Long) -> Long): Long {
      while (true) {
        val old = load()
        val newValue = transform(old)
        if (compareAndSet(old, newValue)) return newValue
      }
    }
  }
}
