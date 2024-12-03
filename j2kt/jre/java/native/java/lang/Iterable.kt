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

package java.lang

import java.util.Spliterator
import java.util.Spliterators
import java.util.function.Consumer
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@ObjCName("J2ktJavaLangIterable", exact = true)
fun interface Iterable<T> : MutableIterable<T> {
  fun forEach(consumer: Consumer<in T>) {
    forEach(consumer::accept)
  }

  fun spliterator(): Spliterator<T> = Spliterators.spliteratorUnknownSize(iterator(), 0)
}
