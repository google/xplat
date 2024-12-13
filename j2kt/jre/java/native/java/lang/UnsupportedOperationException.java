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

package java.lang;

import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * See <a
 * href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/UnsupportedOperationException.html">the
 * official Java API doc</a> for details.
 */
@KtNative(
    name = "kotlin.UnsupportedOperationException",
    bridgeName = "java.lang.UnsupportedOperationException")
@NullMarked
public class UnsupportedOperationException extends RuntimeException {

  public UnsupportedOperationException() {}

  public UnsupportedOperationException(@Nullable String detailMessage) {}

  public UnsupportedOperationException(
      @Nullable String detailMessage, @Nullable Throwable throwable) {}

  public UnsupportedOperationException(@Nullable Throwable throwable) {}
}
