/*
 * Copyright 2025 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package java.lang

import java.util.function.Supplier

// Maps ThreadLocal instances to their thread local values
@kotlin.native.concurrent.ThreadLocal
private val threadLocalValues: MutableMap<ThreadLocal<*>, Any> = mutableMapOf()

open class ThreadLocal<T> {

  fun get(): T? {
    val result = threadLocalValues[this]
    if (result != null) {
      @Suppress("UNCHECKED_CAST")
      return if (result == NULL_VALUE_MARKER) null else result as T
    }
    val value = initialValue()
    threadLocalValues[this] = value ?: NULL_VALUE_MARKER
    return value
  }

  fun set(value: T) {
    threadLocalValues[this] = value ?: NULL_VALUE_MARKER
  }

  fun remove() {
    threadLocalValues.remove(this)
  }

  protected open fun initialValue(): T? = null

  companion object {
    // Allows us to avoid a separate contains() lookup and nullable map values.
    private val NULL_VALUE_MARKER = Any()

    fun <T> withInitial(supplier: Supplier<T?>) =
      object : ThreadLocal<T>() {
        override fun initialValue() = supplier.get()
      }
  }
}
