/*
 * Copyright 2009 Google Inc.
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

package kotlin

import javaemul.lang.J2ktMonitor
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

/** Kotlin native implementation of [java.lang.synchronized] for J2ktMonitor. */
@OptIn(ExperimentalContracts::class, ExperimentalObjCRefinement::class)
@HiddenFromObjC
inline fun <R> synchronized(monitor: J2ktMonitor, block: () -> R): R {
  contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
  return monitor.synchronizedImpl(block)
}

/** Kotlin native implementation of [java.lang.synchronized] when using a class as the monitor. */
@OptIn(ExperimentalContracts::class, ExperimentalObjCRefinement::class)
@HiddenFromObjC
inline fun <R> synchronized(clazz: java.lang.Class<*>, block: () -> R): R {
  contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
  return clazz.j2ktMonitor.synchronizedImpl(block)
}
