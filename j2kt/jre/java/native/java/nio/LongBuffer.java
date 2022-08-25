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

/**
 * A buffer of longs.
 */
public abstract class LongBuffer extends Buffer implements Comparable<LongBuffer> {

  public static LongBuffer allocate(int capacity) {
    if (capacity < 0) {
      throw new IllegalArgumentException();
    }
    return BufferFactory.newLongBuffer(capacity);
  }

  public static LongBuffer wrap(long[] array) {
    return wrap(array, 0, array.length);
  }

  public static LongBuffer wrap(long[] array, int start, int len) {
    if (array == null) {
      throw new NullPointerException();
    }
    if (start < 0 || len < 0 || (long) len + (long) start > array.length) {
      throw new IndexOutOfBoundsException();
    }

    LongBuffer buf = BufferFactory.newLongBuffer(array);
    buf.position = start;
    buf.limit = start + len;

    return buf;
  }

  LongBuffer(int capacity) {
    super(capacity);
    elementSizeShift = 3;
  }

  public final long[] array() {
    return protectedArray();
  }

  public final int arrayOffset() {
    return protectedArrayOffset();
  }

  public abstract LongBuffer asReadOnlyBuffer();

  public abstract LongBuffer compact();

  public int compareTo(LongBuffer otherBuffer) {
    int compareRemaining =
        (remaining() < otherBuffer.remaining()) ? remaining() : otherBuffer.remaining();
    int thisPos = position;
    int otherPos = otherBuffer.position;
    // BEGIN android-changed
    long thisLong, otherLong;
    while (compareRemaining > 0) {
      thisLong = get(thisPos);
      otherLong = otherBuffer.get(otherPos);
      if (thisLong != otherLong) {
        return thisLong < otherLong ? -1 : 1;
      }
      thisPos++;
      otherPos++;
      compareRemaining--;
    }
    // END android-changed
    return remaining() - otherBuffer.remaining();
  }

  public abstract LongBuffer duplicate();

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof LongBuffer)) {
      return false;
    }
    LongBuffer otherBuffer = (LongBuffer) other;

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

  public abstract long get();

  public LongBuffer get(long[] dst) {
    return get(dst, 0, dst.length);
  }

  public LongBuffer get(long[] dst, int off, int len) {
    int length = dst.length;
    if (off < 0 || len < 0 || (long) len + (long) off > length) {
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

  public abstract long get(int index);

  public final boolean hasArray() {
    return protectedHasArray();
  }

  @Override
  public int hashCode() {
    int myPosition = position;
    int hash = 0;
    long l;
    while (myPosition < limit) {
      l = get(myPosition++);
      hash = hash + ((int) l) ^ ((int) (l >> 32));
    }
    return hash;
  }

  public abstract boolean isDirect();

  public abstract ByteOrder order();

  abstract long[] protectedArray();

  abstract int protectedArrayOffset();

  abstract boolean protectedHasArray();

  public abstract LongBuffer put(long l);

  public final LongBuffer put(long[] src) {
    return put(src, 0, src.length);
  }

  public LongBuffer put(long[] src, int off, int len) {
    int length = src.length;
    if (off < 0 || len < 0 || (long) len + (long) off > length) {
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

  public LongBuffer put(LongBuffer src) {
    if (src == this) {
      throw new IllegalArgumentException();
    }
    if (src.remaining() > remaining()) {
      throw new BufferOverflowException();
    }
    long[] contents = new long[src.remaining()];
    src.get(contents);
    put(contents);
    return this;
  }

  public abstract LongBuffer put(int index, long l);

  public abstract LongBuffer slice();

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append(getClass().getName());
    buf.append(", status: capacity=");
    buf.append(capacity());
    buf.append(" position=");
    buf.append(position());
    buf.append(" limit=");
    buf.append(limit());
    return buf.toString();
  }
}
