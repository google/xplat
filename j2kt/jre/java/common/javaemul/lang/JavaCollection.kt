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

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

/** The base class of all transpiled J2kt Collection implementations */
@ObjCName("JavaemulLangJavaCollection", exact = true)
interface JavaCollection<E> : MutableCollectionJvm<E> {
  @Suppress("NOTHING_TO_OVERRIDE") // Super method is hidden on JVM
  override fun toArray(): Array<Any?>

  @Suppress("NOTHING_TO_OVERRIDE") // Super method is hidden on JVM
  override fun <T> toArray(a: Array<T>): Array<T>
}
