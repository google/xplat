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

import static smoke.AssertsBase.assertTrue;

import java.util.BitSet;

/** Smoke test for BitSet. */
public final class BitSets {

  private BitSets() {}

  public static void testBitSets() {
    BitSet bitset = new BitSet();
    bitset.set(100, true);
    assertTrue(bitset.get(100));
    assertTrue(!bitset.get(101));
    assertTrue(!bitset.get(99));
  }
}