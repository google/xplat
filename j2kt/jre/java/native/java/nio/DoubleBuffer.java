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

import jsinterop.annotations.JsNonNull;

/**
 * A buffer of doubles.
 */
public abstract class DoubleBuffer extends Buffer implements Comparable<DoubleBuffer> {

  public static DoubleBuffer allocate(int capacity) {
    if (capacity < 0) {
      throw new IllegalArgumentException();
    }
    return BufferFactory.newDoubleBuffer(capacity);
  }

  public static DoubleBuffer wrap(double[] array) {
    return wrap(array, 0, array.length);
  }

  public static DoubleBuffer wrap(double[] array, int start, int len) {
    int length = array.length;
    if (start < 0 || len < 0 || (long) start + (long) len > length) {
      throw new IndexOutOfBoundsException();
    }

    DoubleBuffer buf = BufferFactory.newDoubleBuffer(array);
    buf.position = start;
    buf.limit = start + len;

    return buf;
  }

  DoubleBuffer(int capacity) {
    super(capacity);
    elementSizeShift = 3;
  }

  @Override
  public final double[] array() {
    return protectedArray();
  }

  @Override
  public final int arrayOffset() {
    return protectedArrayOffset();
  }

  public abstract DoubleBuffer asReadOnlyBuffer();

  public abstract DoubleBuffer compact();

  @SuppressWarnings("IdentityBinaryExpression")
  @Override
  public int compareTo(@JsNonNull DoubleBuffer otherBuffer) {
    int compareRemaining =
        (remaining() < otherBuffer.remaining()) ? remaining() : otherBuffer.remaining();
    int thisPos = position;
    int otherPos = otherBuffer.position;
    double thisDouble, otherDouble;
    while (compareRemaining > 0) {
      thisDouble = get(thisPos);
      otherDouble = otherBuffer.get(otherPos);
      // checks for double and NaN inequality
      if ((thisDouble != otherDouble)
          && ((thisDouble == thisDouble) || (otherDouble == otherDouble))) {
        return thisDouble < otherDouble ? -1 : 1;
      }
      thisPos++;
      otherPos++;
      compareRemaining--;
    }
    return remaining() - otherBuffer.remaining();
  }

  public abstract DoubleBuffer duplicate();

  @Override
  @SuppressWarnings("IdentityBinaryExpression")
  public boolean equals(Object other) {
    if (!(other instanceof DoubleBuffer)) {
      return false;
    }
    DoubleBuffer otherBuffer = (DoubleBuffer) other;

    if (remaining() != otherBuffer.remaining()) {
      return false;
    }

    int myPosition = position;
    int otherPosition = otherBuffer.position;
    boolean equalSoFar = true;
    while (equalSoFar && (myPosition < limit)) {
      double a = get(myPosition++);
      double b = otherBuffer.get(otherPosition++);
      equalSoFar = a == b || (a != a && b != b);
    }

    return equalSoFar;
  }

  public abstract double get();

  public DoubleBuffer get(double[] dst) {
    return get(dst, 0, dst.length);
  }

  public DoubleBuffer get(double[] dst, int off, int len) {
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

  public abstract double get(int index);

  @Override
  public final boolean hasArray() {
    return protectedHasArray();
  }

  @Override
  public int hashCode() {
    int myPosition = position;
    int hash = 0;
    long l;
    while (myPosition < limit) {
      l = Double.doubleToLongBits(get(myPosition++));
      hash = hash + ((int) l) ^ ((int) (l >> 32));
    }
    return hash;
  }

  @Override
  public abstract boolean isDirect();

  public abstract ByteOrder order();

  abstract double[] protectedArray();

  abstract int protectedArrayOffset();

  abstract boolean protectedHasArray();

  public abstract DoubleBuffer put(double d);

  public final DoubleBuffer put(double[] src) {
    return put(src, 0, src.length);
  }

  public DoubleBuffer put(double[] src, int off, int len) {
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

  public DoubleBuffer put(DoubleBuffer src) {
    if (src == this) {
      throw new IllegalArgumentException();
    }
    if (src.remaining() > remaining()) {
      throw new BufferOverflowException();
    }
    double[] doubles = new double[src.remaining()];
    src.get(doubles);
    put(doubles);
    return this;
  }

  public abstract DoubleBuffer put(int index, double d);

  public abstract DoubleBuffer slice();

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
