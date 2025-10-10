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
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.getAndUpdate
import kotlinx.atomicfu.updateAndGet

class AtomicLongArray private constructor(private val array: Array<kotlinx.atomicfu.AtomicLong>) {

  constructor(
    array: LongArray
  ) : this(Array<kotlinx.atomicfu.AtomicLong>(array.size) { kotlinx.atomicfu.atomic(array[it]) })

  constructor(
    length: Int
  ) : this(Array<kotlinx.atomicfu.AtomicLong>(length) { kotlinx.atomicfu.atomic(0L) })

  fun get(i: Int): Long = array[i].value

  fun getAndAccumulate(i: Int, x: Long, accumulatorFunction: LongBinaryOperator): Long =
    array[i].getAndUpdate { accumulatorFunction.applyAsLong(x, it) }

  fun getAndAdd(i: Int, delta: Long): Long = array[i].getAndAdd(delta)

  fun getAndIncrement(i: Int): Long = array[i].getAndIncrement()

  fun getAndDecrement(i: Int): Long = array[i].getAndDecrement()

  fun getAndSet(i: Int, newValue: Long): Long = array[i].getAndSet(newValue)

  fun getAndUpdate(i: Int, updateFunction: LongUnaryOperator): Long =
    array[i].getAndUpdate { updateFunction.applyAsLong(it) }

  fun accumulateAndGet(i: Int, x: Long, accumulatorFunction: LongBinaryOperator): Long =
    array[i].updateAndGet { accumulatorFunction.applyAsLong(x, it) }

  fun addAndGet(i: Int, delta: Long): Long = array[i].addAndGet(delta)

  fun incrementAndGet(i: Int): Long = array[i].incrementAndGet()

  fun decrementAndGet(i: Int): Long = array[i].decrementAndGet()

  fun updateAndGet(i: Int, updateFunction: LongUnaryOperator): Long =
    array[i].updateAndGet { updateFunction.applyAsLong(it) }

  fun compareAndSet(i: Int, expect: Long, update: Long): Boolean =
    array[i].compareAndSet(expect, update)

  fun weakCompareAndSet(i: Int, expect: Long, update: Long): Boolean =
    compareAndSet(i, expect, update)

  fun set(i: Int, newValue: Long) {
    array[i].value = newValue
  }

  fun lazySet(i: Int, newValue: Long) = array[i].lazySet(newValue)

  fun length(): Int = array.size

  override fun toString(): String = array.contentToString()
}
