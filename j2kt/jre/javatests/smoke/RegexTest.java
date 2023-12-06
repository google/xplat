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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RegexTest {

  @Test
  public void testPatternPattern() {
    Pattern pattern = Pattern.compile("Hello.World");
    assertEquals("Hello.World", pattern.pattern());
  }

  @Test
  public void testFind() {
    Pattern pattern = Pattern.compile("Hello.World");
    Matcher matcher = pattern.matcher("~~~Hello-World~~~");
    assertTrue(matcher.find());
    assertEquals(3, matcher.start());
  }

  @Test
  public void testFlagTranslation() {
    Pattern pattern = Pattern.compile("Hello.#\nWorld", Pattern.COMMENTS);
    Matcher matcher = pattern.matcher("~~~Hello-World~~~");
    assertTrue(matcher.find());
    assertEquals(3, matcher.start());
  }

  @Test
  public void testReplace() {
    Pattern pattern = Pattern.compile("cat");
    Matcher matcher = pattern.matcher("one cat two cats in the yard");
    assertEquals("one dog two cats in the yard", matcher.replaceFirst("dog"));
    assertEquals("one dog two dogs in the yard", matcher.replaceAll("dog"));
  }

  @Test
  public void testSplit() {
    Pattern pattern = Pattern.compile("cat");
    String[] parts = pattern.split("one cat two cats in the yard", -1);
    assertEquals(3, parts.length);
    assertEquals("one ", parts[0]);
    assertEquals(" two ", parts[1]);
    assertEquals("s in the yard", parts[2]);

    parts = pattern.split("one cat two cats in the yard", 2);
    assertEquals(2, parts.length);
    assertEquals("one ", parts[0]);
    assertEquals(" two cats in the yard", parts[1]);
  }

  @Test
  public void testSplit_limit() {
    String string = "boo:and:foo";
    Pattern colon = Pattern.compile(":");
    assertArrayEquals(new String[] {"boo", "and:foo"}, colon.split(string, 2));
    assertArrayEquals(new String[] {"boo", "and", "foo"}, colon.split(string, 5));
    assertArrayEquals(new String[] {"boo", "and", "foo"}, colon.split(string, -2));

    Pattern o = Pattern.compile("o");
    assertArrayEquals(new String[] {"b", "", ":and:f", "", ""}, o.split(string, 5));
    assertArrayEquals(new String[] {"b", "", ":and:f", "", ""}, o.split(string, -2));
    assertArrayEquals(new String[] {"b", "", ":and:f"}, o.split(string, 0));
  }

  @Test
  public void testMatcherGroups() {
    Pattern pattern = Pattern.compile("(cat)|(dog)");
    Matcher matcher = pattern.matcher("The cat.");
    assertTrue(matcher.find());
    assertEquals(2, matcher.groupCount());
    assertEquals(4, matcher.start(1));
    assertEquals("cat", matcher.group(1));
    assertEquals(-1, matcher.start(2));
    assertNull(matcher.group(2));
  }
}
