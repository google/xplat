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
import java.lang.IllegalArgumentException
import java.lang.Math
import javaemul.lang.*
import kotlin.Any
import kotlin.Array
import kotlin.Boolean
import kotlin.Cloneable
import kotlin.Comparable
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.arrayOf
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@ObjCName("J2ktJavaUtilDate", exact = true)
open class Date : Cloneable, Comparable<Date>, Serializable {
  private val nativeDate: Date.NativeDate

  constructor() {
    nativeDate = Date.NativeDate()
  }

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
  ) {
    nativeDate = Date.NativeDate()
    nativeDate.setFullYear(year + 1900, month, date)
    nativeDate.setHours(hrs, min, sec, 0)
    fixDaylightSavings(hrs)
  }

  constructor(@ObjCName("Long") date: Long) {
    nativeDate = Date.NativeDate(date.toDouble())
  }

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
    return nativeDate.getTime().toLong()
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
    nativeDate.setTime(time.toDouble())
  }

  @ObjCName("setYear")
  open fun setYear(@ObjCName("withInt") year: Int) {
    val hours: Int = nativeDate.getHours()
    nativeDate.setFullYear(year + 1900)
    fixDaylightSavings(hours)
  }

  @ObjCName("toGMTString")
  open fun toGMTString(): String {
    return "" +
      nativeDate.getUTCDate() +
      " " +
      Date.StringData.MONTHS[nativeDate.getUTCMonth()] +
      " " +
      nativeDate.getUTCFullYear() +
      " " +
      Date.padTwo(nativeDate.getUTCHours()) +
      ":" +
      Date.padTwo(nativeDate.getUTCMinutes()) +
      ":" +
      Date.padTwo(nativeDate.getUTCSeconds()) +
      " GMT"
  }

  @ObjCName("toLocaleString")
  open fun toLocaleString(): String {
    return nativeDate.toLocaleString()
  }

  open override fun toString(): String {
    val offset: Int = -nativeDate.getTimezoneOffset()
    val hourOffset: String = (if (offset >= 0) "+" else "") + offset / 60
    val minuteOffset: String = Date.padTwo(Math.abs(offset) % 60)
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
    val originalTimeInMillis: Double = nativeDate.getTime()
    nativeDate.setTime(originalTimeInMillis + Date.ONE_HOUR_IN_MILLISECONDS.toDouble())
    if (nativeDate.getHours() != requestedHoursFixed) {
      nativeDate.setTime(originalTimeInMillis)
    }
  }

  companion object {
    @ObjCName("parse")
    fun parse(@ObjCName("withNSString") s: String): Long {
      val parsed: Double = Date.NativeDate.parse(s)
      if (Double.isNaN(parsed)) {
        throw IllegalArgumentException()
      }
      return parsed.toLong()
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
      return Date.NativeDate.UTC(year + 1900, month, date, hrs, min, sec, 0).toLong()
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

  // TODO(b/270199989): Implement these methods using kotlinx datetime.
  private class NativeDate {
    constructor() {}

    constructor(@ObjCName("Double") milliseconds: Double) {}

    constructor(
      @ObjCName("Int") year: Int,
      @ObjCName("withInt") month: Int,
      @ObjCName("withInt") dayOfMonth: Int,
      @ObjCName("withInt") hours: Int,
      @ObjCName("withInt") minutes: Int,
      @ObjCName("withInt") seconds: Int,
      @ObjCName("withInt") millis: Int
    ) {}

    @ObjCName("getDate") fun getDate(): Int = 0

    @ObjCName("getDay") fun getDay(): Int = 0

    @ObjCName("getFullYear") fun getFullYear(): Int = 0

    @ObjCName("getHours") fun getHours(): Int = 0

    @ObjCName("getMilliseconds") fun getMilliseconds(): Int = 0

    @ObjCName("getMinutes") fun getMinutes(): Int = 0

    @ObjCName("getMonth") fun getMonth(): Int = 0

    @ObjCName("getSeconds") fun getSeconds(): Int = 0

    @ObjCName("getTime") fun getTime(): Double = 0.0

    @ObjCName("getTimezoneOffset") fun getTimezoneOffset(): Int = 0

    @ObjCName("getUTCDate") fun getUTCDate(): Int = 0

    @ObjCName("getUTCFullYear") fun getUTCFullYear(): Int = 0

    @ObjCName("getUTCHours") fun getUTCHours(): Int = 0

    @ObjCName("getUTCMinutes") fun getUTCMinutes(): Int = 0

    @ObjCName("getUTCMonth") fun getUTCMonth(): Int = 0

    @ObjCName("getUTCSeconds") fun getUTCSeconds(): Int = 0

    @ObjCName("setDate") fun setDate(@ObjCName("withInt") dayOfMonth: Int) {}

    @ObjCName("setFullYear") fun setFullYear(@ObjCName("withInt") year: Int) {}

    @ObjCName("setFullYear")
    fun setFullYear(
      @ObjCName("withInt") year: Int,
      @ObjCName("withInt") month: Int,
      @ObjCName("withInt") day: Int
    ) {}

    @ObjCName("setHours") fun setHours(@ObjCName("withInt") hours: Int) {}

    @ObjCName("setHours")
    fun setHours(
      @ObjCName("withInt") hours: Int,
      @ObjCName("withInt") mins: Int,
      @ObjCName("withInt") secs: Int,
      @ObjCName("withInt") ms: Int
    ) {}

    @ObjCName("setMinutes") fun setMinutes(@ObjCName("withInt") minutes: Int) {}

    @ObjCName("setMonth") fun setMonth(@ObjCName("withInt") month: Int) {}

    @ObjCName("setSeconds") fun setSeconds(@ObjCName("withInt") seconds: Int) {}

    @ObjCName("setTime") fun setTime(@ObjCName("withDouble") milliseconds: Double) {}

    @ObjCName("toLocaleString") fun toLocaleString(): String = ""

    companion object {
      @ObjCName("UTC")
      fun UTC(
        @ObjCName("withInt") year: Int,
        @ObjCName("withInt") month: Int,
        @ObjCName("withInt") dayOfMonth: Int,
        @ObjCName("withInt") hours: Int,
        @ObjCName("withInt") minutes: Int,
        @ObjCName("withInt") seconds: Int,
        @ObjCName("withInt") millis: Int
      ): Double = 0.0

      @ObjCName("parse") fun parse(@ObjCName("withNSString") dateString: String): Double = 0.0
    }
  }
}
