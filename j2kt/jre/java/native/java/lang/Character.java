/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package java.lang;

import static javaemul.internal.KtNativeUtils.ktNative;

import com.google.j2kt.annotations.HiddenFromObjC;
import javaemul.internal.annotations.KtName;
import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

// TODO(b/223774683): Java Character should implement Serializable. Kotlin Char doesn't.
@KtNative(name = "kotlin.Char", companionName = "java.lang.Character")
@NullMarked
public final class Character implements Comparable<Character> {
  public static final char MIN_VALUE = '\u0000';

  public static final char MAX_VALUE = '\uFFFF';

  public static final int MIN_RADIX = 2;

  public static final int MAX_RADIX = 36;

  public static final Class<Character> TYPE = ktNative();

  public static final byte UNASSIGNED = 0;

  public static final byte UPPERCASE_LETTER = 1;

  public static final byte LOWERCASE_LETTER = 2;

  public static final byte TITLECASE_LETTER = 3;

  public static final byte MODIFIER_LETTER = 4;

  public static final byte OTHER_LETTER = 5;

  public static final byte NON_SPACING_MARK = 6;

  public static final byte ENCLOSING_MARK = 7;

  public static final byte COMBINING_SPACING_MARK = 8;

  public static final byte DECIMAL_DIGIT_NUMBER = 9;

  public static final byte LETTER_NUMBER = 10;

  public static final byte OTHER_NUMBER = 11;

  public static final byte SPACE_SEPARATOR = 12;

  public static final byte LINE_SEPARATOR = 13;

  public static final byte PARAGRAPH_SEPARATOR = 14;

  public static final byte CONTROL = 15;

  public static final byte FORMAT = 16;

  public static final byte PRIVATE_USE = 18;

  public static final byte SURROGATE = 19;

  public static final byte DASH_PUNCTUATION = 20;

  public static final byte START_PUNCTUATION = 21;

  public static final byte END_PUNCTUATION = 22;

  public static final byte CONNECTOR_PUNCTUATION = 23;

  public static final byte OTHER_PUNCTUATION = 24;

  public static final byte MATH_SYMBOL = 25;

  public static final byte CURRENCY_SYMBOL = 26;

  public static final byte MODIFIER_SYMBOL = 27;

  public static final byte OTHER_SYMBOL = 28;

  public static final byte INITIAL_QUOTE_PUNCTUATION = 29;

  public static final byte FINAL_QUOTE_PUNCTUATION = 30;

  public static final byte DIRECTIONALITY_UNDEFINED = -1;

  public static final byte DIRECTIONALITY_LEFT_TO_RIGHT = 0;

  public static final byte DIRECTIONALITY_RIGHT_TO_LEFT = 1;

  public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC = 2;

  public static final byte DIRECTIONALITY_EUROPEAN_NUMBER = 3;

  public static final byte DIRECTIONALITY_EUROPEAN_NUMBER_SEPARATOR = 4;

  public static final byte DIRECTIONALITY_EUROPEAN_NUMBER_TERMINATOR = 5;

  public static final byte DIRECTIONALITY_ARABIC_NUMBER = 6;

  public static final byte DIRECTIONALITY_COMMON_NUMBER_SEPARATOR = 7;

  public static final byte DIRECTIONALITY_NONSPACING_MARK = 8;

  public static final byte DIRECTIONALITY_BOUNDARY_NEUTRAL = 9;

  public static final byte DIRECTIONALITY_PARAGRAPH_SEPARATOR = 10;

  public static final byte DIRECTIONALITY_SEGMENT_SEPARATOR = 11;

  public static final byte DIRECTIONALITY_WHITESPACE = 12;

  public static final byte DIRECTIONALITY_OTHER_NEUTRALS = 13;

  public static final byte DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING = 14;

  public static final byte DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE = 15;

  public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING = 16;

  public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE = 17;

  public static final byte DIRECTIONALITY_POP_DIRECTIONAL_FORMAT = 18;

  public static final char MIN_HIGH_SURROGATE = '\uD800';

  public static final char MAX_HIGH_SURROGATE = '\uDBFF';

  public static final char MIN_LOW_SURROGATE = '\uDC00';

  public static final char MAX_LOW_SURROGATE = '\uDFFF';

  public static final char MIN_SURROGATE = MIN_HIGH_SURROGATE;

  public static final char MAX_SURROGATE = MAX_LOW_SURROGATE;

  public static final int MIN_SUPPLEMENTARY_CODE_POINT = 0x010000;

  public static final int MIN_CODE_POINT = 0x000000;

  public static final int MAX_CODE_POINT = 0X10FFFF;

  @HiddenFromObjC public static int SIZE = 16;

  // J2KT removed: Subset, UnicodeBlock

  public Character(char value) {}

  @KtName("toChar")
  public native char charValue();

  @Override
  public native int compareTo(Character c);

  public static native int compare(char lhs, char rhs);

  public static native Character valueOf(char c);

  public static native boolean isValidCodePoint(int codePoint);

  public static native boolean isSupplementaryCodePoint(int codePoint);

  public static native boolean isHighSurrogate(char ch);

  public static native boolean isLowSurrogate(char ch);

  public static native boolean isSurrogate(char ch);

  public static native boolean isSurrogatePair(char high, char low);

  public static native int charCount(int codePoint);

  public static native int toCodePoint(char high, char low);

  public static native int codePointAt(CharSequence seq, int index);

  public static native int codePointAt(char[] seq, int index);

  public static native int codePointAt(char[] seq, int index, int limit);

  public static native int codePointBefore(CharSequence seq, int index);

  public static native int codePointBefore(char[] seq, int index);

  public static native int codePointBefore(char[] seq, int index, int start);

  public static native int toChars(int codePoint, char[] dst, int dstIndex);

  public static native char[] toChars(int codePoint);

  public static native int codePointCount(CharSequence seq, int offset, int count);

  public static native int codePointCount(char[] seq, int offset, int count);

  public static native int offsetByCodePoints(CharSequence seq, int index, int codePointOffset);

  public static native int offsetByCodePoints(
      char[] seq, int start, int count, int index, int codePointOffset);

  public static native int digit(char c, int radix);

  @HiddenFromObjC
  public static native int digit(int codePoint, int radix);

  @Override
  public native boolean equals(@Nullable Object object);

  public static native char forDigit(int digit, int radix);

  @HiddenFromObjC
  public static native String getName(int codePoint);

  @HiddenFromObjC
  public static native int getNumericValue(char c);

  @HiddenFromObjC
  public static native int getNumericValue(int codePoint);

  @HiddenFromObjC
  public static native int getType(char c);

  @HiddenFromObjC
  public static native int getType(int codePoint);

  @HiddenFromObjC
  public static native byte getDirectionality(char c);

  @HiddenFromObjC
  public static native byte getDirectionality(int codePoint);

  @HiddenFromObjC
  public static native boolean isMirrored(char c);

  @HiddenFromObjC
  public static native boolean isMirrored(int codePoint);

  @Override
  public native int hashCode();

  public static native char highSurrogate(int codePoint);

  public static native char lowSurrogate(int codePoint);

  @HiddenFromObjC
  public static native boolean isAlphabetic(int codePoint);

  @HiddenFromObjC
  public static native boolean isBmpCodePoint(int codePoint);

  @HiddenFromObjC
  public static native boolean isDefined(char c);

  @HiddenFromObjC
  public static native boolean isDefined(int codePoint);

  public static native boolean isDigit(char c);

  public static native boolean isDigit(int codePoint);

  @HiddenFromObjC
  public static native boolean isIdentifierIgnorable(char c);

  @HiddenFromObjC
  public static native boolean isIdeographic(int codePoint);

  @HiddenFromObjC
  public static native boolean isIdentifierIgnorable(int codePoint);

  public static native boolean isISOControl(char c);

  @HiddenFromObjC
  public static native boolean isISOControl(int c);

  @HiddenFromObjC
  public static native boolean isJavaIdentifierPart(char c);

  @HiddenFromObjC
  public static native boolean isJavaIdentifierPart(int codePoint);

  @HiddenFromObjC
  public static native boolean isJavaIdentifierStart(char c);

  @HiddenFromObjC
  public static native boolean isJavaIdentifierStart(int codePoint);

  @Deprecated
  @HiddenFromObjC
  public static native boolean isJavaLetter(char c);

  @Deprecated
  @HiddenFromObjC
  public static native boolean isJavaLetterOrDigit(char c);

  @HiddenFromObjC
  public static native boolean isLetter(char c);

  @HiddenFromObjC
  public static native boolean isLetter(int codePoint);

  @HiddenFromObjC
  public static native boolean isLetterOrDigit(char c);

  @HiddenFromObjC
  public static native boolean isLetterOrDigit(int codePoint);

  @HiddenFromObjC
  public static native boolean isLowerCase(char c);

  @HiddenFromObjC
  public static native boolean isLowerCase(int codePoint);

  @Deprecated
  public static native boolean isSpace(char c);

  public static native boolean isSpaceChar(char c);

  public static native boolean isSpaceChar(int codePoint);

  @HiddenFromObjC
  public static native boolean isTitleCase(char c);

  @HiddenFromObjC
  public static native boolean isTitleCase(int codePoint);

  @HiddenFromObjC
  public static native boolean isUnicodeIdentifierPart(char c);

  @HiddenFromObjC
  public static native boolean isUnicodeIdentifierPart(int codePoint);

  @HiddenFromObjC
  public static native boolean isUnicodeIdentifierStart(char c);

  @HiddenFromObjC
  public static native boolean isUnicodeIdentifierStart(int codePoint);

  public static native boolean isUpperCase(char c);

  @HiddenFromObjC
  public static native boolean isUpperCase(int codePoint);

  public static native boolean isWhitespace(char c);

  public static native boolean isWhitespace(int codePoint);

  @HiddenFromObjC
  public static native char reverseBytes(char c);

  public static native char toLowerCase(char c);

  public static native int toLowerCase(int codePoint);

  @Override
  public native String toString();

  public static native String toString(char value);

  public static native String toString(int value);

  @HiddenFromObjC
  public static native char toTitleCase(char c);

  @HiddenFromObjC
  public static native int toTitleCase(int codePoint);

  public static native char toUpperCase(char c);

  public static native int toUpperCase(int codePoint);

  public static native int hashCode(char c);
}
