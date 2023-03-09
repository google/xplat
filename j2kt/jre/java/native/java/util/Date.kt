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
import javaemul.lang.compare
import javaemul.lang.valueOf
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
import kotlinx.datetime.number
import kotlinx.datetime.offsetIn
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

@ObjCName("J2ktJavaUtilDate", exact = true)
open class Date private constructor(private val nativeDate: Date.NativeDate) :
  Cloneable, Comparable<Date>, Serializable {
  constructor() : this(Date.NativeDate())

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
  ) : this(Date.NativeDate()) {
    nativeDate.setFullYear(year + 1900, month, date)
    nativeDate.setHours(hrs, min, sec, 0)
    fixDaylightSavings(hrs)
  }

  constructor(@ObjCName("Long") date: Long) : this(Date.NativeDate(date))

  constructor(@ObjCName("NSString") date: String) : this(Date.parse(date))

  @ObjCName("after")
  open fun after(@ObjCName("withJavaUtilDate") `when`: Date): Boolean {
    return getTime() > `when`.getTime()
  }

  @ObjCName("before")
  open fun before(@ObjCName("withJavaUtilDate") `when`: Date): Boolean {
    return getTime() < `when`.getTime()
  }

  open override fun clone(): Any {
    return Date(getTime())
  }

  open override fun compareTo(other: Date): Int {
    return Long.compare(getTime(), other.getTime())
  }

  open override fun equals(obj: Any?): Boolean {
    return obj is Date && getTime() == obj.getTime()
  }

  @ObjCName("getDate")
  open fun getDate(): Int {
    return nativeDate.getDate()
  }

  @ObjCName("getDay")
  open fun getDay(): Int {
    return nativeDate.getDay()
  }

  @ObjCName("getHours")
  open fun getHours(): Int {
    return nativeDate.getHours()
  }

  @ObjCName("getMinutes")
  open fun getMinutes(): Int {
    return nativeDate.getMinutes()
  }

  @ObjCName("getMonth")
  open fun getMonth(): Int {
    return nativeDate.getMonth()
  }

  @ObjCName("getSeconds")
  open fun getSeconds(): Int {
    return nativeDate.getSeconds()
  }

  @ObjCName("getTime")
  open fun getTime(): Long {
    return nativeDate.getTime()
  }

  @ObjCName("getTimezoneOffset")
  open fun getTimezoneOffset(): Int {
    return nativeDate.getTimezoneOffset()
  }

  @ObjCName("getYear")
  open fun getYear(): Int {
    return nativeDate.getFullYear() - 1900
  }

  open override fun hashCode(): Int {
    val time: Long = getTime()
    return time.xor(time.ushr(32)).toInt()
  }

  @ObjCName("setDate")
  open fun setDate(@ObjCName("withInt") date: Int) {
    val hours: Int = nativeDate.getHours()
    nativeDate.setDate(date)
    fixDaylightSavings(hours)
  }

  @ObjCName("setHours")
  open fun setHours(@ObjCName("withInt") hours: Int) {
    nativeDate.setHours(hours)
    fixDaylightSavings(hours)
  }

  @ObjCName("setMinutes")
  open fun setMinutes(@ObjCName("withInt") minutes: Int) {
    val hours: Int = getHours() + minutes / 60
    nativeDate.setMinutes(minutes)
    fixDaylightSavings(hours)
  }

  @ObjCName("setMonth")
  open fun setMonth(@ObjCName("withInt") month: Int) {
    val hours: Int = nativeDate.getHours()
    nativeDate.setMonth(month)
    fixDaylightSavings(hours)
  }

  @ObjCName("setSeconds")
  open fun setSeconds(@ObjCName("withInt") seconds: Int) {
    val hours: Int = getHours() + seconds / (60 * 60)
    nativeDate.setSeconds(seconds)
    fixDaylightSavings(hours)
  }

  @ObjCName("setTime")
  open fun setTime(@ObjCName("withLong") time: Long) {
    nativeDate.setTime(time)
  }

  @ObjCName("setYear")
  open fun setYear(@ObjCName("withInt") year: Int) {
    val hours: Int = nativeDate.getHours()
    nativeDate.setFullYear(year + 1900)
    fixDaylightSavings(hours)
  }

  @ObjCName("toGMTString")
  open fun toGMTString(): String {
    val utcNativeDate = nativeDate.toUTC()
    return "" +
      utcNativeDate.getDate() +
      " " +
      Date.StringData.MONTHS[utcNativeDate.getMonth()] +
      " " +
      utcNativeDate.getFullYear() +
      " " +
      Date.padTwo(utcNativeDate.getHours()) +
      ":" +
      Date.padTwo(utcNativeDate.getMinutes()) +
      ":" +
      Date.padTwo(utcNativeDate.getSeconds()) +
      " GMT"
  }

  @ObjCName("toLocaleString")
  open fun toLocaleString(): String {
    return nativeDate.toLocaleString()
  }

  open override fun toString(): String {
    val offset: Int = -nativeDate.getTimezoneOffset()
    val hourOffset: String = (if (offset >= 0) "+" else "") + offset / 60
    val minuteOffset: String = Date.padTwo(abs(offset) % 60)
    return Date.StringData.DAYS[nativeDate.getDay()] +
      " " +
      Date.StringData.MONTHS[nativeDate.getMonth()] +
      " " +
      Date.padTwo(nativeDate.getDate()) +
      " " +
      Date.padTwo(nativeDate.getHours()) +
      ":" +
      Date.padTwo(nativeDate.getMinutes()) +
      ":" +
      Date.padTwo(nativeDate.getSeconds()) +
      " GMT" +
      hourOffset +
      minuteOffset +
      " " +
      nativeDate.getFullYear()
  }

  private fun fixDaylightSavings(requestedHours: Int) {
    val requestedHoursFixed: Int = requestedHours % 24
    if (nativeDate.getHours() != requestedHoursFixed) {
      val copy: Date.NativeDate = Date.NativeDate(nativeDate.getTime())
      copy.setDate(copy.getDate() + 1)
      val timeDiff: Int = nativeDate.getTimezoneOffset() - copy.getTimezoneOffset()
      if (timeDiff > 0) {
        val timeDiffHours: Int = timeDiff / 60
        val timeDiffMinutes: Int = timeDiff % 60
        var day: Int = nativeDate.getDate()
        val badHours: Int = nativeDate.getHours()
        if (badHours + timeDiffHours >= 24) {
          day = day + 1
        }
        val newTime: Date.NativeDate =
          Date.NativeDate(
            nativeDate.getFullYear(),
            nativeDate.getMonth(),
            day,
            requestedHoursFixed + timeDiffHours,
            nativeDate.getMinutes() + timeDiffMinutes,
            nativeDate.getSeconds(),
            nativeDate.getMilliseconds()
          )
        nativeDate.setTime(newTime.getTime())
      }
    }
    val originalTimeInMillis: Long = nativeDate.getTime()
    nativeDate.setTime(originalTimeInMillis + Date.ONE_HOUR_IN_MILLISECONDS)
    if (nativeDate.getHours() != requestedHoursFixed) {
      nativeDate.setTime(originalTimeInMillis)
    }
  }

  companion object {
    @ObjCName("parse")
    fun parse(@ObjCName("withNSString") s: String): Long {
      return Date.NativeDate.parse(s)
    }

    @ObjCName("UTC")
    fun UTC(
      @ObjCName("withInt") year: Int,
      @ObjCName("withInt") month: Int,
      @ObjCName("withInt") date: Int,
      @ObjCName("withInt") hrs: Int,
      @ObjCName("withInt") min: Int,
      @ObjCName("withInt") sec: Int
    ): Long {
      return NativeDate.UTC(year + 1900, month, date, hrs, min, sec, 0).getTime()
    }

    @ObjCName("padTwo")
    fun padTwo(@ObjCName("withInt") number: Int): String {
      if (number < 10) {
        return "0" + number
      } else {
        return String.valueOf(number)
      }
    }

    private const val ONE_HOUR_IN_MILLISECONDS: Long = 3600000L
  }

  private class StringData {
    companion object {
      val DAYS: Array<String> = arrayOf<String>("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

      val MONTHS: Array<String> =
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

  private class NativeDate(private var dateTime: LocalDateTime, private val timeZone: TimeZone) {
    constructor() :
      this(
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        TimeZone.currentSystemDefault()
      ) {}

    constructor(
      milliseconds: Long
    ) : this(
      Instant.fromEpochMilliseconds(milliseconds).toLocalDateTime(TimeZone.currentSystemDefault()),
      TimeZone.currentSystemDefault()
    ) {}

    constructor(
      year: Int,
      month: Int,
      dayOfMonth: Int,
      hours: Int,
      minutes: Int,
      seconds: Int,
      millis: Int
    ) : this(
      LocalDateTime(year, month, dayOfMonth, hours, minutes, seconds, millis * 1_000_000),
      TimeZone.currentSystemDefault()
    ) {}

    fun getDate(): Int = dateTime.dayOfMonth

    fun getDay(): Int = dateTime.dayOfMonth

    fun getFullYear(): Int = dateTime.year

    fun getHours(): Int = dateTime.hour

    fun getMilliseconds(): Int = dateTime.nanosecond / 1_000_000

    fun getMinutes(): Int = dateTime.minute

    fun getMonth(): Int = dateTime.month.number

    fun getSeconds(): Int = dateTime.second

    // Returns milliseconds since epoch
    fun getTime(): Long = dateTime.toInstant(timeZone).toEpochMilliseconds()

    fun getTimezoneOffset(): Int = dateTime.toInstant(timeZone).offsetIn(timeZone).totalSeconds

    fun toUTC(): NativeDate =
      NativeDate(dateTime.toInstant(timeZone).toLocalDateTime(TimeZone.UTC), TimeZone.UTC)

    fun setDate(dayOfMonth: Int) {
      dateTime =
        LocalDateTime(
          dateTime.year,
          dateTime.month,
          dayOfMonth,
          dateTime.hour,
          dateTime.minute,
          dateTime.second,
          dateTime.nanosecond
        )
    }

    fun setFullYear(year: Int) {
      dateTime =
        LocalDateTime(
          year,
          dateTime.month,
          dateTime.dayOfMonth,
          dateTime.hour,
          dateTime.minute,
          dateTime.second,
          dateTime.nanosecond
        )
    }

    fun setFullYear(year: Int, month: Int, day: Int) {
      dateTime =
        LocalDateTime(
          year,
          month,
          day,
          dateTime.hour,
          dateTime.minute,
          dateTime.second,
          dateTime.nanosecond
        )
    }

    fun setHours(hours: Int) {
      dateTime =
        LocalDateTime(
          dateTime.year,
          dateTime.month,
          dateTime.dayOfMonth,
          hours,
          dateTime.minute,
          dateTime.second,
          dateTime.nanosecond
        )
    }

    fun setHours(hours: Int, mins: Int, secs: Int, ms: Int) {
      dateTime =
        LocalDateTime(
          dateTime.year,
          dateTime.month,
          dateTime.dayOfMonth,
          hours,
          mins,
          secs,
          ms * 1_000_000
        )
    }

    fun setMinutes(minutes: Int) {
      dateTime =
        LocalDateTime(
          dateTime.year,
          dateTime.month,
          dateTime.dayOfMonth,
          dateTime.hour,
          minutes,
          dateTime.second,
          dateTime.nanosecond
        )
    }

    fun setMonth(month: Int) {
      dateTime =
        LocalDateTime(
          dateTime.year,
          month,
          dateTime.dayOfMonth,
          dateTime.hour,
          dateTime.minute,
          dateTime.second,
          dateTime.nanosecond
        )
    }

    fun setSeconds(seconds: Int) {
      dateTime =
        LocalDateTime(
          dateTime.year,
          dateTime.month,
          dateTime.dayOfMonth,
          dateTime.hour,
          dateTime.minute,
          seconds,
          dateTime.nanosecond
        )
    }

    fun setTime(milliseconds: Long) {
      dateTime = Instant.fromEpochMilliseconds(milliseconds).toLocalDateTime(timeZone)
    }

    fun toLocaleString(): String = dateTime.toString()

    companion object {
      fun parse(dateString: String): Long =
        LocalDateTime.parse(dateString)
          .toInstant(TimeZone.currentSystemDefault())
          .toEpochMilliseconds()

      fun UTC(
        year: Int,
        month: Int,
        dayOfMonth: Int,
        hours: Int,
        minutes: Int,
        seconds: Int,
        millis: Int
      ): NativeDate =
        NativeDate(
          LocalDateTime(year, month, dayOfMonth, hours, minutes, seconds, millis * 1_000_000),
          TimeZone.UTC
        )
    }
  }
}
