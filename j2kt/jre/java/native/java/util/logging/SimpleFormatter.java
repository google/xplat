/*
 * Copyright 2010 Google Inc.
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

package java.util.logging;

import org.jspecify.nullness.NullMarked;

/**
 * A simplified emulation of the java.util.logging.SimpleFormatter class without support for the
 * format method. For details, refer to <a
 * href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/logging/SimpleFormatter.html">The Java
 * API doc for details</a>
 */
@NullMarked
public class SimpleFormatter extends Formatter {

  public SimpleFormatter() {}

  @Override
  public String format(LogRecord r) {
    throw new UnsupportedOperationException();
  }
}
