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
package smoke

import java.lang.Class
import java.lang.Runnable
import kotlin.jvm.javaObjectType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.cinterop.ObjCProtocol
import platform.darwin.NSObject
import platform.objc.objc_getProtocol
import platform.objc.object_getClass

class MyKotlinClass

class MyRunnable : Runnable {
  override fun run() {}
}

class ClassInteropTest {

  @Test
  fun fromObjCClass_withKotlinClass_returnsJavaClass() {
    val instance = MyKotlinClass()
    val objcClass = requireNotNull(object_getClass(instance))
    val javaClass = Class.fromObjCClass(objcClass)
    assertEquals(MyKotlinClass::class.javaObjectType, javaClass)
  }

  @Test
  fun fromObjCClass_withNonKotlinClass_returnsNull() {
    val instance = NSObject()
    val objcClass = requireNotNull(object_getClass(instance))
    val javaClass = Class.fromObjCClass(objcClass)
    assertNull(javaClass)
  }

  @Test
  fun fromObjCProtocol_withNonKotlinProtocol_returnsNull() {
    val protocol = objc_getProtocol("NSCopying")
    val javaClass = Class.fromObjCProtocol(requireNotNull(protocol) as ObjCProtocol)
    assertNull(javaClass)
  }

  @Test
  fun fromObjCProtocol_withKotlinRunnableProtocol_returnsJavaClass() {
    val protocol = objc_getProtocol("J2ktJavaLangRunnable")
    val javaClass = Class.fromObjCProtocol(requireNotNull(protocol) as ObjCProtocol)
    assertEquals(Runnable::class.javaObjectType, javaClass)
  }
}
