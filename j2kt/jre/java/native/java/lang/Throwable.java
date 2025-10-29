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

package java.lang;

import com.google.j2kt.annotations.HiddenFromObjC;
import java.io.PrintStream;
import java.io.PrintWriter;
import javaemul.internal.annotations.KtNative;
import javaemul.internal.annotations.KtProperty;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@KtNative(
    name = "kotlin.Throwable",
    bridgeName = "javaemul.lang.ThrowableJvm",
    companionName = "java.lang.Throwable")
@NullMarked
public class Throwable {

  public Throwable() {}

  public Throwable(@Nullable String detailMessage) {}

  public Throwable(@Nullable String detailMessage, @Nullable Throwable cause) {}

  public Throwable(@Nullable Throwable cause) {}

  protected Throwable(
      @Nullable String detailMessage,
      @Nullable Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace) {}

  public native Throwable fillInStackTrace();

  @KtProperty
  public native @Nullable String getMessage();

  @KtProperty
  public native @Nullable String getLocalizedMessage();

  // Treat as a property to avoid conflict with `fun getStacktrace: Array<String>` on Kotlin/Native.
  @KtProperty
  public native StackTraceElement[] getStackTrace();

  public native void setStackTrace(StackTraceElement[] trace);

  /**
   * Writes a printable representation of this {@code Throwable}'s stack trace to the {@code
   * System.err} stream.
   */
  public native void printStackTrace();

  public native void printStackTrace(PrintStream err);

  public native void printStackTrace(PrintWriter err);

  @Override
  public native String toString();

  public native Throwable initCause(@Nullable Throwable throwable);

  @KtProperty
  public native @Nullable Throwable getCause();

  public final native void addSuppressed(Throwable throwable);

  @KtProperty
  public final native Throwable[] getSuppressed();

  // Method existing in J2CL-JRE and referenced by user code. This method is not available in
  // j2kt-native and j2kt-jvm.
  @HiddenFromObjC
  public static native Throwable of(@Nullable Object e);
}
