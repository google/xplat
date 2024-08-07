/*
 * Copyright 2015 Google Inc.
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
package java.util.function;

import static javaemul.internal.InternalPreconditions.checkCriticalNotNull;

import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html">the
 * official Java API doc</a> for details.
 *
 * @param <T> type of the argument
 */
@FunctionalInterface
@NullMarked
public interface Predicate<T extends @Nullable Object> {

  static <T extends @Nullable Object> Predicate<T> isEqual(Object targetRef) {
    return targetRef == null ? Objects::isNull : targetRef::equals;
  }

  boolean test(T t);

  default Predicate<T> negate() {
    return t -> !test(t);
  }

  default Predicate<T> and(Predicate<? super T> other) {
    checkCriticalNotNull(other);
    return t -> test(t) && other.test(t);
  }

  default Predicate<T> or(Predicate<? super T> other) {
    checkCriticalNotNull(other);
    return t -> test(t) || other.test(t);
  }
}
