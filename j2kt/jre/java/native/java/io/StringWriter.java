/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package java.io;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/io/StringWriter.html">the official Java
 * API doc</a> for details.
 */
@NullMarked
public class StringWriter extends Writer {

  // Note: This is a StringBuffer in the original implementation.
  private final StringBuilder buf;

  public StringWriter() {
    buf = new StringBuilder(16);
  }

  public StringWriter(int initialSize) {
    if (initialSize < 0) {
      throw new IllegalArgumentException("initialSize < 0: " + initialSize);
    }
    buf = new StringBuilder(initialSize);
  }

  @Override
  public void close() throws IOException {
    /* empty */
  }

  @Override
  public void flush() {
    /* empty */
  }

  @Override
  public String toString() {
    return buf.toString();
  }

  @Override
  public void write(char[] chars, int offset, int count) {
    checkOffsetAndCount(chars.length, offset, count);
    if (count == 0) {
      return;
    }
    buf.append(chars, offset, count);
  }

  @Override
  public void write(int oneChar) {
    buf.append((char) oneChar);
  }

  @Override
  public void write(String str) {
    buf.append(str);
  }

  @Override
  public void write(String str, int offset, int count) {
    String sub = str.substring(offset, offset + count);
    buf.append(sub);
  }

  @Override
  public StringWriter append(char c) {
    write(c);
    return this;
  }

  @Override
  public StringWriter append(@Nullable CharSequence csq) {
    if (csq == null) {
      csq = "null";
    }
    write(csq.toString());
    return this;
  }

  @Override
  public StringWriter append(@Nullable CharSequence csq, int start, int end) {
    if (csq == null) {
      csq = "null";
    }
    String output = csq.subSequence(start, end).toString();
    write(output, 0, output.length());
    return this;
  }

  private static void checkOffsetAndCount(int arrayLength, int offset, int count) {
    if ((offset | count) < 0 || offset > arrayLength || arrayLength - offset < count) {
      throw new ArrayIndexOutOfBoundsException();
    }
  }
}
