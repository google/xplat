/*
 * Copyright 2013 Google Inc.
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

import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;

/**
 * See <a href="http://docs.oracle.com/javase/7/docs/api/java/lang/AutoCloseable.html">the official
 * Java API doc</a> for details.
 */
@NullMarked
// TODO(b/272238387): Map to kotlin.AutoCloseable when that's available in Kotlin JVM and J2cl
@KtNative
public interface AutoCloseable {

  /**
   * Closes this resource.
   */
  void close() throws Exception;
}
