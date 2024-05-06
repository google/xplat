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

import javaemul.internal.annotations.KtNative;
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

@KtNative(name = "kotlin.Long", companionName = "java.lang.Long")
@NullMarked
public final class Long extends Number implements Comparable<Long> {

  public static final long MAX_VALUE = 0x7fffffffffffffffL;

  public static final long MIN_VALUE = 0x8000000000000000L;

  public static final Class<Long> TYPE = ktNative();

  public static final int SIZE = 64;

  public static final int BYTES = SIZE / Byte.SIZE;

  public Long(long value) {}

  public Long(String string) throws NumberFormatException {}

  @Override
  public native byte byteValue();

  @Override
  public native int compareTo(Long object);

  public static native int compare(long lhs, long rhs);

  public static native Long decode(String string) throws NumberFormatException;

  @Override
  public native double doubleValue();

  @Override
  public native boolean equals(@Nullable Object o);

  @Override
  public native float floatValue();

  public static native long max(long a, long b);

  public static native long min(long a, long b);

  public static native long sum(long a, long b);

  public static native @Nullable Long getLong(@Nullable String string);

  public static native Long getLong(@Nullable String string, long defaultValue);

  public static native @Nullable Long getLong(@Nullable String string, @Nullable Long defaultValue);

  @Override
  public native int hashCode();

  @Override
  public native int intValue();

  @Override
  public native long longValue();

  public static native long parseLong(String string) throws NumberFormatException;

  public static native long parseLong(String string, int radix) throws NumberFormatException;

  @Override
  public native short shortValue();

  public static native String toBinaryString(long v);

  public static native String toHexString(long v);

  public static native String toOctalString(long v);

  @Override
  public native String toString();

  public static native String toString(long n);

  public static native String toString(long v, int radix);

  public static native String toUnsignedString(long n);

  public static native String toUnsignedString(long v, int radix);

  public static native Long valueOf(String string) throws NumberFormatException;

  public static native Long valueOf(String string, int radix) throws NumberFormatException;

  public static native long highestOneBit(long v);

  public static native long lowestOneBit(long v);

  public static native int numberOfLeadingZeros(long v);

  public static native int numberOfTrailingZeros(long v);

  public static native int bitCount(long v);

  public static native long rotateLeft(long v, int distance);

  public static native long rotateRight(long v, int distance);

  public static native long reverseBytes(long v);

  public static native long reverse(long v);

  public static native int signum(long v);

  public static native Long valueOf(long v);

  public static native int hashCode(long l);
}
