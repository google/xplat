/*
 * Copyright 2008 Google Inc.
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

package java.util;

import java.util.function.BinaryOperator;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javaemul.internal.annotations.KtNative;
import org.jspecify.nullness.Nullable;

/**
 * Utility methods related to native arrays. See <a
 * href="https://docs.oracle.com/javase/8/docs/api/java/util/Arrays.html">the official Java API
 * doc</a> for details.
 */
@KtNative("java.util.Arrays") // Reimplemented in Kotlin, not transpiled.
public class Arrays {

  public static native <T extends @Nullable Object> List<T> asList(T... array);

  public static native int binarySearch(byte[] sortedArray, int fromIndex, int toIndex, byte key);

  public static native int binarySearch(byte[] sortedArray, byte key);

  public static native int binarySearch(char[] sortedArray, int fromIndex, int toIndex, char key);

  public static native int binarySearch(char[] sortedArray, char key);

  public static native int binarySearch(
      double[] sortedArray, int fromIndex, int toIndex, double key);

  public static native int binarySearch(double[] sortedArray, double key);

  public static native int binarySearch(float[] sortedArray, int fromIndex, int toIndex, float key);

  public static native int binarySearch(float[] sortedArray, float key);

  public static native int binarySearch(int[] sortedArray, int fromIndex, int toIndex, int key);

  public static native int binarySearch(int[] sortedArray, int key);

  public static native int binarySearch(long[] sortedArray, int fromIndex, int toIndex, long key);

  public static native int binarySearch(long[] sortedArray, long key);

  public static native int binarySearch(
      @Nullable Object[] sortedArray, int fromIndex, int toIndex, @Nullable Object key);

  public static native int binarySearch(@Nullable Object[] sortedArray, @Nullable Object key);

  public static native int binarySearch(short[] sortedArray, int fromIndex, int toIndex, short key);

  public static native int binarySearch(short[] sortedArray, short key);

  public static native <T extends @Nullable Object> int binarySearch(
      T[] sortedArray,
      int fromIndex,
      int toIndex,
      T key,
      @Nullable Comparator<? super T> comparator);

  public static native <T extends @Nullable Object> int binarySearch(
      T[] sortedArray, T key, @Nullable Comparator<? super T> c);

  public static native boolean[] copyOf(boolean[] original, int newLength);

  public static native byte[] copyOf(byte[] original, int newLength);

  public static native char[] copyOf(char[] original, int newLength);

  public static native double[] copyOf(double[] original, int newLength);

  public static native float[] copyOf(float[] original, int newLength);

  public static native int[] copyOf(int[] original, int newLength);

  public static native long[] copyOf(long[] original, int newLength);

  public static native short[] copyOf(short[] original, int newLength);

  // Note: In JSpecify, T itself cannot be nullable. But it helps J2KT and makes no difference
  public static native <T extends @Nullable Object> @Nullable T[] copyOf(
      @Nullable T[] original, int newLength);

  public static native boolean[] copyOfRange(boolean[] original, int from, int to);

  public static native byte[] copyOfRange(byte[] original, int from, int to);

  public static native char[] copyOfRange(char[] original, int from, int to);

  public static native double[] copyOfRange(double[] original, int from, int to);

  public static native float[] copyOfRange(float[] original, int from, int to);

  public static native int[] copyOfRange(int[] original, int from, int to);

  public static native long[] copyOfRange(long[] original, int from, int to);

  public static native short[] copyOfRange(short[] original, int from, int to);

  // Note: In JSpecify, T itself cannot be nullable. But it helps J2KT and makes no difference
  public static native <T extends @Nullable Object> @Nullable T[] copyOfRange(
      @Nullable T[] original, int from, int to);

  public static native boolean deepEquals(
      @Nullable Object @Nullable [] a1, @Nullable Object @Nullable [] a2);

  public static native int deepHashCode(@Nullable Object @Nullable [] a);

  public static native String deepToString(@Nullable Object @Nullable [] a);

  public static native boolean equals(boolean @Nullable [] array1, boolean @Nullable [] array2);

  public static native boolean equals(byte @Nullable [] array1, byte @Nullable [] array2);

  public static native boolean equals(char @Nullable [] array1, char @Nullable [] array2);

  public static native boolean equals(double @Nullable [] array1, double @Nullable [] array2);

  public static native boolean equals(float @Nullable [] array1, float @Nullable [] array2);

  public static native boolean equals(int @Nullable [] array1, int @Nullable [] array2);

  public static native boolean equals(long @Nullable [] array1, long @Nullable [] array2);

  public static native boolean equals(
      @Nullable Object @Nullable [] array1, @Nullable Object @Nullable [] array2);

  public static native boolean equals(short @Nullable [] array1, short @Nullable [] array2);

  public static native void fill(boolean[] a, boolean val);

  public static native void fill(boolean[] a, int fromIndex, int toIndex, boolean val);

  public static native void fill(byte[] a, byte val);

  public static native void fill(byte[] a, int fromIndex, int toIndex, byte val);

  public static native void fill(char[] a, char val);

  public static native void fill(char[] a, int fromIndex, int toIndex, char val);

  public static native void fill(double[] a, double val);

  public static native void fill(double[] a, int fromIndex, int toIndex, double val);

  public static native void fill(float[] a, float val);

  public static native void fill(float[] a, int fromIndex, int toIndex, float val);

  public static native void fill(int[] a, int val);

  public static native void fill(int[] a, int fromIndex, int toIndex, int val);

  public static native void fill(long[] a, long val);

  public static native void fill(long[] a, int fromIndex, int toIndex, long val);

  public static native void fill(
      @Nullable Object[] a, int fromIndex, int toIndex, @Nullable Object val);

  public static native void fill(@Nullable Object[] a, @Nullable Object val);

  public static native void fill(short[] a, short val);

  public static native void fill(short[] a, int fromIndex, int toIndex, short val);

  public static native int hashCode(boolean @Nullable [] a);

  public static native int hashCode(byte @Nullable [] a);

  public static native int hashCode(char @Nullable [] a);

  public static native int hashCode(double @Nullable [] a);

  public static native int hashCode(float @Nullable [] a);

  public static native int hashCode(int @Nullable [] a);

  public static native int hashCode(long @Nullable [] a);

  public static native int hashCode(@Nullable Object @Nullable [] a);

  public static native int hashCode(short @Nullable [] a);

  public static native void parallelPrefix(double[] array, DoubleBinaryOperator op);

  public static native void parallelPrefix(
      double[] array, int fromIndex, int toIndex, DoubleBinaryOperator op);

  public static native void parallelPrefix(int[] array, IntBinaryOperator op);

  public static native void parallelPrefix(
      int[] array, int fromIndex, int toIndex, IntBinaryOperator op);

  public static native void parallelPrefix(long[] array, LongBinaryOperator op);

  public static native void parallelPrefix(
      long[] array, int fromIndex, int toIndex, LongBinaryOperator op);

  public static native <T extends @Nullable Object> void parallelPrefix(
      T[] array, BinaryOperator<T> op);

  public static native <T extends @Nullable Object> void parallelPrefix(
      T[] array, int fromIndex, int toIndex, BinaryOperator<T> op);

  public static native <T extends @Nullable Object> void setAll(
      T[] array, IntFunction<? extends T> generator);

  public static native void setAll(double[] array, IntToDoubleFunction generator);

  public static native void setAll(int[] array, IntUnaryOperator generator);

  public static native void setAll(long[] array, IntToLongFunction generator);

  public static native <T extends @Nullable Object> void parallelSetAll(
      T[] array, IntFunction<? extends T> generator);

  public static native void parallelSetAll(double[] array, IntToDoubleFunction generator);

  public static native void parallelSetAll(int[] array, IntUnaryOperator generator);

  public static native void parallelSetAll(long[] array, IntToLongFunction generator);

  public static native void sort(byte[] array);

  public static native void sort(byte[] array, int fromIndex, int toIndex);

  public static native void sort(char[] array);

  public static native void sort(char[] array, int fromIndex, int toIndex);

  public static native void sort(double[] array);

  public static native void sort(double[] array, int fromIndex, int toIndex);

  public static native void sort(float[] array);

  public static native void sort(float[] array, int fromIndex, int toIndex);

  public static native void sort(int[] array);

  public static native void sort(int[] array, int fromIndex, int toIndex);

  public static native void sort(long[] array);

  public static native void sort(long[] array, int fromIndex, int toIndex);

  public static native void sort(Object[] array);

  public static native void sort(Object[] array, int fromIndex, int toIndex);

  public static native void sort(short[] array);

  public static native void sort(short[] array, int fromIndex, int toIndex);

  public static native <T extends @Nullable Object> void sort(
      T[] x, @Nullable Comparator<? super T> c);

  public static native <T extends @Nullable Object> void sort(
      T[] x, int fromIndex, int toIndex, @Nullable Comparator<? super T> c);

  public static native void parallelSort(byte[] array);

  public static native void parallelSort(byte[] array, int fromIndex, int toIndex);

  public static native void parallelSort(char[] array);

  public static native void parallelSort(char[] array, int fromIndex, int toIndex);

  public static native void parallelSort(double[] array);

  public static native void parallelSort(double[] array, int fromIndex, int toIndex);

  public static native void parallelSort(float[] array);

  public static native void parallelSort(float[] array, int fromIndex, int toIndex);

  public static native void parallelSort(int[] array);

  public static native void parallelSort(int[] array, int fromIndex, int toIndex);

  public static native void parallelSort(long[] array);

  public static native void parallelSort(long[] array, int fromIndex, int toIndex);

  public static native void parallelSort(short[] array);

  public static native void parallelSort(short[] array, int fromIndex, int toIndex);

  public static native <T extends Comparable<? super T>> void parallelSort(T[] array);

  public static native <T extends @Nullable Object> void parallelSort(
      T[] array, @Nullable Comparator<? super T> c);

  public static native <T extends Comparable<? super T>> void parallelSort(
      T[] array, int fromIndex, int toIndex);

  public static native <T extends @Nullable Object> void parallelSort(
      T[] array, int fromIndex, int toIndex, @Nullable Comparator<? super T> c);

  public static Spliterator.OfDouble spliterator(double[] array) {
    return Spliterators.spliterator(array, Spliterator.IMMUTABLE | Spliterator.ORDERED);
  }

  public static Spliterator.OfDouble spliterator(
      double[] array, int startInclusive, int endExclusive) {
    return Spliterators.spliterator(
        array, startInclusive, endExclusive, Spliterator.IMMUTABLE | Spliterator.ORDERED);
  }

  public static Spliterator.OfInt spliterator(int[] array) {
    return Spliterators.spliterator(array, Spliterator.IMMUTABLE | Spliterator.ORDERED);
  }

  public static Spliterator.OfInt spliterator(int[] array, int startInclusive, int endExclusive) {
    return Spliterators.spliterator(
        array, startInclusive, endExclusive, Spliterator.IMMUTABLE | Spliterator.ORDERED);
  }

  public static Spliterator.OfLong spliterator(long[] array) {
    return Spliterators.spliterator(array, Spliterator.IMMUTABLE | Spliterator.ORDERED);
  }

  public static Spliterator.OfLong spliterator(long[] array, int startInclusive, int endExclusive) {
    return Spliterators.spliterator(
        array, startInclusive, endExclusive, Spliterator.IMMUTABLE | Spliterator.ORDERED);
  }

  public static <T> Spliterator<T> spliterator(T[] array) {
    return Spliterators.spliterator(array, Spliterator.IMMUTABLE | Spliterator.ORDERED);
  }

  public static <T> Spliterator<T> spliterator(T[] array, int startInclusive, int endExclusive) {
    return Spliterators.spliterator(
        array, startInclusive, endExclusive, Spliterator.IMMUTABLE | Spliterator.ORDERED);
  }

  public static DoubleStream stream(double[] array) {
    return stream(array, 0, array.length);
  }

  public static DoubleStream stream(double[] array, int startInclusive, int endExclusive) {
    return StreamSupport.doubleStream(spliterator(array, startInclusive, endExclusive), false);
  }

  public static IntStream stream(int[] array) {
    return stream(array, 0, array.length);
  }

  public static IntStream stream(int[] array, int startInclusive, int endExclusive) {
    return StreamSupport.intStream(spliterator(array, startInclusive, endExclusive), false);
  }

  public static LongStream stream(long[] array) {
    return stream(array, 0, array.length);
  }

  public static LongStream stream(long[] array, int startInclusive, int endExclusive) {
    return StreamSupport.longStream(spliterator(array, startInclusive, endExclusive), false);
  }

  public static <T> Stream<T> stream(T[] array) {
    return stream(array, 0, array.length);
  }

  public static <T> Stream<T> stream(T[] array, int startInclusive, int endExclusive) {
    return StreamSupport.stream(spliterator(array, startInclusive, endExclusive), false);
  }

  public static native String toString(boolean @Nullable [] a);

  public static native String toString(byte @Nullable [] a);

  public static native String toString(char @Nullable [] a);

  public static native String toString(double @Nullable [] a);

  public static native String toString(float @Nullable [] a);

  public static native String toString(int @Nullable [] a);

  public static native String toString(long @Nullable [] a);

  public static native String toString(@Nullable Object @Nullable [] x);

  public static native String toString(short @Nullable [] a);

  private Arrays() { }
}
