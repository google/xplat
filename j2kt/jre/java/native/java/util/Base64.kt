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

package java.util

import kotlin.experimental.ExperimentalObjCName
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("J2ktBase64")
object Base64 {

  private val decoder = Decoder()
  private val encoder = Encoder()

  fun getDecoder(): Decoder = decoder

  fun getEncoder(): Encoder = encoder

  class Decoder internal constructor() {
    fun decode(s: String): ByteArray {
      // Note that we are using an experimental API here for convenience and to reduce overhead, but
      // it would be relatively straightforward to implement base64 "manually" if required.
      @OptIn(ExperimentalEncodingApi::class)
      return Base64.Default.decode(s)
    }
  }

  class Encoder internal constructor() {
    fun encodeToString(b: ByteArray): String {
      val sb = StringBuilder()
      @OptIn(ExperimentalEncodingApi::class) Base64.Default.encodeToAppendable(b, sb)
      return sb.toString()
    }
  }
}
