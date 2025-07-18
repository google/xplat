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
package java.io;

import org.jspecify.annotations.NullMarked;

/**
 * A character encoding is not supported - <a
 * href="http://java.sun.com/javase/6/docs/api/java/io/UnsupportedEncodingException.html">[Sun's
 * docs]</a>.
 */
@NullMarked
public class UnsupportedEncodingException extends IOException {

  public UnsupportedEncodingException() {}

  public UnsupportedEncodingException(String msg) {
    super(msg);
  }
}
