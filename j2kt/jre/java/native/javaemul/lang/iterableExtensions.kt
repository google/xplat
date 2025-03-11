/*
 * Copyright 2023 Google Inc.
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
package javaemul.lang

import java.lang.Iterable as JavaLangIterable
import java.util.Spliterator
import java.util.Spliterators
import java.util.function.Consumer

fun <T> Iterable<T>.forEach(consumer: Consumer<in T>) {
  if (this is JavaLangIterable<*>) {
    (this as JavaLangIterable<T>).forEach(consumer)
  } else {
    forEach(consumer::accept)
  }
}

fun <T> MutableIterable<T>.spliterator(): Spliterator<T> =
  if (this is JavaLangIterable<*>) (this as JavaLangIterable<T>).spliterator()
  else Spliterators.spliteratorUnknownSize<T>(iterator(), 0)
