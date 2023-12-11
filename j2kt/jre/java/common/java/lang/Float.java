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

@KtNative(name = "kotlin.Float")
@NullMarked
public final class Float extends Number implements Comparable<Float> {

  public static final float MAX_VALUE = 0x1.fffffeP+127f;

  public static final float MIN_VALUE = 0x0.000002P-126f;

  public static final float NaN = 0.0f / 0.0f;

  public static final float POSITIVE_INFINITY = 1.0f / 0.0f;

  public static final float NEGATIVE_INFINITY = -1.0f / 0.0f;

  public static final float MIN_NORMAL = 0x1.0p-126f;

  public static final int MAX_EXPONENT = 127;

  public static final int MIN_EXPONENT = -126;

  public static final Class<Float> TYPE = ktNative();

  @KtName("SIZE_BITS")
  public static final int SIZE = 32;

  @KtName("SIZE_BYTES")
  public static final int BYTES = SIZE / Byte.SIZE;

  public Float(float value) {}

  public Float(double value) {}

  public Float(String string) throws NumberFormatException {}

  @Override
  public native int compareTo(Float object);

  @KtName("toInt_toByte")
  @Override
  public native byte byteValue();

  @Override
  public native double doubleValue();

  @Override
  public native boolean equals(@Nullable Object object);

  public static native int floatToIntBits(float value);

  public static native int floatToRawIntBits(float value);

  @Override
  public native float floatValue();

  @Override
  public native int hashCode();

  @KtName("fromBits")
  public static native float intBitsToFloat(int bits);

  @Override
  public native int intValue();

  public native boolean isInfinite();

  public static native boolean isInfinite(float f);

  public static native boolean isFinite(float f);

  public native boolean isNaN();

  public static native boolean isNaN(float f);

  @Override
  public native long longValue();

  public static native float max(float a, float b);

  public static native float min(float a, float b);

  public static native float sum(float a, float b);

  public static native float parseFloat(String string) throws NumberFormatException;

  @KtName("toInt_toShort")
  @Override
  public native short shortValue();

  @Override
  public native String toString();

  public static native String toString(float f);

  public static native Float valueOf(String string) throws NumberFormatException;

  public static native int compare(float float1, float float2);

  public static native Float valueOf(float f);

  public static native String toHexString(float f);

  public static native int hashCode(float f);
}
