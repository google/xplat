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

import kotlin.jvm.javaObjectType
import kotlin.jvm.javaPrimitiveType
import kotlin.reflect.KClass

/**
 * Implementation of java.lang.Class used in Kotlin Native. The constructor and the `kClass`
 * property are not accessible in Java.
 */
class Class<T>(private val kClass: KClass<*>, private val isPrimitive0: Boolean) {
  fun getName() = kClass.qualifiedName
  fun getCanonicalName() = kClass.qualifiedName
  fun getSimpleName() = kClass.simpleName
  fun isPrimitive() = isPrimitive0
  fun isArray() = getComponentType() != null
  fun getComponentType(): Class<*>? = arrayComponentTypeMap[kClass]
  // TODO(b/235808937): Implement
  fun getEnumConstants(): Array<T>? = throw UnsupportedOperationException()
  override fun toString() = kClass.toString()
}

private val arrayComponentTypeMap =
  mapOf<KClass<*>, Class<*>>(
    ByteArray::class to Byte::class.javaPrimitiveType!!,
    ShortArray::class to Short::class.javaPrimitiveType!!,
    IntArray::class to Int::class.javaPrimitiveType!!,
    LongArray::class to Long::class.javaPrimitiveType!!,
    FloatArray::class to Float::class.javaPrimitiveType!!,
    DoubleArray::class to Double::class.javaPrimitiveType!!,
    BooleanArray::class to Boolean::class.javaPrimitiveType!!,
    CharArray::class to Char::class.javaPrimitiveType!!,
    Array::class to Any::class.javaObjectType,
  )
