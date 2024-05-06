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

import java.lang.Double as JavaDouble

/**
 * Pseudo-constructor for emulated java.lang.Double.
 *
 * See regular JRE API documentation for other methods in this file.
 */
operator fun Double.Companion.invoke(d: Double): Double = JavaDouble(d) as Double

operator fun Double.Companion.invoke(s: String): Double = JavaDouble(s) as Double

inline fun Double.toInt_toByte() = toInt().toByte()

inline fun Double.toInt_toShort() = toInt().toShort()
