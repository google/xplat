/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package java.lang;

import java.lang.reflect.Type;
import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

// On Kotlin JVM, the native class is part of the JDK. On Kotlin Native, J2KT provides an
// implementation of java.lang.Class.
/** Minimal stub for {@code Class}. */
@KtNative
@NullMarked
public final class Class<T> implements Type {

  private Class() {}

  public native @Nullable T cast(@Nullable Object obj);

  public native String getName();

  public native @Nullable String getCanonicalName();

  public native String getSimpleName();

  public native @Nullable Class<?> getComponentType();

  public native boolean isArray();

  public native boolean isEnum();

  public native boolean isInterface();

  public native boolean isInstance(@Nullable Object obj);

  public native boolean isPrimitive();

  public native T @Nullable [] getEnumConstants();

  public native boolean desiredAssertionStatus();
}
