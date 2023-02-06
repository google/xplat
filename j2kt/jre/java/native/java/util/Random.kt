/*
 * Copyright 2009 Google Inc.
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

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * INCLUDES MODIFICATIONS BY RICHARD ZSCHECH AS WELL AS GOOGLE.
 */
@file:OptIn(ExperimentalObjCName::class)

package java.util

import kotlin.experimental.ExperimentalObjCName
import kotlin.math.ln
import kotlin.math.sqrt
import kotlin.native.ObjCName
import kotlin.synchronized

@ObjCName("J2ktJavaUtilRandom", exact = true)
open class Random internal constructor(var ktRandom: kotlin.random.Random) {
  var haveNextNextGaussian = false
  var nextNextGaussian = 0.0

  constructor() : this(kotlin.random.Random.Default)

  constructor(seed: Long) : this(kotlin.random.Random(seed))

  open protected fun next(bits: Int) = ktRandom.nextBits(bits)

  open fun nextBoolean() = ktRandom.nextBoolean()

  open fun nextBytes(buf: ByteArray) {
    // Note can not just be = ktRandom.nextBytes(buf) as return type is not Unit.
    ktRandom.nextBytes(buf)
  }

  open fun nextDouble() = ktRandom.nextDouble()

  open fun nextFloat() = ktRandom.nextFloat()

  open fun nextGaussian(): Double {
    return synchronized(this) {
      if (haveNextNextGaussian) {
        // if X1 has been returned, return the second Gaussian
        haveNextNextGaussian = false
        nextNextGaussian
      } else {
        var s: Double
        var v1: Double
        var v2: Double
        do {
          // Generates two independent random variables U1, U2
          v1 = 2 * nextDouble() - 1
          v2 = 2 * nextDouble() - 1
          s = v1 * v1 + v2 * v2
        } while (s >= 1)

        // See errata for TAOCP vol. 2, 3rd ed. for proper handling of s == 0 case
        // (page 5 of http://www-cs-faculty.stanford.edu/~uno/err2.ps.gz)
        val norm = if (s == 0.0) 0.0 else sqrt(-2.0 * ln(s) / s)
        nextNextGaussian = v2 * norm
        haveNextNextGaussian = true
        v1 * norm
      }
    }
  }

  open fun nextInt() = ktRandom.nextInt()

  open fun nextInt(n: Int) = ktRandom.nextInt(n)

  open fun nextLong() = ktRandom.nextLong()

  open fun setSeed(seed: Long) {
    synchronized(this) {
      haveNextNextGaussian = false
      ktRandom = kotlin.random.Random(seed)
    }
  }
}
