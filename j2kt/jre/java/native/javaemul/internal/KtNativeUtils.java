/*
 * Copyright 2023 Google Inc.
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
package javaemul.internal;

import com.google.j2kt.annotations.HiddenFromObjC;
import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

// Hidden from ObjC, as there is no corresponding Kotlin class: `ktNative()` is only used as the
// body of native methods in `@KtNative` types.
@HiddenFromObjC
@KtNative
@NullMarked
public final class KtNativeUtils {
  public static native <T extends @Nullable Object> T ktNative();
}
