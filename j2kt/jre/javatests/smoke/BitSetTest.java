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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.BitSet;
import org.junit.Test;

/** Smoke test for BitSet. */
public final class BitSetTest {

  @Test
  public void testBitSets() {
    BitSet bitset = new BitSet();
    bitset.set(100, true);
    
    assertTrue(bitset.get(100));
    assertTrue(!bitset.get(101));
    assertTrue(!bitset.get(99));

    assertTrue(bitset instanceof Cloneable);
  }

  @Test
  public void testClearRangeBeyondLength() {
    BitSet bitset = new BitSet();
    bitset.set(5);
    bitset.set(200);
    bitset.clear(200);
    bitset.clear(64, 1000);
    assertTrue(bitset.get(5));
    assertEquals(1, bitset.cardinality());

    BitSet presized = new BitSet(200);
    presized.set(5);
    presized.clear(64, 1000);
    assertTrue(presized.get(5));
    assertEquals(1, presized.cardinality());

    presized.clear(10, 1000);
    assertTrue(presized.get(5));
    assertEquals(1, presized.cardinality());

    BitSet empty = new BitSet(200);
    empty.clear(64, 1000);
    assertTrue(empty.isEmpty());
    assertFalse(empty.get(5));
  }
}
