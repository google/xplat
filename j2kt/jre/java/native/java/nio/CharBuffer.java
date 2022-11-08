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

package java.nio;

import java.io.IOException;
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

/** A buffer of chars. */
// TODO(b/242760952): Make CharBuffer implement CharSequence
@NullMarked
public abstract class CharBuffer extends Buffer
    implements Comparable<CharBuffer>, Appendable, Readable {

  public static CharBuffer allocate(int capacity) {
    if (capacity < 0) {
      throw new IllegalArgumentException();
    }
    return BufferFactory.newCharBuffer(capacity);
  }

  public static CharBuffer wrap(char[] array) {
    return wrap(array, 0, array.length);
  }

  public static CharBuffer wrap(char[] array, int start, int len) {
    int length = array.length;
    if ((start < 0) || (len < 0) || (long) start + (long) len > length) {
      throw new IndexOutOfBoundsException();
    }

    CharBuffer buf = BufferFactory.newCharBuffer(array);
    buf.position = start;
    buf.limit = start + len;

    return buf;
  }

  public static CharBuffer wrap(CharSequence chseq) {
    return BufferFactory.newCharBuffer(chseq);
  }

  public static CharBuffer wrap(CharSequence chseq, int start, int end) {
    if (chseq == null) {
      throw new NullPointerException();
    }
    if (start < 0 || end < start || end > chseq.length()) {
      throw new IndexOutOfBoundsException();
    }

    CharBuffer result = BufferFactory.newCharBuffer(chseq);
    result.position = start;
    result.limit = end;
    return result;
  }

  CharBuffer(int capacity) {
    super(capacity);
    elementSizeShift = 1;
  }

  @Override
  public final char[] array() {
    return protectedArray();
  }

  @Override
  public final int arrayOffset() {
    return protectedArrayOffset();
  }

  public abstract CharBuffer asReadOnlyBuffer();

  public final char charAt(int index) {
    if (index < 0 || index >= remaining()) {
      throw new IndexOutOfBoundsException();
    }
    return get(position + index);
  }

  public abstract CharBuffer compact();

  @Override
  public int compareTo(CharBuffer otherBuffer) {
    int compareRemaining =
        (remaining() < otherBuffer.remaining()) ? remaining() : otherBuffer.remaining();
    int thisPos = position;
    int otherPos = otherBuffer.position;
    char thisByte, otherByte;
    while (compareRemaining > 0) {
      thisByte = get(thisPos);
      otherByte = otherBuffer.get(otherPos);
      if (thisByte != otherByte) {
        return thisByte < otherByte ? -1 : 1;
      }
      thisPos++;
      otherPos++;
      compareRemaining--;
    }
    return remaining() - otherBuffer.remaining();
  }

  public abstract CharBuffer duplicate();

  @Override
  public boolean equals(@Nullable Object other) {
    if (!(other instanceof CharBuffer)) {
      return false;
    }
    CharBuffer otherBuffer = (CharBuffer) other;

    if (remaining() != otherBuffer.remaining()) {
      return false;
    }

    int myPosition = position;
    int otherPosition = otherBuffer.position;
    boolean equalSoFar = true;
    while (equalSoFar && (myPosition < limit)) {
      equalSoFar = get(myPosition++) == otherBuffer.get(otherPosition++);
    }

    return equalSoFar;
  }

  public abstract char get();

  public CharBuffer get(char[] dst) {
    return get(dst, 0, dst.length);
  }

  public CharBuffer get(char[] dst, int off, int len) {
    int length = dst.length;
    if ((off < 0) || (len < 0) || (long) off + (long) len > length) {
      throw new IndexOutOfBoundsException();
    }

    if (len > remaining()) {
      throw new BufferUnderflowException();
    }
    for (int i = off; i < off + len; i++) {
      dst[i] = get();
    }
    return this;
  }

  public abstract char get(int index);

  @Override
  public final boolean hasArray() {
    return protectedHasArray();
  }

  @Override
  public int hashCode() {
    int myPosition = position;
    int hash = 0;
    while (myPosition < limit) {
      hash = hash + get(myPosition++);
    }
    return hash;
  }

  @Override
  public abstract boolean isDirect();

  public final int length() {
    return remaining();
  }

  public abstract ByteOrder order();

  abstract char[] protectedArray();

  abstract int protectedArrayOffset();

  abstract boolean protectedHasArray();

  public abstract CharBuffer put(char c);

  public final CharBuffer put(char[] src) {
    return put(src, 0, src.length);
  }

  public CharBuffer put(char[] src, int off, int len) {
    int length = src.length;
    if ((off < 0) || (len < 0) || (long) off + (long) len > length) {
      throw new IndexOutOfBoundsException();
    }

    if (len > remaining()) {
      throw new BufferOverflowException();
    }
    for (int i = off; i < off + len; i++) {
      put(src[i]);
    }
    return this;
  }

  public CharBuffer put(CharBuffer src) {
    if (src == this) {
      throw new IllegalArgumentException();
    }
    if (src.remaining() > remaining()) {
      throw new BufferOverflowException();
    }

    char[] contents = new char[src.remaining()];
    src.get(contents);
    put(contents);
    return this;
  }

  public abstract CharBuffer put(int index, char c);

  public final CharBuffer put(String str) {
    return put(str, 0, str.length());
  }

  public CharBuffer put(String str, int start, int end) {
    int length = str.length();
    if (start < 0 || end < start || end > length) {
      throw new IndexOutOfBoundsException();
    }

    if (end - start > remaining()) {
      throw new BufferOverflowException();
    }
    for (int i = start; i < end; i++) {
      put(str.charAt(i));
    }
    return this;
  }

  public abstract CharBuffer slice();

  // TODO(b/242760952): Add after Charbuffer implements CharSequence
  // public abstract CharSequence subSequence(int start, int end);

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder(limit - position);
    for (int i = position; i < limit; i++) {
      result.append(get(i));
    }
    return result.toString();
  }

  @Override
  public CharBuffer append(char c) {
    return put(c);
  }

  @Override
  public CharBuffer append(CharSequence csq) {
    if (csq != null) {
      return put(csq.toString());
    }
    return put("null");
  }

  @Override
  public CharBuffer append(CharSequence csq, int start, int end) {
    if (csq == null) {
      csq = "null";
    }
    CharSequence cs = csq.subSequence(start, end);
    if (cs.length() > 0) {
      return put(cs.toString());
    }
    return this;
  }

  @Override
  public int read(CharBuffer target) throws IOException {
    int remaining = remaining();
    if (target == this) {
      if (remaining == 0) {
        return -1;
      }
      throw new IllegalArgumentException();
    }
    if (remaining == 0) {
      return limit > 0 && target.remaining() == 0 ? 0 : -1;
    }
    remaining = Math.min(target.remaining(), remaining);
    if (remaining > 0) {
      char[] chars = new char[remaining];
      get(chars);
      target.put(chars);
    }
    return remaining;
  }
}
