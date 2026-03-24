/*
 * Copyright 2026 Google Inc.
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

package java.lang.ref;

import static javaemul.internal.KtNativeUtils.ktNative;

import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Implemented as an alias to {@link WeakReference} since Kotlin Native does not support
 * SoftReference natively.
 *
 * <p><b>Note:</b> This implementation differs significantly from the standard JRE {@code
 * SoftReference}. In the JRE, {@code SoftReference}s are cleared only when the JVM is under memory
 * pressure, making them suitable for memory-sensitive caches. In J2KT, they are reclaimed as soon
 * as they become weakly reachable, identical to {@code WeakReference}.
 *
 * <p><b>Risks and Performance Implications:</b>
 *
 * <ul>
 *   <li><b>Aggressive Reclamation:</b> Objects will be collected much sooner than in a standard
 *       Java environment.
 *   <li><b>Performance Overhead:</b> Code relying on {@code SoftReference} for caching may
 *       experience a high rate of cache misses, leading to frequent re-computation or re-loading of
 *       data.
 *   <li><b>Memory Predictability:</b> While the hit rate may be lower, this implementation avoids
 *       the GC "thrashing" and long pause times sometimes associated with {@code SoftReference}
 *       processing in the JVM.
 * </ul>
 */
// TODO: b/493215315 - Revisit the decision of replacing java.lang.ref.SoftReference with
// WeakReference.
@KtNative
@NullMarked
public class SoftReference<T> {

  public native @Nullable T get();

  public native void clear();

  public SoftReference(T r) {
    ktNative();
  }
}
