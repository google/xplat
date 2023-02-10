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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  ArraysTest.class,
  AtomicTest.class,
  BitSetTest.class,
  CharacterTest.class,
  CollectionsTest.class,
  ErasureTest.class,
  IoTest.class,
  LoggingTest.class,
  MathTest.class,
  PrimitivesTest.class,
  RandomTest.class,
  ReflectArrayTest.class,
  RegexTest.class,
  SecurityTest.class,
  StringTest.class,
  SystemTest.class,
  ThrowableTest.class,
})
public class TestHarness {}
