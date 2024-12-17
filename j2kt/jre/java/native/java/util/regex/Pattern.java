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
package java.util.regex;

import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;

/** JRE Pattern class stub in Javadoc order. The actual wrapper is in the parallel Kotlin class. */
@KtNative
@NullMarked
public class Pattern {

  public static final int UNIX_LINES = 0x01;

  public static final int CASE_INSENSITIVE = 0x02;

  public static final int COMMENTS = 0x04;

  public static final int MULTILINE = 0x08;

  public static final int LITERAL = 0x10;

  public static final int DOTALL = 0x20;

  public static final int UNICODE_CASE = 0x40;

  public static final int CANON_EQ = 0x80;

  public static final int UNICODE_CHARACTER_CLASS = 0x100;

  public static native Pattern compile(String regex);

  public static native Pattern compile(String regex, int flags);

  public native int flags();

  public native Matcher matcher(CharSequence input);

  public static native boolean matches(String regex, CharSequence input);

  public native String pattern();

  public static native String quote(String s);

  public native String[] split(CharSequence input);

  public native String[] split(CharSequence input, int limit);

  @Override
  public native String toString();

  private Pattern() {}
}
