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

import java.lang.Iterable as JavaLangIterable
import java.util.function.Predicate
import java.util.stream.Stream
import java.util.stream.StreamSupport

interface Collection<E> : MutableCollection<E>, JavaLangIterable<E> {
  fun removeIf(filter: Predicate<in E>): Boolean = removeAll(filter::test)

  fun stream(): Stream<E> = StreamSupport.stream(spliterator(), parallel = false)

  fun parallelStream(): Stream<E> = StreamSupport.stream(spliterator(), parallel = false)

  fun toArray(): Array<Any?>

  fun <T> toArray(a: Array<T>): Array<T>
}
