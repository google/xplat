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

import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

/** A buffer for bytes. */
@NullMarked
public abstract class ByteBuffer extends Buffer implements Comparable<ByteBuffer> {

  public static ByteBuffer allocate(int capacity) {
    if (capacity < 0) {
      throw new IllegalArgumentException();
    }
    return BufferFactory.newByteBuffer(capacity);
  }

  // TODO(b/243040477): Add support after DirectBuffers are implemented
  public static ByteBuffer allocateDirect(int capacity) {
    throw new UnsupportedOperationException();
  }

  public static ByteBuffer wrap(byte[] array) {
    return BufferFactory.newByteBuffer(array);
  }

  public static ByteBuffer wrap(byte[] array, int start, int len) {
    int length = array.length;
    if ((start < 0) || (len < 0) || ((long) start + (long) len > length)) {
      throw new IndexOutOfBoundsException();
    }

    ByteBuffer buf = BufferFactory.newByteBuffer(array);
    buf.position = start;
    buf.limit = start + len;

    return buf;
  }

  ByteOrder order = ByteOrder.BIG_ENDIAN;

  ByteBuffer(int capacity) {
    super(capacity);
    elementSizeShift = 0;
  }

  @Override
  public final byte[] array() {
    return protectedArray();
  }

  @Override
  public final int arrayOffset() {
    return protectedArrayOffset();
  }

  public abstract CharBuffer asCharBuffer();

  public abstract DoubleBuffer asDoubleBuffer();

  public abstract FloatBuffer asFloatBuffer();

  public abstract IntBuffer asIntBuffer();

  public abstract LongBuffer asLongBuffer();

  public abstract ByteBuffer asReadOnlyBuffer();

  public abstract ShortBuffer asShortBuffer();

  public abstract ByteBuffer compact();

  @Override
  public int compareTo(ByteBuffer otherBuffer) {
    int compareRemaining =
        (remaining() < otherBuffer.remaining()) ? remaining() : otherBuffer.remaining();
    int thisPos = position;
    int otherPos = otherBuffer.position;
    byte thisByte, otherByte;
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

  public abstract ByteBuffer duplicate();

  @Override
  public boolean equals(@Nullable Object other) {
    if (!(other instanceof ByteBuffer)) {
      return false;
    }
    ByteBuffer otherBuffer = (ByteBuffer) other;

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

  public abstract byte get();

  public ByteBuffer get(byte[] dst) {
    return get(dst, 0, dst.length);
  }

  public ByteBuffer get(byte[] dst, int off, int len) {
    int length = dst.length;
    if ((off < 0) || (len < 0) || ((long) off + (long) len > length)) {
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

  public abstract byte get(int index);

  public abstract char getChar();

  public abstract char getChar(int index);

  public abstract double getDouble();

  public abstract double getDouble(int index);

  public abstract float getFloat();

  public abstract float getFloat(int index);

  public abstract int getInt();

  public abstract int getInt(int index);

  public abstract long getLong();

  public abstract long getLong(int index);

  public abstract short getShort();

  public abstract short getShort(int index);

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

  public final ByteOrder order() {
    return order;
  }

  public final ByteBuffer order(ByteOrder byteOrder) {
    return orderImpl(byteOrder);
  }

  ByteBuffer orderImpl(ByteOrder byteOrder) {
    if (byteOrder == null) {
      byteOrder = ByteOrder.LITTLE_ENDIAN;
    }
    order = byteOrder;
    return this;
  }

  abstract byte[] protectedArray();

  abstract int protectedArrayOffset();

  abstract boolean protectedHasArray();

  public abstract ByteBuffer put(byte b);

  public final ByteBuffer put(byte[] src) {
    return put(src, 0, src.length);
  }

  public ByteBuffer put(byte[] src, int off, int len) {
    int length = src.length;
    if ((off < 0) || (len < 0) || ((long) off + (long) len > length)) {
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

  public ByteBuffer put(ByteBuffer src) {
    if (src == this) {
      throw new IllegalArgumentException();
    }
    if (src.remaining() > remaining()) {
      throw new BufferOverflowException();
    }
    byte[] contents = new byte[src.remaining()];
    src.get(contents);
    put(contents);
    return this;
  }

  public abstract ByteBuffer put(int index, byte b);

  public abstract ByteBuffer putChar(char value);

  public abstract ByteBuffer putChar(int index, char value);

  public abstract ByteBuffer putDouble(double value);

  public abstract ByteBuffer putDouble(int index, double value);

  public abstract ByteBuffer putFloat(float value);

  public abstract ByteBuffer putFloat(int index, float value);

  public abstract ByteBuffer putInt(int value);

  public abstract ByteBuffer putInt(int index, int value);

  public abstract ByteBuffer putLong(long value);

  public abstract ByteBuffer putLong(int index, long value);

  public abstract ByteBuffer putShort(short value);

  public abstract ByteBuffer putShort(int index, short value);

  public abstract ByteBuffer slice();

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
