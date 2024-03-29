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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ClassTest {

  @Test
  public void cast() throws Exception {
    Object o = "test";
    assertEquals("test", String.class.cast(o));
  }

  @Test
  public void cast_null() throws Exception {
    assertNull(String.class.cast(null));
  }

  @Test
  public void cast_fail() throws Exception {
    Object o = "test";
    try {
      Integer i = Integer.class.cast(o);
      fail("Expected ClassCastException");
    } catch (ClassCastException e) {
      // expected
    }
  }

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
    assertTrue(float.class.isPrimitive());
    assertTrue(double.class.isPrimitive());
    assertTrue(char.class.isPrimitive());
    assertTrue(boolean.class.isPrimitive());
    assertTrue(void.class.isPrimitive());

    assertFalse(Byte.class.isPrimitive());
    assertFalse(Short.class.isPrimitive());
    assertFalse(Integer.class.isPrimitive());
    assertFalse(Long.class.isPrimitive());
    assertFalse(Float.class.isPrimitive());
    assertFalse(Double.class.isPrimitive());
    assertFalse(Character.class.isPrimitive());
    assertFalse(Boolean.class.isPrimitive());
    assertFalse(Void.class.isPrimitive());
  }

  @Test
  public void typeConstants() {
    assertSame(byte.class, Byte.TYPE);
    assertSame(short.class, Short.TYPE);
    assertSame(int.class, Integer.TYPE);
    assertSame(long.class, Long.TYPE);
    assertSame(float.class, Float.TYPE);
    assertSame(double.class, Double.TYPE);
    assertSame(char.class, Character.TYPE);
    assertSame(boolean.class, Boolean.TYPE);
    assertSame(void.class, Void.TYPE);
  }
}
