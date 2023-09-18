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

import java.util.Arrays;
import org.jspecify.nullness.NullMarked;

/**
 * A simplified emulation of the java.util.logging.Formatter class: Log record parameters are just
 * appended to the format string instead of properly formatting the message. See <a
 * href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/logging/Formatter.html">The Java API doc
 * for details</a>
 */
@NullMarked
public abstract class Formatter {
  public abstract String format(LogRecord record);

  public String formatMessage(LogRecord record) {
    String pattern = record.getMessage();
    Object[] params = record.getParameters();
    if (pattern != null && (params == null || params.length == 0)) {
      return pattern;
    }
    return "" + pattern + Arrays.toString(params);
  }

  /* Not Implemented */
  // public String getHead(Handler h) {}
  // public String getTail(Handler h) {}

}
