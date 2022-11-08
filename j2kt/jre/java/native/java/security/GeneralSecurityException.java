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
package java.security;

import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

/**
 * A generic security exception type - <a
 * href="http://java.sun.com/j2se/1.4.2/docs/api/java/security/GeneralSecurityException.html">[Sun's
 * docs]</a>.
 */
@NullMarked
public class GeneralSecurityException extends Exception {

  public GeneralSecurityException() {
  }

  public GeneralSecurityException(@Nullable String msg) {
    super(msg);
  }
}
