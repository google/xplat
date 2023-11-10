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

package java.lang;

import java.io.Serializable;
import javaemul.internal.annotations.KtName;
import javaemul.internal.annotations.KtNative;
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

@KtNative(name = "kotlin.text.StringBuilder")
@NullMarked
public final class StringBuilder implements Appendable, CharSequence, Serializable {
  public StringBuilder() {}

  public StringBuilder(int capacity) {}

  public StringBuilder(CharSequence seq) {}

  public StringBuilder(String str) {}

  public native StringBuilder append(boolean b);

  @Override
  public native StringBuilder append(char c);

  public native StringBuilder append(int i);

  public native StringBuilder append(long l);

  public native StringBuilder append(float f);

  public native StringBuilder append(double d);

  public native StringBuilder append(@Nullable Object obj);

  public native StringBuilder append(@Nullable String str);

  // TODO(b/224969395): Add once StringBuffer is available
  // public native StringBuilder append(@Nullable StringBuffer sb);

  public native StringBuilder append(char[] chars);

  @KtName("java_append")
  public native StringBuilder append(char[] str, int offset, int len);

  @Override
  public native StringBuilder append(@Nullable CharSequence csq);

  @Override
  public native StringBuilder append(@Nullable CharSequence csq, int start, int end);

  public native StringBuilder appendCodePoint(int codePoint);

  public native int capacity();

  @Override
  public native char charAt(int index);

  @KtName("deleteRange")
  public native StringBuilder delete(int start, int end);

  @KtName("deleteAt")
  public native StringBuilder deleteCharAt(int index);

  public native void ensureCapacity(int minimumCapacity);

  public native StringBuilder insert(int offset, boolean b);

  public native StringBuilder insert(int offset, char c);

  public native StringBuilder insert(int offset, int i);

  public native StringBuilder insert(int offset, long l);

  public native StringBuilder insert(int offset, float f);

  public native StringBuilder insert(int offset, double d);

  public native StringBuilder insert(int offset, @Nullable Object obj);

  public native StringBuilder insert(int offset, @Nullable String str);

  public native StringBuilder insert(int offset, char[] ch);

  @KtName("java_insert")
  public native StringBuilder insert(int offset, char[] str, int strOffset, int strLen);

  public native StringBuilder insert(int offset, @Nullable CharSequence s);

  public native StringBuilder insert(int offset, @Nullable CharSequence s, int start, int end);

  public native int indexOf(String str);

  public native int indexOf(String str, int fromIndex);

  public native int lastIndexOf(String str);

  public native int lastIndexOf(String str, int fromIndex);

  @Override
  public native int length();

  @KtName("setRange")
  public native StringBuilder replace(int start, int end, String string);

  public native StringBuilder reverse();

  @KtName("set")
  public native void setCharAt(int index, char ch);

  public native void setLength(int newLength);

  @Override
  public native CharSequence subSequence(int start, int end);

  public native String substring(int start);

  public native String substring(int start, int end);

  @Override
  public native String toString();

  public native void trimToSize();
}
