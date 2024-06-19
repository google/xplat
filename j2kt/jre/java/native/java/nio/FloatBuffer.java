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

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/** A buffer of floats. */
@NullMarked
public abstract class FloatBuffer extends Buffer implements Comparable<FloatBuffer> {

  public static FloatBuffer allocate(int capacity) {
    if (capacity < 0) {
      throw new IllegalArgumentException();
    }
    return BufferFactory.newFloatBuffer(capacity);
  }

  public static FloatBuffer wrap(float[] array) {
    return wrap(array, 0, array.length);
  }

  public static FloatBuffer wrap(float[] array, int start, int len) {
    if (array == null) {
      throw new NullPointerException();
    }
    if (start < 0 || len < 0 || (long) start + (long) len > array.length) {
      throw new IndexOutOfBoundsException();
    }

    FloatBuffer buf = BufferFactory.newFloatBuffer(array);
    buf.position = start;
    buf.limit = start + len;

    return buf;
  }

  FloatBuffer(int capacity) {
    super(capacity);
    elementSizeShift = 2;
  }

  @Override
  public final float[] array() {
    return protectedArray();
  }

  @Override
  public final int arrayOffset() {
    return protectedArrayOffset();
  }

  public abstract FloatBuffer asReadOnlyBuffer();

  public abstract FloatBuffer compact();

  @SuppressWarnings("IdentityBinaryExpression")
  @Override
  public int compareTo(FloatBuffer otherBuffer) {
    int compareRemaining =
        (remaining() < otherBuffer.remaining()) ? remaining() : otherBuffer.remaining();
    int thisPos = position;
    int otherPos = otherBuffer.position;
    float thisFloat, otherFloat;
    while (compareRemaining > 0) {
      thisFloat = get(thisPos);
      otherFloat = otherBuffer.get(otherPos);
      // checks for float and NaN inequality
      if ((thisFloat != otherFloat) && ((thisFloat == thisFloat) || (otherFloat == otherFloat))) {
        return thisFloat < otherFloat ? -1 : 1;
      }
      thisPos++;
      otherPos++;
      compareRemaining--;
    }
    return remaining() - otherBuffer.remaining();
  }

  public abstract FloatBuffer duplicate();

  @Override
  @SuppressWarnings("IdentityBinaryExpression")
  public boolean equals(@Nullable Object other) {
    if (!(other instanceof FloatBuffer)) {
      return false;
    }
    FloatBuffer otherBuffer = (FloatBuffer) other;

    if (remaining() != otherBuffer.remaining()) {
      return false;
    }

    int myPosition = position;
    int otherPosition = otherBuffer.position;
    boolean equalSoFar = true;
    while (equalSoFar && (myPosition < limit)) {
      float a = get(myPosition++);
      float b = otherBuffer.get(otherPosition++);
      equalSoFar = a == b || (a != a && b != b);
    }

    return equalSoFar;
  }

  public abstract float get();

  public FloatBuffer get(float[] dst) {
    return get(dst, 0, dst.length);
  }

  public FloatBuffer get(float[] dst, int off, int len) {
    int length = dst.length;
    if (off < 0 || len < 0 || (long) off + (long) len > length) {
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

  public abstract float get(int index);

  @Override
  public final boolean hasArray() {
    return protectedHasArray();
  }

  @Override
  public int hashCode() {
    int myPosition = position;
    int hash = 0;
    while (myPosition < limit) {
      hash = hash + Float.floatToIntBits(get(myPosition++));
    }
    return hash;
  }

  @Override
  public abstract boolean isDirect();

  public abstract ByteOrder order();

  abstract float[] protectedArray();

  abstract int protectedArrayOffset();

  abstract boolean protectedHasArray();

  public abstract FloatBuffer put(float f);

  public final FloatBuffer put(float[] src) {
    return put(src, 0, src.length);
  }

  public FloatBuffer put(float[] src, int off, int len) {
    int length = src.length;
    if (off < 0 || len < 0 || (long) off + (long) len > length) {
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

  public FloatBuffer put(FloatBuffer src) {
    if (src == this) {
      throw new IllegalArgumentException();
    }
    if (src.remaining() > remaining()) {
      throw new BufferOverflowException();
    }
    float[] contents = new float[src.remaining()];
    src.get(contents);
    put(contents);
    return this;
  }

  public abstract FloatBuffer put(int index, float f);

  public abstract FloatBuffer slice();

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
