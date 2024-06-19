/* Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// This file is adapted from libcore/luni/src/main/java/java/util/Formatter.java in Android Open
// Source Project
//
// Differences:
// 1. @Nullable annotations are added.
// 2. References to `DecimalFormat` related classes are removed. For floating points, the formatting
//    will be slow and we only support 'f' conversion type.
// 3. Some IO and file related API are removed.
// 4. Calendar support removed. Time and date format specifiers might throw exceptions
// 5. Things will be formatted in en_US convention regardless of what Locale is specified.
// 6. `java.lang.IntegralToString` is only available in AOSP. `IntegralToString` inner class is
// added, which simply use `toString` to convert it here (which might be slower)
// 7. References to `Character.isDigit(int)` are replaced with `isAsciiDigit` helper function.
// 8. Manually implement `isValidCodePoint`. There is no counterpart in Kotlin anyway.
// 9. @Override annotations are added as needed.

package java.util;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/** Formats arguments according to a format string (like {@code printf} in C). */
@NullMarked
public final class Formatter implements Closeable, Flushable {
  /** The enumeration giving the available styles for formatting very large decimal numbers. */
  public enum BigDecimalLayoutForm {
    /** Use scientific style for BigDecimals. */
    SCIENTIFIC,
    /** Use normal decimal/float style for BigDecimals. */
    DECIMAL_FLOAT
  }

  // User-settable parameters.
  private @Nullable Appendable out;
  private @Nullable Locale locale;

  // Implementation details.
  private @Nullable Object arg;
  private boolean closed = false;
  private @Nullable FormatToken formatToken;
  private @Nullable IOException lastIOException;

  // Mock out LocaleData for now.
  // TODO(b/259213718): Connect to platform-specific APIs.
  static class LocaleData {
    private LocaleData() {}

    public static char zeroDigit = '0';
    public static char groupingSeparator = ',';
    public static String minusSign = "-";
    public static char decimalSeparator = '.';
  }

  // AOSP implemented this class for converting integral types to string for performance reason.
  // Here we simply use {@code toString} to convert it since.
  static class IntegralToString {
    private IntegralToString() {}

    public static void appendInt(StringBuilder sb, int i) {
      sb.append(i);
    }

    public static void appendLong(StringBuilder sb, long l) {
      sb.append(l);
    }
  }

  /**
   * Constructs a {@code Formatter}.
   *
   * <p>The output is written to a {@code StringBuilder} which can be acquired by invoking {@link
   * #out()} and whose content can be obtained by calling {@code toString}.
   *
   * <p>The {@code Locale} used is the user's default locale. See "<a
   * href="../util/Locale.html#default_locale">Be wary of the default locale</a>".
   */
  public Formatter() {
    this(new StringBuilder(), Locale.getDefault());
  }

  /**
   * Constructs a {@code Formatter} whose output will be written to the specified {@code
   * Appendable}.
   *
   * <p>The {@code Locale} used is the user's default locale. See "<a
   * href="../util/Locale.html#default_locale">Be wary of the default locale</a>".
   *
   * @param appendable the output destination of the {@code Formatter}. If {@code a} is {@code
   *     null}, then a {@code StringBuilder} will be used.
   */
  public Formatter(@Nullable Appendable appendable) {
    this(appendable, Locale.getDefault());
  }

  /**
   * Constructs a {@code Formatter} with the specified {@code Locale}.
   *
   * <p>The output is written to a {@code StringBuilder} which can be acquired by invoking {@link
   * #out()} and whose content can be obtained by calling {@code toString}.
   *
   * @param locale the {@code Locale} of the {@code Formatter}. If {@code l} is {@code null}, then
   *     no localization will be used.
   */
  public Formatter(@Nullable Locale locale) {
    this(new StringBuilder(), locale);
  }

  /**
   * Constructs a {@code Formatter} with the specified {@code Locale} and whose output will be
   * written to the specified {@code Appendable}.
   *
   * @param appendable the output destination of the {@code Formatter}. If {@code a} is {@code
   *     null}, then a {@code StringBuilder} will be used.
   * @param locale the {@code Locale} of the {@code Formatter}. If {@code l} is {@code null}, then
   *     no localization will be used.
   */
  public Formatter(@Nullable Appendable appendable, @Nullable Locale locale) {
    if (appendable == null) {
      out = new StringBuilder();
    } else {
      out = appendable;
    }
    this.locale = locale;
  }

  /**
   * Constructs a {@code Formatter} whose output is written to the specified {@code OutputStream}.
   *
   * <p>The charset of the {@code Formatter} is the default charset.
   *
   * <p>The {@code Locale} used is the user's default locale. See "<a
   * href="../util/Locale.html#default_locale">Be wary of the default locale</a>".
   *
   * @param os the stream to be used as the destination of the {@code Formatter}.
   */
  public Formatter(OutputStream os) {
    this(os, Charset.defaultCharset(), Locale.getDefault());
  }

  /**
   * Constructs a {@code Formatter} with the given charset, and whose output is written to the
   * specified {@code OutputStream}.
   *
   * <p>The {@code Locale} used is the user's default locale. See "<a
   * href="../util/Locale.html#default_locale">Be wary of the default locale</a>".
   *
   * @param os the stream to be used as the destination of the {@code Formatter}.
   * @param csn the name of the charset for the {@code Formatter}.
   * @throws UnsupportedEncodingException if the charset with the specified name is not supported.
   */
  public Formatter(OutputStream os, String csn) throws UnsupportedEncodingException {
    this(os, Charset.forName(csn), Locale.getDefault());
  }

  /**
   * Constructs a {@code Formatter} with the given {@code Locale} and charset, and whose output is
   * written to the specified {@code OutputStream}.
   *
   * @param os the stream to be used as the destination of the {@code Formatter}.
   * @param csn the name of the charset for the {@code Formatter}.
   * @param locale the {@code Locale} of the {@code Formatter}. If {@code l} is {@code null}, then
   *     no localization will be used.
   * @throws UnsupportedEncodingException if the charset with the specified name is not supported.
   */
  public Formatter(OutputStream os, String csn, @Nullable Locale locale)
      throws UnsupportedEncodingException {
    this(os, Charset.forName(csn), locale);
  }

  private Formatter(OutputStream os, Charset cs, @Nullable Locale locale) {
    this(new BufferedWriter(new OutputStreamWriter(os, cs)), locale);
  }

  /**
   * Constructs a {@code Formatter} whose output is written to the specified {@code PrintStream}.
   *
   * <p>The charset of the {@code Formatter} is the default charset.
   *
   * <p>The {@code Locale} used is the user's default locale. See "<a
   * href="../util/Locale.html#default_locale">Be wary of the default locale</a>".
   *
   * @param ps the {@code PrintStream} used as destination of the {@code Formatter}. If {@code ps}
   *     is {@code null}, then a {@code NullPointerException} will be raised.
   */
  public Formatter(PrintStream ps) {
    this(ps, Locale.getDefault());
  }

  private void checkNotClosed() {
    if (closed) {
      throw new FormatterClosedException();
    }
  }

  /**
   * Returns the {@code Locale} of the {@code Formatter}.
   *
   * @return the {@code Locale} for the {@code Formatter} or {@code null} for no {@code Locale}.
   * @throws FormatterClosedException if the {@code Formatter} has been closed.
   */
  public @Nullable Locale locale() {
    checkNotClosed();
    return locale;
  }

  /**
   * Returns the output destination of the {@code Formatter}.
   *
   * @return the output destination of the {@code Formatter}.
   * @throws FormatterClosedException if the {@code Formatter} has been closed.
   */
  public Appendable out() {
    checkNotClosed();
    return out;
  }

  /**
   * Returns the content by calling the {@code toString()} method of the output destination.
   *
   * @return the content by calling the {@code toString()} method of the output destination.
   * @throws FormatterClosedException if the {@code Formatter} has been closed.
   */
  @Override
  public String toString() {
    checkNotClosed();
    return out.toString();
  }

  /**
   * Flushes the {@code Formatter}. If the output destination is {@link Flushable}, then the method
   * {@code flush()} will be called on that destination.
   *
   * @throws FormatterClosedException if the {@code Formatter} has been closed.
   */
  @Override
  public void flush() {
    checkNotClosed();
    if (out instanceof Flushable) {
      try {
        ((Flushable) out).flush();
      } catch (IOException e) {
        lastIOException = e;
      }
    }
  }

  /**
   * Closes the {@code Formatter}. If the output destination is {@link Closeable}, then the method
   * {@code close()} will be called on that destination.
   *
   * <p>If the {@code Formatter} has been closed, then calling the this method will have no effect.
   *
   * <p>Any method but the {@link #ioException()} that is called after the {@code Formatter} has
   * been closed will raise a {@code FormatterClosedException}.
   */
  @Override
  public void close() {
    if (!closed) {
      closed = true;
      try {
        if (out instanceof Closeable) {
          ((Closeable) out).close();
        }
      } catch (IOException e) {
        lastIOException = e;
      }
    }
  }

  /**
   * Returns the last {@code IOException} thrown by the {@code Formatter}'s output destination. If
   * the {@code append()} method of the destination does not throw {@code IOException}s, the {@code
   * ioException()} method will always return {@code null}.
   *
   * @return the last {@code IOException} thrown by the {@code Formatter}'s output destination.
   */
  public @Nullable IOException ioException() {
    return lastIOException;
  }

  /**
   * Writes a formatted string to the output destination of the {@code Formatter}.
   *
   * @param l the {@code Locale} used in the method. If {@code locale} is {@code null}, then no
   *     localization will be applied. This parameter does not change this Formatter's default
   *     {@code Locale} as specified during construction, and only applies for the duration of this
   *     call.
   * @param format a format string.
   * @param args the arguments list used in the {@code format()} method. If there are more arguments
   *     than those specified by the format string, then the additional arguments are ignored.
   * @return this {@code Formatter}.
   * @throws IllegalFormatException if the format string is illegal or incompatible with the
   *     arguments, or if fewer arguments are sent than those required by the format string, or any
   *     other illegal situation.
   * @throws FormatterClosedException if the {@code Formatter} has been closed.
   */
  public Formatter format(@Nullable Locale l, String format, @Nullable Object... args) {
    Locale originalLocale = locale;
    try {
      this.locale = (l == null ? Locale.US : l);
      // TODO(b/259213718): Support Locale properly.
      // this.localeData = LocaleData.get(locale);
      doFormat(format, args);
    } finally {
      this.locale = originalLocale;
    }
    return this;
  }

  /**
   * Writes a formatted string to the output destination of the {@code Formatter}.
   *
   * @param format a format string.
   * @param args the arguments list used in the {@code format()} method. If there are more arguments
   *     than those specified by the format string, then the additional arguments are ignored.
   * @return this {@code Formatter}.
   * @throws IllegalFormatException if the format string is illegal or incompatible with the
   *     arguments, or if fewer arguments are sent than those required by the format string, or any
   *     other illegal situation.
   * @throws FormatterClosedException if the {@code Formatter} has been closed.
   */
  public Formatter format(String format, @Nullable Object... args) {
    doFormat(format, args);
    return this;
  }

  private void doFormat(String format, @Nullable Object... args) {
    checkNotClosed();

    FormatSpecifierParser fsp = new FormatSpecifierParser(format);
    int currentObjectIndex = 0;
    Object lastArgument = null;
    boolean hasLastArgumentSet = false;

    int length = format.length();
    int i = 0;
    while (i < length) {
      // Find the maximal plain-text sequence...
      int plainTextStart = i;
      int nextPercent = format.indexOf('%', i);
      int plainTextEnd = (nextPercent == -1) ? length : nextPercent;
      // ...and output it.
      if (plainTextEnd > plainTextStart) {
        outputCharSequence(format, plainTextStart, plainTextEnd);
      }
      i = plainTextEnd;
      // Do we have a format specifier?
      if (i < length) {
        FormatToken token = fsp.parseFormatToken(i + 1);

        Object argument = null;
        if (token.requireArgument()) {
          int index =
              token.getArgIndex() == FormatToken.UNSET ? currentObjectIndex++ : token.getArgIndex();
          argument = getArgument(args, index, fsp, lastArgument, hasLastArgumentSet);
          lastArgument = argument;
          hasLastArgumentSet = true;
        }

        CharSequence substitution = transform(token, argument);
        // The substitution is null if we called Formattable.formatTo.
        if (substitution != null) {
          outputCharSequence(substitution, 0, substitution.length());
        }
        i = fsp.i;
      }
    }
  }

  // Fixes http://code.google.com/p/android/issues/detail?id=1767.
  private void outputCharSequence(CharSequence cs, int start, int end) {
    try {
      out.append(cs, start, end);
    } catch (IOException e) {
      lastIOException = e;
    }
  }

  private @Nullable Object getArgument(
      @Nullable Object[] args,
      int index,
      FormatSpecifierParser fsp,
      @Nullable Object lastArgument,
      boolean hasLastArgumentSet) {
    if (index == FormatToken.LAST_ARGUMENT_INDEX && !hasLastArgumentSet) {
      throw new MissingFormatArgumentException("<");
    }

    if (args == null) {
      return null;
    }

    if (index >= args.length) {
      throw new MissingFormatArgumentException(fsp.getFormatSpecifierText());
    }

    if (index == FormatToken.LAST_ARGUMENT_INDEX) {
      return lastArgument;
    }

    return args[index];
  }

  /*
   * Complete details of a single format specifier parsed from a format string.
   */
  private static class FormatToken {
    static final int LAST_ARGUMENT_INDEX = -2;

    static final int UNSET = -1;

    static final int DEFAULT_PRECISION = 6;

    private int argIndex = UNSET;

    // These have package access for performance. They used to be represented by an int bitmask
    // and accessed via methods, but Android's JIT doesn't yet do a good job of such code.
    // Direct field access, on the other hand, is fast.
    boolean flagComma;
    boolean flagMinus;
    boolean flagParenthesis;
    boolean flagPlus;
    boolean flagSharp;
    boolean flagSpace;
    boolean flagZero;

    private char conversionType = (char) UNSET;
    private char dateSuffix;

    private int precision = UNSET;
    private int width = UNSET;

    private @Nullable StringBuilder strFlags;

    // Tests whether there were no flags, no width, and no precision specified.
    boolean isDefault() {
      return !flagComma
          && !flagMinus
          && !flagParenthesis
          && !flagPlus
          && !flagSharp
          && !flagSpace
          && !flagZero
          && width == UNSET
          && precision == UNSET;
    }

    boolean isPrecisionSet() {
      return precision != UNSET;
    }

    int getArgIndex() {
      return argIndex;
    }

    void setArgIndex(int index) {
      argIndex = index;
    }

    int getWidth() {
      return width;
    }

    void setWidth(int width) {
      this.width = width;
    }

    int getPrecision() {
      return precision;
    }

    void setPrecision(int precise) {
      this.precision = precise;
    }

    /*
     * Sets qualified char as one of the flags. If the char is qualified,
     * sets it as a flag and returns true. Or else returns false.
     */
    boolean setFlag(int ch) {
      boolean dupe = false;
      switch (ch) {
        case ',':
          dupe = flagComma;
          flagComma = true;
          break;
        case '-':
          dupe = flagMinus;
          flagMinus = true;
          break;
        case '(':
          dupe = flagParenthesis;
          flagParenthesis = true;
          break;
        case '+':
          dupe = flagPlus;
          flagPlus = true;
          break;
        case '#':
          dupe = flagSharp;
          flagSharp = true;
          break;
        case ' ':
          dupe = flagSpace;
          flagSpace = true;
          break;
        case '0':
          dupe = flagZero;
          flagZero = true;
          break;
        default:
          return false;
      }
      if (dupe) {
        // The RI documentation implies we're supposed to report all the flags, not just
        // the first duplicate, but the RI behaves the same as we do.
        throw new DuplicateFormatFlagsException(String.valueOf(ch));
      }
      if (strFlags == null) {
        strFlags = new StringBuilder(7); // There are seven possible flags.
      }
      strFlags.append((char) ch);
      return true;
    }

    char getConversionType() {
      return conversionType;
    }

    void setConversionType(char c) {
      conversionType = c;
    }

    void setDateSuffix(char c) {
      dateSuffix = c;
    }

    boolean requireArgument() {
      return conversionType != '%' && conversionType != 'n';
    }

    void checkFlags(Object arg) {
      // Work out which flags are allowed.
      boolean allowComma = false;
      boolean allowMinus = true;
      boolean allowParenthesis = false;
      boolean allowPlus = false;
      boolean allowSharp = false;
      boolean allowSpace = false;
      boolean allowZero = false;
      // Precision and width?
      boolean allowPrecision = true;
      boolean allowWidth = true;
      // Argument?
      boolean allowArgument = true;
      switch (conversionType) {
          // Character and date/time.
        case 'c':
        case 'C':
        case 't':
        case 'T':
          // Only '-' is allowed.
          allowPrecision = false;
          break;

          // String.
        case 's':
        case 'S':
          if (arg instanceof Formattable) {
            allowSharp = true;
          }
          break;

          // Floating point.
        case 'g':
        case 'G':
          allowComma = allowParenthesis = allowPlus = allowSpace = allowZero = true;
          break;
        case 'f':
          allowComma = allowParenthesis = allowPlus = allowSharp = allowSpace = allowZero = true;
          break;
        case 'e':
        case 'E':
          allowParenthesis = allowPlus = allowSharp = allowSpace = allowZero = true;
          break;
        case 'a':
        case 'A':
          allowPlus = allowSharp = allowSpace = allowZero = true;
          break;

          // Integral.
        case 'd':
          allowComma = allowParenthesis = allowPlus = allowSpace = allowZero = true;
          allowPrecision = false;
          break;
        case 'o':
        case 'x':
        case 'X':
          allowSharp = allowZero = true;
          if (arg == null || arg instanceof BigInteger) {
            allowParenthesis = allowPlus = allowSpace = true;
          }
          allowPrecision = false;
          break;

          // Special.
        case 'n':
          // Nothing is allowed.
          allowMinus = false;
          allowArgument = allowPrecision = allowWidth = false;
          break;
        case '%':
          // The only flag allowed is '-', and no argument or precision is allowed.
          allowArgument = false;
          allowPrecision = false;
          break;

          // Booleans and hash codes.
        case 'b':
        case 'B':
        case 'h':
        case 'H':
          break;

        default:
          throw unknownFormatConversionException();
      }

      // Check for disallowed flags.
      String mismatch = null;
      if (!allowComma && flagComma) {
        mismatch = ",";
      } else if (!allowMinus && flagMinus) {
        mismatch = "-";
      } else if (!allowParenthesis && flagParenthesis) {
        mismatch = "(";
      } else if (!allowPlus && flagPlus) {
        mismatch = "+";
      } else if (!allowSharp && flagSharp) {
        mismatch = "#";
      } else if (!allowSpace && flagSpace) {
        mismatch = " ";
      } else if (!allowZero && flagZero) {
        mismatch = "0";
      }
      if (mismatch != null) {
        if (conversionType == 'n') {
          // For no good reason, %n is a special case...
          throw new IllegalFormatFlagsException(mismatch);
        } else {
          throw new FormatFlagsConversionMismatchException(mismatch, conversionType);
        }
      }

      // Check for a missing width with flags that require a width.
      if ((flagMinus || flagZero) && width == UNSET) {
        throw new MissingFormatWidthException("-" + conversionType);
      }

      // Check that no-argument conversion types don't have an argument.
      // Note: the RI doesn't enforce this.
      if (!allowArgument && argIndex != UNSET) {
        throw new IllegalFormatFlagsException("%" + conversionType + " doesn't take an argument");
      }

      // Check that we don't have a precision or width where they're not allowed.
      if (!allowPrecision && precision != UNSET) {
        throw new IllegalFormatPrecisionException(precision);
      }
      if (!allowWidth && width != UNSET) {
        throw new IllegalFormatWidthException(width);
      }

      // Some combinations make no sense...
      if (flagPlus && flagSpace) {
        throw new IllegalFormatFlagsException("the '+' and ' ' flags are incompatible");
      }
      if (flagMinus && flagZero) {
        throw new IllegalFormatFlagsException("the '-' and '0' flags are incompatible");
      }
    }

    public UnknownFormatConversionException unknownFormatConversionException() {
      if (conversionType == 't' || conversionType == 'T') {
        throw new UnknownFormatConversionException(
            String.format("%c%c", conversionType, dateSuffix));
      }
      throw new UnknownFormatConversionException(String.valueOf(conversionType));
    }
  }

  /*
   * Gets the formatted string according to the format token and the
   * argument.
   */
  private @Nullable CharSequence transform(FormatToken token, @Nullable Object argument) {
    this.formatToken = token;
    this.arg = argument;

    // There are only two format specifiers that matter: "%d" and "%s".
    // Nothing else is common in the wild. We fast-path these two to
    // avoid the heavyweight machinery needed to cope with flags, width,
    // and precision.
    if (token.isDefault()) {
      switch (token.getConversionType()) {
        case 's':
          if (arg == null) {
            return "null";
          } else if (!(arg instanceof Formattable)) {
            return arg.toString();
          }
          break;
        case 'd':
          boolean needLocalizedDigits = (LocaleData.zeroDigit != '0');
          if (out instanceof StringBuilder && !needLocalizedDigits) {
            if (arg instanceof Integer || arg instanceof Short || arg instanceof Byte) {
              IntegralToString.appendInt((StringBuilder) out, ((Number) arg).intValue());
              return null;
            } else if (arg instanceof Long) {
              IntegralToString.appendLong((StringBuilder) out, ((Long) arg).longValue());
              return null;
            }
          }
          if (arg instanceof Integer
              || arg instanceof Long
              || arg instanceof Short
              || arg instanceof Byte) {
            String result = arg.toString();
            return needLocalizedDigits ? localizeDigits(result) : result;
          }
      }
    }

    formatToken.checkFlags(arg);
    CharSequence result;
    switch (token.getConversionType()) {
      case 'B':
      case 'b':
        result = transformFromBoolean();
        break;
      case 'H':
      case 'h':
        result = transformFromHashCode();
        break;
      case 'S':
      case 's':
        result = transformFromString();
        break;
      case 'C':
      case 'c':
        result = transformFromCharacter();
        break;
      case 'd':
      case 'o':
      case 'x':
      case 'X':
        if (arg == null || arg instanceof BigInteger) {
          result = transformFromBigInteger();
        } else {
          result = transformFromInteger();
        }
        break;
      case 'A':
      case 'a':
      case 'E':
      case 'e':
      case 'f':
      case 'G':
      case 'g':
        result = transformFromFloat();
        break;
      case '%':
        result = transformFromPercent();
        break;
      case 'n':
        result = System.lineSeparator();
        break;
      case 't':
      case 'T':
        result = transformFromDateTime();
        break;
      default:
        throw token.unknownFormatConversionException();
    }

    if (Character.isUpperCase(token.getConversionType())) {
      if (result != null) {
        result = result.toString().toUpperCase(locale);
      }
    }
    return result;
  }

  private IllegalFormatConversionException badArgumentType() {
    throw new IllegalFormatConversionException(formatToken.getConversionType(), arg.getClass());
  }

  /**
   * Returns a CharSequence corresponding to {@code s} with all the ASCII digits replaced by digits
   * appropriate to this formatter's locale. Other characters remain unchanged.
   */
  private CharSequence localizeDigits(CharSequence s) {
    int length = s.length();
    int offsetToLocalizedDigits = LocaleData.zeroDigit - '0';
    StringBuilder result = new StringBuilder(length);
    for (int i = 0; i < length; ++i) {
      char ch = s.charAt(i);
      if (ch >= '0' && ch <= '9') {
        ch += offsetToLocalizedDigits;
      }
      result.append(ch);
    }
    return result;
  }

  /**
   * Inserts the grouping separator every 3 digits. DecimalFormat lets you configure grouping size,
   * but you can't access that from Formatter, and the default is every 3 digits.
   */
  private CharSequence insertGrouping(CharSequence s) {
    StringBuilder result = new StringBuilder(s.length() + s.length() / 3);

    // A leading '-' doesn't want to be included in the grouping.
    int digitsLength = s.length();
    int i = 0;
    if (s.charAt(0) == '-') {
      --digitsLength;
      ++i;
      result.append('-');
    }

    // Append the digits that come before the first separator.
    int headLength = digitsLength % 3;
    if (headLength == 0) {
      headLength = 3;
    }
    result.append(s, i, i + headLength);
    i += headLength;

    // Append the remaining groups.
    for (; i < s.length(); i += 3) {
      result.append(LocaleData.groupingSeparator);
      result.append(s, i, i + 3);
    }
    return result;
  }

  private CharSequence transformFromBoolean() {
    CharSequence result;
    if (arg instanceof Boolean) {
      result = arg.toString();
    } else if (arg == null) {
      result = "false";
    } else {
      result = "true";
    }
    return padding(result, 0);
  }

  private CharSequence transformFromHashCode() {
    CharSequence result;
    if (arg == null) {
      result = "null";
    } else {
      result = Integer.toHexString(arg.hashCode());
    }
    return padding(result, 0);
  }

  private @Nullable CharSequence transformFromString() {
    if (arg instanceof Formattable) {
      int flags = 0;
      if (formatToken.flagMinus) {
        flags |= FormattableFlags.LEFT_JUSTIFY;
      }
      if (formatToken.flagSharp) {
        flags |= FormattableFlags.ALTERNATE;
      }
      if (Character.isUpperCase(formatToken.getConversionType())) {
        flags |= FormattableFlags.UPPERCASE;
      }
      ((Formattable) arg).formatTo(this, flags, formatToken.getWidth(), formatToken.getPrecision());
      // all actions have been taken out in the
      // Formattable.formatTo, thus there is nothing to do, just
      // returns null, which tells the Parser to add nothing to the
      // output.
      return null;
    }
    CharSequence result = arg != null ? arg.toString() : "null";
    return padding(result, 0);
  }

  private CharSequence transformFromCharacter() {
    if (arg == null) {
      return padding("null", 0);
    }
    if (arg instanceof Character) {
      return padding(String.valueOf(arg), 0);
    } else if (arg instanceof Byte || arg instanceof Short || arg instanceof Integer) {
      int codePoint = ((Number) arg).intValue();
      if (!Character.isValidCodePoint(codePoint)) {
        throw new IllegalFormatCodePointException(codePoint);
      }
      CharSequence result =
          (codePoint < Character.MIN_SUPPLEMENTARY_CODE_POINT)
              ? String.valueOf((char) codePoint)
              : String.valueOf(Character.toChars(codePoint));
      return padding(result, 0);
    } else {
      throw badArgumentType();
    }
  }

  private CharSequence transformFromPercent() {
    return padding("%", 0);
  }

  private CharSequence padding(CharSequence source, int startIndex) {
    int start = startIndex;
    int width = formatToken.getWidth();
    int precision = formatToken.getPrecision();

    int length = source.length();
    if (precision >= 0) {
      length = Math.min(length, precision);
      if (source instanceof StringBuilder) {
        ((StringBuilder) source).setLength(length);
      } else {
        source = source.subSequence(0, length);
      }
    }
    if (width > 0) {
      width = Math.max(source.length(), width);
    }
    if (length >= width) {
      return source;
    }

    char paddingChar = '\u0020'; // space as padding char.
    if (formatToken.flagZero) {
      if (formatToken.getConversionType() == 'd') {
        paddingChar = LocaleData.zeroDigit;
      } else {
        paddingChar = '0'; // No localized digits for bases other than decimal.
      }
    } else {
      // if padding char is space, always pad from the start.
      start = 0;
    }
    char[] paddingChars = new char[width - length];
    Arrays.fill(paddingChars, paddingChar);

    boolean paddingRight = formatToken.flagMinus;
    StringBuilder result = toStringBuilder(source);
    if (paddingRight) {
      result.append(paddingChars);
    } else {
      result.insert(start, paddingChars);
    }
    return result;
  }

  private StringBuilder toStringBuilder(CharSequence cs) {
    return cs instanceof StringBuilder ? (StringBuilder) cs : new StringBuilder(cs);
  }

  private StringBuilder wrapParentheses(StringBuilder result) {
    result.setCharAt(0, '('); // Replace the '-'.
    if (formatToken.flagZero) {
      formatToken.setWidth(formatToken.getWidth() - 1);
      result = (StringBuilder) padding(result, 1);
      result.append(')');
    } else {
      result.append(')');
      result = (StringBuilder) padding(result, 0);
    }
    return result;
  }

  private CharSequence transformFromInteger() {
    int startIndex = 0;
    StringBuilder result = new StringBuilder();
    char currentConversionType = formatToken.getConversionType();

    long value;
    if (arg instanceof Long) {
      value = ((Long) arg).longValue();
    } else if (arg instanceof Integer) {
      value = ((Integer) arg).longValue();
    } else if (arg instanceof Short) {
      value = ((Short) arg).longValue();
    } else if (arg instanceof Byte) {
      value = ((Byte) arg).longValue();
    } else {
      throw badArgumentType();
    }

    if (formatToken.flagSharp) {
      if (currentConversionType == 'o') {
        result.append("0");
        startIndex += 1;
      } else {
        result.append("0x");
        startIndex += 2;
      }
    }

    if (currentConversionType == 'd') {
      CharSequence digits = Long.toString(value);
      if (formatToken.flagComma) {
        digits = insertGrouping(digits);
      }
      if (LocaleData.zeroDigit != '0') {
        digits = localizeDigits(digits);
      }
      result.append(digits);

      if (value < 0) {
        if (formatToken.flagParenthesis) {
          return wrapParentheses(result);
        } else if (formatToken.flagZero) {
          startIndex++;
        }
      } else {
        if (formatToken.flagPlus) {
          result.insert(0, '+');
          startIndex += 1;
        } else if (formatToken.flagSpace) {
          result.insert(0, ' ');
          startIndex += 1;
        }
      }
    } else {
      // Undo sign-extension, since we'll be using Long.to(Octal|Hex)String.
      if (arg instanceof Byte) {
        value &= 0xffL;
      } else if (arg instanceof Short) {
        value &= 0xffffL;
      } else if (arg instanceof Integer) {
        value &= 0xffffffffL;
      }
      if (currentConversionType == 'o') {
        result.append(Long.toOctalString(value));
      } else {
        result.append(Long.toHexString(value));
      }
    }

    return padding(result, startIndex);
  }

  private CharSequence transformFromNull() {
    formatToken.flagZero = false;
    return padding("null", 0);
  }

  private CharSequence transformFromBigInteger() {
    int startIndex = 0;
    StringBuilder result = new StringBuilder();
    BigInteger bigInt = (BigInteger) arg;
    char currentConversionType = formatToken.getConversionType();

    if (bigInt == null) {
      return transformFromNull();
    }

    boolean isNegative = (bigInt.compareTo(BigInteger.ZERO) < 0);

    if (currentConversionType == 'd') {
      CharSequence digits = bigInt.toString(10);
      if (formatToken.flagComma) {
        digits = insertGrouping(digits);
      }
      result.append(digits);
    } else if (currentConversionType == 'o') {
      // convert BigInteger to a string presentation using radix 8
      result.append(bigInt.toString(8));
    } else {
      // convert BigInteger to a string presentation using radix 16
      result.append(bigInt.toString(16));
    }
    if (formatToken.flagSharp) {
      startIndex = isNegative ? 1 : 0;
      if (currentConversionType == 'o') {
        result.insert(startIndex, "0");
        startIndex += 1;
      } else if (currentConversionType == 'x' || currentConversionType == 'X') {
        result.insert(startIndex, "0x");
        startIndex += 2;
      }
    }

    if (!isNegative) {
      if (formatToken.flagPlus) {
        result.insert(0, '+');
        startIndex += 1;
      }
      if (formatToken.flagSpace) {
        result.insert(0, ' ');
        startIndex += 1;
      }
    }

    /* pad paddingChar to the output */
    if (isNegative && formatToken.flagParenthesis) {
      return wrapParentheses(result);
    }
    if (isNegative && formatToken.flagZero) {
      startIndex++;
    }
    return padding(result, startIndex);
  }

  private CharSequence transformFromDateTime() {
    throw new UnsupportedOperationException("Time and date formatting has not been implemented.");
  }

  private @Nullable CharSequence transformFromSpecialNumber(double d) {
    String source = null;
    if (Double.isNaN(d)) {
      source = "NaN";
    } else if (d == Double.POSITIVE_INFINITY) {
      if (formatToken.flagPlus) {
        source = "+Infinity";
      } else if (formatToken.flagSpace) {
        source = " Infinity";
      } else {
        source = "Infinity";
      }
    } else if (d == Double.NEGATIVE_INFINITY) {
      if (formatToken.flagParenthesis) {
        source = "(Infinity)";
      } else {
        source = "-Infinity";
      }
    } else {
      return null;
    }

    formatToken.setPrecision(FormatToken.UNSET);
    formatToken.flagZero = false;
    return padding(source, 0);
  }

  @SuppressWarnings("IdentityBinaryExpression")
  private CharSequence transformFromFloat() {
    if (arg == null) {
      return transformFromNull();
    } else if (arg instanceof Float || arg instanceof Double) {
      Number number = (Number) arg;
      double d = number.doubleValue();
      if (d != d || d == Double.POSITIVE_INFINITY || d == Double.NEGATIVE_INFINITY) {
        return transformFromSpecialNumber(d);
      }
    } else if (arg instanceof BigDecimal) {
      // BigDecimal can't represent NaN or infinities, but its doubleValue method will return
      // infinities if the BigDecimal is too big for a double.
    } else {
      throw badArgumentType();
    }

    char conversionType = formatToken.getConversionType();
    if (conversionType != 'a' && conversionType != 'A' && !formatToken.isPrecisionSet()) {
      formatToken.setPrecision(FormatToken.DEFAULT_PRECISION);
    }

    StringBuilder result = new StringBuilder();
    switch (conversionType) {
      case 'a':
      case 'A':
        transformA(result);
        break;
      case 'e':
      case 'E':
        transformE(result);
        break;
      case 'f':
        transformF(result);
        break;
      case 'g':
      case 'G':
        transformG(result);
        break;
      default:
        throw formatToken.unknownFormatConversionException();
    }

    formatToken.setPrecision(FormatToken.UNSET);

    int startIndex = 0;
    if (startsWithMinusSign(result, LocaleData.minusSign)) {
      if (formatToken.flagParenthesis) {
        return wrapParentheses(result);
      }
    } else {
      if (formatToken.flagSpace) {
        result.insert(0, ' ');
        startIndex++;
      }
      if (formatToken.flagPlus) {
        result.insert(0, '+');
        startIndex++;
      }
    }

    char firstChar = result.charAt(0);
    if (formatToken.flagZero
        && (firstChar == '+' || startsWithMinusSign(result, LocaleData.minusSign))) {
      startIndex = LocaleData.minusSign.length();
    }

    if (conversionType == 'a' || conversionType == 'A') {
      startIndex += 2;
    }
    return padding(result, startIndex);
  }

  private static boolean startsWithMinusSign(CharSequence cs, String minusSign) {
    if (cs.length() < minusSign.length()) {
      return false;
    }

    for (int i = 0; i < minusSign.length(); ++i) {
      if (minusSign.charAt(i) != cs.charAt(i)) {
        return false;
      }
    }

    return true;
  }

  private static boolean isAsciiDigit(int i) {
    return '0' <= i && i <= '9';
  }

  private void transformE(StringBuilder unused) {
    throw new UnsupportedOperationException(
        "Conversion type 'e' (decimal number in computerized scientific notation) has not been"
            + " implemented");
  }

  private void transformG(StringBuilder unused) {
    throw new UnsupportedOperationException(
        "Conversion type 'g' (computerized scientific notation or decimal format, depending on the"
            + " precision and the value after rounding) has not been implemented");
  }

  private void transformF(StringBuilder result) {
    final int precision = formatToken.getPrecision();
    if (formatToken.flagComma) {
      throw new UnsupportedOperationException(
          "',' flag (grouping separators) has not been implemented for conversion type 'f'.");
    }

    // Format the value with BigDecimal class's help. Note that `String.valueOf` won't work here as
    // it might return scientific notation.
    BigDecimal bigDecimal;
    if (arg instanceof BigDecimal) {
      bigDecimal = (BigDecimal) arg;
    } else {
      bigDecimal = new BigDecimal(((Number) arg).doubleValue());
    }
    result.append(bigDecimalFormatRoundHalfUp(bigDecimal, precision));
    // The # flag requires that we always output a decimal separator.
    if (formatToken.flagSharp && precision == 0) {
      result.append(LocaleData.decimalSeparator);
    }
  }

  private void transformA(StringBuilder unused) {
    throw new UnsupportedOperationException(
        "Conversion type 'a' (hexadecimal floating-point number with a significand and an exponent)"
            + " has not been implemented");
  }

  private static class FormatSpecifierParser {
    private String format;
    private int length;

    private int startIndex;
    private int i;

    /** Constructs a new parser for the given format string. */
    FormatSpecifierParser(String format) {
      this.format = format;
      this.length = format.length();
    }

    /**
     * Returns a FormatToken representing the format specifier starting at 'offset'.
     *
     * @param offset the first character after the '%'
     */
    FormatToken parseFormatToken(int offset) {
      this.startIndex = offset;
      this.i = offset;
      return parseArgumentIndexAndFlags(new FormatToken());
    }

    /**
     * Returns a string corresponding to the last format specifier that was parsed. Used to
     * construct error messages.
     */
    String getFormatSpecifierText() {
      return format.substring(startIndex, i);
    }

    private int peek() {
      return (i < length) ? format.charAt(i) : -1;
    }

    private char advance() {
      if (i >= length) {
        throw unknownFormatConversionException();
      }
      return format.charAt(i++);
    }

    private UnknownFormatConversionException unknownFormatConversionException() {
      throw new UnknownFormatConversionException(getFormatSpecifierText());
    }

    private FormatToken parseArgumentIndexAndFlags(FormatToken token) {
      // Parse the argument index, if there is one.
      int position = i;
      int ch = peek();
      if (isAsciiDigit(ch)) {
        int number = nextInt();
        if (peek() == '$') {
          // The number was an argument index.
          advance(); // Swallow the '$'.
          if (number == FormatToken.UNSET) {
            throw new MissingFormatArgumentException(getFormatSpecifierText());
          }
          // k$ stands for the argument whose index is k-1 except that
          // 0$ and 1$ both stand for the first element.
          token.setArgIndex(Math.max(0, number - 1));
        } else {
          if (ch == '0') {
            // The digit zero is a format flag, so reparse it as such.
            i = position;
          } else {
            // The number was a width. This means there are no flags to parse.
            return parseWidth(token, number);
          }
        }
      } else if (ch == '<') {
        token.setArgIndex(FormatToken.LAST_ARGUMENT_INDEX);
        advance();
      }

      // Parse the flags.
      while (token.setFlag(peek())) {
        advance();
      }

      // What comes next?
      ch = peek();
      if (isAsciiDigit(ch)) {
        return parseWidth(token, nextInt());
      } else if (ch == '.') {
        return parsePrecision(token);
      } else {
        return parseConversionType(token);
      }
    }

    // We pass the width in because in some cases we've already parsed it.
    // (Because of the ambiguity between argument indexes and widths.)
    private FormatToken parseWidth(FormatToken token, int width) {
      token.setWidth(width);
      int ch = peek();
      if (ch == '.') {
        return parsePrecision(token);
      } else {
        return parseConversionType(token);
      }
    }

    private FormatToken parsePrecision(FormatToken token) {
      advance(); // Swallow the '.'.
      int ch = peek();
      if (isAsciiDigit(ch)) {
        token.setPrecision(nextInt());
        return parseConversionType(token);
      } else {
        // The precision is required but not given by the format string.
        throw unknownFormatConversionException();
      }
    }

    private FormatToken parseConversionType(FormatToken token) {
      char conversionType = advance(); // A conversion type is mandatory.
      token.setConversionType(conversionType);
      if (conversionType == 't' || conversionType == 'T') {
        char dateSuffix = advance(); // A date suffix is mandatory for 't' or 'T'.
        token.setDateSuffix(dateSuffix);
      }
      return token;
    }

    // Parses an integer (of arbitrary length, but typically just one digit).
    private int nextInt() {
      long value = 0;
      while (i < length && isAsciiDigit(format.charAt(i))) {
        value = 10 * value + (format.charAt(i++) - '0');
        if (value > Integer.MAX_VALUE) {
          return failNextInt();
        }
      }
      return (int) value;
    }

    // Swallow remaining digits to resync our attempted parse, but return failure.
    private int failNextInt() {
      while (isAsciiDigit(peek())) {
        advance();
      }
      return FormatToken.UNSET;
    }
  }

  /*
   * Format a bigDecimal, with {@code precision} digits after decimal separator. Round half up if
   * needed.
   */
  private String bigDecimalFormatRoundHalfUp(BigDecimal bigDecimal, int precision) {
    String plainString = bigDecimal.toPlainString();
    boolean isNegative = plainString.charAt(0) == '-';
    // Everything before '.' should be added to `result` as-is
    int decimalSeparatorIndex = plainString.indexOf('.');
    if (decimalSeparatorIndex == -1) {
      return precision == 0
          ? plainString
          : (plainString + "." + new String(new char[precision]).replace('\0', '0'));
    } else {
      // According to Java doc we should round away from 0 when the discarded fraction is 0.5. To do
      // so, we add a "fraction" number to bigDecimal to make carry happen if necessary, then apply
      // round down.
      BigDecimal fraction =
          new BigDecimal(
              (isNegative ? "-0." : "0.")
                  + new String(new char[precision]).replace('\0', '0')
                  + "5");
      return bigDecimalFormatRoundDown(bigDecimal.add(fraction), precision);
    }
  }

  /*
   * Format a bigDecimal, with {@code precision} digits after decimal separator. Round down if there
   * are more than {@code precision} digits after decimal separator.
   */
  private String bigDecimalFormatRoundDown(BigDecimal bigDecimal, int precision) {
    // Round towards zero. Simply truncates exceeding digits.
    String plainString = bigDecimal.toPlainString();
    int decimalSeparatorIndex = plainString.indexOf('.');
    if (precision == 0) {
      return decimalSeparatorIndex == -1
          ? plainString
          : plainString.substring(0, decimalSeparatorIndex);
    } else if (decimalSeparatorIndex == -1) {
      return plainString + "." + new String(new char[precision]).replace('\0', '0');
    } else {
      int numCharactersAfterDecimalSeparator = plainString.length() - (decimalSeparatorIndex + 1);
      int numZeroPadding = precision - numCharactersAfterDecimalSeparator;
      if (numZeroPadding >= 0) {
        return plainString + new String(new char[numZeroPadding]).replace('\0', '0');
      } else {
        // +1 for the '.' character
        return plainString.substring(0, decimalSeparatorIndex + 1 + precision);
      }
    }
  }
}
