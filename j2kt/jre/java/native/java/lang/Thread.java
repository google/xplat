/*
 * Copyright 2025 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package java.lang;

import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;

/** Thread subset supporting a threadId for simple logging and thread identity checks. */
@NullMarked
@KtNative
public final class Thread {

  public static native Thread currentThread();

  // J2KT threads do not support interruption, so this method always returns false.
  public static native boolean interrupted();

  private Thread() {}

  @SuppressWarnings("NamedLikeContextualKeyword")
  public static native void yield();

  public native long getId();

  public native String getName();

  /** Note that this is a no-op for j2kt-native. */
  public native void interrupt();

  public interface UncaughtExceptionHandler {
    void uncaughtException(Thread t, Throwable e);
  }
}
