/*
 * Copyright 2023 Google Inc.
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

import static org.junit.Assert.assertEquals;

import java.util.Date;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Smoke test for Date. */
@RunWith(JUnit4.class)
public class DateTest {

  @Test
  @SuppressWarnings("JavaUtilDate")
  public void testDateUTC() {
    long utcMillisSinceEpoch = Date.UTC(99, 1, 24, 14, 31, 45);
    long expectedEpoch = 919866705000L; // 24 Feb 1999 14:31:45 GMT as unix timestamp
    Date utcDate = new Date(utcMillisSinceEpoch);
    String dateStr = utcDate.toGMTString();
    System.out.println(dateStr);
    assertEquals("24 Feb 1999 14:31:45 GMT", dateStr);
    assertEquals(expectedEpoch, utcMillisSinceEpoch);
  }
}
