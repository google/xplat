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
 * FloatArrayBuffer, ReadWriteFloatArrayBuffer and ReadOnlyFloatArrayBuffer compose the
 * implementation of array based float buffers.
 */
@NullMarked
final class ReadOnlyFloatArrayBuffer extends FloatArrayBuffer {

  static ReadOnlyFloatArrayBuffer copy(FloatArrayBuffer other, int markOfOther) {
    ReadOnlyFloatArrayBuffer buf =
        new ReadOnlyFloatArrayBuffer(other.capacity(), other.backingArray, other.offset);
    buf.limit = other.limit();
    buf.position = other.position();
    buf.mark = markOfOther;
    return buf;
  }

  ReadOnlyFloatArrayBuffer(int capacity, float[] backingArray, int arrayOffset) {
    super(capacity, backingArray, arrayOffset);
  }

  @Override
  public FloatBuffer asReadOnlyBuffer() {
    return duplicate();
  }

  @Override
  public FloatBuffer compact() {
    throw new ReadOnlyBufferException();
  }

  @Override
  public FloatBuffer duplicate() {
    return copy(this, mark);
  }

  @Override
  public boolean isReadOnly() {
    return true;
  }

  @Override
  protected float[] protectedArray() {
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
  public FloatBuffer put(float c) {
    throw new ReadOnlyBufferException();
  }

  @Override
  public FloatBuffer put(int index, float c) {
    throw new ReadOnlyBufferException();
  }

  @Override
  public FloatBuffer put(FloatBuffer buf) {
    throw new ReadOnlyBufferException();
  }

  @Override
  public final FloatBuffer put(float[] src, int off, int len) {
    throw new ReadOnlyBufferException();
  }

  @Override
  public FloatBuffer slice() {
    return new ReadOnlyFloatArrayBuffer(remaining(), backingArray, offset + position);
  }
}
