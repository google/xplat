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
package java.util.concurrent.locks;

import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;

/** Partial emulation of LockSupport */
@KtNative
@NullMarked
public final class LockSupport {

  private LockSupport() {}

  public static native void park();

  public static native void park(Object lock);

  public static native void parkNanos(Object lock, long nanos);

  public static native void unpark(Thread thread);
}
