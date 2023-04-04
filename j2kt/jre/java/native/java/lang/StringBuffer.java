/*
 * Copyright 2023 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package java.lang;

import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

@NullMarked
public final class StringBuffer implements CharSequence, Appendable {

  private final StringBuilder stringBuilder;

  private StringBuffer(StringBuilder stringBuilder) {
    this.stringBuilder = stringBuilder;
  }

  public StringBuffer() {
    this(new StringBuilder());
  }

  public StringBuffer(CharSequence s) {
    this(new StringBuilder(s));
  }

  public StringBuffer(int capacity) {
    this(new StringBuilder(capacity));
  }

  public StringBuffer(String s) {
    this(new StringBuilder(s));
  }

  @Override
  public synchronized char charAt(int index) {
    return stringBuilder.charAt(index);
  }

  @Override
  public synchronized CharSequence subSequence(int start, int end) {
    return stringBuilder.subSequence(start, end);
  }

  @Override
  public synchronized int length() {
    return stringBuilder.length();
  }

  @Override
  public String toString() {
    return stringBuilder.toString();
  }

  public synchronized StringBuffer append(boolean x) {
    stringBuilder.append(x);
    return this;
  }

  @Override
  public synchronized StringBuffer append(char x) {
    stringBuilder.append(x);
    return this;
  }

  public synchronized StringBuffer append(char[] x) {
    stringBuilder.append(x);
    return this;
  }

  public synchronized StringBuffer append(char[] x, int start, int len) {
    stringBuilder.append(x, start, len);
    return this;
  }

  @Override
  public synchronized StringBuffer append(@Nullable CharSequence x) {
    stringBuilder.append(x);
    return this;
  }

  @Override
  public synchronized StringBuffer append(@Nullable CharSequence x, int start, int end) {
    stringBuilder.append(x, start, end);
    return this;
  }

  public synchronized StringBuffer append(double x) {
    stringBuilder.append(x);
    return this;
  }

  public synchronized StringBuffer append(float x) {
    stringBuilder.append(x);
    return this;
  }

  public synchronized StringBuffer append(int x) {
    stringBuilder.append(x);
    return this;
  }

  public synchronized StringBuffer append(long x) {
    stringBuilder.append(x);
    return this;
  }

  public synchronized StringBuffer append(@Nullable Object x) {
    stringBuilder.append(x);
    return this;
  }

  public synchronized StringBuffer append(@Nullable String x) {
    stringBuilder.append(x);
    return this;
  }

  public synchronized StringBuffer append(StringBuffer x) {
    stringBuilder.append(x);
    return this;
  }

  public synchronized StringBuffer appendCodePoint(int x) {
    stringBuilder.appendCodePoint(x);
    return this;
  }

  public synchronized StringBuffer delete(int start, int end) {
    stringBuilder.delete(start, end);
    return this;
  }

  public synchronized StringBuffer deleteCharAt(int start) {
    stringBuilder.deleteCharAt(start);
    return this;
  }

  public synchronized StringBuffer insert(int index, boolean x) {
    stringBuilder.insert(index, x);
    return this;
  }

  public synchronized StringBuffer insert(int index, char x) {
    stringBuilder.insert(index, x);
    return this;
  }

  public synchronized StringBuffer insert(int index, char[] x) {
    stringBuilder.insert(index, x);
    return this;
  }

  public synchronized StringBuffer insert(int index, char[] x, int offset, int len) {
    stringBuilder.insert(index, x, offset, len);
    return this;
  }

  public synchronized StringBuffer insert(int index, @Nullable CharSequence chars) {
    stringBuilder.insert(index, chars);
    return this;
  }

  public synchronized StringBuffer insert(
      int index, @Nullable CharSequence chars, int start, int end) {
    stringBuilder.insert(index, chars, start, end);
    return this;
  }

  public synchronized StringBuffer insert(int index, double x) {
    stringBuilder.insert(index, x);
    return this;
  }

  public synchronized StringBuffer insert(int index, float x) {
    stringBuilder.insert(index, x);
    return this;
  }

  public synchronized StringBuffer insert(int index, int x) {
    stringBuilder.insert(index, x);
    return this;
  }

  public synchronized StringBuffer insert(int index, long x) {
    stringBuilder.insert(index, x);
    return this;
  }

  public synchronized StringBuffer insert(int index, @Nullable Object x) {
    stringBuilder.insert(index, x);
    return this;
  }

  public synchronized StringBuffer insert(int index, @Nullable String x) {
    stringBuilder.insert(index, x);
    return this;
  }

  public synchronized StringBuffer replace(int start, int end, String toInsert) {
    stringBuilder.replace(start, end, toInsert);
    return this;
  }

  public synchronized StringBuffer reverse() {
    stringBuilder.reverse();
    return this;
  }
}
