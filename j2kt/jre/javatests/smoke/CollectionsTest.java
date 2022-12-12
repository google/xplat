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

import static java.util.Comparator.reverseOrder;
import static smoke.Asserts.assertEquals;
import static smoke.Asserts.assertFalse;
import static smoke.Asserts.assertNull;
import static smoke.Asserts.assertTrue;

import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.Spliterator;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import org.jspecify.nullness.Nullable;

@SuppressWarnings("CollectionIncompatibleType") // To test runtime behavior for incompatible types
public class CollectionsTest {

  private CollectionsTest() {}

  public static void testCollections() {
    testAbstractMapSubclass_bridgedOverridesAreCalled();
    testArrayDeque();
    testIdentityHashMap();
    testJavaMapSignatures();
    testMapMerge();
    testToArrayNativeList();
    testToArrayPolymorphism();
    testListSort();
    testListSortComparator();
    testEnumSet();
    testEnumMap();
    testRemoveIf();
    testStack();
    testTreeMap();
    testTreeSet();
    testVector();
  }

  private static void testArrayDeque() {
    ArrayDeque<String> deque = new ArrayDeque<>();
    assertEquals(0, deque.size());
    deque.add("a");
    assertEquals(1, deque.size());
    assertEquals("a", deque.peekFirst());
    assertEquals("a", deque.peekLast());

    deque.add("o");
    assertEquals(2, deque.size());

    assertEquals("a", deque.peekFirst());
    assertEquals("o", deque.peekLast());
    assertEquals("o", deque.pollLast());

    assertEquals(1, deque.size());

    assertEquals("a", deque.peekFirst());
    assertEquals("a", deque.peekLast());
    assertEquals("a", deque.removeFirst());

    assertEquals(0, deque.size());
  }

  private static void testJavaMapSignatures() {
    // Map and HashMap are mapped to separate native types, we test bridged methods with each type
    // as the target because they are dispatched statically.
    HashMap<String, Integer> hashMap = new HashMap<>();
    AbstractMap<String, Integer> abstractMap = hashMap; // Checking the class hierarchy
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

  private static void testMapMerge() {
    Map<String, Integer> map = new HashMap<>();
    BiFunction<Integer, Integer, Integer> adder = (a, b) -> a + b;

    map.merge("a", 1, adder);
    map.merge("b", 1, adder);
    map.merge("a", 2, adder);
    map.merge("a", 3, adder);

    assertEquals(2, map.size());
    assertEquals(1 + 2 + 3, map.get("a"));
    assertEquals(1, map.get("b"));

    map.merge("a", 42, (a, b) -> null);
    assertEquals(1, map.size());
  }

  private static class TestMap<K, V> extends AbstractMap<K, V> {

    private final Set<Entry<K, V>> entrySet = new HashSet<>();

    public int getCalls = 0;
    public int containsKeyCalls = 0;
    public int containsValueCalls = 0;
    public int removeCalls = 0;

    @Override
    public Set<Entry<K, V>> entrySet() {
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

    // The following overrides are only there to check that the override (with the Java signatures
    // of the method) compiles.
    @Override
    public V compute(
        K key, BiFunction<? super K, ? super @Nullable V, ? extends V> remappingFunction) {
      return super.compute(key, remappingFunction);
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
      return super.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public V computeIfPresent(
        K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
      return super.computeIfPresent(key, remappingFunction);
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
      super.forEach(action);
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
      return super.getOrDefault(key, defaultValue);
    }

    @Override
    public @Nullable V merge(
        K key, V value, BiFunction<? super V, ? super V, ? extends @Nullable V> remap) {
      return super.merge(key, value, remap);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
      super.putAll(m);
    }

    @Override
    public V putIfAbsent(K key, V value) {
      return super.putIfAbsent(key, value);
    }

    @Override
    public boolean remove(@Nullable Object key, @Nullable Object value) {
      return super.remove(key, value);
    }

    @Override
    public V replace(K key, V value) {
      return super.replace(key, value);
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
      return super.replace(key, oldValue, newValue);
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
      super.replaceAll(function);
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
    @Nullable String[] stringArray3 = new @Nullable String[3];

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
    assertEquals(new @Nullable String[] {"Hello", "World", null}, stringArray3);
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

    // The following overrides are only there to check that the override (with the Java signatures
    // of the method) compiles.
    @Override
    public boolean addAll(Collection<? extends E> c) {
      return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
      return super.addAll(index, c);
    }

    @Override
    public boolean contains(@Nullable Object o) {
      return super.contains(o);
    }

    @Override
    public boolean containsAll(Collection<? extends @Nullable Object> c) {
      return super.containsAll(c);
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
    public Stream<E> parallelStream() {
      return super.parallelStream();
    }

    @Override
    public boolean remove(@Nullable Object o) {
      return super.remove(o);
    }

    @Override
    public boolean removeAll(Collection<? extends @Nullable Object> c) {
      return super.removeAll(c);
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
      return super.removeIf(filter);
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
      super.replaceAll(operator);
    }

    @Override
    public boolean retainAll(Collection<? extends @Nullable Object> c) {
      return super.retainAll(c);
    }

    @Override
    public Spliterator<E> spliterator() {
      return super.spliterator();
    }

    @Override
    public @Nullable Object[] toArray() {
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
    public <T extends @Nullable Object> T[] toArray(T[] a) {
      toArrayTypedCalls++;
      return super.toArray(a);
    }
  }

  private static void testRemoveIf() {
    ArrayList<String> list = new ArrayList<>();
    list.add("A");
    list.add("B");
    assertFalse(list.removeIf(e -> e.equals("C")));
    assertEquals(2, list.size());

    assertTrue(list.removeIf(e -> e.equals("A")));
    assertEquals(1, list.size());
  }

  private static void testStack() {
    Stack<String> stack = new Stack<>();
    assertEquals(0, stack.size());
    stack.push("A");
    assertEquals(1, stack.size());
    stack.push("B");
    assertEquals(2, stack.size());

    assertEquals("B", stack.peek());
    assertEquals(2, stack.size());
    assertEquals("B", stack.pop());
    assertEquals(1, stack.size());

    assertEquals("A", stack.peek());
    assertEquals(1, stack.size());
    assertEquals("A", stack.pop());
    assertEquals(0, stack.size());
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

  private static void testVector() {
    Vector<String> list = new Vector<>();
    list.add("A");
    list.add("B");
    assertFalse(list.removeIf(e -> e.equals("C")));
    assertEquals(2, list.size());

    assertTrue(list.removeIf(e -> e.equals("A")));
    assertEquals(1, list.size());
  }

  private static void testListSort() {
    List<Integer> list = new ArrayList<>();
    list.add(1);
    list.add(4);
    list.add(2);
    list.add(3);
    list.sort(null);
    assertEquals(new Integer[] {1, 2, 3, 4}, list.toArray());
  }

  private static void testListSortComparator() {
    List<Integer> list = new ArrayList<>();
    list.add(1);
    list.add(4);
    list.add(2);
    list.add(3);
    list.sort(reverseOrder());
    assertEquals(new Integer[] {4, 3, 2, 1}, list.toArray());
  }

  enum Fruit {
    APPLE,
    ORANGE
  }

  private static void testEnumSet() {
    EnumSet<Fruit> enumSet = EnumSet.of(Fruit.APPLE);
    assertTrue(enumSet.contains(Fruit.APPLE));
    assertFalse(enumSet.contains(Fruit.ORANGE));
  }

  private static void testEnumMap() {
    EnumMap<Fruit, String> enumMap = new EnumMap<>(Fruit.class);
    enumMap.put(Fruit.APPLE, "apple");
    assertEquals(enumMap.get(Fruit.APPLE), "apple");
    assertEquals(enumMap.get(Fruit.ORANGE), null);
  }

  private static void testTreeMap() {
    NavigableMap<String, String> map = new TreeMap<>();
    map.put("foo", "1");
    map.put("bar", "2");
    map.put("baz", "3");
    assertEquals("1", map.get("foo"));
    assertEquals(new String[] {"bar", "baz", "foo"}, map.keySet().toArray());
  }

  private static void testTreeSet() {
    NavigableSet<Integer> set = new TreeSet<>();
    set.add(3);
    set.add(1);
    set.add(4);
    set.add(1);
    set.add(5);
    assertEquals(new Integer[] {1, 3, 4, 5}, set.toArray());
  }

  private static class IdentityHashMapTestObject {
    private final int i;

    IdentityHashMapTestObject(int i) {
      this.i = i;
    }

    @Override
    public boolean equals(@Nullable Object that) {
      if (that == null || !(that instanceof IdentityHashMapTestObject)) {
        return false;
      }

      return this.i == ((IdentityHashMapTestObject) that).i;
    }

    @Override
    public int hashCode() {
      return Integer.hashCode(i);
    }
  }

  private static void testIdentityHashMap() {
    IdentityHashMap<IdentityHashMapTestObject, String> identityHashMap = new IdentityHashMap<>();
    IdentityHashMapTestObject i1 = new IdentityHashMapTestObject(1);
    IdentityHashMapTestObject i1Prime = new IdentityHashMapTestObject(1);
    IdentityHashMapTestObject i2 = new IdentityHashMapTestObject(2);
    IdentityHashMapTestObject i3 = new IdentityHashMapTestObject(3);

    identityHashMap.put(i1, "a");
    identityHashMap.put(i1Prime, "b");
    identityHashMap.put(i2, "c");
    identityHashMap.put(i1, "e");

    assertEquals(3, identityHashMap.size());
    assertEquals("e", identityHashMap.get(i1));
    assertEquals("b", identityHashMap.get(i1Prime));
    assertEquals("c", identityHashMap.get(i2));
    assertNull(identityHashMap.get(new IdentityHashMapTestObject(1)));
  }
}
