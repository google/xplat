/*
 * Copyright 2022 Google Inc.
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
package java.util

import javaemul.internal.CollectionHelper

abstract class AbstractList<E> : AbstractMutableList<E>(), List<E> {
  override fun add(index: Int, element: E) {
    throw UnsupportedOperationException()
  }

  override fun set(index: Int, element: E): E {
    throw UnsupportedOperationException()
  }

  override fun removeAt(index: Int): E {
    throw UnsupportedOperationException()
  }

  override fun toArray(): Array<Any?> = CollectionHelper.toArray(this)

  override fun <T> toArray(a: Array<T>): Array<T> = CollectionHelper.toArray(this, a)
}
