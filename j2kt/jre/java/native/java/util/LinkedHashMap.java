/*
 * Copyright 2008 Google Inc.
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

import static javaemul.internal.KtNativeUtils.ktNative;

import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/LinkedHashMap.html">the official
 * Java API doc</a> for details.
 */
@NullMarked
@KtNative(name = "java.util.LinkedHashMap")
public class LinkedHashMap<K extends @Nullable Object, V extends @Nullable Object>
    extends HashMap<K, V> implements Map<K, V>, Cloneable {

  public LinkedHashMap() {
    ktNative();
  }

  public LinkedHashMap(int initialCapacity) {
    ktNative();
  }

  public LinkedHashMap(int initialCapacity, float loadFactor) {
    ktNative();
  }

  public LinkedHashMap(Map<? extends K, ? extends V> original) {
    ktNative();
  }

  // Note: This method is only available on J2kt-JVM and J2kt-Web, not J2kt-Native
  public LinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder) {
    ktNative();
  }

  // Note: This method is only available on J2kt-JVM and J2kt-Web, not J2kt-Native
  protected native boolean removeEldestEntry(Map.Entry<K, V> eldest);

  @Override
  public native Object clone();
}
