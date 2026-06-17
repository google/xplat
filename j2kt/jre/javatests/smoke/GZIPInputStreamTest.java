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
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.jspecify.annotations.NullMarked;
import org.junit.Test;

@NullMarked
public class GZIPInputStreamTest {

  @Test
  public void testRoundTrip() throws Exception {
    String expected = makeString();
    byte[] expectedBytes = expected.getBytes(UTF_8);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (GZIPOutputStream gos = new GZIPOutputStream(baos)) {
      gos.write(expectedBytes);
    }
    byte[] gzippedBytes = baos.toByteArray();

    ByteArrayInputStream bais = new ByteArrayInputStream(gzippedBytes);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try (GZIPInputStream gis = new GZIPInputStream(bais)) {
      byte[] buf = new byte[1024];
      int read;
      while ((read = gis.read(buf)) != -1) {
        out.write(buf, 0, read);
      }
    }

    assertEquals(expected, new String(out.toByteArray(), UTF_8));
  }

  private static String makeString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 1024; ++i) {
      sb.append(i + 1024);
    }
    return sb.toString();
  }
}
