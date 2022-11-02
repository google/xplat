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
package java.util

import java.lang.UnsupportedOperationException
import kotlin.collections.AbstractMutableMap as KotlinAbstractMutableMap
import kotlin.collections.Map

// TODO(b/255291878): We should transpile J2CL's AbstractMap instead.
abstract class AbstractMap<K, V> protected constructor() : KotlinAbstractMutableMap<K, V>() {
  /** A mutable [Map.Entry] shared by several [Map] implementations. */
  open class SimpleEntry<K, V>(key: K, value: V) : AbstractEntry<K, V>(key, value) {
    constructor(entry: Map.Entry<K, V>) : this(entry.key, entry.value) {}
  }

  /** An immutable [Map.Entry] shared by several [Map] implementations. */
  open class SimpleImmutableEntry<K, V>(key: K, value: V) : AbstractEntry<K, V>(key, value) {
    constructor(entry: Map.Entry<K, V>) : this(entry.key, entry.value) {}
    override fun setValue(value: V): V = throw UnsupportedOperationException()
  }

  /** Basic [Map.Entry] implementation used by [SimpleEntry] and [SimpleImmutableEntry]. */
  /* private */ abstract class AbstractEntry<K, V>
  protected constructor(override val key: K, override var value: V) :
    MutableMap.MutableEntry<K, V> {

    override fun setValue(value: V): V {
      val oldValue = this.value
      this.value = value
      return oldValue
    }

    override fun equals(other: Any?): Boolean {
      if (other !is Map.Entry<*, *>) {
        return false
      }
      val (otherKey, otherValue) = other
      return (key == otherKey && value == otherValue)
    }

    /** Calculate the hash code using Sun's specified algorithm. */
    override fun hashCode(): Int {
      return Objects.hashCode(key) xor Objects.hashCode(value)
    }

    override fun toString(): String {
      return key.toString() + "=" + value
    }
  }
}
