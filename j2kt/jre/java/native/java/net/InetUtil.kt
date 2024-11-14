/*
 * Copyright 2024 Google Inc.
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
package java.net

import kotlinx.cinterop.refTo
import platform.darwin.inet_pton
import platform.posix.AF_INET
import platform.posix.AF_INET6

/** JRE-internal utility functions for working with IPv4 and IPv6 addresses. */
internal object InetUtil {
  // Used as the dst buffer for inet_pton but never read from.
  private val writeOnlyBuffer = ByteArray(16)

  /** Returns whether the given string is valid for IPv4. */
  fun isValidIpv4Address(address: String): Boolean {
    return inet_pton(AF_INET, address, writeOnlyBuffer.refTo(0)) == 1
  }

  /** Returns whether the given string is valid for IPv6. */
  fun isValidIpv6Address(address: String): Boolean {
    return inet_pton(AF_INET6, address, writeOnlyBuffer.refTo(0)) == 1
  }
}
