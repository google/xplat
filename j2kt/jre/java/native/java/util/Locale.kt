@file:OptIn(ExperimentalObjCName::class)

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

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName
import platform.Foundation.NSLocale
import platform.Foundation.NSString
import platform.Foundation.NSUserDefaults
import platform.Foundation.countryCode
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.Foundation.localeIdentifier
import platform.Foundation.lowercaseStringWithLocale
import platform.Foundation.scriptCode
import platform.Foundation.uppercaseStringWithLocale
import platform.Foundation.variantCode

// This could be a data class (excluding the NSLocale property), but we want to special-case
// toUppercase and toLowercase for the ROOT locale.
/**
 * A very simple emulation of Locale for shared-code patterns like `String.toUpperCase(Locale.US)`.
 *
 * Note: Any changes to this class should put into account the assumption that was made in rest of
 * the JRE emulation.
 */
@ObjCName("J2ktJavaUtilLocale", exact = true)
open class Locale
private constructor(
  val language: String,
  val script: String,
  val country: String,
  val variant: String,
  val nsLocale: NSLocale,
) {

  private constructor(
    nsLocale: NSLocale
  ) : this(
    language = nsLocale.languageCode ?: "",
    script = nsLocale.scriptCode ?: "",
    country = nsLocale.countryCode ?: "",
    variant = nsLocale.variantCode ?: "",
    nsLocale = nsLocale,
  )

  open fun toLanguageTag(): String = (nsLocale.localeIdentifier as String).replace('_', '-')

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Locale) return false
    return language == other.language &&
      script == other.script &&
      country == other.country &&
      variant == other.variant
  }

  override fun hashCode(): Int {
    var result = language.hashCode()
    result = 31 * result + script.hashCode()
    result = 31 * result + country.hashCode()
    result = 31 * result + variant.hashCode()
    return result
  }

  override fun toString(): String = nsLocale.localeIdentifier

  internal open fun toUppercase(s: String): String =
    (s as NSString).uppercaseStringWithLocale(nsLocale)

  internal open fun toLowercase(s: String): String =
    (s as NSString).lowercaseStringWithLocale(nsLocale)

  companion object {
    val ROOT: Locale =
      object :
        Locale(language = "", script = "", country = "", variant = "", NSLocale("en_US_POSIX")) {
        override fun toString(): String = ""

        override fun toLanguageTag(): String = "und"

        override fun toUppercase(s: String): String = s.uppercase()

        override fun toLowercase(s: String): String = s.lowercase()
      }

    val ENGLISH: Locale = forLanguageTag("en")
    val US: Locale = forLanguageTag("en_US")

    fun forLanguageTag(languageTag: String): Locale =
      if (languageTag.isEmpty()) ROOT else Locale(NSLocale(languageTag))

    fun getDefault(): Locale = Locale(NSLocale.currentLocale)

    fun setDefault(newLocale: Locale): Unit {
      NSUserDefaults.standardUserDefaults().apply {
        setObject(newLocale.toLanguageTag(), forKey = "LanguageCode")
        synchronize()
      }
    }
  }
}
