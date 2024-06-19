// CHECKSTYLE_OFF: Copyrighted to Guava Authors.
/*
 * Copyright (C) 2009 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// CHECKSTYLE_ON

package java.util.concurrent;

import java.util.Map;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Minimal GWT emulation of a map providing atomic operations.
 *
 * @param <K> key type
 * @param <V> value type
 */
@NullMarked
public interface ConcurrentMap<K extends @Nullable Object, V extends @Nullable Object>
    extends Map<K, V> {
  @Override
  @Nullable V putIfAbsent(K key, V value);

  @Override
  boolean remove(@Nullable Object key, @Nullable Object value);

  @Override
  @Nullable V replace(K key, V value);

  @Override
  boolean replace(K key, V oldValue, V newValue);
}
