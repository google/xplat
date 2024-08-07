/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/**
 * @author Vera Y. Petrashkova
 * @version $Revision$
 */
package javax.crypto;

import java.security.GeneralSecurityException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The exception that is thrown when the result of an operation is attempted to store in a user
 * provided buffer that is too small.
 */
@NullMarked
public class ShortBufferException extends GeneralSecurityException {

  /**
   * @serial
   */
  private static final long serialVersionUID = 8427718640832943747L;

  /**
   * Creates a new instance of {@code ShortBufferException} with the specified message
   *
   * @param msg the exception message.
   */
  public ShortBufferException(@Nullable String msg) {
    super(msg);
  }

  /** Creates a new instance of {@code ShortBufferException}. */
  public ShortBufferException() {}
}
