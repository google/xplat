/*
 * Copyright 2023 Google Inc.
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
package javaemul.lang

// -----------------------
// asMutable() extensions
// -----------------------

/** Returns mutable wrapper which throws on mutation. */
fun <E> Iterator<E>.asMutableIterator(): MutableIterator<E> =
  this as? MutableIterator<E> ?: toUnmodifiableMutableIterator()

/** Returns mutable wrapper which throws on mutation. */
fun <E> ListIterator<E>.asMutableListIterator(): MutableListIterator<E> =
  this as? MutableListIterator<E> ?: toUnmodifiableMutableListIterator()

/** Returns mutable wrapper which throws on mutation. */
fun <E> Iterable<E>.asMutableIterable(): MutableIterable<E> =
  this as? MutableIterable<E> ?: toUnmodifiableMutableIterable()

/** Returns mutable wrapper which throws on mutation. */
fun <E> Collection<E>.asMutableCollection(): MutableCollection<E> =
  this as? MutableCollection<E> ?: toUnmodifiableMutableCollection()

/** Returns mutable wrapper which throws on mutation. */
fun <E> List<E>.asMutableList(): MutableList<E> =
  this as? MutableList<E> ?: UnmodifiableMutableList(this)

/** Returns mutable wrapper which throws on mutation. */
fun <E> Set<E>.asMutableSet(): MutableSet<E> =
  this as? MutableSet<E> ?: UnmodifiableMutableSet(this)

/** Returns mutable wrapper which throws on mutation. */
fun <K, V> Map<K, V>.asMutableMap(): MutableMap<K, V> =
  this as? MutableMap<K, V> ?: UnmodifiableMutableMap(this)

/** Returns mutable wrapper which throws on mutation. */
fun <K, V> Map.Entry<K, V>.asMutableEntry(): MutableMap.MutableEntry<K, V> =
  this as? MutableMap.MutableEntry<K, V> ?: UnmodifiableMutableMapEntry(this)

// -----------------------------------
// toUnmodifiableMutable() extensions
// -----------------------------------

/** Returns unmodifiable mutable iterator wrapper. */
fun <E> Iterator<E>.toUnmodifiableMutableIterator(): MutableIterator<E> =
  UnmodifiableMutableIterator(this)

/** Returns unmodifiable mutable list iterator wrapper. */
fun <E> ListIterator<E>.toUnmodifiableMutableListIterator(): MutableListIterator<E> =
  UnmodifiableMutableListIterator(this)

/** Returns unmodifiable mutable iterable wrapper. */
fun <E> Iterable<E>.toUnmodifiableMutableIterable(): MutableIterable<E> =
  UnmodifiableMutableIterable(this)

/** Returns unmodifiable collection wrapper. */
fun <E> Collection<E>.toUnmodifiableMutableCollection(): MutableCollection<E> =
  UnmodifiableMutableCollection(this)

/** Returns unmodifiable mutable list wrapper. */
fun <E> List<E>.toUnmodifiableMutableList(): MutableList<E> =
  when (this) {
    is RandomAccess -> UnmodifiableMutableRandomAccessList(this)
    else -> UnmodifiableMutableList(this)
  }

/** Returns unmodifiable set wrapper. */
fun <E> Set<E>.toUnmodifiableMutableSet(): MutableSet<E> = UnmodifiableMutableSet(this)

/** Returns unmodifiable map wrapper. */
fun <K, V> Map<K, V>.toUnmodifiableMutableMap(): MutableMap<K, V> = UnmodifiableMutableMap(this)

/** Returns unmodifiable map entry wrapper. */
fun <K, V> Map.Entry<K, V>.toUnmodifiableMutableEntry(): MutableMap.MutableEntry<K, V> =
  UnmodifiableMutableMapEntry(this)

// ----------------------------
// UnmodifiableMutable bridges
// ----------------------------

/** Unmodifiable mutable iterator wrapper. */
internal open class UnmodifiableMutableIterator<E>(private val iterator: Iterator<E>) :
  MutableIterator<E>, Iterator<E> by iterator {
  override fun remove() {
    throw UnsupportedOperationException()
  }
}

/** Unmodifiable mutable list iterator wrapper. */
internal open class UnmodifiableMutableListIterator<E>(private val listIterator: ListIterator<E>) :
  MutableListIterator<E>, ListIterator<E> by listIterator {
  override fun add(element: E) {
    throw UnsupportedOperationException()
  }

  override fun remove() {
    throw UnsupportedOperationException()
  }

  override fun set(element: E) {
    throw UnsupportedOperationException()
  }
}

/** Unmodifiable mutable iterable wrapper. */
internal open class UnmodifiableMutableIterable<E>(private val iterable: Iterable<E>) :
  MutableIterable<E> {
  override fun iterator(): MutableIterator<E> = iterable.iterator().toUnmodifiableMutableIterator()
}

/** Unmodifiable mutable collection wrapper. */
internal open class UnmodifiableMutableCollection<E>(private val collection: Collection<E>) :
  MutableCollection<E>, Collection<E> by collection {
  // Use default hashCode() and equals() implementations, as it's not possible to implement equals()
  // to be symmetric.

  override fun toString(): String = collection.toString()

  override fun iterator(): MutableIterator<E> =
    collection.iterator().toUnmodifiableMutableIterator()

  override fun add(element: E): Boolean {
    throw UnsupportedOperationException()
  }

  override fun addAll(elements: Collection<E>): Boolean {
    throw UnsupportedOperationException()
  }

  override fun clear() {
    throw UnsupportedOperationException()
  }

  override fun retainAll(elements: Collection<E>): Boolean {
    throw UnsupportedOperationException()
  }

  override fun removeAll(elements: Collection<E>): Boolean {
    throw UnsupportedOperationException()
  }

  override fun remove(element: E): Boolean {
    throw UnsupportedOperationException()
  }
}

/** Unmodifiable mutable list wrapper. */
internal open class UnmodifiableMutableList<E>(private val list: List<E>) :
  UnmodifiableMutableCollection<E>(list), MutableList<E> {

  override fun hashCode(): Int = list.hashCode()

  override fun equals(o: Any?): Boolean = list == o

  override fun get(index: Int): E = list[index]

  override fun indexOf(element: E): Int = indexOf(element)

  override fun lastIndexOf(element: E): Int = list.lastIndexOf(element)

  override fun listIterator(): MutableListIterator<E> =
    UnmodifiableMutableListIterator(list.listIterator())

  override fun listIterator(index: Int): MutableListIterator<E> =
    UnmodifiableMutableListIterator(list.listIterator(index))

  override fun subList(fromIndex: Int, toIndex: Int): MutableList<E> =
    UnmodifiableMutableList(list.subList(fromIndex, toIndex))

  override fun add(index: Int, element: E) {
    throw UnsupportedOperationException()
  }

  override fun addAll(index: Int, elements: Collection<E>): Boolean {
    throw UnsupportedOperationException()
  }

  override fun removeAt(index: Int): E {
    throw UnsupportedOperationException()
  }

  override fun set(index: Int, element: E): E {
    throw UnsupportedOperationException()
  }
}

/** Unmodifiable mutable random access list wrapper. */
internal open class UnmodifiableMutableRandomAccessList<E>(private val randomAccessList: List<E>) :
  UnmodifiableMutableList<E>(randomAccessList), RandomAccess {
  override fun subList(fromIndex: Int, toIndex: Int): MutableList<E> =
    UnmodifiableMutableRandomAccessList(randomAccessList.subList(fromIndex, toIndex))
}

/** Unmodifiable mutable set wrapper. */
internal open class UnmodifiableMutableSet<E>(private val set: Set<E>) :
  UnmodifiableMutableCollection<E>(set), MutableSet<E> {
  override fun equals(o: Any?): Boolean = set == o

  override fun hashCode(): Int = set.hashCode()
}

/** Unmodifiable mutable map wrapper. */
internal open class UnmodifiableMutableMap<K, V>(private val map: Map<K, V>) : MutableMap<K, V> {
  override fun equals(o: Any?): Boolean = map == o

  override fun hashCode(): Int = map.hashCode()

  override fun isEmpty(): Boolean = map.isEmpty()

  override val size: Int
    get() = map.size

  override fun containsKey(key: K): Boolean = map.containsKey(key)

  override fun containsValue(value: V): Boolean = map.containsValue(value)

  override fun get(key: K): V? = map[key]

  override val entries: MutableSet<MutableMap.MutableEntry<K, V>> by lazy {
    UnmodifiableMutableMapEntries(map.entries)
  }

  override val keys: MutableSet<K> by lazy { UnmodifiableMutableSet(map.keys) }

  override val values: MutableCollection<V> by lazy { UnmodifiableMutableCollection(map.values) }

  override fun clear() {
    throw UnsupportedOperationException()
  }

  override fun put(key: K, value: V): V? {
    throw UnsupportedOperationException()
  }

  override fun putAll(from: Map<out K, V>) {
    throw UnsupportedOperationException()
  }

  override fun remove(key: K): V? {
    throw UnsupportedOperationException()
  }
}

/** Unmodifiable mutable entry wrapper. */
internal open class UnmodifiableMutableMapEntry<K, V>(val entry: Map.Entry<K, V>) :
  MutableMap.MutableEntry<K, V> {
  override fun hashCode(): Int = entry.hashCode()

  override fun equals(other: Any?): Boolean = entry == this

  override fun toString(): String = entry.toString()

  override val key: K
    get() = entry.key

  override val value: V
    get() = entry.value

  override fun setValue(newValue: V): V {
    throw UnsupportedOperationException()
  }
}

/** Unmodifiable mutable entries wrapper. */
internal open class UnmodifiableMutableMapEntries<K, V>(val entries: Set<Map.Entry<K, V>>) :
  UnmodifiableMutableSet<MutableMap.MutableEntry<K, V>>(
    entries as Set<MutableMap.MutableEntry<K, V>>
  ) {
  override fun iterator(): MutableIterator<MutableMap.MutableEntry<K, V>> =
    object : MutableIterator<MutableMap.MutableEntry<K, V>> {
      private val iterator = iterator()

      override fun hasNext(): Boolean = iterator.hasNext()

      override fun next(): MutableMap.MutableEntry<K, V> =
        UnmodifiableMutableMapEntry(iterator().next())

      override fun remove() {
        throw UnsupportedOperationException()
      }
    }
}
