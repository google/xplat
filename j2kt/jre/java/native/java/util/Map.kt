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

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

interface Map {

  interface Entry {

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
