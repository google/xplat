/*
 * Copyright 2023 Google Inc.
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
package interop

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ImpliedPropertyNameTest {

  val javaGetters = JavaGetters()

  @Test
  fun getIf_impliedPropertyNameIsIf() {
    assertThat(javaGetters.getIf()).isEqualTo(javaGetters.`if`)
  }

  @Test
  fun getAs_impliedPropertyNameIsAs() {
    assertThat(javaGetters.getAs()).isEqualTo(javaGetters.`as`)
  }
}
