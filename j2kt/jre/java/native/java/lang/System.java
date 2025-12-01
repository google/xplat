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
package java.lang;

import java.io.PrintStream;
import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/** General-purpose low-level utility methods. */
@KtNative
@NullMarked
public final class System {
  public static native void arraycopy(Object src, int srcOfs, Object dest, int destOfs, int len);

  public static native long currentTimeMillis();

  public static native long nanoTime();

  public static native int identityHashCode(@Nullable Object o);

  public static PrintStream err;

  public static PrintStream out;

  public static native void setErr(PrintStream err);

  public static native void setOut(PrintStream out);

  /**
   * Has no effect; just here for source compatibility.
   *
   * @skip
   */
  public static native void gc();

  /** Added for source compatibility */
  public static native @Nullable String getProperty(String key);

  public static native @Nullable String getProperty(String key, @Nullable String def);

  public static native void setProperty(String key, String def);

  public static native String lineSeparator();

  private System() {}
}
