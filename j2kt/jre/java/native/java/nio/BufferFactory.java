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

/** Provide factory service of buffer classes. */
final class BufferFactory {

  public static ByteBuffer newByteBuffer(byte[] array) {
    return new ReadWriteHeapByteBuffer(array);
  }

  public static ByteBuffer newByteBuffer(int capacity) {
    return new ReadWriteHeapByteBuffer(capacity);
  }

  public static CharBuffer newCharBuffer(char[] array) {
    return new ReadWriteCharArrayBuffer(array);
  }

  public static CharBuffer newCharBuffer(CharSequence chseq) {
    return new CharSequenceAdapter(chseq);
  }

  public static CharBuffer newCharBuffer(int capacity) {
    return new ReadWriteCharArrayBuffer(capacity);
  }

  public static ByteBuffer newDirectByteBuffer(int capacity) {
    throw new UnsupportedOperationException();
  }

  public static DoubleBuffer newDoubleBuffer(double[] array) {
    return new ReadWriteDoubleArrayBuffer(array);
  }

  public static DoubleBuffer newDoubleBuffer(int capacity) {
    return new ReadWriteDoubleArrayBuffer(capacity);
  }

  public static FloatBuffer newFloatBuffer(float[] array) {
    return new ReadWriteFloatArrayBuffer(array);
  }

  public static FloatBuffer newFloatBuffer(int capacity) {
    return new ReadWriteFloatArrayBuffer(capacity);
  }

  public static IntBuffer newIntBuffer(int capacity) {
    return new ReadWriteIntArrayBuffer(capacity);
  }

  public static IntBuffer newIntBuffer(int[] array) {
    return new ReadWriteIntArrayBuffer(array);
  }

  public static LongBuffer newLongBuffer(int capacity) {
    return new ReadWriteLongArrayBuffer(capacity);
  }

  public static LongBuffer newLongBuffer(long[] array) {
    return new ReadWriteLongArrayBuffer(array);
  }

  public static ShortBuffer newShortBuffer(int capacity) {
    return new ReadWriteShortArrayBuffer(capacity);
  }

  public static ShortBuffer newShortBuffer(short[] array) {
    return new ReadWriteShortArrayBuffer(array);
  }
}
