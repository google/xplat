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

/**
 * LongArrayBuffer, ReadWriteLongArrayBuffer and ReadOnlyLongArrayBuffer compose the implementation
 * of array based long buffers.
 */
@NullMarked
final class ReadWriteLongArrayBuffer extends LongArrayBuffer {

  static ReadWriteLongArrayBuffer copy(LongArrayBuffer other, int markOfOther) {
    ReadWriteLongArrayBuffer buf =
        new ReadWriteLongArrayBuffer(other.capacity(), other.backingArray, other.offset);
    buf.limit = other.limit();
    buf.position = other.position();
    buf.mark = markOfOther;
    return buf;
  }

  ReadWriteLongArrayBuffer(long[] array) {
    super(array);
  }

  ReadWriteLongArrayBuffer(int capacity) {
    super(capacity);
  }

  ReadWriteLongArrayBuffer(int capacity, long[] backingArray, int arrayOffset) {
    super(capacity, backingArray, arrayOffset);
  }

  @Override
  public LongBuffer asReadOnlyBuffer() {
    return ReadOnlyLongArrayBuffer.copy(this, mark);
  }

  @Override
  public LongBuffer compact() {
    System.arraycopy(backingArray, position + offset, backingArray, offset, remaining());
    position = limit - position;
    limit = capacity;
    mark = UNSET_MARK;
    return this;
  }

  @Override
  public LongBuffer duplicate() {
    return copy(this, mark);
  }

  @Override
  public boolean isReadOnly() {
    return false;
  }

  @Override
  protected long[] protectedArray() {
    return backingArray;
  }

  @Override
  protected int protectedArrayOffset() {
    return offset;
  }

  @Override
  protected boolean protectedHasArray() {
    return true;
  }

  @Override
  public LongBuffer put(long c) {
    if (position == limit) {
      throw new BufferOverflowException();
    }
    backingArray[offset + position++] = c;
    return this;
  }

  @Override
  public LongBuffer put(int index, long c) {
    if (index < 0 || index >= limit) {
      throw new IndexOutOfBoundsException();
    }
    backingArray[offset + index] = c;
    return this;
  }

  @Override
  public LongBuffer put(long[] src, int off, int len) {
    int length = src.length;
    if (off < 0 || len < 0 || (long) off + (long) len > length) {
      throw new IndexOutOfBoundsException();
    }
    if (len > remaining()) {
      throw new BufferOverflowException();
    }
    System.arraycopy(src, off, backingArray, offset + position, len);
    position += len;
    return this;
  }

  @Override
  public LongBuffer slice() {
    return new ReadWriteLongArrayBuffer(remaining(), backingArray, offset + position);
  }
}
