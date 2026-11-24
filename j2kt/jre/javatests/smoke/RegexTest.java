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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.junit.Test;

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
  public void testSplit_zeroWidth() {
    Pattern empty = Pattern.compile("");
    assertArrayEquals(new String[] {"a", "b", "c"}, empty.split("abc"));
    assertArrayEquals(new String[] {"a", "b", "c", ""}, empty.split("abc", -1));
    assertArrayEquals(new String[] {"a", "b", "c"}, empty.split("abc", 3));
    assertArrayEquals(new String[] {"a", "bc"}, empty.split("abc", 2));
    assertArrayEquals(new String[] {""}, empty.split(""));
    assertArrayEquals(new String[] {""}, empty.split("", -1));
    assertArrayEquals(new String[] {""}, empty.split("", 2));

    Pattern wordBoundary = Pattern.compile("\\b");
    assertArrayEquals(new String[] {"a", " ", "b"}, wordBoundary.split("a b"));

    Pattern zeroWidthQuantifier = Pattern.compile("b*");
    assertArrayEquals(new String[] {"a", "", "a"}, zeroWidthQuantifier.split("aba"));
    assertArrayEquals(new String[] {"a", ""}, zeroWidthQuantifier.split("a", -1));
    assertArrayEquals(new String[] {"a"}, zeroWidthQuantifier.split("a", 0));
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

  @Test
  public void testPatternPattern_preservesOriginalWithFlags() {
    Pattern comments = Pattern.compile("Hello.#\nWorld", Pattern.COMMENTS);
    assertEquals("Hello.#\nWorld", comments.pattern());
    assertEquals("Hello.#\nWorld", comments.toString());

    Pattern dotall = Pattern.compile("a.b", Pattern.DOTALL | Pattern.UNIX_LINES);
    assertEquals("a.b", dotall.pattern());
    assertTrue(dotall.matcher("a\nb").matches());

    Pattern unicode = Pattern.compile("abc", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    assertEquals("abc", unicode.pattern());
    assertTrue(unicode.matcher("ABC").matches());
  }

  @Test
  public void testLiteralFlag() {
    Pattern literal = Pattern.compile("a.b[c]+(d)", Pattern.LITERAL);
    assertEquals("a.b[c]+(d)", literal.pattern());
    assertTrue(literal.matcher("a.b[c]+(d)").matches());
    assertFalse(literal.matcher("axbcd").matches());

    Pattern literalWithComments = Pattern.compile("a.b # keep", Pattern.LITERAL | Pattern.COMMENTS);
    assertTrue(literalWithComments.matcher("a.b # keep").matches());
    assertFalse(literalWithComments.matcher("a.b").matches());
  }

  @Test
  @SuppressWarnings("GoogleRegexCheck") // Intentionally tests java.util.regex.Pattern.CANON_EQ.
  public void testCanonEqFlag() {
    Pattern canonEq = Pattern.compile("\u00e9", Pattern.CANON_EQ);
    assertTrue(canonEq.matcher("e\u0301").matches());

    Pattern literalComposed = Pattern.compile("\u00e9", Pattern.LITERAL | Pattern.CANON_EQ);
    assertTrue(literalComposed.matcher("\u00e9").matches());
    assertFalse(literalComposed.matcher("e\u0301").matches());

    Pattern literalDecomposed = Pattern.compile("e\u0301", Pattern.LITERAL | Pattern.CANON_EQ);
    assertTrue(literalDecomposed.matcher("e\u0301").matches());
    assertFalse(literalDecomposed.matcher("\u00e9").matches());

    Pattern literalSingleDecomp = Pattern.compile("\u00c5", Pattern.LITERAL | Pattern.CANON_EQ);
    assertTrue(literalSingleDecomp.matcher("\u00c5").matches());
    assertFalse(literalSingleDecomp.matcher("A\u030a").matches());
  }

  @Test
  @SuppressWarnings("InvalidPatternSyntax")
  public void testUnknownFlagThrowsIllegalArgumentException() {
    int unknownFlag = 0x1000;
    IllegalArgumentException e =
        assertThrows(IllegalArgumentException.class, () -> Pattern.compile("a", unknownFlag));
    assertFalse(e instanceof PatternSyntaxException);

    int negativeFlag = -1;
    IllegalArgumentException neg =
        assertThrows(IllegalArgumentException.class, () -> Pattern.compile("a", negativeFlag));
    String negMessage = neg.getMessage();
    assertTrue(negMessage != null && negMessage.contains("0xffffffff"));
  }

  @Test
  public void testPatternSyntaxException_getMessage() {
    PatternSyntaxException e = new PatternSyntaxException("Unclosed", "\ta[", 2);
    assertEquals("Unclosed near index 2\n\ta[\n\t ^", e.getMessage());
  }

  @Test
  @SuppressWarnings("InvalidPatternSyntax")
  public void testPatternMatches_invalidSyntaxThrowsPatternSyntaxException() {
    String invalidRegex = "[";
    PatternSyntaxException e1 =
        assertThrows(PatternSyntaxException.class, () -> Pattern.matches(invalidRegex, "a"));
    assertNotNull(e1.getMessage());

    PatternSyntaxException e2 =
        assertThrows(PatternSyntaxException.class, () -> "a".matches(invalidRegex));
    assertNotNull(e2.getMessage());
  }

  @Test
  public void testFind_emptyInput() {
    Matcher exactEmpty = Pattern.compile("^$").matcher("");
    assertTrue(exactEmpty.find());
    assertEquals(0, exactEmpty.start());
    assertEquals(0, exactEmpty.end());
    assertEquals("", exactEmpty.group());
    assertFalse(exactEmpty.find());
    assertFalse(exactEmpty.find());

    Matcher optionalEmpty = Pattern.compile("^(FALSE)?$").matcher("");
    assertTrue(optionalEmpty.find());
    assertEquals("", optionalEmpty.group());
    assertFalse(optionalEmpty.find());
  }

  @Test
  public void testFind_zeroWidthMatches() {
    Matcher m1 = Pattern.compile("a*").matcher("b");
    assertTrue(m1.find());
    assertEquals(0, m1.start());
    assertEquals(0, m1.end());
    assertTrue(m1.find());
    assertEquals(1, m1.start());
    assertEquals(1, m1.end());
    assertFalse(m1.find());
    assertFalse(m1.find());

    Matcher m2 = Pattern.compile("a*").matcher("a");
    assertTrue(m2.find());
    assertEquals(0, m2.start());
    assertEquals(1, m2.end());
    assertEquals("a", m2.group());
    assertTrue(m2.find());
    assertEquals(1, m2.start());
    assertEquals(1, m2.end());
    assertEquals("", m2.group());
    assertFalse(m2.find());

    Matcher endAnchor = Pattern.compile("$").matcher("abc");
    assertTrue(endAnchor.find());
    assertEquals(3, endAnchor.start());
    assertEquals(3, endAnchor.end());
    assertFalse(endAnchor.find());
  }

  @Test
  public void testFind_withStartIndex() {
    Matcher empty = Pattern.compile("^$").matcher("");
    assertTrue(empty.find(0));
    assertEquals(0, empty.start());
    assertEquals(0, empty.end());
    assertFalse(empty.find());

    Matcher endAnchor = Pattern.compile("$").matcher("abc");
    assertTrue(endAnchor.find(3));
    assertEquals(3, endAnchor.start());
    assertEquals(3, endAnchor.end());
    assertFalse(endAnchor.find());

    Matcher m = Pattern.compile("c").matcher("abcd");
    m.region(0, 2);
    assertFalse(m.find());
    assertTrue(m.find(2));
    assertEquals(0, m.regionStart());
    assertEquals(4, m.regionEnd());
    assertEquals(2, m.start());

    assertThrows(IndexOutOfBoundsException.class, () -> m.find(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> m.find(5));
  }

  @Test
  public void testFind_doesNotWrapAfterFailure() {
    Matcher m = Pattern.compile("b").matcher("abc");
    assertTrue(m.find());
    assertEquals(1, m.start());
    assertFalse(m.find());
    assertFalse(m.find());
  }

  @Test
  public void testLookingAt_andSubsequentFind() {
    Matcher m1 = Pattern.compile("a*").matcher("b");
    assertTrue(m1.lookingAt());
    assertEquals(0, m1.start());
    assertEquals(0, m1.end());
    assertTrue(m1.find());
    assertEquals(1, m1.start());
    assertEquals(1, m1.end());
    assertFalse(m1.find());

    Matcher m2 = Pattern.compile("b").matcher("ab");
    assertFalse(m2.lookingAt());
    assertTrue(m2.find());
    assertEquals(1, m2.start());

    Matcher afterFindLookingAt = Pattern.compile("b").matcher("ab");
    assertTrue(afterFindLookingAt.find());
    assertEquals(1, afterFindLookingAt.start());
    assertFalse(afterFindLookingAt.lookingAt());
    assertFalse(afterFindLookingAt.find());

    Matcher afterFindMatches = Pattern.compile("b").matcher("ab");
    assertTrue(afterFindMatches.find());
    assertEquals(1, afterFindMatches.start());
    assertFalse(afterFindMatches.matches());
    assertFalse(afterFindMatches.find());
  }

  @Test
  public void testUsePattern_preservesZeroWidthAdvancement() {
    Matcher m = Pattern.compile("a*").matcher("b");
    assertTrue(m.find());
    assertEquals(0, m.start());
    assertEquals(0, m.end());

    m.usePattern(Pattern.compile("a*"));
    assertTrue(m.find());
    assertEquals(1, m.start());
    assertEquals(1, m.end());
    assertFalse(m.find());

    m.usePattern(Pattern.compile("a*"));
    assertFalse(m.find());
  }

  @Test
  public void testAppendReplacement_withRegionAndReset() {
    Matcher m = Pattern.compile("cat").matcher("cat xxcatyy cat");
    m.region(4, 11);
    StringBuilder sb = new StringBuilder();
    while (m.find()) {
      m.appendReplacement(sb, "dog");
    }
    m.appendTail(sb);
    assertEquals("cat xxdogyy cat", sb.toString());

    m.reset();
    StringBuilder afterReset = new StringBuilder();
    while (m.find()) {
      m.appendReplacement(afterReset, "dog");
    }
    m.appendTail(afterReset);
    assertEquals("dog xxdogyy dog", afterReset.toString());
  }

  @Test
  public void testSplit_zeroWidthEdgeCases() {
    assertArrayEquals(new String[] {"", "", ""}, Pattern.compile("a*").split("a", -1));
    assertArrayEquals(new String[] {}, Pattern.compile("a*").split("a", 0));
    assertArrayEquals(new String[] {}, Pattern.compile("a").split("a", 0));
    assertArrayEquals(new String[] {"a"}, Pattern.compile("^").split("a", -1));
    assertArrayEquals(
        new String[] {"a", "b", "c", ""}, Pattern.compile("").split("abc", Integer.MAX_VALUE));
  }
}
