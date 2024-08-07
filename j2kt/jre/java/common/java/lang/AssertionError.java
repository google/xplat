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

@KtNative(name = "kotlin.AssertionError", bridgeName = "java.lang.AssertionError")
@NullMarked
public class AssertionError extends Error {

  public AssertionError() {}

  public AssertionError(@Nullable Object detailMessage) {}

  public AssertionError(@Nullable String message, @Nullable Throwable cause) {}

  public AssertionError(boolean detailMessage) {}

  public AssertionError(char detailMessage) {}

  public AssertionError(int detailMessage) {}

  public AssertionError(long detailMessage) {}

  public AssertionError(float detailMessage) {}

  public AssertionError(double detailMessage) {}
}
