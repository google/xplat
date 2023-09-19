/*
 * Copyright 2023 Google Inc.
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
package javaemul.lang

/*
 * On Kotlin Native, the [NativeThrows] annotations maps to [kotlin.Throws]. On other platforms it's
 * ignored.<p>
 *
 * We have the indirection with different typealiases on different platforms here because we cannot
 * use [kotlin.Throws] in the transpiler output directly. We need different class lists on different
 * platforms (because of https://youtrack.jetbrains.com/issue/KT-60731/).
 */
typealias NativeThrows = Throws
