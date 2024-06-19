/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package java.text;

import java.util.Locale;
import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/** JRE Collator stub; See IOSCollator.kt for the actual stub. */
@NullMarked
@KtNative(name = "java.text.IOSCollator")
class IOSCollator extends Collator {
  IOSCollator(Locale locale) {}

  @Override
  public native int compare(String string1, String string2);

  @Override
  public native int getDecomposition();

  @Override
  public native int getStrength();

  @Override
  public native void setDecomposition(int value);

  @Override
  public native void setStrength(int value);

  @Override
  public native boolean equals(@Nullable Object obj);

  @Override
  public native CollationKey getCollationKey(String string);

  @Override
  public native int hashCode();

  @Override
  public native Object clone();
}
