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
package kotlin.jvm

import java.lang.Class
import java.lang.Void
import javaemul.lang.J2ktMonitor
import kotlin.reflect.KClass
import kotlin.synchronized

private val objectClassMapMonitor = J2ktMonitor()
private val objectClassMap: MutableMap<KClass<*>, Class<*>> = mutableMapOf()

private val primitiveClassMapMonitor = J2ktMonitor()
private val primitiveClassMap: MutableMap<KClass<*>, Class<*>> = mutableMapOf()

val <T : Any> KClass<T>.javaObjectType: Class<T>
  get() =
    synchronized(objectClassMapMonitor) {
      objectClassMap.getOrPut(this) { Class<T>(this, isPrimitive0 = false) } as Class<T>
    }

val <T : Any> KClass<T>.javaPrimitiveType: Class<T>?
  get() =
    if (hasJavaPrimitiveType) {
      synchronized(primitiveClassMapMonitor) {
        primitiveClassMap.getOrPut(this) { Class<T>(this, isPrimitive0 = true) } as Class<T>
      }
    } else {
      null
    }

private val KClass<*>.hasJavaPrimitiveType: Boolean
  get() =
    when (this) {
      Boolean::class,
      Char::class,
      Byte::class,
      Short::class,
      Int::class,
      Long::class,
      Float::class,
      Double::class,
      Void::class -> true
      else -> false
    }
