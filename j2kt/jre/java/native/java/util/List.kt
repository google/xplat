/*
 * Copyright 2022 Google Inc.
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
@file:OptIn(ExperimentalObjCName::class)

package java.util

import java.util.function.UnaryOperator
import kotlin.Comparator as KotlinComparator
import kotlin.collections.replaceAll as kotlinReplaceAll
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@ObjCName("J2ktJavaUtilList", exact = true)
interface List<E> : MutableList<E>, Collection<E> {
  fun replaceAll(operator: UnaryOperator<E>): Unit = kotlinReplaceAll(operator::apply)

  fun sort(c: KotlinComparator<in E>?): Unit =
    if (c == null) sortBy { it as Comparable<Any> } else sortWith(c)
}
