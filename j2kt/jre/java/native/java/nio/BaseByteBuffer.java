/* Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package java.nio;

import org.jspecify.nullness.NullMarked;

/**
 * Serves as the root of other byte buffer impl classes, implements common methods that can be
 * shared by child classes.
 */
@NullMarked
abstract class BaseByteBuffer extends ByteBuffer {

  protected BaseByteBuffer(int capacity) {
    super(capacity);
  }

  @Override
  public final CharBuffer asCharBuffer() {
    throw new UnsupportedOperationException();
  }

  @Override
  public final DoubleBuffer asDoubleBuffer() {
    throw new UnsupportedOperationException();
  }

  @Override
  public final FloatBuffer asFloatBuffer() {
    throw new UnsupportedOperationException();
  }

  @Override
  public final IntBuffer asIntBuffer() {
    throw new UnsupportedOperationException();
  }

  @Override
  public final LongBuffer asLongBuffer() {
    throw new UnsupportedOperationException();
  }

  @Override
  public final ShortBuffer asShortBuffer() {
    throw new UnsupportedOperationException();
  }

  @Override
  public final char getChar() {
    return (char) getShort();
  }

  @Override
  public final char getChar(int index) {
    return (char) getShort(index);
  }

  @Override
  public final ByteBuffer putChar(char value) {
    return putShort((short) value);
  }

  @Override
  public final ByteBuffer putChar(int index, char value) {
    return putShort(index, (short) value);
  }
}
