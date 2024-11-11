/*
 * Copyright 2022 Google Inc.
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Formatter;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class IoTest {

  @Test
  public void testStringReader() throws IOException {
    StringBuilder sb = new StringBuilder();
    for (char c = 0; c < 4096; c++) {
      sb.append(c);
    }
    String s = sb.toString();
    StringReader reader = new StringReader(s);
    reader.skip(2048);
    assertEquals(2048, reader.read());
    reader.close();
  }

  // TODO: b/378477917 - This relies on Charset.newDecoder which is not implemented yet.
  @Ignore
  @Test
  public void testInputStreamReader() throws Exception {
    ByteArrayInputStream is = new ByteArrayInputStream("Hello, World".getBytes(UTF_8));
    InputStreamReader reader = new InputStreamReader(is, UTF_8);
    char[] buf = new char[128];
    int count = reader.read(buf);
    assertEquals(12, count);
    assertEquals("Hello, World", String.valueOf(buf, 0, count));
  }

  @Test
  public void testPrintStream() {
    {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      PrintStream ps = new PrintStream(os);
      ps.print("hello, ");
      ps.append("world");
      ps.flush();
      assertEquals("hello, world", os.toString());
    }
    {
      // Test usage with Formatter
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      PrintStream ps = new PrintStream(os);
      Formatter fmt = new Formatter(ps);
      fmt.format("hello, %d world", 1234);
      ps.flush();
      assertEquals("hello, 1234 world", os.toString());
    }
  }

  @Test
  public void testPrintWriter() {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    PrintWriter writer = new PrintWriter(os);
    writer.printf("%d %s", 1234, null);
    writer.flush();
    assertEquals("1234 null", os.toString());
  }
}
