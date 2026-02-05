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

import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.jvm.javaPrimitiveType

/** java.lang.Boolean static method emulations */
@OptIn(ExperimentalObjCRefinement::class)
class Boolean {
  companion object {
    operator fun invoke(b: kotlin.Boolean): kotlin.Boolean = b

    operator fun invoke(s: kotlin.String?): kotlin.Boolean = parseBoolean(s)

    @HiddenFromObjC const val TRUE: kotlin.Boolean = true

    @HiddenFromObjC const val FALSE: kotlin.Boolean = false

    val TYPE: Class<kotlin.Boolean> = kotlin.Boolean::class.javaPrimitiveType!!

    fun valueOf(b: kotlin.Boolean): kotlin.Boolean = b

    fun valueOf(s: kotlin.String?): kotlin.Boolean = s.toBoolean()

    fun hashCode(b: kotlin.Boolean): Int = b.hashCode()

    fun parseBoolean(s: kotlin.String?): kotlin.Boolean = s.toBoolean()

    fun toString(b: kotlin.Boolean): kotlin.String = b.toString()

    fun compare(b1: kotlin.Boolean, b2: kotlin.Boolean): Int = b1.compareTo(b2)

    fun logicalAnd(a: kotlin.Boolean, b: kotlin.Boolean): kotlin.Boolean = a && b

    fun logicalOr(a: kotlin.Boolean, b: kotlin.Boolean): kotlin.Boolean = a || b

    fun logicalXor(a: kotlin.Boolean, b: kotlin.Boolean): kotlin.Boolean = a xor b
  }
}
