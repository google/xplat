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

class AtomicLong internal constructor(private val ktAtomicLong: kotlinx.atomicfu.AtomicLong) :
  kotlin.Number() {

  constructor() : this(kotlinx.atomicfu.atomic(0L))

  constructor(initialValue: Long) : this(kotlinx.atomicfu.atomic(initialValue))

  fun get(): Long = ktAtomicLong.value

  fun set(newValue: Long) {
    ktAtomicLong.value = newValue
  }

  fun lazySet(newValue: Long) = ktAtomicLong.lazySet(newValue)

  fun getAndSet(newValue: Long): Long = ktAtomicLong.getAndSet(newValue)

  fun compareAndSet(expect: Long, update: Long): Boolean =
    ktAtomicLong.compareAndSet(expect, update)

  fun getAndIncrement(): Long = ktAtomicLong.getAndIncrement()

  fun getAndDecrement(): Long = ktAtomicLong.getAndDecrement()

  fun getAndAdd(delta: Long): Long = ktAtomicLong.getAndAdd(delta)

  fun incrementAndGet(): Long = ktAtomicLong.incrementAndGet()

  fun decrementAndGet(): Long = ktAtomicLong.decrementAndGet()

  fun addAndGet(delta: Long): Long = ktAtomicLong.addAndGet(delta)

  override fun toString(): String = ktAtomicLong.toString()

  override fun toInt(): Int = ktAtomicLong.value.toInt()

  override fun toLong(): Long = ktAtomicLong.value

  override fun toFloat(): Float = ktAtomicLong.value.toFloat()

  override fun toDouble(): Double = ktAtomicLong.value.toDouble()

  override fun toByte(): Byte = ktAtomicLong.value.toByte()

  override fun toChar(): Char = ktAtomicLong.value.toChar()

  override fun toShort(): Short = ktAtomicLong.value.toShort()
}
