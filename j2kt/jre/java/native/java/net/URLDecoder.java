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
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import org.jspecify.annotations.NullMarked;

/**
 * This class is used to decode a string which is encoded in the {@code
 * application/x-www-form-urlencoded} MIME content type.
 */
@NullMarked
public class URLDecoder {

  @Deprecated
  public static String decode(String s) {
    return decode(s, Charset.defaultCharset());
  }

  public static String decode(String s, String encoding) throws UnsupportedEncodingException {
    if (encoding == null) {
      throw new NullPointerException();
    }
    if (encoding.isEmpty()) {
      throw new UnsupportedEncodingException(encoding);
    }

    if (s.indexOf('%') == -1) {
      return s.replace('+', ' ');
    }

    Charset charset = null;
    try {
      charset = Charset.forName(encoding);
    } catch (IllegalCharsetNameException e) {
      throw (UnsupportedEncodingException)
          (new UnsupportedEncodingException(encoding).initCause(e));
    } catch (UnsupportedCharsetException e) {
      throw (UnsupportedEncodingException)
          (new UnsupportedEncodingException(encoding).initCause(e));
    }

    return decode(s, charset);
  }

  private static String decode(String s, Charset charset) {

    StringBuilder sb = new StringBuilder(s.length());
    byte[] buf = new byte[s.length() / 3];

    for (int i = 0; i < s.length(); ) {
      char c = s.charAt(i);
      if (c == '+') {
        sb.append(' ');
      } else if (c == '%') {

        int len = 0;
        do {
          if (i + 2 >= s.length()) {
            throw new IllegalArgumentException("Incomplete % sequence at: " + i);
          }
          int d1 = Character.digit(s.charAt(i + 1), 16);
          int d2 = Character.digit(s.charAt(i + 2), 16);
          if (d1 == -1 || d2 == -1) {
            throw new IllegalArgumentException(
                "Invalid % sequence " + s.substring(i, i + 3) + " at " + i);
          }
          buf[len++] = (byte) ((d1 << 4) + d2);
          i += 3;
        } while (i < s.length() && s.charAt(i) == '%');

        sb.append(new String(buf, 0, len, charset));
        continue;
      } else {
        sb.append(c);
      }
      i++;
    }
    return sb.toString();
  }
}
