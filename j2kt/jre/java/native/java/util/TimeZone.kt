/*
 * Copyright 2025 Google Inc.
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
package java.util

import java.io.Serializable
import kotlin.Cloneable
import kotlinx.datetime.TimeZone as KotlinTimeZone

/** Minimal j2kt emulation of TimeZone, backed by kotlinx.datetime.TimeZone. */
open class TimeZone internal constructor(internal val timeZone: KotlinTimeZone) :
  Cloneable, Serializable {

  public constructor() : this(KotlinTimeZone.currentSystemDefault())

  /** Creates a copy of this TimeZone. */
  open override fun clone(): TimeZone = TimeZone(timeZone)

  /** Gets the ID of this time zone. */
  fun getID(): String = timeZone.id

  companion object {

    private var defaultTimeZone: KotlinTimeZone? = null

    /** Gets the default TimeZone for this host. */
    fun getDefault(): TimeZone = TimeZone(defaultTimeZone ?: KotlinTimeZone.currentSystemDefault())

    /** Sets the TimeZone that is returned by the getDefault method. */
    fun setDefault(dateTimeZone: TimeZone?) {
      defaultTimeZone = dateTimeZone?.timeZone
    }
  }
}
