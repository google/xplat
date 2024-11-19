/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package java.net;

import java.util.Date;
import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;

/** Best-effort parser for HTTP dates. */
@NullMarked
@KtNative(name = "java.net.HttpDate")
final class HttpDate {

  /** Returns the date for {@code value}. Returns null if the value couldn't be parsed. */
  public static native Date parse(String value);
}
