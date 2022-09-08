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

package java.net;

import java.io.UnsupportedEncodingException;
import jsinterop.annotations.JsNonNull;

/**
 * This class is used to encode a string using the format required by {@code
 * application/x-www-form-urlencoded} MIME content type.
 */
public class URLEncoder {

  static final String digits = "0123456789ABCDEF";

  /** Prevents this class from being instantiated. */
  private URLEncoder() {}

  @Deprecated
  public static String encode(String s) {
    // Guess a bit bigger for encoded form
    StringBuilder buf = new StringBuilder(s.length() + 16);
    for (int i = 0; i < s.length(); i++) {
      char ch = s.charAt(i);
      if ((ch >= 'a' && ch <= 'z')
          || (ch >= 'A' && ch <= 'Z')
          || (ch >= '0' && ch <= '9')
          || ".-*_".indexOf(ch) > -1) {
        buf.append(ch);
      } else if (ch == ' ') {
        buf.append('+');
      } else {
        byte[] bytes = new String(new char[] {ch}).getBytes();
        for (int j = 0; j < bytes.length; j++) {
          buf.append('%');
          buf.append(digits.charAt((bytes[j] & 0xf0) >> 4));
          buf.append(digits.charAt(bytes[j] & 0xf));
        }
      }
    }
    return buf.toString();
  }

  @SuppressWarnings("ReturnValueIgnored")
  public static String encode(String s, String enc) throws UnsupportedEncodingException {
    if (s == null || enc == null) {
      throw new NullPointerException();
    }
    // check for UnsupportedEncodingException
    "".getBytes(enc);

    // Guess a bit bigger for encoded form
    StringBuilder buf = new StringBuilder(s.length() + 16);
    int start = -1;
    for (int i = 0; i < s.length(); i++) {
      char ch = s.charAt(i);
      if ((ch >= 'a' && ch <= 'z')
          || (ch >= 'A' && ch <= 'Z')
          || (ch >= '0' && ch <= '9')
          || " .-*_".indexOf(ch) > -1) {
        if (start >= 0) {
          convert(s.substring(start, i), buf, enc);
          start = -1;
        }
        if (ch != ' ') {
          buf.append(ch);
        } else {
          buf.append('+');
        }
      } else {
        if (start < 0) {
          start = i;
        }
      }
    }
    if (start >= 0) {
      convert(s.substring(start, s.length()), buf, enc);
    }
    return buf.toString();
  }

  private static void convert(String s, StringBuilder buf, @JsNonNull String enc)
      throws UnsupportedEncodingException {
    byte[] bytes = s.getBytes(enc);
    for (int j = 0; j < bytes.length; j++) {
      buf.append('%');
      buf.append(digits.charAt((bytes[j] & 0xf0) >> 4));
      buf.append(digits.charAt(bytes[j] & 0xf));
    }
  }
}
