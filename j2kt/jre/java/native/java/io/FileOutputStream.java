/*
 * Copyright 2025 Google Inc.
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

import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;

/** An output stream that reads bytes from a file. */
@NullMarked
@KtNative
public class FileOutputStream extends OutputStream {
  /**
   * Constructs a new {@code FileOutputStream} that writes to {@code file}. The file will be
   * truncated if it exists, and created if it doesn't exist.
   *
   * @throws FileNotFoundException if file cannot be opened for writing.
   */
  public FileOutputStream(File file) throws FileNotFoundException {}

  /**
   * Constructs a new {@code FileOutputStream} that writes to {@code file}. If {@code append} is
   * true and the file already exists, it will be appended to; otherwise it will be truncated. The
   * file will be created if it does not exist.
   *
   * @throws FileNotFoundException if the file cannot be opened for writing.
   */
  public FileOutputStream(File file, boolean append) throws FileNotFoundException {}

  /**
   * Constructs a new {@code FileOutputStream} that writes to {@code path}. The file will be
   * truncated if it exists, and created if it doesn't exist.
   *
   * @throws FileNotFoundException if file cannot be opened for writing.
   */
  public FileOutputStream(String path) throws FileNotFoundException {}

  /**
   * Constructs a new {@code FileOutputStream} that writes to {@code path}. If {@code append} is
   * true and the file already exists, it will be appended to; otherwise it will be truncated. The
   * file will be created if it does not exist.
   *
   * @throws FileNotFoundException if the file cannot be opened for writing.
   */
  public FileOutputStream(String path, boolean append) throws FileNotFoundException {}

  @Override
  public native void close() throws IOException;

  @Override
  public native void write(int oneByte) throws IOException;
}
