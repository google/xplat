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

package java.security;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * {@code NoSuchProviderException} indicates that a requested security provider could not be found.
 */
@NullMarked
public class NoSuchProviderException extends GeneralSecurityException {

  private static final long serialVersionUID = 8488111756688534474L;

  /**
   * Constructs a new instance of {@code NoSuchProviderException} with the given message.
   *
   * @param msg the detail message for this exception.
   */
  public NoSuchProviderException(@Nullable String msg) {
    super(msg);
  }

  /** Constructs a new instance of {@code NoSuchProviderException}. */
  public NoSuchProviderException() {}
}
