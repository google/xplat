/*
 * Copyright 2019 Google Inc.
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
package java.lang.invoke;

import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;

/** Exists solely to make javac happy. */
@NullMarked
@KtNative
public class LambdaMetafactory {
  public static native CallSite metafactory(
      MethodHandles.Lookup caller,
      String interfaceMethodName,
      MethodType factoryType,
      MethodType interfaceMethodType,
      MethodHandle implementation,
      MethodType dynamicMethodType);

  public static native CallSite altMetafactory(
      MethodHandles.Lookup caller, String interfaceMethodName, MethodType factoryType, Object... a);
}
