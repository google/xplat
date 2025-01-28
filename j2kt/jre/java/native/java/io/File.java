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

import org.jspecify.annotations.NullMarked;

/** Minimal File emulation currently only suitable for pass-through purposes. */
@NullMarked
public final class File {
  public static final char separatorChar;

  public static final String separator;

  public static final char pathSeparatorChar;

  public static final String pathSeparator;

  private String path;

  static {
    separatorChar = System.getProperty("file.separator", "/").charAt(0);
    pathSeparatorChar = System.getProperty("path.separator", ":").charAt(0);
    separator = String.valueOf(separatorChar);
    pathSeparator = String.valueOf(pathSeparatorChar);
  }

  public File(String path) {
    this.path = fixSlashes(path);
  }

  // Removes duplicate adjacent slashes and any trailing slash.
  private static String fixSlashes(String origPath) {
    // Remove duplicate adjacent slashes.
    boolean lastWasSlash = false;
    char[] newPath = origPath.toCharArray();
    int length = newPath.length;
    int newLength = 0;
    for (int i = 0; i < length; ++i) {
      char ch = newPath[i];
      if (ch == '/') {
        if (!lastWasSlash) {
          newPath[newLength++] = separatorChar;
          lastWasSlash = true;
        }
      } else {
        newPath[newLength++] = ch;
        lastWasSlash = false;
      }
    }
    // Remove any trailing slash (unless this is the root of the file system).
    if (lastWasSlash && newLength > 1) {
      newLength--;
    }
    // Reuse the original string if possible.
    return (newLength != length) ? new String(newPath, 0, newLength) : origPath;
  }

  public String getPath() {
    return path;
  }

  @Override
  public String toString() {
    return getPath();
  }
}
