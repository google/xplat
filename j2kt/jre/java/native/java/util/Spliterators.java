/*
 * Copyright 2016 Google Inc.
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

import static javaemul.internal.InternalPreconditions.checkCriticalElement;
import static javaemul.internal.InternalPreconditions.checkCriticalState;
import static javaemul.internal.InternalPreconditions.checkNotNull;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Spliterators.html">the official
 * Java API doc</a> for details.
 *
 * <p>Since it's hard to implement parallel algorithms in the browser environment and to keep code
 * simple, implementation does not provide splitting.
 */
@NullMarked
public final class Spliterators {

  private abstract static class BaseSpliterator<
          T extends @Nullable Object, S extends Spliterator<T>>
      implements Spliterator<T> {
    private final int characteristics;
    private long sizeEstimate;

    BaseSpliterator(long size, int characteristics) {
      this.sizeEstimate = size;
      this.characteristics = (characteristics & Spliterator.SIZED) != 0 ?
          characteristics | Spliterator.SUBSIZED : characteristics;
    }

    @Override
    public int characteristics() {
      return characteristics;
    }

    @Override
    public long estimateSize() {
      return sizeEstimate;
    }

    @Override
    public @Nullable S trySplit() {
      // see javadoc for java.util.Spliterator
      return null;
    }
  }

  /**
   * See <a
   * href="https://docs.oracle.com/javase/8/docs/api/java/util/Spliterators.AbstractSpliterator.html">
   * the official Java API doc</a> for details.
   */
  public abstract static class AbstractSpliterator<T extends @Nullable Object>
      extends BaseSpliterator<T, Spliterator<T>> implements Spliterator<T> {

    protected AbstractSpliterator(long size, int characteristics) {
      super(size, characteristics);
    }
  }

  /**
   * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Spliterators.AbstractDoubleSpliterator.html">
   * the official Java API doc</a> for details.
   */
  public abstract static class AbstractDoubleSpliterator
      extends BaseSpliterator<Double, Spliterator.OfDouble> implements Spliterator.OfDouble {

    protected AbstractDoubleSpliterator(long size, int characteristics) {
      super(size, characteristics);
    }
  }

  /**
   * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Spliterators.AbstractIntSpliterator.html">
   * the official Java API doc</a> for details.
   */
  public abstract static class AbstractIntSpliterator
      extends BaseSpliterator<Integer, Spliterator.OfInt> implements Spliterator.OfInt {

    protected AbstractIntSpliterator(long size, int characteristics) {
      super(size, characteristics);
    }
  }

  /**
   * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Spliterators.AbstractLongSpliterator.html">
   * the official Java API doc</a> for details.
   */
  public abstract static class AbstractLongSpliterator
      extends BaseSpliterator<Long, Spliterator.OfLong> implements Spliterator.OfLong {

    protected AbstractLongSpliterator(long size, int characteristics) {
      super(size, characteristics);
    }
  }

  @SuppressWarnings("unchecked")
  public static <T extends @Nullable Object> Spliterator<T> emptySpliterator() {
    return (Spliterator<T>) EmptySpliterator.OF_REF;
  }

  public static Spliterator.OfDouble emptyDoubleSpliterator() {
    return EmptySpliterator.OF_DOUBLE;
  }

  public static Spliterator.OfInt emptyIntSpliterator() {
    return EmptySpliterator.OF_INT;
  }

  public static Spliterator.OfLong emptyLongSpliterator() {
    return EmptySpliterator.OF_LONG;
  }

  public static <T extends @Nullable Object> Spliterator<T> spliterator(
      @Nullable Object[] array, int characteristics) {
    return new ArraySpliterator<>(array, characteristics);
  }

  public static <T extends @Nullable Object> Spliterator<T> spliterator(
      @Nullable Object[] array, int fromIndex, int toIndex, int characteristics) {
    checkCriticalArrayBounds(fromIndex, toIndex, array.length);
    return new ArraySpliterator<>(array, fromIndex, toIndex, characteristics);
  }

  public static Spliterator.OfInt spliterator(int[] array, int characteristics) {
    return new IntArraySpliterator(array, characteristics);
  }

  public static Spliterator.OfInt spliterator(
      int[] array, int fromIndex, int toIndex, int characteristics) {
    checkCriticalArrayBounds(fromIndex, toIndex, array.length);
    return new IntArraySpliterator(array, fromIndex, toIndex, characteristics);
  }

  public static Spliterator.OfLong spliterator(long[] array, int characteristics) {
    return new LongArraySpliterator(array, characteristics);
  }

  public static Spliterator.OfLong spliterator(
      long[] array, int fromIndex, int toIndex, int characteristics) {
    checkCriticalArrayBounds(fromIndex, toIndex, array.length);
    return new LongArraySpliterator(array, fromIndex, toIndex, characteristics);
  }

  public static Spliterator.OfDouble spliterator(double[] array, int characteristics) {
    return new DoubleArraySpliterator(array, characteristics);
  }

  public static Spliterator.OfDouble spliterator(
      double[] array, int fromIndex, int toIndex, int characteristics) {
    checkCriticalArrayBounds(fromIndex, toIndex, array.length);
    return new DoubleArraySpliterator(array, fromIndex, toIndex, characteristics);
  }

  public static <T extends @Nullable Object> Spliterator<T> spliterator(
      Collection<? extends T> c, int characteristics) {
    return new IteratorSpliterator<>(c, characteristics);
  }

  public static <T extends @Nullable Object> Spliterator<T> spliterator(
      Iterator<? extends T> it, long size, int characteristics) {
    return new IteratorSpliterator<>(it, size, characteristics);
  }

  public static <T extends @Nullable Object> Spliterator<T> spliteratorUnknownSize(
      Iterator<? extends T> it, int characteristics) {
    return new IteratorSpliterator<>(it, characteristics);
  }

  public static Spliterator.OfInt spliterator(
      PrimitiveIterator.OfInt it, long size, int characteristics) {
    return new IntIteratorSpliterator(it, size, characteristics);
  }

  public static Spliterator.OfInt spliteratorUnknownSize(
      PrimitiveIterator.OfInt it, int characteristics) {
    return new IntIteratorSpliterator(it, characteristics);
  }

  public static Spliterator.OfLong spliterator(
      PrimitiveIterator.OfLong it, long size, int characteristics) {
    return new LongIteratorSpliterator(it, size, characteristics);
  }

  public static Spliterator.OfLong spliteratorUnknownSize(
      PrimitiveIterator.OfLong it, int characteristics) {
    return new LongIteratorSpliterator(it, characteristics);
  }

  public static Spliterator.OfDouble spliterator(
      PrimitiveIterator.OfDouble it, long size, int characteristics) {
    return new DoubleIteratorSpliterator(it, size, characteristics);
  }

  public static Spliterator.OfDouble spliteratorUnknownSize(
      PrimitiveIterator.OfDouble it, int characteristics) {
    return new DoubleIteratorSpliterator(it, characteristics);
  }

  public static <T extends @Nullable Object> Iterator<T> iterator(
      Spliterator<? extends T> spliterator) {
    return new ConsumerIterator<>(spliterator);
  }

  public static PrimitiveIterator.OfDouble iterator(Spliterator.OfDouble spliterator) {
    return new DoubleConsumerIterator(spliterator);
  }

  public static PrimitiveIterator.OfInt iterator(Spliterator.OfInt spliterator) {
    return new IntConsumerIterator(spliterator);
  }

  public static PrimitiveIterator.OfLong iterator(Spliterator.OfLong spliterator) {
    return new LongConsumerIterator(spliterator);
  }

  private abstract static class EmptySpliterator<
          T extends @Nullable Object, S extends Spliterator<T>, C>
      implements Spliterator<T> {

    static final Spliterator<@Nullable Object> OF_REF = new EmptySpliterator.OfRef<>();
    static final Spliterator.OfDouble OF_DOUBLE = new EmptySpliterator.OfDouble();
    static final Spliterator.OfInt OF_INT = new EmptySpliterator.OfInt();
    static final Spliterator.OfLong OF_LONG = new EmptySpliterator.OfLong();

    @Override
    public int characteristics() {
      return Spliterator.SIZED | Spliterator.SUBSIZED;
    }

    @Override
    public long estimateSize() {
      return 0;
    }

    protected final void forEachRemainingBase(C consumer) {
      checkNotNull(consumer);
    }

    protected final boolean tryAdvanceBase(C consumer) {
      checkNotNull(consumer);
      return false;
    }

    @Override
    public @Nullable S trySplit() {
      return null;
    }

    private static final class OfRef<T extends @Nullable Object>
        extends EmptySpliterator<T, Spliterator<T>, Consumer<? super T>> implements Spliterator<T> {

      OfRef() { }

      @Override
      public void forEachRemaining(Consumer<? super T> consumer) {
        super.forEachRemainingBase(consumer);
      }

      @Override
      public boolean tryAdvance(Consumer<? super T> consumer) {
        return super.tryAdvanceBase(consumer);
      }
    }

    private static final class OfDouble
        extends EmptySpliterator<Double, Spliterator.OfDouble, DoubleConsumer>
        implements Spliterator.OfDouble {

      OfDouble() { }

      @Override
      public void forEachRemaining(DoubleConsumer consumer) {
        super.forEachRemainingBase(consumer);
      }

      @Override
      public boolean tryAdvance(DoubleConsumer consumer) {
        return super.tryAdvanceBase(consumer);
      }
    }

    private static final class OfInt
        extends EmptySpliterator<Integer, Spliterator.OfInt, IntConsumer>
        implements Spliterator.OfInt {

      OfInt() { }

      @Override
      public void forEachRemaining(IntConsumer consumer) {
        super.forEachRemainingBase(consumer);
      }

      @Override
      public boolean tryAdvance(IntConsumer consumer) {
        return super.tryAdvanceBase(consumer);
      }
    }

    private static final class OfLong
        extends EmptySpliterator<Long, Spliterator.OfLong, LongConsumer>
        implements Spliterator.OfLong {

      OfLong() { }

      @Override
      public void forEachRemaining(LongConsumer consumer) {
        super.forEachRemainingBase(consumer);
      }

      @Override
      public boolean tryAdvance(LongConsumer consumer) {
        return super.tryAdvanceBase(consumer);
      }
    }
  }

  private static final class ConsumerIterator<T extends @Nullable Object>
      implements Consumer<T>, Iterator<T> {
    private final Spliterator<? extends T> spliterator;
    private @Nullable T nextElement;
    private boolean hasElement = false;

    ConsumerIterator(Spliterator<? extends T> spliterator) {
      this.spliterator = checkNotNull(spliterator);
    }

    @Override
    public void accept(T element) {
      nextElement = element;
    }

    @Override
    public boolean hasNext() {
      if (!hasElement) {
        hasElement = spliterator.tryAdvance(this);
      }
      return hasElement;
    }

    @Override
    public T next() {
      checkCriticalElement(hasNext());
      hasElement = false;
      T element = nextElement;
      nextElement = null;
      return element;
    }
  }

  private static final class DoubleConsumerIterator
      implements DoubleConsumer, PrimitiveIterator.OfDouble {

    private final Spliterator.OfDouble spliterator;
    private double nextElement;
    private boolean hasElement = false;

    DoubleConsumerIterator(Spliterator.OfDouble spliterator) {
      this.spliterator = checkNotNull(spliterator);
    }

    @Override
    public void accept(double d) {
      nextElement = d;
    }

    @Override
    public boolean hasNext() {
      if (!hasElement) {
        hasElement = spliterator.tryAdvance(this);
      }
      return hasElement;
    }

    @Override
    public double nextDouble() {
      checkCriticalElement(hasNext());
      hasElement = false;
      return nextElement;
    }
  }

  private static final class IntConsumerIterator
      implements IntConsumer, PrimitiveIterator.OfInt {

    private final Spliterator.OfInt spliterator;
    private int nextElement;
    private boolean hasElement = false;

    IntConsumerIterator(Spliterator.OfInt spliterator) {
      this.spliterator = checkNotNull(spliterator);
    }

    @Override
    public void accept(int i) {
      nextElement = i;
    }

    @Override
    public boolean hasNext() {
      if (!hasElement) {
        hasElement = spliterator.tryAdvance(this);
      }
      return hasElement;
    }

    @Override
    public int nextInt() {
      checkCriticalElement(hasNext());
      hasElement = false;
      return nextElement;
    }
  }

  private static final class LongConsumerIterator
      implements LongConsumer, PrimitiveIterator.OfLong {

    private final Spliterator.OfLong spliterator;
    private long nextElement;
    private boolean hasElement = false;

    LongConsumerIterator(Spliterator.OfLong spliterator) {
      this.spliterator = checkNotNull(spliterator);
    }

    @Override
    public void accept(long l) {
      nextElement = l;
    }

    @Override
    public boolean hasNext() {
      if (!hasElement) {
        hasElement = spliterator.tryAdvance(this);
      }
      return hasElement;
    }

    @Override
    public long nextLong() {
      checkCriticalElement(hasNext());
      hasElement = false;
      return nextElement;
    }
  }

  static class IteratorSpliterator<T extends @Nullable Object> implements Spliterator<T> {
    private @Nullable Collection<? extends T> collection;
    private @Nullable Iterator<? extends T> it;
    private final int characteristics;
    private long estimateSize;

    IteratorSpliterator(Collection<? extends T> collection, int characteristics) {
      this.collection = checkNotNull(collection);
      this.characteristics = sizeKnownIteratorSpliteratorCharacteristics(characteristics);
    }

    IteratorSpliterator(Iterator<? extends T> it, long size, int characteristics) {
      this.it = checkNotNull(it);
      this.characteristics = sizeKnownIteratorSpliteratorCharacteristics(characteristics);
      this.estimateSize = size;
    }

    IteratorSpliterator(Iterator<? extends T> it, int characteristics) {
      this.it = checkNotNull(it);
      this.characteristics = sizeUnknownSpliteratorCharacteristics(characteristics);
      this.estimateSize = Long.MAX_VALUE;
    }

    @Override
    public int characteristics() {
      return characteristics;
    }

    @Override
    public long estimateSize() {
      initIterator();
      return estimateSize;
    }

    @Override
    public void forEachRemaining(Consumer<? super T> consumer) {
      initIterator();
      it.forEachRemaining(consumer);
    }

    @Override
    public @Nullable Comparator<? super T> getComparator() {
      checkSorted(characteristics);
      return null;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> consumer) {
      checkNotNull(consumer);
      initIterator();
      if (it.hasNext()) {
        consumer.accept(it.next());
        return true;
      }
      return false;
    }

    @Override
    public @Nullable Spliterator<T> trySplit() {
      // see javadoc for java.util.Spliterator
      return null;
    }

    private void initIterator() {
      if (it == null) {
        it = collection.iterator();
        estimateSize = (long) collection.size();
      }
    }
  }

  private static final class DoubleIteratorSpliterator extends AbstractDoubleSpliterator {
    private final PrimitiveIterator.OfDouble it;

    DoubleIteratorSpliterator(PrimitiveIterator.OfDouble it, long size, int characteristics) {
      super(size, sizeKnownIteratorSpliteratorCharacteristics(characteristics));
      this.it = checkNotNull(it);
    }

    DoubleIteratorSpliterator(PrimitiveIterator.OfDouble it, int characteristics) {
      super(Long.MAX_VALUE, sizeUnknownSpliteratorCharacteristics(characteristics));
      this.it = checkNotNull(it);
    }

    @Override
    public void forEachRemaining(DoubleConsumer consumer) {
      it.forEachRemaining(consumer);
    }

    @Override
    public @Nullable Comparator<? super Double> getComparator() {
      checkSorted(characteristics());
      return null;
    }

    @Override
    public boolean tryAdvance(DoubleConsumer consumer) {
      checkNotNull(consumer);
      if (it.hasNext()) {
        consumer.accept(it.nextDouble());
        return true;
      }
      return false;
    }
  }

  private static final class IntIteratorSpliterator extends AbstractIntSpliterator {
    private final PrimitiveIterator.OfInt it;

    IntIteratorSpliterator(PrimitiveIterator.OfInt it, long size, int characteristics) {
      super(size, sizeKnownIteratorSpliteratorCharacteristics(characteristics));
      this.it = checkNotNull(it);
    }

    IntIteratorSpliterator(PrimitiveIterator.OfInt it, int characteristics) {
      super(Long.MAX_VALUE, sizeUnknownSpliteratorCharacteristics(characteristics));
      this.it = checkNotNull(it);
    }

    @Override
    public void forEachRemaining(IntConsumer consumer) {
      it.forEachRemaining(consumer);
    }

    @Override
    public @Nullable Comparator<? super Integer> getComparator() {
      checkSorted(characteristics());
      return null;
    }

    @Override
    public boolean tryAdvance(IntConsumer consumer) {
      checkNotNull(consumer);
      if (it.hasNext()) {
        consumer.accept(it.nextInt());
        return true;
      }
      return false;
    }
  }

  private static final class LongIteratorSpliterator extends AbstractLongSpliterator {
    private final PrimitiveIterator.OfLong it;

    LongIteratorSpliterator(PrimitiveIterator.OfLong it, long size, int characteristics) {
      super(size, sizeKnownIteratorSpliteratorCharacteristics(characteristics));
      this.it = checkNotNull(it);
    }

    LongIteratorSpliterator(PrimitiveIterator.OfLong it, int characteristics) {
      super(Long.MAX_VALUE, sizeUnknownSpliteratorCharacteristics(characteristics));
      this.it = checkNotNull(it);
    }

    @Override
    public void forEachRemaining(LongConsumer consumer) {
      it.forEachRemaining(consumer);
    }

    @Override
    public @Nullable Comparator<? super Long> getComparator() {
      checkSorted(characteristics());
      return null;
    }

    @Override
    public boolean tryAdvance(LongConsumer consumer) {
      checkNotNull(consumer);
      if (it.hasNext()) {
        consumer.accept(it.nextLong());
        return true;
      }
      return false;
    }
  }

  private abstract static class BaseArraySpliterator<
          T extends @Nullable Object, S extends Spliterator<T>, C>
      implements Spliterator<T> {
    private int index;
    private final int limit;
    private final int characteristics;

    BaseArraySpliterator(int from, int limit, int characteristics) {
      this.index = from;
      this.limit = limit;
      this.characteristics = sizeKnownSpliteratorCharacteristics(characteristics);
    }

    public int characteristics() {
      return characteristics;
    }

    public long estimateSize() {
      return limit - index;
    }

    protected final void forEachRemainingBase(C consumer) {
      checkNotNull(consumer);
      while (index < limit) {
        consume(consumer, index++);
      }
    }

    public @Nullable Comparator<? super T> getComparator() {
      checkSorted(characteristics);
      return null;
    }

    protected final boolean tryAdvanceBase(C consumer) {
      checkNotNull(consumer);
      if (index < limit) {
        consume(consumer, index++);
        return true;
      }
      return false;
    }

    public @Nullable S trySplit() {
      // see javadoc for java.util.Spliterator
      return null;
    }

    protected abstract void consume(C consumer, int index);
  }

  private static final class ArraySpliterator<T extends @Nullable Object>
      extends BaseArraySpliterator<T, Spliterator<T>, Consumer<? super T>>
      implements Spliterator<T> {

    private final @Nullable Object[] array;

    ArraySpliterator(@Nullable Object[] array, int characteristics) {
      this(array, 0, array.length, characteristics);
    }

    ArraySpliterator(@Nullable Object[] array, int from, int limit, int characteristics) {
      super(from, limit, characteristics);
      this.array = array;
    }

    @Override
    public void forEachRemaining(Consumer<? super T> consumer) {
      super.forEachRemainingBase(consumer);
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> consumer) {
      return super.tryAdvanceBase(consumer);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void consume(Consumer<? super T> consumer, int index) {
      consumer.accept((T) array[index]);
    }
  }

  private static final class DoubleArraySpliterator
      extends BaseArraySpliterator<Double, Spliterator.OfDouble, DoubleConsumer>
      implements Spliterator.OfDouble {

    private final double[] array;

    DoubleArraySpliterator(double[] array, int characteristics) {
      this(array, 0, array.length, characteristics);
    }

    DoubleArraySpliterator(double[] array, int from, int limit, int characteristics) {
      super(from, limit, characteristics);
      this.array = array;
    }

    @Override
    public void forEachRemaining(DoubleConsumer consumer) {
      super.forEachRemainingBase(consumer);
    }

    @Override
    public boolean tryAdvance(DoubleConsumer action) {
      return super.tryAdvanceBase(action);
    }

    @Override
    protected void consume(DoubleConsumer consumer, int index) {
      consumer.accept(array[index]);
    }
  }

  private static final class IntArraySpliterator
      extends BaseArraySpliterator<Integer, Spliterator.OfInt, IntConsumer>
      implements Spliterator.OfInt {

    private final int[] array;

    IntArraySpliterator(int[] array, int characteristics) {
      this(array, 0, array.length, characteristics);
    }

    IntArraySpliterator(int[] array, int from, int limit, int characteristics) {
      super(from, limit, characteristics);
      this.array = array;
    }

    @Override
    public void forEachRemaining(IntConsumer consumer) {
      super.forEachRemainingBase(consumer);
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
      return super.tryAdvanceBase(action);
    }

    @Override
    protected void consume(IntConsumer consumer, int index) {
      consumer.accept(array[index]);
    }
  }

  private static final class LongArraySpliterator
      extends BaseArraySpliterator<Long, Spliterator.OfLong, LongConsumer>
      implements Spliterator.OfLong {

    private final long[] array;

    LongArraySpliterator(long[] array, int characteristics) {
      this(array, 0, array.length, characteristics);
    }

    LongArraySpliterator(long[] array, int from, int limit, int characteristics) {
      super(from, limit, characteristics);
      this.array = array;
    }

    @Override
    public void forEachRemaining(LongConsumer consumer) {
      super.forEachRemainingBase(consumer);
    }

    @Override
    public boolean tryAdvance(LongConsumer action) {
      return super.tryAdvanceBase(action);
    }

    @Override
    protected void consume(LongConsumer consumer, int index) {
      consumer.accept(array[index]);
    }
  }

  private static void checkSorted(int characteristics) {
    checkCriticalState((characteristics & Spliterator.SORTED) != 0);
  }

  private static int sizeKnownSpliteratorCharacteristics(int characteristics) {
    return characteristics | Spliterator.SIZED | Spliterator.SUBSIZED;
  }

  private static int sizeKnownIteratorSpliteratorCharacteristics(int characteristics) {
    return (characteristics & Spliterator.CONCURRENT) == 0 ?
        sizeKnownSpliteratorCharacteristics(characteristics) : characteristics;
  }

  private static int sizeUnknownSpliteratorCharacteristics(int characteristics) {
    return characteristics & ~(Spliterator.SIZED | Spliterator.SUBSIZED);
  }

  /**
   * We cant use InternalPreconditions.checkCriticalArrayBounds here because
   * Spliterators must throw only ArrayIndexOutOfBoundsException on range check by contract.
   */
  private static void checkCriticalArrayBounds(int start, int end, int length) {
    if (start > end || start < 0 || end > length) {
      throw new ArrayIndexOutOfBoundsException(
          "fromIndex: " + start + ", toIndex: " + end + ", length: " + length);
    }
  }

  private Spliterators() { }

}
