/*
 * Copyright 2006 Google Inc.
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
package java.lang;

import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/** Indicates that an objet was in an invalid state during an attempted operation. */
@KtNative(name = "kotlin.IllegalStateException", bridgeName = "java.lang.IllegalStateException")
@NullMarked
public class IllegalStateException extends RuntimeException {

  public IllegalStateException() {
  }

  public IllegalStateException(@Nullable String s) {}

  public IllegalStateException(@Nullable String message, @Nullable Throwable cause) {}

  public IllegalStateException(@Nullable Throwable cause) {}
}
