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

import static smoke.Asserts.assertEquals;
import static smoke.Asserts.assertFalse;
import static smoke.Asserts.assertTrue;

import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import jsinterop.annotations.JsNonNull;
import org.jspecify.nullness.Nullable;

@SuppressWarnings("CollectionIncompatibleType")  // To test runtime behavior for incompatible types
public class Collections {

  public static void testCollections() {
    testJavaMapSignatures();
    testAbstractMapSubclass_bridgedOverridesAreCalled();
    testToArrayNativeList();
    testToArrayPolymorphism();
  }

  private static void testJavaMapSignatures() {
    // Map and HashMap are mapped to separate native types, we test bridged methods with each type
    // as the target because they are dispatched statically.
    HashMap<String, Integer> hashMap = new HashMap<>();
    Map<String, Integer> map = hashMap;
    map.put("a", 1);
    hashMap.put("b", 2);

    assertEquals((Object) 1, map.get("a"));
    assertEquals((Object) 1, hashMap.get("a"));
    assertEquals((Object) 2, map.get((Object) "b"));
    assertEquals((Object) 2, hashMap.get((Object) "b"));
    assertEquals(null, map.get("c"));
    assertEquals(null, hashMap.get("c"));
    assertEquals(null, map.get(new Object()));
    assertEquals(null, hashMap.get(new Object()));

    assertTrue(map.containsKey("b"));
    assertTrue(hashMap.containsKey("b"));
    assertTrue(map.containsKey((Object) "a"));
    assertTrue(hashMap.containsKey((Object) "a"));
    assertFalse(map.containsKey("c"));
    assertFalse(hashMap.containsKey("c"));
    assertFalse(map.containsKey(new Object()));
    assertFalse(hashMap.containsKey(new Object()));

    assertTrue(map.containsValue(1));
    assertTrue(hashMap.containsValue(1));
    assertTrue(map.containsValue((Object) 2));
    assertTrue(hashMap.containsValue((Object) 2));
    assertFalse(map.containsValue(3));
    assertFalse(hashMap.containsValue(3));
    assertFalse(map.containsValue(new Object()));
    assertFalse(hashMap.containsValue(new Object()));

    assertEquals(null, map.remove(new Object()));
    assertEquals(null, hashMap.remove(new Object()));

    assertEquals((Object) 1, map.remove("a"));
    assertEquals(1, map.size());
    map.put("a", 1);
    assertEquals((Object) 1, hashMap.remove("a"));
    assertEquals(1, map.size());

    assertEquals((Object) 2, map.remove((Object) "b"));
    assertEquals(0, map.size());
    map.put("b", 2);
    assertEquals((Object) 2, hashMap.remove((Object) "b"));
    assertEquals(0, hashMap.size());
  }

  private static class TestMap<K, V> extends AbstractMap<K, V> {

    private final Set<@JsNonNull Entry<K, V>> entrySet = new HashSet<>();

    public int getCalls = 0;
    public int containsKeyCalls = 0;
    public int containsValueCalls = 0;
    public int removeCalls = 0;

    @Override
    public Set<@JsNonNull Entry<K, V>> entrySet() {
      return entrySet;
    }

    @Override
    public @Nullable V get(@Nullable Object key) {
      getCalls++;
      return super.get(key);
    }

    @Override
    public boolean containsKey(@Nullable Object key) {
      containsKeyCalls++;
      return super.containsKey(key);
    }

    @Override
    public boolean containsValue(@Nullable Object value) {
      containsValueCalls++;
      return super.containsValue(value);
    }

    @Override
    public @Nullable V remove(@Nullable Object key) {
      removeCalls++;
      return super.remove(key);
    }
  }

  private static void testAbstractMapSubclass_bridgedOverridesAreCalled() {
    TestMap<Integer, String> testMap = new TestMap<>();
    Map<Integer, String> map = testMap; // This will be typed as plain MutableMap in Kotlin output

    assertEquals(null, map.get(1));
    assertEquals(1, testMap.getCalls);

    assertEquals(null, map.get((Object) 2));
    assertEquals(2, testMap.getCalls);

    assertEquals(false, map.containsKey(3));
    assertEquals(1, testMap.containsKeyCalls);

    assertEquals(false, map.containsKey((Object) 4));
    assertEquals(2, testMap.containsKeyCalls);

    assertEquals(false, map.containsValue(5));
    assertEquals(1, testMap.containsValueCalls);

    assertEquals(false, map.containsValue((Object) 6));
    assertEquals(2, testMap.containsValueCalls);

    assertEquals(null, map.remove(7));
    assertEquals(1, testMap.removeCalls);

    assertEquals(null, map.remove((Object) 8));
    assertEquals(2, testMap.removeCalls);
  }

  private static void testToArrayNativeList() {
    Collection<String> stringCollection = Arrays.asList("Hello", "World");
    String[] stringArray1 = new String[1];
    String[] stringArray2 = new String[2];
    String[] stringArray3 = new String[3];

    Object[] toArrayResult = stringCollection.toArray();
    assertEquals(new Object[] {"Hello", "World"}, toArrayResult);
    assertEquals(Object[].class, toArrayResult.getClass());

    String[] toStringArrayResult = stringCollection.toArray(stringArray1);
    assertTrue(toStringArrayResult != stringArray1);
    assertEquals(new String[] {"Hello", "World"}, toStringArrayResult);
    assertEquals(stringArray1.getClass(), toStringArrayResult.getClass());

    assertTrue(stringArray2 == stringCollection.toArray(stringArray2));
    assertEquals(new String[] {"Hello", "World"}, stringArray2);

    stringArray3[2] = "unmodified"; // We expect this to get nulled by toArray.
    assertTrue(stringArray3 == stringCollection.toArray(stringArray3));
    assertEquals(new String[] {"Hello", "World", null}, stringArray3);
  }

  private static class TestList<E> extends AbstractList<E> {

    private final E theElement;
    public int toArrayCalls = 0;
    public int toArrayTypedCalls = 0;

    public TestList(E theElement) {
      this.theElement = theElement;
    }

    @Override
    public E get(int index) {
      return theElement;
    }

    @Override
    public int size() {
      return 1;
    }

    @Override
    public void add(int index, E element) {
      throw new UnsupportedOperationException("TODO there should be a bridge");
    }

    @Override
    public E set(int index, E element) {
      throw new UnsupportedOperationException("TODO there should be a bridge");
    }

    @Override
    public E remove(int index) {
      throw new UnsupportedOperationException("TODO there should be a bridge");
    }

    // The following overrides are only there to check that the override (with the Java signatures
    // of the method) compiles.
    @Override
    public boolean contains(@Nullable Object o) {
      return super.contains(o);
    }

    @Override
    public boolean remove(@Nullable Object o) {
      return super.remove(o);
    }

    @Override
    public int indexOf(@Nullable Object o) {
      return super.indexOf(o);
    }

    @Override
    public int lastIndexOf(@Nullable Object o) {
      return super.lastIndexOf(o);
    }

    @Override
    public Object[] toArray() {
      toArrayCalls++;
      int savedToArrayTypedCalls = toArrayTypedCalls;
      try {
        return super.toArray();
      } finally {
        // Restore number of toArray(T[] a) calls, to ignore potential calls from super.toArray().
        toArrayTypedCalls = savedToArrayTypedCalls;
      }
    }

    @Override
    public <T> T[] toArray(T[] a) {
      toArrayTypedCalls++;
      return super.toArray(a);
    }
  }

  private static void testToArrayPolymorphism() {
    TestList<String> testList = new TestList<>("content");

    Object[] untypedResult = testList.toArray();
    assertEquals(Object[].class, untypedResult.getClass());
    assertTrue(Arrays.equals(new Object[] {"content"}, untypedResult));
    assertEquals(1, testList.toArrayCalls);

    String[] typedResult = testList.toArray(new String[0]);
    assertEquals(1, testList.toArrayTypedCalls);
    // Note: new String[0].getClass() on JVM will result in String[].class on Kotlin JVM at runtime.
    // Whereas String[].class would result in Object[].class (there is no Kotlin syntax for
    // String[].class).
    assertEquals(new String[0].getClass(), typedResult.getClass());
    assertTrue(Arrays.equals(new String[] {"content"}, typedResult));
  }
}
