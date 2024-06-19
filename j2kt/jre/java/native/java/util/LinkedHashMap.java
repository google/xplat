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

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

// Note: This implementation relies on the fact that Kotlin Native's HashMap is linked.
/**
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/LinkedHashMap.html">the official
 * Java API doc</a> for details.
 */
@NullMarked
public class LinkedHashMap<K extends @Nullable Object, V extends @Nullable Object>
    extends HashMap<K, V> implements Map<K, V>, Cloneable {

  public LinkedHashMap() {
    super();
  }

  public LinkedHashMap(int initialCapacity) {
    super(initialCapacity);
  }

  public LinkedHashMap(int initialCapacity, float loadFactor) {
    super(initialCapacity, loadFactor);
  }

  public LinkedHashMap(Map<? extends K, ? extends V> original) {
    super(original);
  }

  @Override
  public Object clone() {
    return new LinkedHashMap<K, V>(this);
  }
}
