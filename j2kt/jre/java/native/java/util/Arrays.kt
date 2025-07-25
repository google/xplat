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

import java.lang.ArrayIndexOutOfBoundsException
import java.util.function.BinaryOperator
import java.util.function.DoubleBinaryOperator
import java.util.function.IntBinaryOperator
import java.util.function.IntFunction
import java.util.function.IntToDoubleFunction
import java.util.function.IntToLongFunction
import java.util.function.IntUnaryOperator
import java.util.function.LongBinaryOperator
import java.util.stream.DoubleStream
import java.util.stream.IntStream
import java.util.stream.LongStream
import java.util.stream.Stream
import java.util.stream.StreamSupport
import javaemul.internal.InternalPreconditions.Companion.checkCriticalArrayBounds
import kotlin.Comparator as KotlinComparator
import kotlin.math.min

/**
 * Utility methods related to native arrays. See <a
 * href="https://docs.oracle.com/javase/8/docs/api/java/util/Arrays.html"> the official Java API
 * doc</a> for details.
 */
object Arrays {
  // TODO(b/239034072): Revisit set after varargs are fixed
  fun <T> asList(vararg elements: T): MutableList<T> {
    @Suppress("UNCHECKED_CAST") val array: Array<T> = elements as Array<T>
    return object : AbstractMutableList<T>(), RandomAccess {
      override val size: Int
        get() = array.size

      override fun isEmpty(): Boolean = array.isEmpty()

      override fun contains(element: T): Boolean = array.contains(element)

      override fun get(index: Int): T = array[index]

      override fun indexOf(element: T): Int = this.indexOf(element)

      override fun lastIndexOf(element: T): Int = this.lastIndexOf(element)

      override fun set(index: Int, element: T): T {
        val prevValue = array[index]
        array[index] = element
        return prevValue
      }

      override fun add(index: Int, element: T) = throw UnsupportedOperationException()

      override fun removeAt(index: Int) = throw UnsupportedOperationException()
    }
  }

  fun binarySearch(sortedArray: ByteArray, fromIndex: Int, toIndex: Int, key: Byte): Int {
    checkCriticalArrayBounds(fromIndex, toIndex, sortedArray.size)
    return sortedArray.asList().binarySearch(key, fromIndex, toIndex)
  }

  fun binarySearch(sortedArray: ByteArray, key: Byte): Int {
    return sortedArray.asList().binarySearch(key)
  }

  fun binarySearch(sortedArray: CharArray, fromIndex: Int, toIndex: Int, key: Char): Int {
    checkCriticalArrayBounds(fromIndex, toIndex, sortedArray.size)
    return sortedArray.asList().binarySearch(key, fromIndex, toIndex)
  }

  fun binarySearch(sortedArray: CharArray, key: Char): Int {
    return sortedArray.asList().binarySearch(key)
  }

  fun binarySearch(sortedArray: DoubleArray, fromIndex: Int, toIndex: Int, key: Double): Int {
    checkCriticalArrayBounds(fromIndex, toIndex, sortedArray.size)
    return sortedArray.asList().binarySearch(key, fromIndex, toIndex)
  }

  fun binarySearch(sortedArray: DoubleArray, key: Double): Int {
    return sortedArray.asList().binarySearch(key)
  }

  fun binarySearch(sortedArray: FloatArray, fromIndex: Int, toIndex: Int, key: Float): Int {
    checkCriticalArrayBounds(fromIndex, toIndex, sortedArray.size)
    return sortedArray.asList().binarySearch(key, fromIndex, toIndex)
  }

  fun binarySearch(sortedArray: FloatArray, key: Float): Int {
    return sortedArray.asList().binarySearch(key)
  }

  fun binarySearch(sortedArray: IntArray, fromIndex: Int, toIndex: Int, key: Int): Int {
    checkCriticalArrayBounds(fromIndex, toIndex, sortedArray.size)
    return sortedArray.asList().binarySearch(key, fromIndex, toIndex)
  }

  fun binarySearch(sortedArray: IntArray, key: Int): Int {
    return sortedArray.asList().binarySearch(key)
  }

  fun binarySearch(sortedArray: LongArray, fromIndex: Int, toIndex: Int, key: Long): Int {
    checkCriticalArrayBounds(fromIndex, toIndex, sortedArray.size)
    return sortedArray.asList().binarySearch(key, fromIndex, toIndex)
  }

  fun binarySearch(sortedArray: LongArray, key: Long): Int {
    return sortedArray.asList().binarySearch(key)
  }

  fun binarySearch(sortedArray: Array<out Any?>, fromIndex: Int, toIndex: Int, key: Any?): Int {
    return binarySearch(sortedArray, fromIndex, toIndex, key, null)
  }

  fun binarySearch(sortedArray: Array<out Any?>, key: Any?): Int {
    return binarySearch(sortedArray, key, null)
  }

  fun binarySearch(sortedArray: ShortArray, fromIndex: Int, toIndex: Int, key: Short): Int {
    checkCriticalArrayBounds(fromIndex, toIndex, sortedArray.size)
    return sortedArray.toList().binarySearch(key, fromIndex, toIndex)
  }

  fun binarySearch(sortedArray: ShortArray, key: Short): Int {
    return sortedArray.toList().binarySearch(key)
  }

  fun <T> binarySearch(
    sortedArray: Array<out T>,
    fromIndex: Int,
    toIndex: Int,
    key: T,
    comparator: KotlinComparator<in T>?,
  ): Int {
    checkCriticalArrayBounds(fromIndex, toIndex, sortedArray.size)
    val comparator = Comparators.nullToNaturalOrder(comparator)
    return sortedArray.asList().binarySearch(key, comparator, fromIndex, toIndex)
  }

  fun <T> binarySearch(sortedArray: Array<out T>, key: T, c: KotlinComparator<in T>?): Int {
    val c = Comparators.nullToNaturalOrder(c)
    return sortedArray.asList().binarySearch(key, c)
  }

  fun copyOf(original: BooleanArray, newLength: Int): BooleanArray = original.copyOf(newLength)

  fun copyOf(original: ByteArray, newLength: Int): ByteArray = original.copyOf(newLength)

  fun copyOf(original: CharArray, newLength: Int): CharArray = original.copyOf(newLength)

  fun copyOf(original: DoubleArray, newLength: Int): DoubleArray = original.copyOf(newLength)

  fun copyOf(original: FloatArray, newLength: Int): FloatArray = original.copyOf(newLength)

  fun copyOf(original: IntArray, newLength: Int): IntArray = original.copyOf(newLength)

  fun copyOf(original: LongArray, newLength: Int): LongArray = original.copyOf(newLength)

  fun copyOf(original: ShortArray, newLength: Int): ShortArray = original.copyOf(newLength)

  fun <T> copyOf(original: Array<T?>, newLength: Int): Array<T?> = original.copyOf(newLength)

  fun <T, O> copyOf(original: Array<O?>, newLength: Int, newType: java.lang.Class<*>): Array<T?> =
    copyOfRange(original, 0, newLength, newType)

  fun checkCopyOfRangeArguments(originalSize: Int, from: Int, to: Int) {
    if (from < 0 || from > originalSize) throw ArrayIndexOutOfBoundsException()
    if (from > to) throw IllegalArgumentException()
  }

  fun copyOfRange(original: BooleanArray, from: Int, to: Int): BooleanArray {
    checkCopyOfRangeArguments(original.size, from, to)
    val copy = BooleanArray(to - from)
    return original.copyInto(copy, startIndex = from, endIndex = min(to, original.size))
  }

  fun copyOfRange(original: ByteArray, from: Int, to: Int): ByteArray {
    checkCopyOfRangeArguments(original.size, from, to)
    val copy = ByteArray(to - from)
    return original.copyInto(copy, startIndex = from, endIndex = min(to, original.size))
  }

  fun copyOfRange(original: CharArray, from: Int, to: Int): CharArray {
    checkCopyOfRangeArguments(original.size, from, to)
    val copy = CharArray(to - from)
    return original.copyInto(copy, startIndex = from, endIndex = min(to, original.size))
  }

  fun copyOfRange(original: DoubleArray, from: Int, to: Int): DoubleArray {
    checkCopyOfRangeArguments(original.size, from, to)
    val copy = DoubleArray(to - from)
    return original.copyInto(copy, startIndex = from, endIndex = min(to, original.size))
  }

  fun copyOfRange(original: FloatArray, from: Int, to: Int): FloatArray {
    checkCopyOfRangeArguments(original.size, from, to)
    val copy = FloatArray(to - from)
    return original.copyInto(copy, startIndex = from, endIndex = min(to, original.size))
  }

  fun copyOfRange(original: IntArray, from: Int, to: Int): IntArray {
    checkCopyOfRangeArguments(original.size, from, to)
    val copy = IntArray(to - from)
    return original.copyInto(copy, startIndex = from, endIndex = min(to, original.size))
  }

  fun copyOfRange(original: LongArray, from: Int, to: Int): LongArray {
    checkCopyOfRangeArguments(original.size, from, to)
    val copy = LongArray(to - from)
    return original.copyInto(copy, startIndex = from, endIndex = min(to, original.size))
  }

  fun copyOfRange(original: ShortArray, from: Int, to: Int): ShortArray {
    checkCopyOfRangeArguments(original.size, from, to)
    val copy = ShortArray(to - from)
    return original.copyInto(copy, startIndex = from, endIndex = min(to, original.size))
  }

  inline fun <reified T> copyOfRange(original: Array<T?>, from: Int, to: Int): Array<T?> {
    checkCopyOfRangeArguments(original.size, from, to)
    val copy = arrayOfNulls<T>(to - from)
    return original.copyInto(copy, startIndex = from, endIndex = min(to, original.size))
  }

  // The type of array doesn't matter because the emulated java.util.Arrays is never used on the
  // JVM.
  fun <T, O> copyOfRange(
    original: Array<O?>,
    from: Int,
    to: Int,
    newType: java.lang.Class<*>,
  ): Array<T?> {
    checkCopyOfRangeArguments(original.size, from, to)
    val copy = arrayOfNulls<Any?>(to - from)
    @Suppress("UNCHECKED_CAST")
    return original.copyInto(copy, startIndex = from, endIndex = min(to, original.size))
      as Array<T?>
  }

  fun deepEquals(a1: Array<Any?>?, a2: Array<Any?>?): Boolean {
    return a1.contentDeepEquals(a2)
  }

  fun deepHashCode(a: Array<Any?>?): Int {
    return a.contentDeepHashCode()
  }

  fun deepToString(a: Array<Any?>?): String {
    return a.contentDeepToString()
  }

  fun equals(array1: BooleanArray?, array2: BooleanArray?): Boolean {
    return array1 contentEquals array2
  }

  fun equals(array1: ByteArray?, array2: ByteArray?): Boolean {
    return array1 contentEquals array2
  }

  fun equals(array1: CharArray?, array2: CharArray?): Boolean {
    return array1 contentEquals array2
  }

  fun equals(array1: DoubleArray?, array2: DoubleArray?): Boolean {
    return array1 contentEquals array2
  }

  fun equals(array1: FloatArray?, array2: FloatArray?): Boolean {
    return array1 contentEquals array2
  }

  fun equals(array1: IntArray?, array2: IntArray?): Boolean {
    return array1 contentEquals array2
  }

  fun equals(array1: LongArray?, array2: LongArray?): Boolean {
    return array1 contentEquals array2
  }

  fun equals(array1: Array<Any?>?, array2: Array<Any?>?): Boolean {
    return array1 contentEquals array2
  }

  fun equals(array1: ShortArray?, array2: ShortArray?): Boolean {
    return array1 contentEquals array2
  }

  fun fill(a: BooleanArray, `val`: Boolean) {
    a.fill(`val`)
  }

  fun fill(a: BooleanArray, fromIndex: Int, toIndex: Int, `val`: Boolean) {
    a.fill(`val`, fromIndex, toIndex)
  }

  fun fill(a: ByteArray, `val`: Byte) {
    a.fill(`val`)
  }

  fun fill(a: ByteArray, fromIndex: Int, toIndex: Int, `val`: Byte) {
    a.fill(`val`, fromIndex, toIndex)
  }

  fun fill(a: CharArray, `val`: Char) {
    a.fill(`val`)
  }

  fun fill(a: CharArray, fromIndex: Int, toIndex: Int, `val`: Char) {
    a.fill(`val`, fromIndex, toIndex)
  }

  fun fill(a: DoubleArray, `val`: Double) {
    a.fill(`val`)
  }

  fun fill(a: DoubleArray, fromIndex: Int, toIndex: Int, `val`: Double) {
    a.fill(`val`, fromIndex, toIndex)
  }

  fun fill(a: FloatArray, `val`: Float) {
    a.fill(`val`)
  }

  fun fill(a: FloatArray, fromIndex: Int, toIndex: Int, `val`: Float) {
    a.fill(`val`, fromIndex, toIndex)
  }

  fun fill(a: IntArray, `val`: Int) {
    a.fill(`val`)
  }

  fun fill(a: IntArray, fromIndex: Int, toIndex: Int, `val`: Int) {
    a.fill(`val`, fromIndex, toIndex)
  }

  fun fill(a: LongArray, `val`: Long) {
    a.fill(`val`)
  }

  fun fill(a: LongArray, fromIndex: Int, toIndex: Int, `val`: Long) {
    a.fill(`val`, fromIndex, toIndex)
  }

  fun fill(a: Array<Any?>, `val`: Any?) {
    a.fill(`val`)
  }

  fun fill(a: Array<Any?>, fromIndex: Int, toIndex: Int, `val`: Any?) {
    a.fill(`val`, fromIndex, toIndex)
  }

  fun fill(a: ShortArray, `val`: Short) {
    a.fill(`val`)
  }

  fun fill(a: ShortArray, fromIndex: Int, toIndex: Int, `val`: Short) {
    a.fill(`val`, fromIndex, toIndex)
  }

  fun hashCode(a: BooleanArray?): Int {
    return a.contentHashCode()
  }

  fun hashCode(a: ByteArray?): Int {
    return a.contentHashCode()
  }

  fun hashCode(a: CharArray?): Int {
    return a.contentHashCode()
  }

  fun hashCode(a: DoubleArray?): Int {
    return a.contentHashCode()
  }

  fun hashCode(a: FloatArray?): Int {
    return a.contentHashCode()
  }

  fun hashCode(a: IntArray?): Int {
    return a.contentHashCode()
  }

  fun hashCode(a: LongArray?): Int {
    return a.contentHashCode()
  }

  fun hashCode(a: Array<Any?>?): Int {
    return a.contentHashCode()
  }

  fun hashCode(a: ShortArray?): Int {
    return a.contentHashCode()
  }

  fun toString(a: Array<*>?): String {
    return a.contentToString()
  }

  fun toString(a: BooleanArray?): String {
    return a.contentToString()
  }

  fun toString(a: ByteArray?): String {
    return a.contentToString()
  }

  fun toString(a: CharArray?): String {
    return a.contentToString()
  }

  fun toString(a: DoubleArray?): String {
    return a.contentToString()
  }

  fun toString(a: FloatArray?): String {
    return a.contentToString()
  }

  fun toString(a: IntArray?): String {
    return a.contentToString()
  }

  fun toString(a: LongArray?): String {
    return a.contentToString()
  }

  fun toString(a: ShortArray?): String {
    return a.contentToString()
  }

  // TODO(b/238392635): Parallelize parallelPrefix, currently a single-threaded operation
  fun parallelPrefix(array: DoubleArray, op: DoubleBinaryOperator) {
    parallelPrefix0(array, 0, array.size, op)
  }

  fun parallelPrefix(array: DoubleArray, fromIndex: Int, toIndex: Int, op: DoubleBinaryOperator) {
    checkCriticalArrayBounds(fromIndex, toIndex, array.size)
    parallelPrefix0(array, fromIndex, toIndex, op)
  }

  private fun parallelPrefix0(
    array: DoubleArray,
    fromIndex: Int,
    toIndex: Int,
    op: DoubleBinaryOperator,
  ) {
    var acc: Double = array[fromIndex]
    for (i in fromIndex + 1 until toIndex) {
      acc = op.applyAsDouble(acc, array[i])
      array[i] = acc
    }
  }

  fun parallelPrefix(array: IntArray, op: IntBinaryOperator) {
    parallelPrefix0(array, 0, array.size, op)
  }

  fun parallelPrefix(array: IntArray, fromIndex: Int, toIndex: Int, op: IntBinaryOperator) {
    checkCriticalArrayBounds(fromIndex, toIndex, array.size)
    parallelPrefix0(array, fromIndex, toIndex, op)
  }

  private fun parallelPrefix0(
    array: IntArray,
    fromIndex: Int,
    toIndex: Int,
    op: IntBinaryOperator,
  ) {
    var acc: Int = array[fromIndex]
    for (i in fromIndex + 1 until toIndex) {
      acc = op.applyAsInt(acc, array[i])
      array[i] = acc
    }
  }

  fun parallelPrefix(array: LongArray, op: LongBinaryOperator) {
    parallelPrefix0(array, 0, array.size, op)
  }

  fun parallelPrefix(array: LongArray, fromIndex: Int, toIndex: Int, op: LongBinaryOperator) {
    checkCriticalArrayBounds(fromIndex, toIndex, array.size)
    parallelPrefix0(array, fromIndex, toIndex, op)
  }

  private fun parallelPrefix0(
    array: LongArray,
    fromIndex: Int,
    toIndex: Int,
    op: LongBinaryOperator,
  ) {
    var acc: Long = array[fromIndex]
    for (i in fromIndex + 1 until toIndex) {
      acc = op.applyAsLong(acc, array[i])
      array[i] = acc
    }
  }

  fun <T> parallelPrefix(array: Array<T>?, op: BinaryOperator<T>) {
    requireNotNull(array)
    parallelPrefix0(array, 0, array.size, op)
  }

  fun <T> parallelPrefix(array: Array<T>?, fromIndex: Int, toIndex: Int, op: BinaryOperator<T>) {
    requireNotNull(array)
    checkCriticalArrayBounds(fromIndex, toIndex, array.size)
    parallelPrefix0(array, fromIndex, toIndex, op)
  }

  private fun <T> parallelPrefix0(
    array: Array<T>,
    fromIndex: Int,
    toIndex: Int,
    op: BinaryOperator<T>,
  ) {
    requireNotNull(op)
    var acc = array[fromIndex]
    for (i in fromIndex + 1 until toIndex) {
      acc = op.apply(acc, array[i])!!
      array[i] = acc
    }
  }

  fun <T> setAll(array: Array<T>, generator: IntFunction<out T>) {
    for (i in array.indices) {
      array[i] = generator.apply(i)!!
    }
  }

  fun setAll(array: DoubleArray, generator: IntToDoubleFunction) {
    for (i in array.indices) {
      array[i] = generator.applyAsDouble(i)
    }
  }

  fun setAll(array: IntArray, generator: IntUnaryOperator) {
    for (i in array.indices) {
      array[i] = generator.applyAsInt(i)
    }
  }

  fun setAll(array: LongArray, generator: IntToLongFunction) {
    for (i in array.indices) {
      array[i] = generator.applyAsLong(i)
    }
  }

  fun <T> parallelSetAll(array: Array<T>, generator: IntFunction<out T>) {
    setAll(array, generator)
  }

  fun parallelSetAll(array: DoubleArray, generator: IntToDoubleFunction) {
    setAll(array, generator)
  }

  fun parallelSetAll(array: IntArray, generator: IntUnaryOperator) {
    setAll(array, generator)
  }

  fun parallelSetAll(array: LongArray, generator: IntToLongFunction) {
    setAll(array, generator)
  }

  fun sort(array: ByteArray) {
    array.sort()
  }

  // For Java, Arrays.sort(array, fromIndex, toIndex) with fromIndex == toIndex is a no-op
  // For Kotlin, this case throws as fromIndex is required to be smaller than toIndex.

  fun sort(array: ByteArray, fromIndex: Int, toIndex: Int) {
    if (fromIndex != toIndex) {
      array.sort(fromIndex, toIndex)
    }
  }

  fun sort(array: CharArray) {
    array.sort()
  }

  fun sort(array: CharArray, fromIndex: Int, toIndex: Int) {
    if (fromIndex != toIndex) {
      array.sort(fromIndex, toIndex)
    }
  }

  fun sort(array: DoubleArray) {
    array.sort()
  }

  fun sort(array: DoubleArray, fromIndex: Int, toIndex: Int) {
    if (fromIndex != toIndex) {
      array.sort(fromIndex, toIndex)
    }
  }

  fun sort(array: FloatArray) {
    array.sort()
  }

  fun sort(array: FloatArray, fromIndex: Int, toIndex: Int) {
    if (fromIndex != toIndex) {
      array.sort(fromIndex, toIndex)
    }
  }

  fun sort(array: IntArray) {
    array.sort()
  }

  fun sort(array: IntArray, fromIndex: Int, toIndex: Int) {
    if (fromIndex != toIndex) {
      array.sort(fromIndex, toIndex)
    }
  }

  fun sort(array: LongArray) {
    array.sort()
  }

  fun sort(array: LongArray, fromIndex: Int, toIndex: Int) {
    if (fromIndex != toIndex) {
      array.sort(fromIndex, toIndex)
    }
  }

  fun sort(array: ShortArray) {
    array.sort()
  }

  fun sort(array: ShortArray, fromIndex: Int, toIndex: Int) {
    if (fromIndex != toIndex) {
      array.sort(fromIndex, toIndex)
    }
  }

  fun <T> sort(x: Array<T>, c: KotlinComparator<in T>?) {
    val c = Comparators.nullToNaturalOrder(c)
    x.sortWith(c)
  }

  fun <T> sort(x: Array<T>, fromIndex: Int, toIndex: Int, c: KotlinComparator<in T>?) {
    val c = Comparators.nullToNaturalOrder(c)
    if (fromIndex != toIndex) {
      x.sortWith(c, fromIndex, toIndex)
    }
  }

  @Suppress("UNCHECKED_CAST") fun sort(x: Array<Any>) = (x as Array<Comparable<Any>>).sort()

  @Suppress("UNCHECKED_CAST")
  fun sort(x: Array<Any>, fromIndex: Int, toIndex: Int) =
    if (fromIndex != toIndex) (x as Array<Comparable<Any>>).sort(fromIndex, toIndex) else Unit

  fun parallelSort(array: ByteArray) {
    array.sort()
  }

  fun parallelSort(array: ByteArray, fromIndex: Int, toIndex: Int) = sort(array, fromIndex, toIndex)

  fun parallelSort(array: CharArray) {
    array.sort()
  }

  fun parallelSort(array: CharArray, fromIndex: Int, toIndex: Int) = sort(array, fromIndex, toIndex)

  fun parallelSort(array: DoubleArray) {
    array.sort()
  }

  fun parallelSort(array: DoubleArray, fromIndex: Int, toIndex: Int) =
    sort(array, fromIndex, toIndex)

  fun parallelSort(array: FloatArray) {
    array.sort()
  }

  fun parallelSort(array: FloatArray, fromIndex: Int, toIndex: Int) =
    sort(array, fromIndex, toIndex)

  fun parallelSort(array: IntArray) {
    array.sort()
  }

  fun parallelSort(array: IntArray, fromIndex: Int, toIndex: Int) = sort(array, fromIndex, toIndex)

  fun parallelSort(array: LongArray) {
    array.sort()
  }

  fun parallelSort(array: LongArray, fromIndex: Int, toIndex: Int) = sort(array, fromIndex, toIndex)

  fun parallelSort(array: ShortArray) {
    array.sort()
  }

  fun parallelSort(array: ShortArray, fromIndex: Int, toIndex: Int) =
    sort(array, fromIndex, toIndex)

  fun <T : Comparable<T>> parallelSort(array: Array<T>) {
    array.sort()
  }

  fun <T> parallelSort(array: Array<T>, c: KotlinComparator<in T>?) {
    val c = Comparators.nullToNaturalOrder(c)
    array.sortWith(c)
  }

  fun <T : Comparable<T>> parallelSort(array: Array<T>, fromIndex: Int, toIndex: Int) {
    if (fromIndex != toIndex) {
      array.sort(fromIndex, toIndex)
    }
  }

  fun <T> parallelSort(array: Array<T>, fromIndex: Int, toIndex: Int, c: KotlinComparator<in T>?) {
    val c = Comparators.nullToNaturalOrder(c)
    if (fromIndex != toIndex) {
      array.sortWith(c, fromIndex, toIndex)
    }
  }

  fun spliterator(array: DoubleArray): Spliterator.OfDouble =
    Spliterators.spliterator(array, Spliterator.IMMUTABLE or Spliterator.ORDERED)

  fun spliterator(
    array: DoubleArray,
    startInclusive: Int,
    endExclusive: Int,
  ): Spliterator.OfDouble =
    Spliterators.spliterator(
      array,
      startInclusive,
      endExclusive,
      Spliterator.IMMUTABLE or Spliterator.ORDERED,
    )

  fun spliterator(array: IntArray): Spliterator.OfInt =
    Spliterators.spliterator(array, Spliterator.IMMUTABLE or Spliterator.ORDERED)

  fun spliterator(array: IntArray, startInclusive: Int, endExclusive: Int): Spliterator.OfInt =
    Spliterators.spliterator(
      array,
      startInclusive,
      endExclusive,
      Spliterator.IMMUTABLE or Spliterator.ORDERED,
    )

  fun spliterator(array: LongArray): Spliterator.OfLong =
    Spliterators.spliterator(array, Spliterator.IMMUTABLE or Spliterator.ORDERED)

  fun spliterator(array: LongArray, startInclusive: Int, endExclusive: Int): Spliterator.OfLong =
    Spliterators.spliterator(
      array,
      startInclusive,
      endExclusive,
      Spliterator.IMMUTABLE or Spliterator.ORDERED,
    )

  fun <T> spliterator(array: Array<T>): Spliterator<T> {
    @Suppress("UNCHECKED_CAST")
    return Spliterators.spliterator(
      array as Array<Any?>,
      Spliterator.IMMUTABLE or Spliterator.ORDERED,
    )
  }

  fun <T> spliterator(array: Array<T>, startInclusive: Int, endExclusive: Int): Spliterator<T> {
    @Suppress("UNCHECKED_CAST")
    return Spliterators.spliterator(
      array as Array<Any?>,
      startInclusive,
      endExclusive,
      Spliterator.IMMUTABLE or Spliterator.ORDERED,
    )
  }

  fun stream(
    array: DoubleArray,
    startInclusive: Int = 0,
    endExclusive: Int = array.size,
  ): DoubleStream =
    StreamSupport.doubleStream(spliterator(array, startInclusive, endExclusive), false)

  fun stream(array: IntArray, startInclusive: Int = 0, endExclusive: Int = array.size): IntStream =
    StreamSupport.intStream(spliterator(array, startInclusive, endExclusive), false)

  fun stream(
    array: LongArray,
    startInclusive: Int = 0,
    endExclusive: Int = array.size,
  ): LongStream = StreamSupport.longStream(spliterator(array, startInclusive, endExclusive), false)

  fun <T> stream(
    array: Array<T>,
    startInclusive: Int = 0,
    endExclusive: Int = array.size,
  ): Stream<T> = StreamSupport.stream(spliterator(array, startInclusive, endExclusive), false)
}
