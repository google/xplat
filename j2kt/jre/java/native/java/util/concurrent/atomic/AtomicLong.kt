/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:OptIn(ExperimentalAtomicApi::class)

package java.util.concurrent.atomic

import kotlin.concurrent.atomics.AtomicLong as KotlinAtomicLong
import kotlin.concurrent.atomics.ExperimentalAtomicApi

class AtomicLong(initialValue: Long) : kotlin.Number() {
  private val ktAtomicLong = KotlinAtomicLong(initialValue)

  constructor() : this(0L)

  fun get(): Long = ktAtomicLong.load()

  fun set(newValue: Long) {
    ktAtomicLong.store(newValue)
  }

  fun lazySet(newValue: Long) {
    ktAtomicLong.store(newValue)
  }

  fun getAndSet(newValue: Long): Long = ktAtomicLong.exchange(newValue)

  fun compareAndSet(expect: Long, update: Long): Boolean =
    ktAtomicLong.compareAndSet(expect, update)

  fun getAndIncrement(): Long = ktAtomicLong.fetchAndAdd(1L)

  fun getAndDecrement(): Long = ktAtomicLong.fetchAndAdd(-1L)

  fun getAndAdd(delta: Long): Long = ktAtomicLong.fetchAndAdd(delta)

  fun incrementAndGet(): Long = ktAtomicLong.addAndFetch(1L)

  fun decrementAndGet(): Long = ktAtomicLong.addAndFetch(-1L)

  fun addAndGet(delta: Long): Long = ktAtomicLong.addAndFetch(delta)

  override fun toString(): String = get().toString()

  override fun toInt(): Int = get().toInt()

  override fun toLong(): Long = get()

  override fun toFloat(): Float = get().toFloat()

  override fun toDouble(): Double = get().toDouble()

  override fun toByte(): Byte = get().toByte()

  @Deprecated(
    "Direct conversion to Char is deprecated. Use toInt().toChar() or other conversions instead.",
    ReplaceWith("toInt().toChar()"),
  )
  override fun toChar(): Char = get().toInt().toChar()

  override fun toShort(): Short = get().toShort()
}
