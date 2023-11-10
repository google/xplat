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

// TODO(b/223774683): Java Boolean should implement Serializable. Kotlin Boolean doesn't.
@KtNative(name = "kotlin.Boolean")
@NullMarked
public final class Boolean implements Comparable<Boolean> {

  public static final Class<Boolean> TYPE = ktNative();

  public static final Boolean TRUE = true;

  public static final Boolean FALSE = false;

  public Boolean(@Nullable String string) {}

  public Boolean(boolean value) {}

  public native boolean booleanValue();

  @Override
  public native boolean equals(@Nullable Object o);

  @Override
  public native int compareTo(Boolean that);

  public static native int compare(boolean lhs, boolean rhs);

  @Override
  public native int hashCode();

  @Override
  public native String toString();

  // J2KT: Removed for now
  // public native static boolean getBoolean(@Nullable String string);

  public static native boolean parseBoolean(@Nullable String s);

  public static native String toString(boolean value);

  public static native Boolean valueOf(@Nullable String string);

  public static native Boolean valueOf(boolean b);

  public static native int hashCode(boolean value);
}
