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
@file:OptIn(ExperimentalObjCName::class)

package javaemul.lang

import java.util.Spliterator
import java.util.Spliterators
import java.util.function.Consumer
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@ObjCName("JavaemulLangJavaIterable", exact = true)
fun interface JavaIterable<T> : MutableIterable<T> {
  fun forEach(consumer: Consumer<in T>) {
    default_forEach(consumer)
  }

  fun spliterator(): Spliterator<T> = default_spliterator()
}

// Note: On Kotlin JVM, this is shadowed by member function forEach
fun <T> MutableIterable<T>.forEach(consumer: Consumer<in T>) {
  if (this is JavaIterable) {
    forEach(consumer)
  } else {
    default_forEach(consumer)
  }
}

private inline fun <T> MutableIterable<T>.default_forEach(consumer: Consumer<in T>) {
  forEach(consumer::accept)
}

// Note: On Kotlin JVM, this is shadowed by member function spliterator
fun <T> MutableIterable<T>.spliterator(): Spliterator<T> =
  if (this is JavaIterable) spliterator() else default_spliterator()

private fun <T> MutableIterable<T>.default_spliterator(): Spliterator<T> =
  Spliterators.spliteratorUnknownSize(iterator(), 0)
