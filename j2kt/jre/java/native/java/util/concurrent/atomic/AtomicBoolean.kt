// CHECKSTYLE_OFF: Copyrighted to Guava Authors.
/*
 * Copyright (C) 2015 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// CHECKSTYLE_ON
@file:OptIn(ExperimentalAtomicApi::class)

package java.util.concurrent.atomic

import kotlin.concurrent.atomics.AtomicBoolean as KotlinAtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

/** J2KT Native emulated version of `AtomicBoolean`. */
class AtomicBoolean(initialValue: Boolean) : java.io.Serializable {
  private val atomicBool = KotlinAtomicBoolean(initialValue)

  constructor() : this(false)

  final fun get(): Boolean = atomicBool.load()

  final fun compareAndSet(expect: Boolean, update: Boolean): Boolean =
    atomicBool.compareAndSet(expect, update)

  fun weakCompareAndSet(expect: Boolean, update: Boolean): Boolean = compareAndSet(expect, update)

  final fun set(newValue: Boolean) {
    atomicBool.store(newValue)
  }

  final fun lazySet(newValue: Boolean) {
    atomicBool.store(newValue)
  }

  final fun getAndSet(newValue: Boolean): Boolean = atomicBool.exchange(newValue)

  override fun toString(): String = get().toString()
}
