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

import jsinterop.annotations.JsNonNull;

// TODO(b/238061901): Implement the other Collections methods
// Only methods necessary for transpiling non-collection/stream code are implemented right now.
/**
 * Utility methods that operate on collections. See <a
 * href="https://docs.oracle.com/javase/8/docs/api/java/util/Collections.html">the official Java API
 * doc</a> for details.
 */
public class Collections {
  public static <T> Enumeration<T> enumeration(Collection<T> c) {
    final Iterator<T> it = c.iterator();
    return new Enumeration<T>() {
      @Override
      public boolean hasMoreElements() {
        return it.hasNext();
      }

      @Override
      public T nextElement() {
        return it.next();
      }
    };
  }

  public static <T> List<T> nCopies(int n, @JsNonNull T o) {
    ArrayList<T> list = new ArrayList<T>();
    for (int i = 0; i < n; ++i) {
      list.add(o);
    }
    // TODO(b/238061901): return unmodifiableList(list);
    return list;
  }
}
