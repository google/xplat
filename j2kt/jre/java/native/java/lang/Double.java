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

import com.google.j2kt.annotations.HiddenFromObjC;
import javaemul.internal.annotations.KtName;
import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@KtNative(name = "kotlin.Double", companionName = "java.lang.Double")
@NullMarked
public final class Double extends Number implements Comparable<Double> {

  public static final double MAX_VALUE = 0x1.fffffffffffffP+1023;

  public static final double MIN_VALUE = 0x0.0000000000001P-1022;

  public static final double NaN = 0.0d / 0.0;

  public static final double POSITIVE_INFINITY = 1.0 / 0.0;

  public static final double NEGATIVE_INFINITY = -1.0 / 0.0;

  public static final double MIN_NORMAL = 0x1.0p-1022;

  public static final int MAX_EXPONENT = 1023;

  public static final int MIN_EXPONENT = -1022;

  public static final Class<Double> TYPE = ktNative();

  public static final int SIZE = 64;

  public static final int BYTES = SIZE / Byte.SIZE;

  public Double(double value) {}

  public Double(String string) throws NumberFormatException {}

  @Override
  public native int compareTo(Double object);

  @KtName("toInt_toByte")
  @Override
  public native byte byteValue();

  public static native long doubleToLongBits(double value);

  public static native long doubleToRawLongBits(double value);

  @Override
  public native double doubleValue();

  @Override
  public native boolean equals(@Nullable Object object);

  @Override
  public native float floatValue();

  @Override
  public native int hashCode();

  @Override
  public native int intValue();

  public native boolean isInfinite();

  public static native boolean isInfinite(double d);

  public static native boolean isFinite(double d);

  public native boolean isNaN();

  public static native boolean isNaN(double d);

  public static native double longBitsToDouble(long bits);

  @Override
  public native long longValue();

  public static native double max(double a, double b);

  public static native double min(double a, double b);

  public static native double sum(double a, double b);

  public static native double parseDouble(String string) throws NumberFormatException;

  @KtName("toInt_toShort")
  @Override
  public native short shortValue();

  @Override
  public native String toString();

  public static native String toString(double d);

  public static native Double valueOf(String string) throws NumberFormatException;

  public static native int compare(double double1, double double2);

  public static native Double valueOf(double d);

  @HiddenFromObjC
  public static native String toHexString(double d);

  public static native int hashCode(double d);
}
