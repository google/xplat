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

import static javaemul.internal.InternalPreconditions.checkArgument;
import static javaemul.internal.InternalPreconditions.checkNotNull;
import static javaemul.internal.InternalPreconditions.checkState;

import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

/**
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/io/BufferedWriter.html">the official
 * Java API doc</a> for details.
 */
@NullMarked
public class BufferedWriter extends Writer {
  private static int defaultCharBufferSize = 8192;

  private @Nullable Writer out;
  private char @Nullable [] buf;
  private int pos;
  private int size;

  public BufferedWriter(Writer out) {
    this(out, defaultCharBufferSize);
  }

  public BufferedWriter(Writer out, int size) {
    super(out);
    checkArgument(size > 0, "Buffer size <= 0");
    this.out = out;
    this.buf = new char[size];
    this.size = size;
    this.pos = 0;
  }

  @Override
  public void close() throws IOException {
    if (out == null) {
      return;
    }
    try (Writer w = out) {
      flushBuffer();
    } finally {
      out = null;
      buf = null;
    }
  }

  @Override
  public void flush() throws IOException {
    flushBuffer();
    out.flush();
  }

  private Writer ensureOpen() throws IOException {
    Writer w = out;
    if (w != null) {
      return w;
    }
    throw new IOException("Stream closed");
  }

  private void flushBuffer() throws IOException {
    Writer w = ensureOpen();
    if (pos > 0) {
      w.write(buf, 0, pos);
    }
    pos = 0;
  }

  public void newLine() throws IOException {
    write("\n");
  }

  @Override
  public void write(char[] buffer, int offset, int count) throws IOException {
    Writer w = ensureOpen();
    IOUtils.checkOffsetAndCount(buffer, offset, count);
    if (count >= size) {
      /* If the request length exceeds the size of the output buffer,
      flush the buffer and then write the data directly.  In this
      way buffered streams will cascade harmlessly. */
      flushBuffer();
      w.write(buffer, offset, count);
      return;
    }

    char[] localBuf = checkNotNull(buf);

    int b = offset, t = offset + count;
    while (b < t) {
      int d = Math.min(size - pos, t - b);
      System.arraycopy(buffer, b, localBuf, pos, d);
      b += d;
      pos += d;
      if (pos >= size) {
        flushBuffer();
      }
    }
  }

  @Override
  public void write(int oneChar) throws IOException {
    Writer w = ensureOpen();
    if (pos >= size) {
      w.write(buf, 0, buf.length);
      pos = 0;
    }
    buf[pos++] = (char) oneChar;
  }

  @Override
  public void write(String str, int offset, int count) throws IOException {
    ensureOpen();
    // Ensure we throw a NullPointerException in case the given string is null.
    checkNotNull(str);
    int b = offset, t = offset + count;
    char[] localBuf = checkNotNull(buf);
    while (b < t) {
      int d = Math.min(size - pos, t - b);
      str.getChars(b, b + d, localBuf, pos);
      b += d;
      pos += d;
      if (pos >= size) {
        flushBuffer();
      }
    }
  }
}
