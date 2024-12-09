/*
 * Copyright 2024 Google Inc.
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
package java.net

import java.util.Date
import kotlin.native.concurrent.ThreadLocal
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.localeWithLocaleIdentifier
import platform.Foundation.timeIntervalSince1970

// Always use US locale for parsing dates in HTTP Cookie headers.
@ThreadLocal private val US_LOCALE = NSLocale.localeWithLocaleIdentifier("en_US_POSIX")

@ThreadLocal
private val STANDARD_DATE_FORMAT: NSDateFormatter =
  NSDateFormatter().apply {
    locale = US_LOCALE
    dateFormat = "EEE, dd MMM yyyy HH:mm:ss zzz"
  }

internal object HttpDate {
  /**
   * Most websites serve cookies in the blessed format. Eagerly create the parser to ensure such
   * cookies are on the fast path.
   */

  /** If we fail to parse a date in a non-standard format, try each of these formats in sequence. */
  private val BROWSER_COMPATIBLE_DATE_FORMATS =
    listOf(
      /* This list comes from  {@code org.apache.http.impl.cookie.BrowserCompatSpec}. */
      "EEEE, dd-MMM-yy HH:mm:ss zzz", // RFC 1036
      "EEE MMM d HH:mm:ss yyyy", // ANSI C asctime()
      "EEE, dd-MMM-yyyy HH:mm:ss z",
      "EEE, dd-MMM-yyyy HH-mm-ss z",
      "EEE, dd MMM yy HH:mm:ss z",
      "EEE dd-MMM-yyyy HH:mm:ss z",
      "EEE dd MMM yyyy HH:mm:ss z",
      "EEE dd-MMM-yyyy HH-mm-ss z",
      "EEE dd-MMM-yy HH:mm:ss z",
      "EEE dd MMM yy HH:mm:ss z",
      "EEE,dd-MMM-yy HH:mm:ss z",
      "EEE,dd-MMM-yyyy HH:mm:ss z",
      "EEE, dd-MM-yyyy HH:mm:ss z", /* RI bug 6641315 claims a cookie of this format was once served by www.yahoo.com */
      "EEE MMM d yyyy HH:mm:ss z",
    )

  /** Returns the date for `value`. Returns null if the value couldn't be parsed. */
  fun parse(value: String): Date? {
    STANDARD_DATE_FORMAT.parse(value)?.let {
      return it
    }
    for (formatString in BROWSER_COMPATIBLE_DATE_FORMATS) {
      NSDateFormatter()
        .run {
          locale = US_LOCALE
          dateFormat = formatString
          parse(value)
        }
        ?.let {
          return it
        }
    }
    return null
  }

  private fun NSDateFormatter.parse(value: String): Date? {
    val nsDate = dateFromString(value) ?: return null
    return Date((nsDate.timeIntervalSince1970 * 1000.0).toLong())
  }
}
