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
package java.lang

import kotlin.reflect.KClass

/**
 * Implementation of java.lang.Class used in Kotlin Native. The constructor and the `kClass`
 * property are not accessible in Java.
 */
class Class<T>(val kClass: KClass<*>) {
  fun getName() = kClass.qualifiedName
  fun getCanonicalName() = kClass.qualifiedName
  fun getSimpleName() = kClass.simpleName
  fun isArray() = arrayKClassSet.contains(kClass)
  // TODO(b/235808937): Implement
  fun getEnumConstants(): Array<T>? = throw UnsupportedOperationException()
}

private val arrayKClassSet =
  setOf<KClass<*>>(
    ByteArray::class,
    ShortArray::class,
    IntArray::class,
    LongArray::class,
    FloatArray::class,
    DoubleArray::class,
    BooleanArray::class,
    CharArray::class,
    Array::class
  )
