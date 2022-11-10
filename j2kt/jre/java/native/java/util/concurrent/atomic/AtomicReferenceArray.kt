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
package java.util.concurrent.atomic

/**
 * GWT emulated version of {@link AtomicReferenceArray}.
 *
 * @param <V> the element type.
 */
class AtomicReferenceArray<V> internal constructor(private val values: List<AtomicReference<V>>) {

  constructor(array: Array<V>) : this(array.map { AtomicReference<V>(it) })

  fun compareAndSet(i: Int, expect: V, update: V) = values[i].compareAndSet(expect, update)

  fun get(i: Int) = values[i].get()

  fun getAndSet(i: Int, x: V) = values[i].getAndSet(x)

  fun lazySet(i: Int, x: V) = values[i].lazySet(x)

  fun length(): Int = values.size

  fun set(i: Int, x: V) = values[i].set(x)

  fun weakCompareAndSet(i: Int, expect: V, update: V): Boolean = compareAndSet(i, expect, update)

  override fun toString(): String = values.toString()

  companion object {
    operator fun <V> invoke(length: Int): AtomicReferenceArray<V?> =
      AtomicReferenceArray<V?>((1..length).map { AtomicReference<V>() })
  }
}
