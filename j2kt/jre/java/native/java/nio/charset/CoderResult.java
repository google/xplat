/* Licensed to the Apache Software Foundation (ASF) under one or more
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

package java.nio.charset;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;

/**
 * Used to indicate the result of encoding/decoding. There are four types of results:
 *
 * <ol>
 *   <li>UNDERFLOW indicates that all input has been processed but more input is required. It is
 *       represented by the unique object <code>CoderResult.UNDERFLOW</code>.
 *   <li>OVERFLOW indicates an insufficient output buffer size. It is represented by the unique
 *       object <code>CoderResult.OVERFLOW</code>.
 *   <li>A malformed-input error indicates that an unrecognizable sequence of input units has been
 *       encountered. Get an instance of this type of result by calling <code>
 *       CoderResult.malformedForLength(int)</code> with the length of the malformed-input.
 *   <li>An unmappable-character error indicates that a sequence of input units can not be mapped to
 *       the output charset. Get an instance of this type of result by calling <code>
 *       CoderResult.unmappableForLength(int)</code> with the input sequence size indicating the
 *       identity of the unmappable character.
 * </ol>
 */
public class CoderResult {

  private static final int TYPE_UNDERFLOW = 1;

  private static final int TYPE_OVERFLOW = 2;

  private static final int TYPE_MALFORMED_INPUT = 3;

  private static final int TYPE_UNMAPPABLE_CHAR = 4;

  public static final CoderResult UNDERFLOW = new CoderResult(TYPE_UNDERFLOW, 0);

  public static final CoderResult OVERFLOW = new CoderResult(TYPE_OVERFLOW, 0);

  // TODO(b/242762033): Add after java.util.WeakHashMap is supported
  // private static WeakHashMap<Integer, CoderResult> _malformedErrors = new WeakHashMap<Integer,
  // CoderResult>();

  // private static WeakHashMap<Integer, CoderResult> _unmappableErrors = new WeakHashMap<Integer,
  // CoderResult>();

  private final int type;

  private final int length;

  private CoderResult(int type, int length) {
    super();
    this.type = type;
    this.length = length;
  }

  // TODO(b/242762033): Add after java.util.WeakHashMap is supported
  public static synchronized CoderResult malformedForLength(int length) {
    throw new UnsupportedOperationException();
  }

  // TODO(b/242762033): Add after java.util.WeakHashMap is supported
  public static synchronized CoderResult unmappableForLength(int length) {
    throw new UnsupportedOperationException();
  }

  public boolean isUnderflow() {
    return this.type == TYPE_UNDERFLOW;
  }

  public boolean isError() {
    return this.type == TYPE_MALFORMED_INPUT || this.type == TYPE_UNMAPPABLE_CHAR;
  }

  public boolean isMalformed() {
    return this.type == TYPE_MALFORMED_INPUT;
  }

  public boolean isOverflow() {
    return this.type == TYPE_OVERFLOW;
  }

  public boolean isUnmappable() {
    return this.type == TYPE_UNMAPPABLE_CHAR;
  }

  public int length() throws UnsupportedOperationException {
    if (this.type == TYPE_MALFORMED_INPUT || this.type == TYPE_UNMAPPABLE_CHAR) {
      return this.length;
    }
    throw new UnsupportedOperationException("length meaningless for " + toString());
  }

  public void throwException()
      throws BufferUnderflowException, BufferOverflowException, UnmappableCharacterException,
          MalformedInputException, CharacterCodingException {
    switch (this.type) {
      case TYPE_UNDERFLOW:
        throw new BufferUnderflowException();
      case TYPE_OVERFLOW:
        throw new BufferOverflowException();
      case TYPE_UNMAPPABLE_CHAR:
        throw new UnmappableCharacterException(this.length);
      case TYPE_MALFORMED_INPUT:
        throw new MalformedInputException(this.length);
      default:
        throw new CharacterCodingException();
    }
  }

  @Override
  public String toString() {
    String dsc = null;
    switch (this.type) {
      case TYPE_UNDERFLOW:
        dsc = "UNDERFLOW error";
        break;
      case TYPE_OVERFLOW:
        dsc = "OVERFLOW error";
        break;
      case TYPE_UNMAPPABLE_CHAR:
        dsc = "Unmappable-character error with erroneous input length " + this.length;
        break;
      case TYPE_MALFORMED_INPUT:
        dsc = "Malformed-input error with erroneous input length " + this.length;
        break;
      default:
        dsc = "";
        break;
    }
    return getClass().getName() + "[" + dsc + "]";
  }
}
