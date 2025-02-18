/*
 * Copyright 2024 Google Inc.
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

/** An input stream that reads bytes from a file. */
@NullMarked
@KtNative
public class FileInputStream extends InputStream {

  /**
   * Constructs a new {@code FileInputStream} that reads from {@code file}.
   *
   * @param file the file from which this stream reads.
   * @throws FileNotFoundException if {@code file} does not exist.
   */
  public FileInputStream(File file) throws FileNotFoundException {}

  /** Equivalent to {@code new FileInputStream(new File(path))}. */
  public FileInputStream(String path) throws FileNotFoundException {}

  @Override
  public native int available() throws IOException;

  @Override
  public native void close() throws IOException;

  @Override
  public native int read() throws IOException;

  @Override
  public native int read(byte[] buffer, int byteOffset, int byteCount) throws IOException;

  @Override
  public native long skip(long byteCount) throws IOException;
}
