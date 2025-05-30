/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
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

package java.util.zip;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * {@code DataFormatException} is used to indicate an error in the format of a particular data
 * stream which is to be uncompressed.
 */
@NullMarked
public class DataFormatException extends Exception {

  /** Constructs a new {@code DataFormatException} instance. */
  public DataFormatException() {}

  /**
   * Constructs a new {@code DataFormatException} instance with the specified message.
   *
   * @param detailMessage the detail message for the exception.
   */
  public DataFormatException(@Nullable String detailMessage) {
    super(detailMessage);
  }
}
