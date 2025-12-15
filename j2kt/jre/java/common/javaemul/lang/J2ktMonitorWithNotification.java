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
package javaemul.lang;

import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;

/**
 * A slightly more expensive j2kt monitor supporting wait/notify. Maps to a regular class for JVM
 * and an actual native monitor implementation for native.
 */
@KtNative
@NullMarked
public class J2ktMonitorWithNotification extends J2ktMonitor {

  public native void wait();

  public native void wait(long millis, int nanos);

  public native void notify();

  public native void notifyAll();
}
