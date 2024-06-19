/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package java.text;

import java.util.Comparator;
import java.util.Locale;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * An implementation of java.text.Collator for Kotlin/native. Derived from <a
 * href="https://docs.oracle.com/javase/8/docs/api/java/text/Collator.html">java.text.Collator</a>.
 *
 * <p>All ICU references have been replaced by calls to native ios functionality via {@link
 * IOSCollator}.
 */
@NullMarked
public abstract class Collator implements Comparator<Object>, Cloneable {
  public static final int NO_DECOMPOSITION = 0;

  public static final int CANONICAL_DECOMPOSITION = 1;

  public static final int FULL_DECOMPOSITION = 2;

  public static final int PRIMARY = 0;

  public static final int SECONDARY = 1;

  public static final int TERTIARY = 2;

  public static final int IDENTICAL = 3;

  @Override
  public int compare(Object object1, Object object2) {
    return compare((String) object1, (String) object2);
  }

  public abstract int compare(String string1, String string2);

  public boolean equals(String string1, String string2) {
    return compare(string1, string2) == 0;
  }

  @Override
  public abstract boolean equals(@Nullable Object other);

  public abstract CollationKey getCollationKey(String string);

  public abstract int getDecomposition();

  public static Collator getInstance() {
    return getInstance(Locale.getDefault());
  }

  public static Collator getInstance(Locale locale) {
    return new IOSCollator(locale);
  }

  public abstract int getStrength();

  @Override
  public abstract int hashCode();

  public abstract void setDecomposition(int value);

  public abstract void setStrength(int value);
}
