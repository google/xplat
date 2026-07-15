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

import java.util.function.BiConsumer
import java.util.function.BiFunction
import java.util.function.Function
import javaemul.lang.default_compute
import javaemul.lang.default_computeIfAbsent
import javaemul.lang.default_computeIfPresent
import javaemul.lang.default_forEach
import javaemul.lang.default_getOrDefault
import javaemul.lang.default_merge
import javaemul.lang.default_putIfAbsent
import javaemul.lang.default_remove
import javaemul.lang.default_replace
import javaemul.lang.default_replaceAll
import kotlin.Comparator as KotlinComparator
import kotlin.collections.Map as KotlinMap

interface Map<K, V> : MutableMap<K, V> {

  companion object {
    fun <K : Any, V : Any> entry(k: K, v: V): KotlinMap.Entry<K, V> =
      AbstractMap.SimpleImmutableEntry(k, v)

    fun <K, V> of(): KotlinMap<K, V> = createInternal<K, V>()

    fun <K : Any, V : Any> of(k1: K, v1: V): KotlinMap<K, V> = createInternal(k1, v1)

    fun <K : Any, V : Any> of(k1: K, v1: V, k2: K, v2: V): KotlinMap<K, V> =
      createInternal(k1, v1, k2, v2)

    fun <K : Any, V : Any> of(k1: K, v1: V, k2: K, v2: V, k3: K, v3: V): KotlinMap<K, V> =
      createInternal(k1, v1, k2, v2, k3, v3)

    fun <K : Any, V : Any> of(
      k1: K,
      v1: V,
      k2: K,
      v2: V,
      k3: K,
      v3: V,
      k4: K,
      v4: V,
    ): KotlinMap<K, V> = createInternal(k1, v1, k2, v2, k3, v3, k4, v4)

    fun <K : Any, V : Any> of(
      k1: K,
      v1: V,
      k2: K,
      v2: V,
      k3: K,
      v3: V,
      k4: K,
      v4: V,
      k5: K,
      v5: V,
    ): KotlinMap<K, V> = createInternal(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5)

    fun <K : Any, V : Any> of(
      k1: K,
      v1: V,
      k2: K,
      v2: V,
      k3: K,
      v3: V,
      k4: K,
      v4: V,
      k5: K,
      v5: V,
      k6: K,
      v6: V,
    ): KotlinMap<K, V> = createInternal(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6)

    fun <K : Any, V : Any> of(
      k1: K,
      v1: V,
      k2: K,
      v2: V,
      k3: K,
      v3: V,
      k4: K,
      v4: V,
      k5: K,
      v5: V,
      k6: K,
      v6: V,
      k7: K,
      v7: V,
    ): KotlinMap<K, V> = createInternal(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7)

    fun <K : Any, V : Any> of(
      k1: K,
      v1: V,
      k2: K,
      v2: V,
      k3: K,
      v3: V,
      k4: K,
      v4: V,
      k5: K,
      v5: V,
      k6: K,
      v6: V,
      k7: K,
      v7: V,
      k8: K,
      v8: V,
    ): KotlinMap<K, V> =
      createInternal(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8)

    fun <K : Any, V : Any> of(
      k1: K,
      v1: V,
      k2: K,
      v2: V,
      k3: K,
      v3: V,
      k4: K,
      v4: V,
      k5: K,
      v5: V,
      k6: K,
      v6: V,
      k7: K,
      v7: V,
      k8: K,
      v8: V,
      k9: K,
      v9: V,
    ): KotlinMap<K, V> =
      createInternal(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9)

    fun <K : Any, V : Any> of(
      k1: K,
      v1: V,
      k2: K,
      v2: V,
      k3: K,
      v3: V,
      k4: K,
      v4: V,
      k5: K,
      v5: V,
      k6: K,
      v6: V,
      k7: K,
      v7: V,
      k8: K,
      v8: V,
      k9: K,
      v9: V,
      k10: K,
      v10: V,
    ): KotlinMap<K, V> =
      createInternal(
        k1,
        v1,
        k2,
        v2,
        k3,
        v3,
        k4,
        v4,
        k5,
        v5,
        k6,
        v6,
        k7,
        v7,
        k8,
        v8,
        k9,
        v9,
        k10,
        v10,
      )

    fun <K : Any, V : Any> ofEntries(vararg entries: KotlinMap.Entry<K, V>): KotlinMap<K, V> =
      buildMap {
        for (entry in entries) {
          val k = Objects.requireNonNull(entry.key)
          val v = Objects.requireNonNull(entry.value)
          if (put(k, v) != null) {
            throw IllegalArgumentException("Duplicate key: $k")
          }
        }
      }

    fun <K : Any, V : Any> copyOf(map: KotlinMap<out K, V>): KotlinMap<K, V> = buildMap {
      for (entry in map.entries) {
        val k = Objects.requireNonNull(entry.key)
        val v = Objects.requireNonNull(entry.value)
        put(k, v)
      }
    }

    private fun <K, V> createInternal(vararg elements: Any): KotlinMap<K, V> = buildMap {
      var i = 0
      while (i < elements.size) {
        val k = elements[i++] as K
        val v = elements[i++] as V
        if (put(k, v) != null) {
          throw IllegalArgumentException("Duplicate key: $k")
        }
      }
    }
  }

  fun compute(key: K, remappingFunction: BiFunction<in K, in V?, out V?>): V? =
    default_compute(key, remappingFunction)

  fun computeIfAbsent(key: K, mappingFunction: Function<in K, out V>): V =
    default_computeIfAbsent(key, mappingFunction)

  fun computeIfPresent(key: K, remappingFunction: BiFunction<in K, in V & Any, out V?>): V? =
    default_computeIfPresent(key, remappingFunction)

  fun forEach(action: BiConsumer<in K, in V>) = default_forEach(action)

  fun getOrDefault(key: K, defaultValue: V): V = default_getOrDefault(key, defaultValue)

  fun putIfAbsent(key: K, value: V): V? = default_putIfAbsent(key, value)

  fun replace(key: K, value: V): V? = default_replace(key, value)

  fun replace(key: K, oldValue: V, newValue: V): Boolean = default_replace(key, oldValue, newValue)

  fun replaceAll(function: BiFunction<in K, in V, out V>) = default_replaceAll(function)

  fun merge(key: K, value: V & Any, remap: BiFunction<in V & Any, in V & Any, out V?>): V? =
    default_merge(key, value, remap)

  fun remove(key: K, value: V): Boolean = default_remove(key, value)

  interface Entry<K, V> : MutableMap.MutableEntry<K, V> {

    companion object {
      fun <K : Comparable<K>, V> comparingByKey(): KotlinComparator<KotlinMap.Entry<K, V>> =
        comparingByKey(Comparator.naturalOrder())

      fun <K, V> comparingByKey(
        cmp: KotlinComparator<in K>
      ): KotlinComparator<KotlinMap.Entry<K, V>> =
        object : KotlinComparator<KotlinMap.Entry<K, V>> {
          override fun compare(a: KotlinMap.Entry<K, V>, b: KotlinMap.Entry<K, V>) =
            cmp.compare(a.key, b.key)
        }

      fun <K, V : Comparable<V>> comparingByValue(): KotlinComparator<KotlinMap.Entry<K, V>> =
        comparingByValue(Comparator.naturalOrder())

      fun <K, V> comparingByValue(
        cmp: KotlinComparator<in V>
      ): KotlinComparator<KotlinMap.Entry<K, V>> =
        object : KotlinComparator<KotlinMap.Entry<K, V>> {
          override fun compare(a: KotlinMap.Entry<K, V>, b: KotlinMap.Entry<K, V>) =
            cmp.compare(a.value, b.value)
        }
    }
  }
}
