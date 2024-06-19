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

/**
 * LongArrayBuffer, ReadWriteLongArrayBuffer and ReadOnlyLongArrayBuffer compose the implementation
 * of array based long buffers.
 */
@NullMarked
final class ReadOnlyLongArrayBuffer extends LongArrayBuffer {

  static ReadOnlyLongArrayBuffer copy(LongArrayBuffer other, int markOfOther) {
    ReadOnlyLongArrayBuffer buf =
        new ReadOnlyLongArrayBuffer(other.capacity(), other.backingArray, other.offset);
    buf.limit = other.limit();
    buf.position = other.position();
    buf.mark = markOfOther;
    return buf;
  }

  ReadOnlyLongArrayBuffer(int capacity, long[] backingArray, int arrayOffset) {
    super(capacity, backingArray, arrayOffset);
  }

  @Override
  public LongBuffer asReadOnlyBuffer() {
    return duplicate();
  }

  @Override
  public LongBuffer compact() {
    throw new ReadOnlyBufferException();
  }

  @Override
  public LongBuffer duplicate() {
    return copy(this, mark);
  }

  @Override
  public boolean isReadOnly() {
    return true;
  }

  @Override
  protected long[] protectedArray() {
    throw new ReadOnlyBufferException();
  }

  @Override
  protected int protectedArrayOffset() {
    throw new ReadOnlyBufferException();
  }

  @Override
  protected boolean protectedHasArray() {
    return false;
  }

  @Override
  public LongBuffer put(long c) {
    throw new ReadOnlyBufferException();
  }

  @Override
  public LongBuffer put(int index, long c) {
    throw new ReadOnlyBufferException();
  }

  @Override
  public LongBuffer put(LongBuffer buf) {
    throw new ReadOnlyBufferException();
  }

  @Override
  public final LongBuffer put(long[] src, int off, int len) {
    throw new ReadOnlyBufferException();
  }

  @Override
  public LongBuffer slice() {
    return new ReadOnlyLongArrayBuffer(remaining(), backingArray, offset + position);
  }
}
