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

import java.lang.reflect.Type
import javaemul.lang.J2ktMonitor
import kotlin.experimental.ExperimentalObjCName
import kotlin.jvm.javaObjectType
import kotlin.jvm.javaPrimitiveType
import kotlin.native.ObjCName
import kotlin.reflect.KClass

/**
 * Implementation of java.lang.Class used in Kotlin Native. The constructor and the `kClass`
 * property are not accessible in Java.
 */
@ObjCName("J2ktJavaLangClass", exact = true)
class Class<T : Any>(private val kClass: KClass<T>, private val isPrimitive0: kotlin.Boolean) :
  Type {
  val j2ktMonitor = J2ktMonitor()

  fun cast(obj: Any?): T? =
    if (obj == null || kClass.isInstance(obj)) obj as T? else throw ClassCastException()

  fun getName(): kotlin.String = kClass.qualifiedName ?: ""

  fun getCanonicalName(): kotlin.String? = kClass.qualifiedName

  fun getSimpleName(): kotlin.String = kClass.simpleName ?: ""

  fun isPrimitive(): kotlin.Boolean = isPrimitive0

  fun isArray(): kotlin.Boolean = getComponentType() != null

  fun isInstance(obj: Any?): kotlin.Boolean = !isPrimitive0 && kClass.isInstance(obj)

  fun getComponentType(): Class<*>? = arrayComponentTypeMap[kClass]

  // TODO(b/235808937): Implement
  fun getEnumConstants(): Array<T>? = throw UnsupportedOperationException()

  override fun toString(): kotlin.String = kClass.toString()
}

private val arrayComponentTypeMap: Map<KClass<*>, Class<*>> =
  mapOf(
    ByteArray::class to kotlin.Byte::class.javaPrimitiveType!!,
    ShortArray::class to kotlin.Short::class.javaPrimitiveType!!,
    IntArray::class to kotlin.Int::class.javaPrimitiveType!!,
    LongArray::class to kotlin.Long::class.javaPrimitiveType!!,
    FloatArray::class to kotlin.Float::class.javaPrimitiveType!!,
    DoubleArray::class to kotlin.Double::class.javaPrimitiveType!!,
    BooleanArray::class to kotlin.Boolean::class.javaPrimitiveType!!,
    CharArray::class to kotlin.Char::class.javaPrimitiveType!!,
    Array::class to Any::class.javaObjectType,
  )
