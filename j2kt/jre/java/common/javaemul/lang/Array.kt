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
package javaemul.lang

fun <T> Array<T>.clone(): Array<T> = copyOf()

fun BooleanArray.clone(): BooleanArray = copyOf()

fun ByteArray.clone(): ByteArray = copyOf()

fun CharArray.clone(): CharArray = copyOf()

fun DoubleArray.clone(): DoubleArray = copyOf()

fun FloatArray.clone(): FloatArray = copyOf()

fun IntArray.clone(): IntArray = copyOf()

fun LongArray.clone(): LongArray = copyOf()

fun ShortArray.clone(): ShortArray = copyOf()
