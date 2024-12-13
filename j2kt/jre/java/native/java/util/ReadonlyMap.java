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
package java.util;

import javaemul.internal.annotations.KtNative;
import javaemul.internal.annotations.KtOut;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/** Representation of the readonly Kotlin map interface. Only for use by the JRE emulation. */
@KtNative(name = "kotlin.collections.Map")
@NullMarked
public interface ReadonlyMap<K extends @Nullable Object, @KtOut V extends @Nullable Object> {
  // Methods omitted (type is only used in native method parameter/return types).
}
