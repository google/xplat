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

import java.util.function.IntBinaryOperator
import java.util.function.IntUnaryOperator
import kotlin.collections.contentToString

@OptIn(kotlin.concurrent.atomics.ExperimentalAtomicApi::class)
class AtomicIntegerArray
private constructor(private val array: Array<kotlin.concurrent.atomics.AtomicInt>) {

  constructor(
    array: IntArray
  ) : this(
    Array<kotlin.concurrent.atomics.AtomicInt>(array.size) {
      kotlin.concurrent.atomics.AtomicInt(array[it])
    }
  )

  constructor(
    length: Int
  ) : this(
    Array<kotlin.concurrent.atomics.AtomicInt>(length) { kotlin.concurrent.atomics.AtomicInt(0) }
  )

  fun get(i: Int): Int = array[i].load()

  fun getAndAccumulate(i: Int, x: Int, accumulatorFunction: IntBinaryOperator): Int {
    return array[i].fetchAndUpdate { accumulatorFunction.applyAsInt(x, it) }
  }

  fun getAndAdd(i: Int, delta: Int): Int = array[i].fetchAndAdd(delta)

  fun getAndIncrement(i: Int): Int = array[i].fetchAndAdd(1)

  fun getAndDecrement(i: Int): Int = array[i].fetchAndAdd(-1)

  fun getAndSet(i: Int, newValue: Int): Int = array[i].exchange(newValue)

  fun getAndUpdate(i: Int, updateFunction: IntUnaryOperator): Int =
    array[i].fetchAndUpdate { updateFunction.applyAsInt(it) }

  fun accumulateAndGet(i: Int, x: Int, accumulatorFunction: IntBinaryOperator): Int =
    array[i].updateAndFetch { accumulatorFunction.applyAsInt(x, it).toInt() }

  fun addAndGet(i: Int, delta: Int): Int = array[i].addAndFetch(delta)

  fun incrementAndGet(i: Int): Int = array[i].addAndFetch(1)

  fun decrementAndGet(i: Int): Int = array[i].addAndFetch(-1)

  fun updateAndGet(i: Int, updateFunction: IntUnaryOperator): Int =
    array[i].updateAndFetch { updateFunction.applyAsInt(it) }

  fun compareAndSet(i: Int, expect: Int, update: Int): Boolean =
    array[i].compareAndSet(expect, update)

  fun weakCompareAndSet(i: Int, expect: Int, update: Int): Boolean =
    compareAndSet(i, expect, update)

  fun set(i: Int, newValue: Int) {
    array[i].store(newValue)
  }

  fun lazySet(i: Int, newValue: Int) {
    array[i].store(newValue)
  }

  fun length(): Int = array.size

  override fun toString(): String = array.contentToString()

  // TODO(b/444408156): Replace this with extension function imports.
  companion object {
    fun kotlin.concurrent.atomics.AtomicInt.fetchAndUpdate(transform: (Int) -> Int): Int {
      while (true) {
        val old = load()
        val newValue = transform(old)
        if (compareAndSet(old, newValue)) return old
      }
    }

    fun kotlin.concurrent.atomics.AtomicInt.updateAndFetch(transform: (Int) -> Int): Int {
      while (true) {
        val old = load()
        val newValue = transform(old)
        if (compareAndSet(old, newValue)) return newValue
      }
    }
  }
}
