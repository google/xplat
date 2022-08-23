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
package smoke

import kotlin.test.Test

class TestHarness {
  @Test fun testMath() = Math.testMath();
  @Test fun testPrimitives() = Primitives.testPrimitives();
  @Test fun testString() = Strings.testString();
  @Test fun testStringBuilder() = Strings.testStringBuilder();
  @Test fun testJavaEmul() = Strings.testJavaEmul();
  @Test fun testReflectArray() = ReflectArray.testReflectArray();
  @Test fun testRegex() = Regex.testRegex();
  @Test fun testSystemTime() = LangSystem.testSystemTime();
  @Test fun testArrayCopy() = LangSystem.testArrayCopy();
  @Test fun testHashCode() = LangSystem.testHashCode();
  @Test fun testRandom() = RandomNumbers.testRandom();
  @Test fun testArrays() = Array.testArrays();
  @Test fun testCollections() = Collections.testCollections();
}
