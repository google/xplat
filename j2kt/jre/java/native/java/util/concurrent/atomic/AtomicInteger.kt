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
@file:OptIn(ExperimentalObjCName::class)

package java.util.concurrent.atomic

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@ObjCName("J2ktJavaUtilConcurrentAtomicInteger", exact = true)
open class AtomicInteger internal constructor(private val ktAtomicInt: kotlinx.atomicfu.AtomicInt) :
  kotlin.Number() {

  constructor() : this(kotlinx.atomicfu.atomic(0))

  constructor(initialValue: Int) : this(kotlinx.atomicfu.atomic(initialValue))

  fun get(): Int = ktAtomicInt.value

  fun set(newValue: Int) {
    ktAtomicInt.value = newValue
  }

  fun lazySet(newValue: Int) = ktAtomicInt.lazySet(newValue)

  fun getAndSet(newValue: Int): Int = ktAtomicInt.getAndSet(newValue)

  fun compareAndSet(expect: Int, update: Int): Boolean = ktAtomicInt.compareAndSet(expect, update)

  fun getAndIncrement(): Int = ktAtomicInt.getAndIncrement()

  fun getAndDecrement(): Int = ktAtomicInt.getAndDecrement()

  fun getAndAdd(delta: Int): Int = ktAtomicInt.getAndAdd(delta)

  fun incrementAndGet(): Int = ktAtomicInt.incrementAndGet()

  fun decrementAndGet(): Int = ktAtomicInt.decrementAndGet()

  fun addAndGet(delta: Int): Int = ktAtomicInt.addAndGet(delta)

  override fun toString(): String = ktAtomicInt.toString()

  override fun toInt(): Int = ktAtomicInt.value

  override fun toLong(): Long = ktAtomicInt.value.toLong()

  override fun toFloat(): Float = ktAtomicInt.value.toFloat()

  override fun toDouble(): Double = ktAtomicInt.value.toDouble()

  override fun toByte(): Byte = ktAtomicInt.value.toByte()

  override fun toChar(): Char = ktAtomicInt.value.toChar()

  override fun toShort(): Short = ktAtomicInt.value.toShort()
}
