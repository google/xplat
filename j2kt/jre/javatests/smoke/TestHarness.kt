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
  @Test fun testArrayCopy() = SystemTest.testArrayCopy()
  @Test fun testArrays() = ArraysTest.testArrays()
  @Test fun testAtomic() = AtomicTest.testAtomic()
  @Test fun testBitSets() = BitSetTest.testBitSets()
  @Test fun testCharacter() = CharacterTest.testCharacter()
  @Test fun testCollections() = CollectionsTest.testCollections()
  @Test fun testHashCode() = SystemTest.testHashCode()
  @Test fun testLogging() = LoggingTest.testLogging()
  @Test fun testMath() = MathTest.testMath()
  @Test fun testPrimitives() = PrimitivesTest.testPrimitives()
  @Test fun testRandom() = RandomTest.testRandom()
  @Test fun testReflectArray() = ReflectArrayTest.testReflectArray()
  @Test fun testRegex() = RegexTest.testRegex()
  @Test fun testSecurity() = SecurityTest.testSecurity()
  @Test fun testStrings() = StringTest.testStrings()
  @Test fun testSystemTime() = SystemTest.testSystemTime()
  @Test fun testThrowables() = ThrowableTest.testThrowables()
}
