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

import static org.junit.Assert.assertArrayEquals;

import java.nio.charset.Charset;
import java.security.Key;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.junit.Test;

public class MacTest {

  @SuppressWarnings("nullness:initialization.field.uninitialized")
  private Mac mac1;

  @SuppressWarnings("nullness:initialization.field.uninitialized")
  private Mac mac2;

  @SuppressWarnings("nullness:initialization.field.uninitialized")
  private SecretKeySpec keySpec;

  // Precomputed HMAC-SHA256 hash of "message" with key "key".
  private static final byte[] precomputedSha256Mac =
      new byte[] {
        (byte) 0x6e,
        (byte) 0x9e,
        (byte) 0xf2,
        (byte) 0x9b,
        (byte) 0x75,
        (byte) 0xff,
        (byte) 0xfc,
        (byte) 0x5b,
        (byte) 0x7a,
        (byte) 0xba,
        (byte) 0xe5,
        (byte) 0x27,
        (byte) 0xd5,
        (byte) 0x8f,
        (byte) 0xda,
        (byte) 0xdb,
        (byte) 0x2f,
        (byte) 0xe4,
        (byte) 0x2e,
        (byte) 0x72,
        (byte) 0x19,
        (byte) 0x01,
        (byte) 0x19,
        (byte) 0x76,
        (byte) 0x91,
        (byte) 0x73,
        (byte) 0x43,
        (byte) 0x06,
        (byte) 0x5f,
        (byte) 0x58,
        (byte) 0xed,
        (byte) 0x4a,
      };

  private void initialize(String algorithm) throws Exception {
    mac1 = Mac.getInstance(algorithm);
    mac2 = Mac.getInstance(algorithm);
    keySpec = new SecretKeySpec("key".getBytes(), algorithm);
  }

  @Test
  public void testHmacSHA256_emptyKey() throws Exception {
    initialize("HmacSHA256");

    Key emptyKey =
        new SecretKey() {
          @Override
          public String getAlgorithm() {
            return "HmacSHA256";
          }

          @Override
          public byte[] getEncoded() {
            return new byte[0];
          }

          @Override
          public String getFormat() {
            return "RAW";
          }
        };

    mac1.init(emptyKey);
    mac1.update("message".getBytes());
    byte[] result1 = mac1.doFinal();

    mac1.reset();
    mac1.update("message".getBytes());
    byte[] result2 = mac1.doFinal();

    assertArrayEquals(result1, result2);
  }

  @Test
  public void testHmacSHA256_update() throws Exception {
    initialize("HmacSHA256");

    // Update with a single byte array.
    mac1.init(keySpec);
    mac1.update("message".getBytes(Charset.forName("UTF-8")));
    byte[] result1 = mac1.doFinal();

    // Update with multiple byte arrays.
    mac2.init(keySpec);
    mac2.update("mess".getBytes(Charset.forName("UTF-8")));
    mac2.update("age".getBytes(Charset.forName("UTF-8")));
    byte[] result2 = mac2.doFinal();

    assertArrayEquals(result1, precomputedSha256Mac);
    assertArrayEquals(result2, precomputedSha256Mac);
  }

  @Test
  public void testHmacMD5_update() throws Exception {
    initialize("HmacMD5");

    // Update with a single byte array.
    mac1.init(keySpec);
    mac1.update("message".getBytes(Charset.forName("UTF-8")));
    byte[] result1 = mac1.doFinal();

    mac2.init(keySpec);
    mac2.update("mess".getBytes(Charset.forName("UTF-8")));
    mac2.update("age".getBytes(Charset.forName("UTF-8")));
    byte[] result2 = mac2.doFinal();

    assertArrayEquals(result1, result2);
  }

  @Test
  public void testHmacSHA256_byteUpdate() throws Exception {
    initialize("HmacSHA256");

    mac1.init(keySpec);
    mac1.update("message".getBytes(Charset.forName("UTF-8")));
    byte[] result1 = mac1.doFinal();

    mac2.init(keySpec);
    for (byte b : "message".getBytes(Charset.forName("UTF-8"))) {
      mac2.update(b);
    }
    byte[] result2 = mac2.doFinal();

    assertArrayEquals(result1, precomputedSha256Mac);
    assertArrayEquals(result2, precomputedSha256Mac);
  }

  @Test
  public void testHmacSHA256_update_emptyArray() throws Exception {
    initialize("HmacSHA256");
    mac1.init(keySpec);
    mac1.update(new byte[0]);
    byte[] result = mac1.doFinal();

    mac2.init(keySpec);
    byte[] expected = mac2.doFinal();
    assertArrayEquals(expected, result);
  }

  @Test
  public void testHmacSHA256_update_invalidOffset() throws Exception {
    initialize("HmacSHA256");
    mac1.init(keySpec);
    try {
      mac1.update(new byte[10], -1, 5);
      org.junit.Assert.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException expected) {
      // Expected
    }
  }

  @Test
  public void testHmacSHA256_update_invalidLength() throws Exception {
    initialize("HmacSHA256");
    mac1.init(keySpec);
    try {
      mac1.update(new byte[10], 0, 15);
      org.junit.Assert.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException expected) {
      // Expected
    }
  }

  @Test
  public void testHmacSHA256_update_negativeLength() throws Exception {
    initialize("HmacSHA256");
    mac1.init(keySpec);
    try {
      mac1.update(new byte[10], 0, -1);
      org.junit.Assert.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException expected) {
      // Expected
    }
  }
}
