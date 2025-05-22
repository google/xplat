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
package javaemul.internal;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/** A utility class that provides utility functions to do precondition checks. */
// Some parts adapted from Guava
@NullMarked
public final class InternalPreconditions {

  public static void checkType(boolean expression) {
    checkType(expression, null);
  }

  public static void checkType(boolean expression, @Nullable String message) {
    checkCriticalType(expression, message);
  }

  public static void checkCriticalType(boolean expression) {
    checkCriticalType(expression, null);
  }

  public static void checkCriticalType(boolean expression, @Nullable String message) {
    throw new ClassCastException(message);
  }

  /**
   * Ensures the truth of an expression that verifies array type.
   */
  public static void checkArrayType(boolean expression) {
    checkCriticalArrayType(expression);
  }

  public static void checkCriticalArrayType(boolean expression) {
    if (!expression) {
      throw new ArrayStoreException();
    }
  }

  /**
   * Ensures the truth of an expression that verifies array type.
   */
  public static void checkArrayType(boolean expression, Object errorMessage) {
    checkCriticalArrayType(expression, errorMessage);
  }

  public static void checkCriticalArrayType(boolean expression, Object errorMessage) {
    if (!expression) {
      throw new ArrayStoreException(String.valueOf(errorMessage));
    }
  }

  public static void checkArithmetic(boolean expression) {
    checkCriticalArithmetic(expression);
  }

  public static void checkCriticalArithmetic(boolean expression) {
    if (!expression) {
      throw new ArithmeticException();
    }
  }

  /**
   * Ensures the truth of an expression involving existence of an element.
   */
  public static void checkElement(boolean expression) {
    checkCriticalElement(expression);
  }

  /**
   * Ensures the truth of an expression involving existence of an element.
   * <p>
   * For cases where failing fast is pretty important and not failing early could cause bugs that
   * are much harder to debug.
   */
  public static void checkCriticalElement(boolean expression) {
    if (!expression) {
      throw new NoSuchElementException();
    }
  }

  /**
   * Ensures the truth of an expression involving existence of an element.
   */
  public static void checkElement(boolean expression, Object errorMessage) {
    checkCriticalElement(expression, errorMessage);
  }

  /**
   * Ensures the truth of an expression involving existence of an element.
   * <p>
   * For cases where failing fast is pretty important and not failing early could cause bugs that
   * are much harder to debug.
   */
  public static void checkCriticalElement(boolean expression, Object errorMessage) {
    if (!expression) {
      throw new NoSuchElementException(String.valueOf(errorMessage));
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   */
  public static void checkArgument(boolean expression) {
      checkCriticalArgument(expression);
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   * <p>
   * For cases where failing fast is pretty important and not failing early could cause bugs that
   * are much harder to debug.
   */
  public static void checkCriticalArgument(boolean expression) {
    if (!expression) {
      throw new IllegalArgumentException();
    }
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   */
  public static void checkArgument(boolean expression, Object errorMessage) {
      checkCriticalArgument(expression, errorMessage);
  }

  /**
   * Ensures the truth of an expression involving one or more parameters to the calling method.
   * <p>
   * For cases where failing fast is pretty important and not failing early could cause bugs that
   * are much harder to debug.
   */
  public static void checkCriticalArgument(boolean expression, Object errorMessage) {
    if (!expression) {
      throw new IllegalArgumentException(String.valueOf(errorMessage));
    }
  }

  /**
   * Ensures the truth of an expression involving the state of the calling instance, but not
   * involving any parameters to the calling method.
   *
   * @param expression a boolean expression
   * @throws IllegalStateException if {@code expression} is false
   */
  public static void checkState(boolean expression) {
      checkCriticalState(expression);
  }

  /**
   * Ensures the truth of an expression involving the state of the calling instance, but not
   * involving any parameters to the calling method.
   * <p>
   * For cases where failing fast is pretty important and not failing early could cause bugs that
   * are much harder to debug.
   */
  public static void checkCriticalState(boolean expression) {
    if (!expression) {
      throw new IllegalStateException();
    }
  }

  /**
   * Ensures the truth of an expression involving the state of the calling instance, but not
   * involving any parameters to the calling method.
   */
  public static void checkState(boolean expression, Object errorMessage) {
      checkCriticalState(expression, errorMessage);
  }

  /**
   * Ensures the truth of an expression involving the state of the calling instance, but not
   * involving any parameters to the calling method.
   */
  public static void checkCriticalState(boolean expression, Object errorMessage) {
    if (!expression) {
      throw new IllegalStateException(String.valueOf(errorMessage));
    }
  }

  /** Ensures that an object reference passed as a parameter to the calling method is not null. */
  public static <T> T checkNotNull(@Nullable T reference) {
      checkCriticalNotNull(reference);

    return reference;
  }

  public static <T extends @Nullable Object> T checkCriticalNotNull(@Nullable T reference) {
    if (reference == null) {
      throw new NullPointerException();
    }
    return reference;
  }

  /** Ensures that an object reference passed as a parameter to the calling method is not null. */
  public static void checkNotNull(@Nullable Object reference, Object errorMessage) {
      checkCriticalNotNull(reference, errorMessage);
  }

  public static void checkCriticalNotNull(@Nullable Object reference, Object errorMessage) {
    if (reference == null) {
      throw new NullPointerException(String.valueOf(errorMessage));
    }
  }

  /**
   * Ensures that {@code size} specifies a valid array size (i.e. non-negative).
   */
  public static void checkArraySize(int size) {
      checkCriticalArraySize(size);
  }

  public static void checkCriticalArraySize(int size) {
    if (size < 0) {
      throw new NegativeArraySizeException("Negative array size: " + size);
    }
  }

  /**
   * Ensures that {@code index} specifies a valid <i>element</i> in a list or string of size
   * {@code size}. An element index may range from zero, inclusive, to {@code size}, exclusive.
   */
  public static void checkElementIndex(int index, int size) {
      checkCriticalElementIndex(index, size);
  }

  public static void checkCriticalElementIndex(int index, int size) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
  }

  public static void checkStringElementIndex(int index, int size) {
      checkCriticalStringElementIndex(index, size);
  }

  public static void checkCriticalStringElementIndex(int index, int size) {
    if (index < 0 || index >= size) {
      throw new StringIndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
  }

  /**
   * Ensures that {@code index} specifies a valid <i>position</i> in a list of
   * size {@code size}. A position index may range from zero to {@code size}, inclusive.
   */
  public static void checkPositionIndex(int index, int size) {
      checkCriticalPositionIndex(index, size);
  }

  public static void checkCriticalPositionIndex(int index, int size) {
    if (index < 0 || index > size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
  }

  /**
   * Ensures that {@code start} and {@code end} specify a valid <i>positions</i> in a list
   * of size {@code size}, and are in order. A position index may range from zero to
   * {@code size}, inclusive.
   */
  public static void checkPositionIndexes(int start, int end, int size) {
      checkCriticalPositionIndexes(start, end, size);
  }

  /**
   * Ensures that {@code start} and {@code end} specify a valid <i>positions</i> in a list
   * of size {@code size}, and are in order. A position index may range from zero to
   * {@code size}, inclusive.
   */
  public static void checkCriticalPositionIndexes(int start, int end, int size) {
    if (start < 0 || end > size) {
      throw new IndexOutOfBoundsException(
          "fromIndex: " + start + ", toIndex: " + end + ", size: " + size);
    }
    if (start > end) {
      throw new IllegalArgumentException("fromIndex: " + start + " > toIndex: " + end);
    }
  }

  /**
   * Checks that array bounds are correct.
   *
   * @throws IllegalArgumentException if {@code start > end}
   * @throws ArrayIndexOutOfBoundsException if the range is not legal
   */
  public static void checkCriticalArrayBounds(int start, int end, int length) {
    if (start > end) {
      throw new IllegalArgumentException("fromIndex: " + start + " > toIndex: " + end);
    }
    if (start < 0 || end > length) {
      throw new ArrayIndexOutOfBoundsException(
          "fromIndex: " + start + ", toIndex: " + end + ", length: " + length);
    }
  }

  /**
   * Checks that string bounds are correct.
   *
   * @throws StringIndexOutOfBoundsException if the range is not legal
   */
  public static void checkStringBounds(int start, int end, int length) {
      checkCriticalStringBounds(start, end, length);
  }

  /**
   * Checks that string bounds are correct.
   *
   * @throws StringIndexOutOfBoundsException if the range is not legal
   */
  public static void checkCriticalStringBounds(int start, int end, int length) {
    if (start < 0 || end > length || end < start) {
      throw new StringIndexOutOfBoundsException(
          "fromIndex: " + start + ", toIndex: " + end + ", length: " + length);
    }
  }

  public static void checkConcurrentModification(int currentModCount, int recordedModCount) {
      checkCriticalConcurrentModification(currentModCount, recordedModCount);
  }

  public static void checkCriticalConcurrentModification(
      double currentModCount, double recordedModCount) {
    if (currentModCount != recordedModCount) {
      throw new ConcurrentModificationException();
    }
  }

  /**
   * Throws a MatchException for exhaustive switch expressions on unexpected values.
   *
   * @throws MatchException
   */
  public static void checkCriticalExhaustive() {
    throw new MatchException();
  }

  public static void checkExhaustive() {
      checkCriticalExhaustive();
  }

  // Hides the constructor for this static utility class.
  private InternalPreconditions() { }
}
