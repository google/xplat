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

import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
@SuppressWarnings("unchecked") // The test is about unchecked casts.
public class ErasureTest {
  @Test
  public void testNoErasure() {
    List<Object> objectList = new ArrayList<>();
    objectList.add("foo");

    List<String> stringList = (List) objectList;
    String unused = stringList.get(0);
  }

  // TODO: ClassCastException is not thrown on Kotlin Native.
  // Uncomment when fixed, either by a workaround in transpiler or a fix in Kotlin Native compiler.
  // @Test
  // public void testTypeErasure() {
  //   List<Object> objectList = new ArrayList<>();
  //   objectList.add(new Object());
  //
  //   List<String> stringList = (List) objectList;
  //
  //   try {
  //     String unused = stringList.get(0);
  //     throw new AssertionError("Expected ClassCastException");
  //   } catch (ClassCastException e) {
  //     // OK
  //   }
  // }

  @Test
  public void testNullabilityErasure() {
    List<@Nullable String> stringList = new ArrayList<>();
    stringList.add(null);

    List<String> nonNullStringList = (List) stringList;

    String string = nonNullStringList.get(0);
    try {
      int unused = string.length();
      throw new AssertionError("Expected NullPointerException");
    } catch (NullPointerException e) {
      // OK
    }
  }

  // TODO: Crashes on Kotlin Native.
  // Uncomment when fixed, either by a workaround in transpiler or a fix in Kotlin Native compiler.
  // @Test
  // public void testNullabilityUnboxingErasure() {
  //   List<@Nullable Boolean> booleanList = new ArrayList<>();
  //   booleanList.add(null);
  //
  //   List<Boolean> nonNullBooleanList = (List) booleanList;
  //
  //   try {
  //     boolean unused = nonNullBooleanList.get(0);
  //     throw new AssertionError("Expected NullPointerException");
  //   } catch (NullPointerException e) {
  //     // OK
  //   }
  // }
}
