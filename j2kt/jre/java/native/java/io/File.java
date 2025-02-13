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

import javaemul.internal.KtNativeUtils;
import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/** Minimal File emulation currently only suitable for pass-through purposes. */
@NullMarked
@KtNative
public final class File {
  public static final char separatorChar = KtNativeUtils.ktNative();

  public static final String separator = KtNativeUtils.ktNative();

  public static final char pathSeparatorChar = KtNativeUtils.ktNative();

  public static final String pathSeparator = KtNativeUtils.ktNative();

  public File(String pathname) {}

  public File(@Nullable File parent, String child) {}

  public File(@Nullable String parent, String child) {}

  /** Note */
  public native boolean createNewFile() throws IOException;

  public native boolean delete();

  public native boolean exists();

  public native String getAbsolutePath();

  public native String getPath();

  public native boolean isDirectory();

  public native File @Nullable [] listFiles();

  public native boolean mkdir();

  public native boolean mkdirs();

  public native boolean renameTo(File dest);
}
