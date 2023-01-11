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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ReflectArrayTest {

  @Test
  public void testGetLength() {
    assertEquals(0, Array.getLength(new byte[0]));
    assertEquals(1, Array.getLength(new boolean[1]));
    assertEquals(2, Array.getLength(new String[2]));
    assertEquals(3, Array.getLength(new int[3]));
  }

  @Test
  public void testNewInstance() {
    Object testArray = Array.newInstance(String.class, 5);
    assertEquals(5, ((Object[]) testArray).length);
    assertEquals(
        new String[0].getClass().getComponentType(), testArray.getClass().getComponentType());
    assertEquals(new String[5], (String[]) testArray);

    assertTrue(Array.newInstance(byte.class, 1) instanceof byte[]);
    assertTrue(Array.newInstance(short.class, 1) instanceof short[]);
    assertTrue(Array.newInstance(char.class, 1) instanceof char[]);
    assertTrue(Array.newInstance(int.class, 1) instanceof int[]);
    assertTrue(Array.newInstance(long.class, 1) instanceof long[]);
    assertTrue(Array.newInstance(float.class, 1) instanceof float[]);
    assertTrue(Array.newInstance(double.class, 1) instanceof double[]);
    assertTrue(Array.newInstance(boolean.class, 1) instanceof boolean[]);

    assertTrue(Array.newInstance(Byte.class, 1) instanceof Object[]);
    assertTrue(Array.newInstance(Short.class, 1) instanceof Object[]);
    assertTrue(Array.newInstance(Character.class, 1) instanceof Object[]);
    assertTrue(Array.newInstance(Integer.class, 1) instanceof Object[]);
    assertTrue(Array.newInstance(Long.class, 1) instanceof Object[]);
    assertTrue(Array.newInstance(Float.class, 1) instanceof Object[]);
    assertTrue(Array.newInstance(Double.class, 1) instanceof Object[]);
    assertTrue(Array.newInstance(Boolean.class, 1) instanceof Object[]);
  }
}
