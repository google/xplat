/*
 * Copyright 2019 Google Inc.
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

import kotlin.concurrent.atomics.AtomicReference as KotlinAtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi

/**
 * J2KT Native emulated version of `AtomicReference`.
 *
 * @param V the type of object referred to by this reference.
 */
class AtomicReference<V>(initialValue: V) {
  private val atomicValue = KotlinAtomicReference<V>(initialValue)

  final fun compareAndSet(expect: V, update: V): Boolean = atomicValue.compareAndSet(expect, update)

  final fun get(): V = atomicValue.load()

  final fun getAndSet(newValue: V): V = atomicValue.exchange(newValue)

  final fun lazySet(newValue: V) {
    atomicValue.store(newValue)
  }

  final fun set(newValue: V) {
    atomicValue.store(newValue)
  }

  final fun weakCompareAndSet(expect: V, update: V): Boolean =
    atomicValue.compareAndSet(expect, update)

  override fun toString(): String = get().toString()

  companion object {
    operator fun <V> invoke(): AtomicReference<V?> = AtomicReference<V?>(null)
  }
}
