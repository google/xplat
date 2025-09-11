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
package java.util.concurrent.atomic

@OptIn(kotlin.concurrent.atomics.ExperimentalAtomicApi::class)
@Suppress("DEPRECATION")
class AtomicLong
internal constructor(private val ktAtomicLong: kotlin.concurrent.atomics.AtomicLong) :
  kotlin.Number() {

  constructor() : this(kotlin.concurrent.atomics.AtomicLong(0L))

  constructor(initialValue: Long) : this(kotlin.concurrent.atomics.AtomicLong(initialValue))

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

  fun getAndIncrement(): Long = ktAtomicLong.fetchAndAdd(1)

  fun getAndDecrement(): Long = ktAtomicLong.fetchAndAdd(-1)

  fun getAndAdd(delta: Long): Long = ktAtomicLong.fetchAndAdd(delta)

  fun incrementAndGet(): Long = ktAtomicLong.addAndFetch(1)

  fun decrementAndGet(): Long = ktAtomicLong.addAndFetch(-1)

  fun addAndGet(delta: Long): Long = ktAtomicLong.addAndFetch(delta)

  override fun toString(): String = ktAtomicLong.toString()

  override fun toInt(): Int = ktAtomicLong.load().toInt()

  override fun toLong(): Long = ktAtomicLong.load()

  override fun toFloat(): Float = ktAtomicLong.load().toFloat()

  override fun toDouble(): Double = ktAtomicLong.load().toDouble()

  override fun toByte(): Byte = ktAtomicLong.load().toByte()

  override fun toChar(): Char = ktAtomicLong.load().toChar()

  override fun toShort(): Short = ktAtomicLong.load().toShort()
}
