/*
 * Copyright 2024 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package java.lang.ref;

import static javaemul.internal.KtNativeUtils.ktNative;

import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/lang/ref/WeakReference.html">the
 * official Java API doc</a> for details.
 *
 * <p>Contains only the subset functionality offered by the native WeakReference implementation.
 */
@KtNative(name = "java.lang.ref.WeakReference")
@NullMarked
public class WeakReference<T> {

  public native @Nullable T get();

  public native void clear();

  public WeakReference(T r) {
    ktNative();
  }
}