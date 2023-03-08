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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ClassTest {

  @Test
  public void isArray() throws Exception {
    assertTrue((new String[0]).getClass().isArray());
    assertFalse(String.class.isArray());
    assertFalse(int.class.isArray());
  }

  @Test
  @SuppressWarnings("IsInstanceIncompatibleType")
  public void isInstance() throws Exception {
    assertTrue(String.class.isInstance(""));
    assertTrue(Object.class.isInstance(""));
    assertFalse(String.class.isInstance(new Object()));
    assertTrue((new String[0]).getClass().isInstance(new String[1]));
    assertFalse((new String[0]).getClass().isInstance(""));
  }

  @Test
  public void isPrimitive() throws Exception {
    assertTrue(byte.class.isPrimitive());
    assertTrue(short.class.isPrimitive());
    assertTrue(int.class.isPrimitive());
    assertTrue(long.class.isPrimitive());
    assertTrue(char.class.isPrimitive());
    assertTrue(boolean.class.isPrimitive());

    assertFalse(Byte.class.isPrimitive());
    assertFalse(Short.class.isPrimitive());
    assertFalse(Integer.class.isPrimitive());
    assertFalse(Long.class.isPrimitive());
    assertFalse(Character.class.isPrimitive());
    assertFalse(Boolean.class.isPrimitive());
  }
}
