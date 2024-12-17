/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
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
package java.util.zip;

import java.util.function.Consumer;
import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;

@NullMarked
@KtNative
class DeflaterImpl {

  public static native int deflate(
      byte[] buf,
      int offset,
      int byteCount,
      NativeZipStream nativeZipStream,
      int flushParm,
      Runnable streamEndCallback,
      Consumer<Integer> bytesReadConsumer);

  public static native void end(NativeZipStream nativeZipStream);

  public static native int getAdler(NativeZipStream nativeZipStream);

  public static native long getTotalIn(NativeZipStream nativeZipStream);

  public static native long getTotalOut(NativeZipStream nativeZipStream);

  public static native void reset(NativeZipStream nativeZipStream);

  public static native void setDictionary(
      byte[] buf, int offset, int byteCount, NativeZipStream nativeZipStream);

  public static native void setLevels(int level, int strategy, NativeZipStream nativeZipStream);

  public static native void setInput(
      byte[] buf, int offset, int byteCount, NativeZipStream nativeZipStream);

  public static native NativeZipStream createStream(int level, int strategy1, boolean noHeader1);

  private DeflaterImpl() {}
}
