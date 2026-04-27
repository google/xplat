/*
 * Copyright 2026 Google Inc.
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
package java.security

import kotlin.native.ref.createCleaner
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.cValuesOf
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.refTo
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import platform.CoreCrypto.CC_SHA1_CTX
import platform.CoreCrypto.CC_SHA1_DIGEST_LENGTH
import platform.CoreCrypto.CC_SHA1_Final
import platform.CoreCrypto.CC_SHA1_Init
import platform.CoreCrypto.CC_SHA1_Update

/** SHA1 implementation for J2KT using Apple's CommonCrypto. */
class SHA1Digest : MessageDigest("SHA-1") {
  private val ctx = nativeHeap.alloc<CC_SHA1_CTX>()

  // Ensure native memory is freed when this object is GC'd
  private val cleaner = createCleaner(ctx) { nativeHeap.free(it.ptr.rawValue) }

  init {
    CC_SHA1_Init(ctx.ptr)
  }

  override fun engineUpdate(input: Byte) {
    CC_SHA1_Update(ctx.ptr, cValuesOf(input), 1.toUInt())
  }

  override fun engineUpdate(input: ByteArray, offset: Int, len: Int) {
    if (len > 0) {
      CC_SHA1_Update(ctx.ptr, input.refTo(offset), len.toUInt())
    }
  }

  override fun engineDigest(): ByteArray {
    val digest = ByteArray(CC_SHA1_DIGEST_LENGTH.toInt())
    digest.usePinned { pinned -> CC_SHA1_Final(pinned.addressOf(0).reinterpret(), ctx.ptr) }
    engineReset()
    return digest
  }

  override fun engineReset() {
    CC_SHA1_Init(ctx.ptr)
  }

  override fun engineGetDigestLength(): Int = CC_SHA1_DIGEST_LENGTH.toInt()
}
