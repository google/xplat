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
package javaemul.internal;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class CollectionHelper {
  public static @Nullable Object[] toArray(Collection<? extends @Nullable Object> collection) {
    return toArray(collection, new @Nullable Object[collection.size()]);
  }

  @SuppressWarnings("unchecked") // Cast to T[] is safe due to the contract of newInstance().
  public static <E extends @Nullable Object, T extends @Nullable Object> T[] toArray(
      Collection<E> collection, T[] a) {
    int size = collection.size();
    if (a.length < size) {
      a = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
    }
    @Nullable Object[] result = a;
    Iterator<E> it = collection.iterator();
    int i = 0;
    for (; i < a.length && it.hasNext(); ++i) {
      result[i] = it.next();
    }
    if (i < a.length) {
      a[i] = null;
    }
    // TODO: b/381065164 - else if (it.hasNext()) { reallocate a and keep copying }
    return a;
  }

  private CollectionHelper() {}
}
