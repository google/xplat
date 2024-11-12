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
  private final String pathname;

  public File(String pathname) {
    this.pathname = pathname;
  }

  public String getPath() {
    return pathname;
  }

  @Override
  public String toString() {
    return getPath();
  }
}
