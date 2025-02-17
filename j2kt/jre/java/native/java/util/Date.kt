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
import kotlin.native.ObjCName
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.asTimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.number
import kotlinx.datetime.offsetAt
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import platform.Foundation.NSTimeZone
import platform.Foundation.resetSystemTimeZone
import platform.Foundation.secondsFromGMT
import platform.Foundation.systemTimeZone

@ObjCName("J2ktJavaUtilDate", exact = true)
open class Date private constructor(private var instant: Instant, private var timeZone: TimeZone) :
  Cloneable, Comparable<Date>, Serializable {
  constructor() : this(Clock.System.now(), currentSystemDefaultTimeZone())

  constructor(
    @ObjCName("Int") year: Int,
    @ObjCName("withInt") month: Int,
    @ObjCName("withInt") date: Int,
  ) : this(year, month, date, 0, 0, 0)

  constructor(
    @ObjCName("Int") year: Int,
    @ObjCName("withInt") month: Int,
    @ObjCName("withInt") date: Int,
    @ObjCName("withInt") hrs: Int,
    @ObjCName("withInt") min: Int,
  ) : this(year, month, date, hrs, min, 0)

  // Clamping the month and day to match legacy calendar behaviour.
  constructor(
    @ObjCName("Int") year: Int,
    @ObjCName("withInt") month: Int,
    @ObjCName("withInt") date: Int,
    @ObjCName("withInt") hrs: Int,
    @ObjCName("withInt") min: Int,
    @ObjCName("withInt") sec: Int,
  ) : this(
    createInstant(
      year + 1900,
      (month + 1).coerceIn(1, 12),
      if (date < 1) 1 else date,
      hrs,
      min,
      sec,
      0,
      currentSystemDefaultTimeZone(),
    ),
    currentSystemDefaultTimeZone(),
  )

  constructor(
    @ObjCName("Long") date: Long
  ) : this(Instant.fromEpochMilliseconds(date), currentSystemDefaultTimeZone())

  constructor(@ObjCName("NSString") date: String) : this(Date.parse(date))

  @ObjCName("after")
  open fun after(@ObjCName("withJavaUtilDate") `when`: Date): Boolean {
    return getTime() > `when`.getTime()
  }

  @ObjCName("before")
  open fun before(@ObjCName("withJavaUtilDate") `when`: Date): Boolean {
    return getTime() < `when`.getTime()
  }

  open override fun clone(): Any = Date(instant, timeZone)

  open override fun compareTo(other: Date): Int = getTime().compareTo(other.getTime())

  open override fun equals(obj: Any?) = obj is Date && getTime() == obj.getTime()

  @ObjCName("getDate") open fun getDate(): Int = toLocalDateTime().dayOfMonth

  @ObjCName("getDay") open fun getDay(): Int = toLocalDateTime().dayOfWeek.isoDayNumber

  @ObjCName("getHours") open fun getHours(): Int = toLocalDateTime().hour

  @ObjCName("getMinutes") open fun getMinutes(): Int = toLocalDateTime().minute

  @ObjCName("getMonth") open fun getMonth(): Int = toLocalDateTime().month.number - 1

  @ObjCName("getSeconds") open fun getSeconds(): Int = toLocalDateTime().second

  @ObjCName("getTime") open fun getTime(): Long = instant.toEpochMilliseconds()

  @ObjCName("getTimezoneOffset")
  open fun getTimezoneOffset(): Int = -timeZone.offsetAt(instant).totalSeconds / 60

  @ObjCName("getYear")
  open fun getYear(): Int {
    return toLocalDateTime().year - 1900
  }

  open override fun hashCode(): Int {
    val time = getTime()
    return time.xor(time.ushr(32)).toInt()
  }

  @ObjCName("setDate")
  open fun setDate(@ObjCName("withInt") date: Int) {
    val localDateTime = toLocalDateTime()
    val hours = localDateTime.hour
    instant =
      createInstant(
        localDateTime.year,
        localDateTime.month.number,
        date,
        localDateTime.hour,
        localDateTime.minute,
        localDateTime.second,
        localDateTime.nanosecond,
        timeZone,
      )
  }

  @ObjCName("setHours")
  open fun setHours(@ObjCName("withInt") hours: Int) {
    val localDateTime = toLocalDateTime()
    instant =
      createInstant(
        localDateTime.year,
        localDateTime.month.number,
        localDateTime.dayOfMonth,
        hours,
        localDateTime.minute,
        localDateTime.second,
        localDateTime.nanosecond,
        timeZone,
      )
  }

  @ObjCName("setMinutes")
  open fun setMinutes(@ObjCName("withInt") minutes: Int) {
    val localDateTime = toLocalDateTime()
    val hours: Int = localDateTime.hour + minutes / 60
    instant =
      createInstant(
        localDateTime.year,
        localDateTime.month.number,
        localDateTime.dayOfMonth,
        localDateTime.hour,
        minutes,
        localDateTime.second,
        localDateTime.nanosecond,
        timeZone,
      )
  }

  @ObjCName("setMonth")
  open fun setMonth(@ObjCName("withInt") month: Int) {
    val hours: Int = getHours()
    val localDateTime = toLocalDateTime()
    instant =
      createInstant(
        localDateTime.year,
        month + 1,
        localDateTime.dayOfMonth,
        localDateTime.hour,
        localDateTime.minute,
        localDateTime.second,
        localDateTime.nanosecond,
        timeZone,
      )
  }

  @ObjCName("setSeconds")
  open fun setSeconds(@ObjCName("withInt") seconds: Int) {
    val localDateTime = toLocalDateTime()
    val hours: Int = localDateTime.hour + seconds / (60 * 60)
    instant =
      createInstant(
        localDateTime.year,
        localDateTime.month.number,
        localDateTime.dayOfMonth,
        localDateTime.hour,
        localDateTime.minute,
        seconds,
        localDateTime.nanosecond,
        timeZone,
      )
  }

  @ObjCName("setTime")
  open fun setTime(@ObjCName("withLong") time: Long) {
    instant = Instant.fromEpochMilliseconds(time)
  }

  @ObjCName("setYear")
  open fun setYear(@ObjCName("withInt") year: Int) {
    val localDateTime = toLocalDateTime()
    val hours: Int = localDateTime.hour
    instant =
      createInstant(
        year + 1900,
        localDateTime.month.number,
        localDateTime.dayOfMonth,
        localDateTime.hour,
        localDateTime.minute,
        localDateTime.second,
        localDateTime.nanosecond,
        timeZone,
      )
  }

  @ObjCName("toGMTString")
  open fun toGMTString(): String {
    val utcDate = instant.toLocalDateTime(TimeZone.UTC)
    return "" +
      utcDate.dayOfMonth +
      " " +
      Date.MONTHS[utcDate.month.number - 1] +
      " " +
      utcDate.year +
      " " +
      Date.padTwo(utcDate.hour) +
      ":" +
      Date.padTwo(utcDate.minute) +
      ":" +
      Date.padTwo(utcDate.second) +
      " GMT"
  }

  @ObjCName("toLocaleString") open fun toLocaleString(): String = toString()

  open override fun toString(): String {
    // If we can't parse the zome back, use UTC. This makes sure we can always round-trip
    // dates through toString() and parse(), as parse() may get confused if a time zone offset
    // precedes the year.
    val useUtc = zone(timeZone.id) == 0
    val tz = if (useUtc) TimeZone.UTC else timeZone
    val localDateTime = instant.toLocalDateTime(tz)
    return DAYS[localDateTime.dayOfWeek.isoDayNumber - 1] +
      " " +
      MONTHS[localDateTime.month.number - 1] +
      " " +
      Date.padTwo(localDateTime.dayOfMonth) +
      " " +
      Date.padTwo(localDateTime.hour) +
      ":" +
      Date.padTwo(localDateTime.minute) +
      ":" +
      Date.padTwo(localDateTime.second) +
      " " +
      (if (useUtc) "UTC" else tz.id) + // utc id is "Z" in kotlin
      " " +
      localDateTime.year
  }

  private fun toLocalDateTime() = instant.toLocalDateTime(timeZone)

  companion object {

    // Copied from Android JRE
    @ObjCName("parse")
    fun parse(@ObjCName("withNSString") string: String): Long {
      val creationYear = Date().getYear()
      var sign = '\u0000'
      var commentLevel = 0
      var offset = 0
      var length = string.length
      var state = 0
      var year = -1
      var month = -1
      var date = -1
      var hour = -1
      var minute = -1
      var second = -1
      var zoneOffset = 0
      var minutesOffset = 0
      var zone = false
      val PAD = 0
      val LETTERS = 1
      val NUMBERS = 2
      val buffer = StringBuilder()

      while (offset <= length) {
        var next = if (offset < length) string[offset] else '\r'
        offset++

        if (next == '(') {
          commentLevel++
        }
        if (commentLevel > 0) {
          if (next == ')') {
            commentLevel--
          }
          if (commentLevel == 0) {
            next = ' '
          } else {
            continue
          }
        }

        var nextState = PAD
        if ('a' <= next && next <= 'z' || 'A' <= next && next <= 'Z') {
          nextState = LETTERS
        } else if ('0' <= next && next <= '9') {
          nextState = NUMBERS
        } else if (!next.isWhitespace() && ",+-:/".indexOf(next) == -1) {
          throw parseError("a:" + string)
        }

        if (state == NUMBERS && nextState != NUMBERS) {
          var digit = buffer.toString().toInt()
          buffer.setLength(0)
          if (sign == '+' || sign == '-') {
            if (zoneOffset == 0) {
              zone = true
              if (next == ':') {
                minutesOffset =
                  if (sign == '-') -string.substring(offset, offset + 2).toInt()
                  else string.substring(offset, offset + 2).toInt()
                offset += 2
              }
              zoneOffset = if (sign == '-') -digit else digit
              sign = '\u0000'
            } else {
              throw parseError("a:" + string)
            }
          } else if (digit >= 70) {
            if (year == -1 && (next.isWhitespace() || next == ',' || next == '/' || next == '\r')) {
              year = digit
            } else {
              throw parseError("digit:$digit; next:${next.toInt()}, year:$year:" + string)
            }
          } else if (next == ':') {
            if (hour == -1) {
              hour = digit
            } else if (minute == -1) {
              minute = digit
            } else {
              throw parseError("c:" + string)
            }
          } else if (next == '/') {
            if (month == -1) {
              month = digit - 1
            } else if (date == -1) {
              date = digit
            } else {
              throw parseError("d:" + string)
            }
          } else if (next.isWhitespace() || next == ',' || next == '-' || next == '\r') {
            if (hour != -1 && minute == -1) {
              minute = digit
            } else if (minute != -1 && second == -1) {
              second = digit
            } else if (date == -1) {
              date = digit
            } else if (year == -1) {
              year = digit
            } else {
              throw parseError("e:" + string)
            }
          } else if (year == -1 && month != -1 && date != -1) {
            year = digit
          } else {
            throw parseError("f:" + string)
          }
        } else if (state == LETTERS && nextState != LETTERS) {
          var text = buffer.toString().uppercase()
          buffer.setLength(0)
          if (text.length == 1) {
            throw parseError("g:" + string)
          }
          if (text.equals("AM")) {
            if (hour == 12) {
              hour = 0
            } else if (hour < 1 || hour > 12) {
              throw parseError("h:" + string)
            }
          } else if (text.equals("PM")) {
            if (hour == 12) {
              hour = 0
            } else if (hour < 1 || hour > 12) {
              throw parseError("i:" + string)
            }
            hour += 12
          } else {
            var value: Int
            if (parse(text, DAYS) != -1) {
              /* empty */
            } else if (month == -1 && parse(text, MONTHS) != -1) {
              month = parse(text, MONTHS)
            } else if (text.equals("GMT") || text.equals("UT") || text.equals("UTC")) {
              zone = true
              zoneOffset = 0
            } else if ((zone(text)) != 0) {
              value = zone(text)
              zone = true
              zoneOffset = value
            } else {
              throw parseError("j:" + string)
            }
          }
        }

        if (next == '+' || (year != -1 && next == '-')) {
          sign = next
        } else if (!next.isWhitespace() && next != ',' && nextState != NUMBERS) {
          sign = '\u0000'
        }

        if (nextState == LETTERS || nextState == NUMBERS) {
          buffer.append(next)
        }
        state = nextState
      }

      if (year != -1 && month != -1 && date != -1) {
        if (hour == -1) {
          hour = 0
        }
        if (minute == -1) {
          minute = 0
        }
        if (second == -1) {
          second = 0
        }
        if (year < (creationYear - 80)) {
          year += 2000
        } else if (year < 100) {
          year += 1900
        }
        minute -= minutesOffset
        if (zone) {
          if (zoneOffset >= 24 || zoneOffset <= -24) {
            hour -= zoneOffset / 100
            minute -= zoneOffset % 100
          } else {
            hour -= zoneOffset
          }
          return UTC(year - 1900, month, date, hour, minute, second)
        }
        return Date(year - 1900, month, date, hour, minute, second).getTime()
      }
      throw parseError("k:" + string)
    }

    private fun parse(string: String, array: Array<String>): Int {
      for (i in array.indices) {
        if (string.regionMatches(0, array[i], 0, array[i].length, true)) {
          return i
        }
      }
      return -1
    }

    private fun parseError(string: String): IllegalArgumentException {
      throw IllegalArgumentException("Parse error: $string")
    }

    private fun zone(text: String) =
      when (text) {
        "EST" -> -5
        "EDT" -> -4
        "CST" -> -6
        "CDT" -> -5
        "MST" -> -7
        "MDT" -> -6
        "PST" -> -8
        "PDT" -> -7
        else -> 0
      }

    fun clamp(n: Int, min: Int, max: Int) = if (n < min) min else if (n > max) max else n

    @ObjCName("UTC")
    fun UTC(
      @ObjCName("withInt") year: Int,
      @ObjCName("withInt") month: Int,
      @ObjCName("withInt") date: Int,
      @ObjCName("withInt") hrs: Int,
      @ObjCName("withInt") min: Int,
      @ObjCName("withInt") sec: Int,
    ) =
      Date(
          createInstant(year + 1900, month + 1, date, hrs, min, sec, 0, TimeZone.UTC),
          TimeZone.UTC,
        )
        .getTime()

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
        "Dec",
      )

    private fun twoDigits(n: Long) = if (n < 10) "0$n" else n

    private fun currentSystemDefaultTimeZone(): TimeZone {
      try {
        return TimeZone.currentSystemDefault()
      } catch (e: Exception) {
        // TODO(b/396634033): Remove this workaround. It will fail for calculating time differences
        //   accross DST boundaries. We are going ahead with this for now to avoid crashes in prod
        //   without a significant refactoring until kotlinx.datetime is fixed -- under the
        //   assumption that this class is only really used for time stamps.
        NSTimeZone.resetSystemTimeZone() // Avoid stale values
        val utcOffset = UtcOffset(seconds = NSTimeZone.systemTimeZone.secondsFromGMT.toInt())
        return utcOffset.asTimeZone()
      }
    }

    /**
     * Sets the date to the given date, overflowing all values as needed into the next higher unit.
     */
    private fun createInstant(
      year: Int,
      month: Int,
      date: Int,
      hrs: Int,
      min: Int,
      sec: Int,
      nanosecond: Int = 0,
      timeZone: TimeZone = currentSystemDefaultTimeZone(),
    ): Instant {
      val normalizedNanosecond = nanosecond.mod(1_000_000_000)

      val secondsWithCarry = sec + normalizedNanosecond / 1_000_000_000
      val normalizedSecond = secondsWithCarry.mod(60)

      val minutesWithCarry = min + secondsWithCarry / 60
      val normalizedMinute = minutesWithCarry.mod(60)

      val hoursWithCarry = hrs + minutesWithCarry / 60
      val normalizedHour = hoursWithCarry.mod(24)

      val normalizedDays = date - 1 + hoursWithCarry / 24

      val localDateTime =
        LocalDateTime(
          LocalDate(year, 1, 1) + DatePeriod(months = month - 1, days = normalizedDays),
          LocalTime(normalizedHour, normalizedMinute, normalizedSecond, normalizedNanosecond),
        )

      val result = localDateTime.toInstant(timeZone)
      val oneHourLater = result + 1.hours
      return if (oneHourLater.toLocalDateTime(timeZone) == localDateTime) oneHourLater else result
    }
  }
}
