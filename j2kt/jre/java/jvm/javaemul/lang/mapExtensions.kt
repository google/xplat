/*
 * Copyright 2026 Google Inc.
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
package javaemul.lang

@JvmSynthetic fun <K, V> Map<K, V>.asMutableMap(): MutableMap<K, V> = asMutable()

@JvmName("alreadyMutableAsMutableMap")
@JvmSynthetic
fun <K, V> MutableMap<K, V>.asMutableMap(): MutableMap<K, V> = this

@JvmSynthetic
fun <K, V> Map.Entry<K, V>.asMutableEntry(): MutableMap.MutableEntry<K, V> = asMutable()

@JvmName("alreadyMutableAsMutableEntry")
@JvmSynthetic
fun <K, V> MutableMap.MutableEntry<K, V>.asMutableEntry(): MutableMap.MutableEntry<K, V> = this

@JvmSynthetic
@Suppress("UNCHECKED_CAST")
fun <K, V> Set<Map.Entry<K, V>>.asMutableEntrySet(): MutableSet<MutableMap.MutableEntry<K, V>> =
  asMutable()

@JvmName("alreadyMutableAsMutableEntrySet")
@JvmSynthetic
@Suppress("UNCHECKED_CAST")
fun <K, V> MutableSet<Map.Entry<K, V>>.asMutableEntrySet():
  MutableSet<MutableMap.MutableEntry<K, V>> = this as MutableSet<MutableMap.MutableEntry<K, V>>
