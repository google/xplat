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
 * CharArrayBuffer, ReadWriteCharArrayBuffer and ReadOnlyCharArrayBuffer compose the implementation
 * of array based char buffers.
 */
@NullMarked
abstract class CharArrayBuffer extends CharBuffer {

  protected final char[] backingArray;

  protected final int offset;

  CharArrayBuffer(char[] array) {
    this(array.length, array, 0);
  }

  CharArrayBuffer(int capacity) {
    this(capacity, new char[capacity], 0);
  }

  CharArrayBuffer(int capacity, char[] backingArray, int offset) {
    super(capacity);
    this.backingArray = backingArray;
    this.offset = offset;
  }

  @Override
  public final char get() {
    if (position == limit) {
      throw new BufferUnderflowException();
    }
    return backingArray[offset + position++];
  }

  @Override
  public final char get(int index) {
    if (index < 0 || index >= limit) {
      throw new IndexOutOfBoundsException();
    }
    return backingArray[offset + index];
  }

  @Override
  public final CharBuffer get(char[] dst, int off, int len) {
    int length = dst.length;
    if ((off < 0) || (len < 0) || (long) off + (long) len > length) {
      throw new IndexOutOfBoundsException();
    }
    if (len > remaining()) {
      throw new BufferUnderflowException();
    }
    System.arraycopy(backingArray, offset + position, dst, off, len);
    position += len;
    return this;
  }

  @Override
  public final boolean isDirect() {
    return false;
  }

  @Override
  public final ByteOrder order() {
    return ByteOrder.nativeOrder();
  }

  // TODO(b/242760952): Change return type after Charbuffer is made to implement Charsequence
  public final CharBuffer subSequence(int start, int end) {
    if (start < 0 || end < start || end > remaining()) {
      throw new IndexOutOfBoundsException();
    }

    CharBuffer result = duplicate();
    result.limit(position + end);
    result.position(position + start);
    return result;
  }

  @Override
  public final String toString() {
    return String.copyValueOf(backingArray, offset + position, remaining());
  }
}
