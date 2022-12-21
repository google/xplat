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

import static smoke.Asserts.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Formatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class IoTest {
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
