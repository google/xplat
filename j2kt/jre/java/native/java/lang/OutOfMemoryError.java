/*
 * Copyright 2024 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
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

/**
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/OutOfMemoryError.html">the
 * official Java API doc</a> for details.
 */
@NullMarked
@KtNative
public class OutOfMemoryError extends Error /* VirtualMachineError */ {

  public OutOfMemoryError() {}

  public OutOfMemoryError(@Nullable String detailMessage) {
    super(detailMessage);
  }
}
