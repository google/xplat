/*
 * Copyright (C) 2025 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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

import java.util.Locale;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LocaleTest {

  private static final Locale DEFAULT_LOCALE = Locale.getDefault();

  @Before
  public void setUp() {
    Locale.setDefault(DEFAULT_LOCALE);
  }

  @Test
  public void locale() {
    assertEquals("en", Locale.ENGLISH.getLanguage());
    assertEquals("", Locale.ENGLISH.getCountry());
    assertEquals("en", Locale.ENGLISH.toString());
    assertEquals("en", Locale.ENGLISH.toLanguageTag());

    assertEquals("en", Locale.US.getLanguage());
    assertEquals("US", Locale.US.getCountry());
    assertEquals("en_US", Locale.US.toString());
    assertEquals("en-US", Locale.US.toLanguageTag());

    assertEquals("", Locale.ROOT.toString());
    assertEquals("", Locale.ROOT.getLanguage());
    assertEquals("", Locale.ROOT.getCountry());
    assertEquals("und", Locale.ROOT.toLanguageTag());
  }

  @Test
  public void forLanguageTag() {
    Locale en = Locale.forLanguageTag("en");
    assertEquals("en", en.getLanguage());
    assertEquals("", en.getCountry());
    assertEquals("en", en.toLanguageTag());

    Locale enUS = Locale.forLanguageTag("en-US");
    assertEquals("en", enUS.getLanguage());
    assertEquals("US", enUS.getCountry());
    assertEquals("en-US", enUS.toLanguageTag());

    Locale root = Locale.forLanguageTag("");
    assertEquals("", root.getLanguage());
    assertEquals("", root.getCountry());
    assertEquals("und", root.toLanguageTag());

    Locale deCH = Locale.forLanguageTag("de-CH");
    assertEquals("de", deCH.getLanguage());
    assertEquals("CH", deCH.getCountry());
    assertEquals("de-CH", deCH.toLanguageTag());

    Locale zhHans = Locale.forLanguageTag("zh-Hans");
    assertEquals("zh", zhHans.getLanguage());
    assertEquals("Hans", zhHans.getScript());
    assertEquals("", zhHans.getCountry());
    assertEquals("zh-Hans", zhHans.toLanguageTag());
  }

  @Test
  @SuppressWarnings("SelfAssertion")
  public void equals() {
    assertEquals(Locale.ENGLISH, Locale.ENGLISH);
    assertEquals(Locale.US, Locale.US);
    assertEquals(Locale.ROOT, Locale.ROOT);

    assertNotEquals(Locale.ENGLISH, Locale.US);
    assertNotEquals(Locale.ENGLISH, Locale.ROOT);
    assertNotEquals(Locale.US, Locale.ROOT);

    assertEquals(Locale.ENGLISH, Locale.forLanguageTag("en"));
    assertEquals(Locale.US, Locale.forLanguageTag("en-US"));
    assertEquals(Locale.ROOT, Locale.forLanguageTag(""));

    assertNotEquals(Locale.ENGLISH, Locale.forLanguageTag("en-US"));
    assertNotEquals(Locale.ENGLISH, Locale.forLanguageTag(""));
    assertNotEquals(Locale.US, Locale.forLanguageTag(""));

    assertEquals(Locale.forLanguageTag("de-CH"), Locale.forLanguageTag("de-CH"));
  }

  @Test
  public void setDefault() {
    Locale.setDefault(Locale.US);
    assertEquals(Locale.US, Locale.getDefault());
  }
}
