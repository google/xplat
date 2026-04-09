/*
 * Copyright 2026 Google Inc.
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

import kotlin.collections.MutableIterator
import kotlin.collections.MutableListIterator

@JvmSynthetic fun <T> Iterator<T>.asMutableIterator(): MutableIterator<T> = asMutable()

@JvmName("alreadyMutableAsMutableIterator")
@JvmSynthetic
fun <T> MutableIterator<T>.asMutableIterator(): MutableIterator<T> = this

@JvmSynthetic fun <T> ListIterator<T>.asMutableListIterator(): MutableListIterator<T> = asMutable()

@JvmName("alreadyMutableAsMutableListIterator")
@JvmSynthetic
fun <T> MutableListIterator<T>.asMutableListIterator(): MutableListIterator<T> = this
