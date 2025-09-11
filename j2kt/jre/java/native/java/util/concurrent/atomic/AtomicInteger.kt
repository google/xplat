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
package java.util.concurrent.atomic

@OptIn(kotlin.concurrent.atomics.ExperimentalAtomicApi::class)
open class AtomicInteger
internal constructor(private val ktAtomicInt: kotlin.concurrent.atomics.AtomicInt) :
  kotlin.Number() {

  constructor() : this(kotlin.concurrent.atomics.AtomicInt(0))

  constructor(initialValue: Int) : this(kotlin.concurrent.atomics.AtomicInt(initialValue))

  fun get(): Int = ktAtomicInt.load()

  fun set(newValue: Int) {
    ktAtomicInt.store(newValue)
  }

  fun lazySet(newValue: Int) {
    ktAtomicInt.store(newValue)
  }

  fun getAndSet(newValue: Int): Int = ktAtomicInt.exchange(newValue)

  fun compareAndSet(expect: Int, update: Int): Boolean = ktAtomicInt.compareAndSet(expect, update)

  fun getAndIncrement(): Int = ktAtomicInt.fetchAndAdd(1)

  fun getAndDecrement(): Int = ktAtomicInt.fetchAndAdd(-1)

  fun getAndAdd(delta: Int): Int = ktAtomicInt.fetchAndAdd(delta)

  fun incrementAndGet(): Int = ktAtomicInt.addAndFetch(1)

  fun decrementAndGet(): Int = ktAtomicInt.addAndFetch(-1)

  fun addAndGet(delta: Int): Int = ktAtomicInt.addAndFetch(delta)

  override fun toString(): String = ktAtomicInt.toString()

  override fun toInt(): Int = ktAtomicInt.load()

  override fun toLong(): Long = ktAtomicInt.load().toLong()

  override fun toFloat(): Float = ktAtomicInt.load().toFloat()

  override fun toDouble(): Double = ktAtomicInt.load().toDouble()

  override fun toByte(): Byte = ktAtomicInt.load().toByte()

  override fun toChar(): Char = ktAtomicInt.load().toChar()

  override fun toShort(): Short = ktAtomicInt.load().toShort()
}
