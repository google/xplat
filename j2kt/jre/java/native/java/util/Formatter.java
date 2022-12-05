/* Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// This file is adapted from libcore/luni/src/main/java/java/util/Formatter.java in Android Open
// Source Project

package java.util;

import java.io.IOException;
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

@NullMarked
public final class Formatter {
  // TODO(b/259213718): Port the implementation.

  private final Appendable out;

  private @Nullable IOException lastIOException = null;

  public Formatter() {
    out = new StringBuilder();
  }

  public IOException ioException() {
    return lastIOException;
  }

  public Formatter format(String format, @Nullable Object... args) {
    if (format.contains("%")) {
      throw new UnsupportedOperationException("Format specifier not implemented");
    }
    try {
      out.append(format);
    } catch (IOException e) {
      lastIOException = e;
    }
    return this;
  }

  @Override
  public String toString() {
    return out.toString();
  }
}
