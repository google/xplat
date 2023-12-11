/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package java.lang;

import static javaemul.internal.KtNativeUtils.ktNative;

import javaemul.internal.annotations.KtName;
import javaemul.internal.annotations.KtNative;
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

@KtNative(name = "kotlin.Int")
@NullMarked
public final class Integer extends Number implements Comparable<Integer> {

  public static final int MAX_VALUE = 0x7fffffff;

  public static final int MIN_VALUE = 0x80000000;

  @KtName("SIZE_BITS")
  public static final int SIZE = 32;

  @KtName("SIZE_BYTES")
  public static final int BYTES = SIZE / Byte.SIZE;

  public static final Class<Integer> TYPE = ktNative();

  public Integer(int value) {}

  public Integer(String string) throws NumberFormatException {}

  @Override
  public native byte byteValue();

  @Override
  public native int compareTo(Integer object);

  public static native int compare(int lhs, int rhs);

  public static native Integer decode(String string) throws NumberFormatException;

  @Override
  public native double doubleValue();

  @Override
  public native boolean equals(@Nullable Object o);

  @Override
  public native float floatValue();

  public static native int max(int a, int b);

  public static native int min(int a, int b);

  public static native int sum(int a, int b);

  public static native @Nullable Integer getInteger(@Nullable String string);

  public static native Integer getInteger(@Nullable String string, int defaultValue);

  public static native @Nullable Integer getInteger(
      @Nullable String string, @Nullable Integer defaultValue);

  @Override
  public native int hashCode();

  @Override
  public native int intValue();

  @Override
  public native long longValue();

  public static native int parseInt(String string) throws NumberFormatException;

  public static native int parseInt(String string, int radix) throws NumberFormatException;

  @Override
  public native short shortValue();

  public static native String toBinaryString(int i);

  public static native String toHexString(int i);

  public static native String toOctalString(int i);

  @Override
  public native String toString();

  public static native String toString(int i);

  public static native String toString(int i, int radix);

  public static native Integer valueOf(String string) throws NumberFormatException;

  public static native Integer valueOf(String string, int radix) throws NumberFormatException;

  public static native int highestOneBit(int i);

  public static native int lowestOneBit(int i);

  public static native int numberOfLeadingZeros(int i);

  public static native int numberOfTrailingZeros(int i);

  public static native int bitCount(int i);

  public static native int rotateLeft(int i, int distance);

  public static native int rotateRight(int i, int distance);

  public static native int reverseBytes(int i);

  public static native int reverse(int i);

  public static native int signum(int i);

  public static native Integer valueOf(int i);

  public static native int hashCode(int i);
}
