/*
 * Copyright 2025 Google Inc.
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

import static java.util.Objects.checkIndex;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class ObjectsTest {
  @Test
  public void testCheckIndex() {
    checkIndex(0, 1);
    checkIndex(3, 6);

    try {
      checkIndex(-1, 5);
      fail();
    } catch (IndexOutOfBoundsException e) {
      // expected
    }

    try {
      checkIndex(0, -1);
      fail();
    } catch (IndexOutOfBoundsException e) {
      // expected
    }

    try {
      checkIndex(5, 3);
      fail();
    } catch (IndexOutOfBoundsException e) {
      // expected
    }

    try {
      checkIndex(0, 0);
      fail();
    } catch (IndexOutOfBoundsException e) {
      // expected
    }

    try {
      checkIndex(-1, 0);
      fail();
    } catch (IndexOutOfBoundsException e) {
      // expected
    }
  }
}
