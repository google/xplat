/*
 * Copyright 2022 Google Inc.
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
package java.io;

import static javaemul.internal.KtNativeUtils.ktNative;

import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/** KtNative stub of the corresponding JRE-Class */
@NullMarked
@KtNative(name = "java.io.IOException")
public class IOException extends Exception {

  public IOException() {
    ktNative();
  }

  public IOException(@Nullable String message) {
    ktNative();
  }

  public IOException(@Nullable String message, @Nullable Throwable cause) {
    ktNative();
  }

  public IOException(@Nullable Throwable cause) {
    ktNative();
  }
}
