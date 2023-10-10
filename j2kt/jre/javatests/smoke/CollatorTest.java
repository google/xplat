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
import static org.junit.Assert.assertNotEquals;

import java.text.Collator;
import java.util.Locale;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CollatorTest {

  @Test
  public void comparePrimary() {
    Collator collator = Collator.getInstance(Locale.US);
    assertEquals(0, collator.getStrength(), Collator.PRIMARY); // Default strength

    assertEquals(0, collator.compare("a", "á"));
    assertEquals(0, collator.compare("a", "A"));
    assertEquals(0, collator.compare("a", "a"));
    assertNotEquals(0, collator.compare("a", "b"));
  }

  @Test
  public void compareSecondary() {
    Collator collator = Collator.getInstance(Locale.US);
    collator.setStrength(Collator.SECONDARY);

    assertNotEquals(0, collator.compare("a", "á"));
    assertEquals(0, collator.compare("a", "A"));
    assertEquals(0, collator.compare("a", "a"));
    assertNotEquals(0, collator.compare("a", "b"));
  }

  @Test
  public void compareTertiary() {
    Collator collator = Collator.getInstance(Locale.US);
    collator.setStrength(Collator.TERTIARY);

    assertNotEquals(0, collator.compare("a", "á"));
    assertNotEquals(0, collator.compare("a", "A"));
    assertEquals(0, collator.compare("a", "a"));
    assertNotEquals(0, collator.compare("a", "b"));
  }

  @Test
  public void compareNoDecomposition() {
    Collator collator = Collator.getInstance(Locale.US);
    collator.setStrength(Collator.PRIMARY);
    collator.setDecomposition(Collator.NO_DECOMPOSITION);

    assertNotEquals(0, collator.compare("ℌ", "H"));
  }

  @Test
  public void compareCanonicalDecomposition() {
    Collator collator = Collator.getInstance(Locale.US);
    collator.setDecomposition(Collator.PRIMARY);
    collator.setDecomposition(Collator.NO_DECOMPOSITION);

    assertEquals(0, collator.compare("ℌ", "H"));
  }

  @Test
  public void compareCollationKey() {
    Collator collator = Collator.getInstance(Locale.US);
    assertEquals(0, collator.getCollationKey("a").compareTo(collator.getCollationKey("A")));
  }
}
