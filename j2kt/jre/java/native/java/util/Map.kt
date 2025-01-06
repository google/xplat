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
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

interface Map<K, V> : MutableMap<K, V> {

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
      @ObjCName("comparingByKey")
      fun <K : Comparable<K>, V> comparingByKey():
        kotlin.Comparator<MutableMap.MutableEntry<K, V>> = comparingByKey(Comparator.naturalOrder())

      @ObjCName("comparingByKey")
      fun <K, V> comparingByKey(
        @ObjCName("withJavaUtilComparator") cmp: kotlin.Comparator<in K>
      ): kotlin.Comparator<MutableMap.MutableEntry<K, V>> =
        object : kotlin.Comparator<MutableMap.MutableEntry<K, V>> {
          override fun compare(a: MutableMap.MutableEntry<K, V>, b: MutableMap.MutableEntry<K, V>) =
            cmp.compare(a.key, b.key)
        }

      @ObjCName("comparingByValue")
      fun <K, V : Comparable<V>> comparingByValue():
        kotlin.Comparator<MutableMap.MutableEntry<K, V>> =
        comparingByValue(Comparator.naturalOrder())

      @ObjCName("comparingByValue")
      fun <K, V> comparingByValue(
        @ObjCName("withJavaUtilComparator") cmp: kotlin.Comparator<in V>
      ): kotlin.Comparator<MutableMap.MutableEntry<K, V>> =
        object : kotlin.Comparator<MutableMap.MutableEntry<K, V>> {
          override fun compare(a: MutableMap.MutableEntry<K, V>, b: MutableMap.MutableEntry<K, V>) =
            cmp.compare(a.value, b.value)
        }
    }
  }
}
