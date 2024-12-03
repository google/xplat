/*
 * Copyright 2024 Google Inc.
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
package javaemul.lang

/** A KMP subtype of MutableIterable with all the additional methods of JVM's MutableIterable */
typealias MutableIterableJvm<E> = MutableIterable<E>

// Make it possible to instantiate with a lambda, like a fun interface.
inline fun <T> MutableIterableJvm(
  crossinline iteratorFn: () -> MutableIterator<T>
): MutableIterableJvm<T> =
  object : MutableIterableJvm<T> {
    override fun iterator(): MutableIterator<T> = iteratorFn()
  }
