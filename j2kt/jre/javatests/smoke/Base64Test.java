/*
 * Copyright 2026 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package smoke;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class Base64Test {
  private static final byte[] BYTES_0_255 = new byte[256];
  private static final String ENCODED_0_255 =
      "AAECAwQFBgcICQoLDA0ODxAREhMUFRYXGBkaGxwdHh8gISIjJCUmJygpKissLS4vMDEyMzQ1Njc4OTo7PD0+P0BBQkNERUZHSElKS0xNTk9QUVJTVFVWV1hZWltcXV5fYGFiY2RlZmdoaWprbG1ub3BxcnN0dXZ3eHl6e3x9fn+AgYKDhIWGh4iJiouMjY6PkJGSk5SVlpeYmZqbnJ2en6ChoqOkpaanqKmqq6ytrq+wsbKztLW2t7i5uru8vb6/wMHCw8TFxsfIycrLzM3Oz9DR0tPU1dbX2Nna29zd3t/g4eLj5OXm5+jp6uvs7e7v8PHy8/T19vf4+fr7/P3+/w==";
  private static final String MIME_ENCODED_0_255 =
      "AAECAwQFBgcICQoLDA0ODxAREhMUFRYXGBkaGxwdHh8gISIjJCUmJygpKissLS4vMDEyMzQ1Njc4\r\n"
          + "OTo7PD0+P0BBQkNERUZHSElKS0xNTk9QUVJTVFVWV1hZWltcXV5fYGFiY2RlZmdoaWprbG1ub3Bx\r\n"
          + "cnN0dXZ3eHl6e3x9fn+AgYKDhIWGh4iJiouMjY6PkJGSk5SVlpeYmZqbnJ2en6ChoqOkpaanqKmq\r\n"
          + "q6ytrq+wsbKztLW2t7i5uru8vb6/wMHCw8TFxsfIycrLzM3Oz9DR0tPU1dbX2Nna29zd3t/g4eLj\r\n"
          + "5OXm5+jp6uvs7e7v8PHy8/T19vf4+fr7/P3+/w==";

  static {
    for (int i = 0; i < 256; i++) {
      BYTES_0_255[i] = (byte) i;
    }
  }

  @Test
  public void testRfc4648_basic() {
    assertEquals("", Base64.getEncoder().encodeToString("".getBytes(UTF_8)));
    assertEquals("Zg==", Base64.getEncoder().encodeToString("f".getBytes(UTF_8)));
    assertEquals("Zm8=", Base64.getEncoder().encodeToString("fo".getBytes(UTF_8)));
    assertEquals("Zm9v", Base64.getEncoder().encodeToString("foo".getBytes(UTF_8)));
    assertEquals("Zm9vYg==", Base64.getEncoder().encodeToString("foob".getBytes(UTF_8)));
    assertEquals("Zm9vYmE=", Base64.getEncoder().encodeToString("fooba".getBytes(UTF_8)));
    assertEquals("Zm9vYmFy", Base64.getEncoder().encodeToString("foobar".getBytes(UTF_8)));
  }

  @Test
  public void testRfc4648_url() {
    assertEquals("", Base64.getUrlEncoder().encodeToString("".getBytes(UTF_8)));
    assertEquals("Zg==", Base64.getUrlEncoder().encodeToString("f".getBytes(UTF_8)));
    assertEquals("Zm8=", Base64.getUrlEncoder().encodeToString("fo".getBytes(UTF_8)));
    assertEquals("Zm9v", Base64.getUrlEncoder().encodeToString("foo".getBytes(UTF_8)));
    assertEquals("Zm9vYg==", Base64.getUrlEncoder().encodeToString("foob".getBytes(UTF_8)));
    assertEquals("Zm9vYmE=", Base64.getUrlEncoder().encodeToString("fooba".getBytes(UTF_8)));
    assertEquals("Zm9vYmFy", Base64.getUrlEncoder().encodeToString("foobar".getBytes(UTF_8)));
  }

  @Test
  public void testRfc4648_urlNoPadding() {
    Base64.Encoder urlEncoderNoPadding = Base64.getUrlEncoder().withoutPadding();
    assertEquals("", urlEncoderNoPadding.encodeToString("".getBytes(UTF_8)));
    assertEquals("Zg", urlEncoderNoPadding.encodeToString("f".getBytes(UTF_8)));
    assertEquals("Zm8", urlEncoderNoPadding.encodeToString("fo".getBytes(UTF_8)));
    assertEquals("Zm9v", urlEncoderNoPadding.encodeToString("foo".getBytes(UTF_8)));
    assertEquals("Zm9vYg", urlEncoderNoPadding.encodeToString("foob".getBytes(UTF_8)));
    assertEquals("Zm9vYmE", urlEncoderNoPadding.encodeToString("fooba".getBytes(UTF_8)));
    assertEquals("Zm9vYmFy", urlEncoderNoPadding.encodeToString("foobar".getBytes(UTF_8)));
  }

  @Test
  public void allBytesEncode() {
    String encoded = Base64.getEncoder().encodeToString(BYTES_0_255);
    assertEquals(ENCODED_0_255, encoded);
  }

  @Test
  public void allBytesDecode() {
    byte[] decoded = Base64.getDecoder().decode(ENCODED_0_255);
    assertArrayEquals(BYTES_0_255, decoded);
  }

  @Test
  public void mimeEncode() {
    byte[] input = new byte[256];
    for (int i = 0; i < input.length; i++) {
      input[i] = (byte) i;
    }
    assertEquals(MIME_ENCODED_0_255, Base64.getMimeEncoder().encodeToString(input));
  }

  @Test
  public void mimeEncodeDecode() {
    byte[] input = new byte[256];
    for (int i = 0; i < input.length; i++) {
      input[i] = (byte) i;
    }
    String encoded = Base64.getMimeEncoder().encodeToString(input);
    byte[] decoded = Base64.getMimeDecoder().decode(encoded);
    assertTrue(Arrays.equals(input, decoded));
  }

  @Test
  public void wrap() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (OutputStream wrapped = Base64.getEncoder().wrap(baos)) {
      wrapped.write(BYTES_0_255);
    }
    assertEquals(ENCODED_0_255, baos.toString());
  }

  @Test
  public void wrapMime() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (OutputStream wrapped = Base64.getMimeEncoder().wrap(baos)) {
      wrapped.write(BYTES_0_255);
    }
    assertEquals(MIME_ENCODED_0_255, baos.toString());
  }
}
