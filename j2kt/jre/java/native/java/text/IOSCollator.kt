/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package java.text

import java.util.Locale
import kotlin.native.ObjCName
import platform.Foundation.NSCaseInsensitiveSearch
import platform.Foundation.NSDiacriticInsensitiveSearch
import platform.Foundation.NSLiteralSearch
import platform.Foundation.NSLocale
import platform.Foundation.NSMakeRange
import platform.Foundation.NSString
import platform.Foundation.NSStringCompareOptions
import platform.Foundation.compare

/**
 * Replaces missing JRE functionality in java.text.Collator with calls to Kotlin/native ios
 * functionality.
 */
@OptIn(kotlin.experimental.ExperimentalObjCName::class)
@ObjCName("J2ktJavaTextIOSCollator", exact = true)
internal class IOSCollator(private val locale: Locale) : Collator() {
  private var strength = PRIMARY
  private var decomposition = NO_DECOMPOSITION
  private val nsLocale = NSLocale(locale.toString())

  override fun getDecomposition() = decomposition

  override fun setDecomposition(decompositionMode: Int) {
    require(decompositionMode in NO_DECOMPOSITION..FULL_DECOMPOSITION) {
      "decomposition must match any decomposition constant from java.text.Collator, was $decompositionMode"
    }
    decomposition = decompositionMode
  }

  override fun setStrength(newStrength: Int) {
    require(newStrength in PRIMARY..IDENTICAL) {
      "strength must match any strength constant from java.text.Collator, was $newStrength"
    }
    strength = newStrength
  }

  override fun getStrength() = strength

  override fun hashCode() = nsLocale.hash() as Int

  override fun equals(other: Any?): Boolean {
    if (this === other) {
      return true
    }
    if (other == null) {
      return false
    }
    if (other !is IOSCollator) {
      return false
    }
    return locale == other.locale &&
      strength == other.strength &&
      decomposition == other.decomposition
  }

  override fun compare(string1: String, string2: String): Int {
    val compareOptions: NSStringCompareOptions =
      when (strength) {
        PRIMARY -> NSCaseInsensitiveSearch or NSDiacriticInsensitiveSearch
        SECONDARY -> NSCaseInsensitiveSearch
        TERTIARY -> 0u
        IDENTICAL -> NSLiteralSearch
        else -> {
          throw IllegalStateException("Strength value is invalid: $strength")
        }
      }

    return (string1 as NSString)
      .compare(string2, compareOptions, NSMakeRange(0u, string2.length.toULong()), nsLocale)
      .toInt()
  }

  override fun getCollationKey(string: String) = IOSCollationKey(string)

  override fun clone(): IOSCollator =
    IOSCollator(locale).also {
      it.strength = strength
      it.decomposition = decomposition
    }
}

internal class IOSCollationKey(source: String) : CollationKey(source) {
  override fun compareTo(other: CollationKey) = 0

  override fun toByteArray(): ByteArray {
    return getSourceString().encodeToByteArray()
  }
}
