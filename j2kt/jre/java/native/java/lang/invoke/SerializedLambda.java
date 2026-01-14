/*
 * Copyright 2016 Google Inc.
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

import java.io.Serializable;
import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;

/** Exists solely to make javac happy. */
@NullMarked
@KtNative
public final class SerializedLambda implements Serializable {

  public native Object getCapturedArg(int i);

  public native String getFunctionalInterfaceClass();

  public native String getFunctionalInterfaceMethodName();

  public native String getFunctionalInterfaceMethodSignature();

  public native String getImplClass();

  public native int getImplMethodKind();

  public native String getImplMethodName();

  public native String getImplMethodSignature();

  public native String getInstantiatedMethodType();
}
