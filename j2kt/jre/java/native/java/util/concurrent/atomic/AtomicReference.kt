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
package java.util.concurrent.atomic

import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic

/**
 * GWT emulation of AtomicReference.
 *
 * @param <V> The type of object referred to by this reference
 */
class AtomicReference<V> internal constructor(private val atomicValue: AtomicRef<V>) {

  constructor(initialValue: V) : this(atomic<V>(initialValue))

  final fun compareAndSet(expect: V, update: V) = atomicValue.compareAndSet(expect, update)

  final fun get(): V = atomicValue.value

  final fun getAndSet(newValue: V): V = atomicValue.getAndSet(newValue)

  final fun lazySet(newValue: V) = atomicValue.lazySet(newValue)

  final fun set(newValue: V) {
    atomicValue.value = newValue
  }

  final fun weakCompareAndSet(expect: V, update: V) = atomicValue.compareAndSet(expect, update)

  override fun toString(): String = atomicValue.toString()

  companion object {
    operator fun <V> invoke(): AtomicReference<V?> = AtomicReference<V?>(atomic<V?>(null))
  }
}
