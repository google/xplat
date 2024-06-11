/*
 * Copyright 2020 Google Inc.
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

import java.util.Objects;
import javaemul.internal.J2ktMonitor;
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

/**
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/io/Writer.html">the official Java API
 * doc</a> for details.
 */
@NullMarked
public abstract class Writer implements Appendable, Closeable, Flushable {
  protected final J2ktMonitor lock;

  protected Writer() {
    this(new J2ktMonitor());
  }

  /**
   * Restricted to package visibility as it requires our own lock implementation -- opposed to the
   * corresponding JRE constructor, taking any object as lock.
   */
  Writer(J2ktMonitor lock) {
    this.lock = lock;
  }

  @Override
  public abstract void close() throws IOException;

  @Override
  public abstract void flush() throws IOException;

  public void write(char[] buf) throws IOException {
    write(buf, 0, buf.length);
  }

  public abstract void write(char[] buf, int offset, int count) throws IOException;

  public void write(int oneChar) throws IOException {
    synchronized (lock) {
      char[] oneCharArray = new char[1];
      oneCharArray[0] = (char) oneChar;
      write(oneCharArray);
    }
  }

  public void write(String str) throws IOException {
    write(str, 0, str.length());
  }

  public void write(String str, int offset, int count) throws IOException {
    char[] buf = new char[count];
    try {
    str.getChars(offset, offset + count, buf, 0);
    } catch (IndexOutOfBoundsException e) {
      throw new StringIndexOutOfBoundsException(e.getMessage());
    }
    synchronized (lock) {
      write(buf, 0, buf.length);
    }
  }

  @Override
  public Writer append(char c) throws IOException {
    write(c);
    return this;
  }

  @Override
  public Writer append(@Nullable CharSequence csq) throws IOException {
    write(Objects.toString(csq));
    return this;
  }

  @Override
  public Writer append(@Nullable CharSequence csq, int start, int end) throws IOException {
    if (csq == null) {
      csq = "null";
    }
    try {
      write(csq.subSequence(start, end).toString());
    } catch (IndexOutOfBoundsException e) {
      throw new StringIndexOutOfBoundsException(e.getMessage());
    }
    return this;
  }
}
