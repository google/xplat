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
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@ObjCName("JavaemulLangJavaAbstractCollection", exact = true)
abstract class JavaAbstractCollection<E> : AbstractMutableCollection<E>(), JavaCollection<E> {
  override fun spliterator(): Spliterator<E> = super<JavaCollection>.spliterator()

  override fun add(e: E): Boolean = throw UnsupportedOperationException()

  override fun clear(): Unit = super<AbstractMutableCollection>.clear()

  override fun java_toArray(): Array<Any?> = default_toArray()

  override fun <T> java_toArray(a: Array<T>): Array<T> = default_toArray(a)
}
