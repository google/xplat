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

import java.util.function.Consumer
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@ObjCName("JavaemulLangJavaIterator", exact = true)
interface JavaIterator<T> : MutableIterator<T> {
  override fun remove() {
    throw UnsupportedOperationException()
  }

  fun java_forEachRemaining(consumer: Consumer<in T>) {
    default_forEachRemaining(consumer)
  }
}

fun <T> MutableIterator<T>.java_forEachRemaining(consumer: Consumer<in T>) =
  if (this is JavaIterator) java_forEachRemaining(consumer) else default_forEachRemaining(consumer)

private fun <T> MutableIterator<T>.default_forEachRemaining(consumer: Consumer<in T>) {
  while (hasNext()) consumer.accept(next())
}
