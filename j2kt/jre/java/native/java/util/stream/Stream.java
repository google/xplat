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
package java.util.stream;

import static javaemul.internal.InternalPreconditions.checkState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html">the official
 * Java API doc</a> for details.
 *
 * @param <T> the type of data being streamed
 */
@NullMarked
public interface Stream<T extends @Nullable Object> extends BaseStream<T, Stream<T>> {

  /**
   * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.Builder.html">
   * the official Java API doc</a> for details.
   */
  interface Builder<T extends @Nullable Object> extends Consumer<T> {
    @Override
    void accept(T t);

    default Stream.Builder<T> add(T t) {
      accept(t);
      return this;
    }

    Stream<T> build();
  }

  static <T extends @Nullable Object> Stream.Builder<T> builder() {
    return new Builder<T>() {
      private @Nullable ArrayList<T> items = new ArrayList<>();

      @Override
      public void accept(T t) {
        checkState(items != null, "Builder already built");
        items.add(t);
      }

      @Override
      @SuppressWarnings("unchecked")
      public Stream<T> build() {
        checkState(items != null, "Builder already built");
        Stream<T> stream = items.stream();
        items = null;
        return stream;
      }
    };
  }

  static <T extends @Nullable Object> Stream<T> concat(
      Stream<? extends T> a, Stream<? extends T> b) {
    // This is nearly the same as flatMap, but inlined, wrapped around a single spliterator of
    // these two objects, and without close() called as the stream progresses. Instead, close is
    // invoked as part of the resulting stream's own onClose, so that either can fail without
    // affecting the other, and correctly collecting suppressed exceptions.

    // TODO replace this flatMap-ish spliterator with one that directly combines the two root
    // streams
    Spliterator<? extends Stream<? extends T>> spliteratorOfStreams =
        Arrays.asList(a, b).spliterator();

    AbstractSpliterator<T> spliterator =
        new Spliterators.AbstractSpliterator<T>(Long.MAX_VALUE, 0) {
          @Nullable Spliterator<? extends T> next;

          @Override
          public boolean tryAdvance(Consumer<? super T> action) {
            // look for a new spliterator
            while (advanceToNextSpliterator()) {
              // if we have one, try to read and use it
              if (next.tryAdvance(action)) {
                return true;
              } else {
                // failed, null it out so we can find another
                next = null;
              }
            }
            return false;
          }

          private boolean advanceToNextSpliterator() {
            while (next == null) {
              if (!spliteratorOfStreams.tryAdvance(
                  n -> {
                    if (n != null) {
                      next = n.spliterator();
                    }
                  })) {
                return false;
              }
            }
            return true;
          }
        };

    Stream<T> result = new StreamImpl<T>(null, spliterator);

    return result.onClose(a::close).onClose(b::close);
  }

  static <T extends @Nullable Object> Stream<T> empty() {
    return new StreamImpl.Empty<T>(null);
  }

  static <T extends @Nullable Object> Stream<T> generate(Supplier<T> s) {
    AbstractSpliterator<T> spliterator =
        new Spliterators.AbstractSpliterator<T>(
            Long.MAX_VALUE, Spliterator.IMMUTABLE | Spliterator.ORDERED) {
          @Override
          public boolean tryAdvance(Consumer<? super T> action) {
            action.accept(s.get());
            return true;
          }
        };
    return StreamSupport.stream(spliterator, false);
  }

  static <T extends @Nullable Object> Stream<T> iterate(T seed, UnaryOperator<T> f) {
    return iterate(seed, ignore -> true, f);
  }

  static <T extends @Nullable Object> Stream<T> iterate(
      T seed, Predicate<? super T> hasNext, UnaryOperator<T> f) {
    AbstractSpliterator<T> spliterator =
        new Spliterators.AbstractSpliterator<T>(
            Long.MAX_VALUE, Spliterator.IMMUTABLE | Spliterator.ORDERED) {
          private T next = seed;
          private boolean isFirst = true;
          private boolean isTerminated = false;

          @Override
          public boolean tryAdvance(Consumer<? super T> action) {
            if (isTerminated) {
              return false;
            }
            if (!isFirst) {
              next = f.apply(next);
            }
            isFirst = false;

            if (!hasNext.test(next)) {
              isTerminated = true;
              return false;
            }
            action.accept(next);
            return true;
          }
        };
    return StreamSupport.stream(spliterator, false);
  }

  static <T extends @Nullable Object> Stream<T> of(T t) {
    // TODO consider a splittable that returns only a single value, either for use here or in the
    //      singleton collection types
    return Collections.singleton(t).stream();
  }

  static <T extends @Nullable Object> Stream<T> of(T... values) {
    return Arrays.stream(values);
  }

  static <T extends @Nullable Object> Stream<T> ofNullable(@Nullable T t) {
    if (t == null) {
      return empty();
    } else {
      return of(t);
    }
  }

  boolean allMatch(Predicate<? super T> predicate);

  boolean anyMatch(Predicate<? super T> predicate);

  <R extends @Nullable Object, A extends @Nullable Object> R collect(
      Collector<? super T, A, R> collector);

  <R extends @Nullable Object> R collect(
      Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner);

  long count();

  Stream<T> distinct();

  Stream<T> filter(Predicate<? super T> predicate);

  Optional<@NonNull T> findAny();

  Optional<@NonNull T> findFirst();

  <R extends @Nullable Object> Stream<R> flatMap(
      Function<? super T, ? extends Stream<? extends R>> mapper);

  DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper);

  IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper);

  LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper);

  void forEach(Consumer<? super T> action);

  void forEachOrdered(Consumer<? super T> action);

  Stream<T> limit(long maxSize);

  <R extends @Nullable Object> Stream<R> map(Function<? super T, ? extends R> mapper);

  DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper);

  IntStream mapToInt(ToIntFunction<? super T> mapper);

  LongStream mapToLong(ToLongFunction<? super T> mapper);

  Optional<@NonNull T> max(Comparator<? super T> comparator);

  Optional<@NonNull T> min(Comparator<? super T> comparator);

  boolean noneMatch(Predicate<? super T> predicate);

  Stream<T> peek(Consumer<? super T> action);

  Optional<@NonNull T> reduce(BinaryOperator<T> accumulator);

  T reduce(T identity, BinaryOperator<T> accumulator);

  <U extends @Nullable Object> U reduce(
      U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner);

  Stream<T> skip(long n);

  Stream<T> sorted();

  Stream<T> sorted(Comparator<? super T> comparator);

  default Stream<T> dropWhile(Predicate<? super T> predicate) {
    Spliterator<T> prev = spliterator();
    Spliterator<T> spliterator =
        new Spliterators.AbstractSpliterator<T>(
            prev.estimateSize(),
            prev.characteristics() & ~(Spliterator.SIZED | Spliterator.SUBSIZED)) {
          private boolean dropping = true;
          private boolean found;

          @Override
          public boolean tryAdvance(Consumer<? super T> action) {
            if (!dropping) {
              // Predicate matched, stop dropping items.
              return prev.tryAdvance(action);
            }

            found = false;
            // Drop items until we find one that matches predicate.
            while (dropping
                && prev.tryAdvance(
                    item -> {
                      if (!predicate.test(item)) {
                        dropping = false;
                        found = true;
                        action.accept(item);
                      }
                    })) {
              // Do nothing, work is done in tryAdvance
            }
            // Only return true if we accepted at least one item
            return found;
          }
        };
    return StreamSupport.stream(spliterator, false);
  }

  default Stream<T> takeWhile(Predicate<? super T> predicate) {
    Spliterator<T> original = spliterator();
    Spliterator<T> spliterator =
        new Spliterators.AbstractSpliterator<T>(
            original.estimateSize(),
            original.characteristics() & ~(Spliterator.SIZED | Spliterator.SUBSIZED)) {
          private boolean taking = true;
          private boolean found;

          @Override
          public boolean tryAdvance(Consumer<? super T> action) {
            if (!taking) {
              // Already failed the predicate.
              return false;
            }

            found = false;
            original.tryAdvance(
                item -> {
                  if (predicate.test(item)) {
                    found = true;
                    action.accept(item);
                  } else {
                    taking = false;
                  }
                });
            return found;
          }
        };
    return StreamSupport.stream(spliterator, false);
  }

  @Nullable Object[] toArray();

  <A extends @Nullable Object> A[] toArray(IntFunction<A[]> generator);
}
