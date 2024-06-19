/*
 * Copyright 2007 Google Inc.
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
package java.util;

import java.io.Serializable;
import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/** Represents a date and time. */
@NullMarked
@KtNative(name = "java.util.Date")
public class Date implements Cloneable, Comparable<Date>, Serializable {

  public static native long parse(String s);

  // CHECKSTYLE_OFF: Matching the spec.
  public static native long UTC(int year, int month, int date, int hrs, int min, int sec);

  // CHECKSTYLE_ON

  /**
   * Ensure a number is displayed with two digits.
   *
   * @return a two-character base 10 representation of the number
   */
  protected static native String padTwo(int number);

  public Date() {}

  public Date(int year, int month, int date) {}

  public Date(int year, int month, int date, int hrs, int min) {}

  public Date(int year, int month, int date, int hrs, int min, int sec) {}

  public Date(long date) {}

  public Date(String date) {}

  public native boolean after(Date when);

  public native boolean before(Date when);

  @Override
  public native Object clone();

  @Override
  public native int compareTo(Date other);

  @Override
  public native boolean equals(@Nullable Object obj);

  public native int getDate();

  public native int getDay();

  public native int getHours();

  public native int getMinutes();

  public native int getMonth();

  public native int getSeconds();

  public native long getTime();

  public native int getTimezoneOffset();

  public native int getYear();

  @Override
  public native int hashCode();

  public native void setDate(int date);

  public native void setHours(int hours);

  public native void setMinutes(int minutes);

  public native void setMonth(int month);

  public native void setSeconds(int seconds);

  public native void setTime(long time);

  public native void setYear(int year);

  public native String toGMTString();

  public native String toLocaleString();

  @Override
  public native String toString();
}
