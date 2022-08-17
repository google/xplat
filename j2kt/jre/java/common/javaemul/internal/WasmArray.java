/*
 * Copyright 2021 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javaemul.internal;

/** Exists solely to make j2cl happy. */
abstract class WasmArray {

  static class OfObject extends WasmArray {}

  static class OfByte extends WasmArray {}

  static class OfShort extends WasmArray {}

  static class OfChar extends WasmArray {}

  static class OfInt extends WasmArray {}

  static class OfLong extends WasmArray {}

  static class OfFloat extends WasmArray {}

  static class OfDouble extends WasmArray {}

  static class OfBoolean extends WasmArray {}
}
