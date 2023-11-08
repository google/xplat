/*
 * Copyright 2007 Google Inc.
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

package java.util

import java.io.Serializable
import kotlin.Cloneable
import kotlin.Comparable
import kotlin.OptIn
import kotlin.experimental.ExperimentalObjCName
import kotlin.math.abs
import kotlin.native.ObjCName
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.number
import kotlinx.datetime.offsetIn
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

@ObjCName("J2ktJavaUtilDate", exact = true)
open class Date
private constructor(private var localDateTime: LocalDateTime, private var timeZone: TimeZone) :
  Cloneable, Comparable<Date>, Serializable {
  constructor() :
    this(
      Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
      TimeZone.currentSystemDefault()
    )

  constructor(
    @ObjCName("Int") year: Int,
    @ObjCName("withInt") month: Int,
    @ObjCName("withInt") date: Int
  ) : this(year, month, date, 0, 0, 0)

  constructor(
    @ObjCName("Int") year: Int,
    @ObjCName("withInt") month: Int,
    @ObjCName("withInt") date: Int,
    @ObjCName("withInt") hrs: Int,
    @ObjCName("withInt") min: Int
  ) : this(year, month, date, hrs, min, 0)

  constructor(
    @ObjCName("Int") year: Int,
    @ObjCName("withInt") month: Int,
    @ObjCName("withInt") date: Int,
    @ObjCName("withInt") hrs: Int,
    @ObjCName("withInt") min: Int,
    @ObjCName("withInt") sec: Int
  ) : this(
    LocalDateTime(year + 1900, month + 1, date, hrs, min, sec, 0),
    TimeZone.currentSystemDefault()
  ) {
    fixDaylightSavings(hrs)
  }

  constructor(
    @ObjCName("Long") date: Long
  ) : this(
    Instant.fromEpochMilliseconds(date).toLocalDateTime(TimeZone.currentSystemDefault()),
    TimeZone.currentSystemDefault()
  )

  constructor(@ObjCName("NSString") date: String) : this(Date.parse(date))

  @ObjCName("after")
  open fun after(@ObjCName("withJavaUtilDate") `when`: Date): Boolean {
    return getTime() > `when`.getTime()
  }

  @ObjCName("before")
  open fun before(@ObjCName("withJavaUtilDate") `when`: Date): Boolean {
    return getTime() < `when`.getTime()
  }

  open override fun clone(): Any = Date(localDateTime, timeZone)

  open override fun compareTo(other: Date): Int = getTime().compareTo(other.getTime())

  open override fun equals(obj: Any?) = obj is Date && getTime() == obj.getTime()

  @ObjCName("getDate") open fun getDate(): Int = localDateTime.dayOfMonth

  @ObjCName("getDay") open fun getDay(): Int = localDateTime.dayOfWeek.isoDayNumber

  @ObjCName("getHours") open fun getHours(): Int = localDateTime.hour

  @ObjCName("getMinutes") open fun getMinutes(): Int = localDateTime.minute

  @ObjCName("getMonth") open fun getMonth(): Int = localDateTime.month.number - 1

  @ObjCName("getSeconds") open fun getSeconds(): Int = localDateTime.second

  @ObjCName("getTime")
  open fun getTime(): Long = localDateTime.toInstant(timeZone).toEpochMilliseconds()

  @ObjCName("getTimezoneOffset")
  open fun getTimezoneOffset(): Int =
    localDateTime.toInstant(timeZone).offsetIn(timeZone).totalSeconds

  @ObjCName("getYear")
  open fun getYear(): Int {
    return localDateTime.year - 1900
  }

  open override fun hashCode(): Int {
    val time = getTime()
    return time.xor(time.ushr(32)).toInt()
  }

  @ObjCName("setDate")
  open fun setDate(@ObjCName("withInt") date: Int) {
    val hours = localDateTime.hour
    localDateTime =
      LocalDateTime(
        localDateTime.year,
        localDateTime.month,
        date,
        localDateTime.hour,
        localDateTime.minute,
        localDateTime.second,
        localDateTime.nanosecond
      )
    fixDaylightSavings(hours)
  }

  @ObjCName("setHours")
  open fun setHours(@ObjCName("withInt") hours: Int) {
    localDateTime =
      LocalDateTime(
        localDateTime.year,
        localDateTime.month,
        localDateTime.dayOfMonth,
        hours,
        localDateTime.minute,
        localDateTime.second,
        localDateTime.nanosecond
      )
    fixDaylightSavings(hours)
  }

  @ObjCName("setMinutes")
  open fun setMinutes(@ObjCName("withInt") minutes: Int) {
    val hours: Int = getHours() + minutes / 60
    localDateTime =
      LocalDateTime(
        localDateTime.year,
        localDateTime.month,
        localDateTime.dayOfMonth,
        localDateTime.hour,
        minutes,
        localDateTime.second,
        localDateTime.nanosecond
      )
    fixDaylightSavings(hours)
  }

  @ObjCName("setMonth")
  open fun setMonth(@ObjCName("withInt") month: Int) {
    val hours: Int = localDateTime.hour
    localDateTime =
      LocalDateTime(
        localDateTime.year,
        month,
        localDateTime.dayOfMonth,
        localDateTime.hour,
        localDateTime.minute,
        localDateTime.second,
        localDateTime.nanosecond
      )
    fixDaylightSavings(hours)
  }

  @ObjCName("setSeconds")
  open fun setSeconds(@ObjCName("withInt") seconds: Int) {
    val hours: Int = localDateTime.hour + seconds / (60 * 60)
    localDateTime =
      LocalDateTime(
        localDateTime.year,
        localDateTime.month,
        localDateTime.dayOfMonth,
        localDateTime.hour,
        localDateTime.minute,
        seconds,
        localDateTime.nanosecond
      )
    fixDaylightSavings(hours)
  }

  @ObjCName("setTime")
  open fun setTime(@ObjCName("withLong") time: Long) {
    localDateTime = Instant.fromEpochMilliseconds(time).toLocalDateTime(timeZone)
  }

  @ObjCName("setYear")
  open fun setYear(@ObjCName("withInt") year: Int) {
    val hours: Int = localDateTime.hour
    localDateTime =
      LocalDateTime(
        year + 1900,
        localDateTime.month,
        localDateTime.dayOfMonth,
        localDateTime.hour,
        localDateTime.minute,
        localDateTime.second,
        localDateTime.nanosecond
      )
    fixDaylightSavings(hours)
  }

  @ObjCName("toGMTString")
  open fun toGMTString(): String {
    val utcDate =
      Date(localDateTime.toInstant(timeZone).toLocalDateTime(TimeZone.UTC), TimeZone.UTC)
    return "" +
      utcDate.getDate() +
      " " +
      Date.MONTHS[utcDate.getMonth()] +
      " " +
      utcDate.localDateTime.year +
      " " +
      Date.padTwo(utcDate.getHours()) +
      ":" +
      Date.padTwo(utcDate.getMinutes()) +
      ":" +
      Date.padTwo(utcDate.getSeconds()) +
      " GMT"
  }

  @ObjCName("toLocaleString") open fun toLocaleString(): String = localDateTime.toString()

  open override fun toString(): String {
    val offset: Int = -localDateTime.toInstant(timeZone).offsetIn(timeZone).totalSeconds
    val hourOffset: String = (if (offset >= 0) "+" else "") + offset / 60
    val minuteOffset: String = Date.padTwo(abs(offset) % 60)
    return DAYS[getDay() - 1] +
      " " +
      MONTHS[getMonth()] +
      " " +
      Date.padTwo(getDate()) +
      " " +
      Date.padTwo(getHours()) +
      ":" +
      Date.padTwo(getMinutes()) +
      ":" +
      Date.padTwo(getSeconds()) +
      " GMT" +
      hourOffset +
      minuteOffset +
      " " +
      localDateTime.year
  }

  private fun fixDaylightSavings(requestedHours: Int) {
    val requestedHoursFixed: Int = requestedHours % 24
    if (localDateTime.hour != requestedHoursFixed) {
      val copy = Date(getTime())
      copy.setDate(copy.getDay() + 1)
      val timeDiff: Int = getTimezoneOffset() - copy.getTimezoneOffset()
      if (timeDiff > 0) {
        val timeDiffHours: Int = timeDiff / 60
        val timeDiffMinutes: Int = timeDiff % 60
        var day: Int = getDay()
        val badHours: Int = getHours()
        if (badHours + timeDiffHours >= 24) {
          day = day + 1
        }
        localDateTime =
          LocalDateTime(
            localDateTime.year,
            localDateTime.month,
            day,
            requestedHoursFixed + timeDiffHours,
            localDateTime.minute + timeDiffMinutes,
            localDateTime.second,
            localDateTime.nanosecond
          )
      }
    }
    val originalTimeInMillis: Long = getTime()
    setTime(originalTimeInMillis + Date.ONE_HOUR_IN_MILLISECONDS)
    if (getHours() != requestedHoursFixed) {
      setTime(originalTimeInMillis)
    }
  }

  companion object {
    @ObjCName("parse")
    fun parse(@ObjCName("withNSString") s: String): Long {
      return LocalDateTime.parse(s).toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    // Unlike java.util.Date, Kotlin expects years in CE, and months in 1-12.
    @ObjCName("UTC")
    fun UTC(
      @ObjCName("withInt") year: Int,
      @ObjCName("withInt") month: Int,
      @ObjCName("withInt") date: Int,
      @ObjCName("withInt") hrs: Int,
      @ObjCName("withInt") min: Int,
      @ObjCName("withInt") sec: Int
    ) = Date(LocalDateTime(year + 1900, month + 1, date, hrs, min, sec, 0), TimeZone.UTC).getTime()

    @ObjCName("padTwo")
    fun padTwo(@ObjCName("withInt") number: Int) =
      if (number < 10) "0" + number else number.toString()

    private const val ONE_HOUR_IN_MILLISECONDS: Long = 3600000L

    private val DAYS: Array<String> =
      arrayOf<String>("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    private val MONTHS: Array<String> =
      arrayOf<String>(
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec"
      )
  }
}
