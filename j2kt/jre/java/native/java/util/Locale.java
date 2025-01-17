/*
 * Copyright 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package java.util;

import static javaemul.internal.KtNativeUtils.ktNative;

import javaemul.internal.annotations.KtNative;
import javaemul.internal.annotations.KtProperty;
import org.jspecify.annotations.NullMarked;

/**
 * A very simple emulation of Locale for shared-code patterns like {@code
 * String.toUpperCase(Locale.US)}.
 *
 * <p>Note: Any changes to this class should put into account the assumption that was made in rest
 * of the JRE emulation.
 */
@KtNative
@NullMarked
public class Locale {

  public static final Locale ROOT = ktNative();
  public static final Locale ENGLISH = ktNative();
  public static final Locale US = ktNative();

  @KtProperty
  public native String getLanguage();

  @KtProperty
  public native String getScript();

  @KtProperty
  public native String getCountry();

  @KtProperty
  public native String getVariant();

  public native String toLanguageTag();

  /**
   * Returns an instance that represents the browser's default locale (not necessarily the one
   * defined by 'gwt.locale').
   */
  public static native Locale getDefault();

  public static native void setDefault(Locale newLocale);

  public static native Locale forLanguageTag(String languageTag);

  // Hidden as we don't support manual creation of Locales.
  private Locale() {}
}
