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
package java.security

import java.util.Random
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Security.SecRandomCopyBytes
import platform.Security.kSecRandomDefault

/**
 * Implementation of SecureRandom that passes the randomization into to iOS random number generator.
 */
public class SecureRandom() : Random() {

  // We ignore the seed as the docs say that two SecureRandoms even with the same seed are going
  // to return different results.
  constructor(seed: ByteArray) : this()

  override fun next(bits: Int): Int = memScoped {
    val intVar: IntVar = alloc<IntVar>()
    SecRandomCopyBytes(kSecRandomDefault, 4UL, intVar.ptr)
    return if (bits == 32) intVar.value else intVar.value and ((1 shl bits) - 1)
  }
}
