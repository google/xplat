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
@file:OptIn(ExperimentalAtomicApi::class)

package java.util.concurrent.atomic

import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi

open class AtomicInteger(initialValue: Int) : kotlin.Number() {
  private val ktAtomicInt = AtomicInt(initialValue)

  constructor() : this(0)

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

  override fun toString(): String = get().toString()

  override fun toInt(): Int = get()

  override fun toLong(): Long = get().toLong()

  override fun toFloat(): Float = get().toFloat()

  override fun toDouble(): Double = get().toDouble()

  override fun toByte(): Byte = get().toByte()

  @Deprecated(
    "Direct conversion to Char is deprecated. Use toInt().toChar() or other conversions instead.",
    ReplaceWith("toInt().toChar()"),
  )
  override fun toChar(): Char = get().toChar()

  override fun toShort(): Short = get().toShort()
}
