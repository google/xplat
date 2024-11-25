/*
 * Copyright 2024 Google Inc.
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

package java.util

import kotlin.Cloneable

// Note: This implementation relies on the fact that Kotlin Native's HashMap is linked.
open class LinkedHashMap<K, V> : HashMap<K, V>, MutableMap<K, V>, Cloneable {
  constructor() : super()

  constructor(initialCapacity: Int) : super(initialCapacity)

  constructor(initialCapacity: Int, loadFactor: Float) : super(initialCapacity, loadFactor)

  constructor(original: MutableMap<out K, out V>) : super(original)

  override fun clone(): Any = LinkedHashMap(this)
}
