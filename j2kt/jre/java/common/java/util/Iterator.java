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
package java.util;

import java.util.function.Consumer;
import javaemul.internal.annotations.KtName;
import javaemul.internal.annotations.KtNative;
import javaemul.internal.annotations.KtOut;
import javaemul.internal.annotations.KtPropagateNullability;
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

/**
 * See <a href="https://docs.oracle.com/javase/8/docs/api/java/util/Iterator.html">the official Java
 * API doc</a> for details.
 */
@KtNative(value = "kotlin.collections.MutableIterator", bridgeWith = "javaemul.lang.JavaIterator")
@NullMarked
public interface Iterator<@KtOut E extends @Nullable Object> {

  boolean hasNext();

  @KtPropagateNullability
  E next();

  default void remove() {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }

  @KtPropagateNullability
  @KtName("java_forEachRemaining")
  default void forEachRemaining(Consumer<? super E> consumer) {
    throw new IllegalStateException("Native interface method should not be transpiled");
  }
}
