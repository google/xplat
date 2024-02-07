/*
 * Copyright 2022 Google Inc.
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
@file:OptIn(ExperimentalObjCName::class)

package java.util

import java.io.Serializable
import javaemul.lang.JavaAbstractMap
import kotlin.Cloneable
import kotlin.collections.HashMap as KotlinHashMap
import kotlin.collections.Map as KotlinMap
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

private const val DEFAULT_INITIAL_CAPACITY = 16
private const val DEFAULT_LOAD_FACTOR = 0.75f

@ObjCName("J2ktJavaUtilHashMap", exact = true)
open class HashMap<K, V> private constructor(val ktHashMap: KotlinHashMap<K, V>) :
  JavaAbstractMap<K, V>(), Cloneable, Serializable {

  constructor(
    initialCapacity: Int = DEFAULT_INITIAL_CAPACITY,
    loadFactor: Float = DEFAULT_LOAD_FACTOR,
  ) : this(KotlinHashMap<K, V>(initialCapacity, loadFactor))

  constructor(original: KotlinMap<out K, V>) : this(KotlinHashMap(original))

  override val size: Int
    get() = ktHashMap.size

  override fun isEmpty(): Boolean = ktHashMap.isEmpty()

  override final fun containsKey(key: K): Boolean = java_containsKey(key)

  @Suppress("UNCHECKED_CAST")
  override fun java_containsKey(key: Any?): Boolean = ktHashMap.containsKey(key as K)

  override final fun containsValue(value: V): Boolean = java_containsValue(value)

  @Suppress("UNCHECKED_CAST")
  override fun java_containsValue(value: Any?): Boolean = ktHashMap.containsValue(value as V)

  override final operator fun get(key: K): V? = java_get(key)

  @Suppress("UNCHECKED_CAST") override fun java_get(key: Any?): V? = ktHashMap[key as K]

  override fun put(key: K, value: V): V? = ktHashMap.put(key, value)

  override final fun remove(key: K): V? = java_remove(key)

  @Suppress("UNCHECKED_CAST") override fun java_remove(key: Any?): V? = ktHashMap.remove(key as K)

  override fun putAll(m: KotlinMap<out K, V>) = ktHashMap.putAll(m)

  override fun clear() = ktHashMap.clear()

  override val keys: MutableSet<K>
    get() = ktHashMap.keys

  override val values: MutableCollection<V>
    get() = ktHashMap.values

  override val entries: MutableSet<MutableMap.MutableEntry<K, V>> = ktHashMap.entries

  override fun clone(): Any = HashMap<K, V>(KotlinHashMap<K, V>(ktHashMap))
}
