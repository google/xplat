/*
 * Copyright 2026 Google Inc.
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
package java.nio.charset.spi;

import java.nio.charset.Charset;
import java.util.Iterator;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/** The service provider class for character sets. */
@NullMarked
public abstract class CharsetProvider {
  /** Constructor for subclassing with concrete types. */
  protected CharsetProvider() {}

  /**
   * Returns an iterator over all the available charsets.
   *
   * @return the iterator.
   */
  public abstract Iterator<Charset> charsets();

  /**
   * Returns the named charset.
   *
   * <p>If the charset is unavailable the method returns <code>null</code>.
   *
   * @param charsetName the canonical or alias name of a character set.
   * @return the charset, or <code>null</code> if unavailable.
   */
  public abstract @Nullable Charset charsetForName(String charsetName);
}
