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
package javax.crypto

import java.lang.UnsupportedOperationException
import java.security.InvalidKeyException
import java.security.Key
import java.security.NoSuchAlgorithmException
import java.security.Provider
import java.security.spec.AlgorithmParameterSpec
import kotlin.Cloneable
import kotlinx.cinterop.refTo
import platform.CoreCrypto.CCHmac
import platform.CoreCrypto.CCHmacAlgorithm
import platform.CoreCrypto.CC_MD5_DIGEST_LENGTH
import platform.CoreCrypto.CC_SHA256_DIGEST_LENGTH
import platform.CoreCrypto.kCCHmacAlgMD5
import platform.CoreCrypto.kCCHmacAlgSHA256

/**
 * Partial implementation for Mac on Kotlin Native which uses the built-in CoreCrypto library from
 * the CommonCrypto module.
 *
 * Note that this currently only supports one-shot encryption, the message may not be updated after
 * initialization.
 *
 * Currently supported hashing algorithms are: Sha256 and MD5.
 *
 * @property algorithm the name of the algorithm used to generate the HMAC
 * @property algorithmId CCHmacAlgorithm enum of the algorithm used to generate the HMAC
 * @property macLength the length of the digest used to generate the HMAC
 */
class Mac
private constructor(
  private val algorithm: String,
  private val algorithmId: CCHmacAlgorithm,
  private val macLength: Int,
  private var key: Key? = null,
  private var message: ByteArray? = null
) : Cloneable {

  // TODO(b/253428486): Complete this implementation of message hashing.

  /** Returns the name of the MAC algorithm. */
  fun getAlgorithm(): String = this.algorithm

  /** Returns the provider of this [Mac] instance. */
  fun getProvider(): Provider {
    throw UnsupportedOperationException("Providers are not supported")
  }

  companion object {
    /**
     * Creates a new [Mac] instance that provides the specified MAC algorithm.
     *
     * @param algorithm the name of the requested MAC algorithm.
     * @return the new [Mac] instance.
     * @throws NoSuchAlgorithmException if the specified algorithm is not available by any provider.
     */
    fun getInstance(algorithm: String): Mac {
      when (algorithm) {
        "HmacSHA256" -> return Mac(algorithm, kCCHmacAlgSHA256, CC_SHA256_DIGEST_LENGTH)
        "HmacMD5" -> return Mac(algorithm, kCCHmacAlgMD5, CC_MD5_DIGEST_LENGTH)
        else -> {
          throw NoSuchAlgorithmException()
        }
      }
    }

    /**
     * Creates a new [Mac] instance that provides the specified MAC algorithm from the specified
     * provider.
     *
     * @param algorithm the name of the requested MAC algorithm.
     * @param provider the name of the provider that is providing the algorithm.
     * @return the new [Mac] instance.
     * @throws NoSuchAlgorithmException if the specified algorithm is not provided by the specified
     *   provider.
     * @throws NoSuchProviderException if the specified provider is not available.
     * @throws IllegalArgumentException if the specified provider name is null or empty.
     * @throws NullPointerException if algorithm is null (instead of NoSuchAlgorithmException as in
     *   1.4 release).
     */
    fun getInstance(algorithm: String, provider: String): Mac {
      throw UnsupportedOperationException("Providers are not supported")
    }

    /**
     * Creates a new [Mac] instance that provides the specified MAC algorithm from the specified
     * provider. The {@code provider} supplied does not have to be registered.
     *
     * @param algorithm the name of the requested MAC algorithm.
     * @param provider the provider that is providing the algorithm.
     * @return the new [Mac] instance.
     * @throws NoSuchAlgorithmException if the specified algorithm is not provided by the specified
     *   provider.
     * @throws IllegalArgumentException if {@code provider} is {@code null}.
     * @throws NullPointerException if {@code algorithm} is {@code null} (instead of
     *   NoSuchAlgorithmException as in 1.4 release).
     */
    fun getInstance(algorithm: String, provider: Provider): Mac {
      throw UnsupportedOperationException("Providers are not supported")
    }
  }

  /**
   * Returns the length of this MAC (in bytes).
   *
   * @return the length of this MAC (in bytes).
   */
  public fun getMacLength(): Int = this.macLength

  /**
   * Initializes this [Mac] instance with the specified key and algorithm parameters.
   *
   * @param key the key to initialize this algorithm.
   * @param params the parameters for this algorithm.
   * @throws InvalidKeyException if the specified key cannot be used to initialize this algorithm,
   *   or it is null.
   * @throws InvalidAlgorithmParameterException if the specified parameters cannot be used to
   *   initialize this algorithm.
   */
  public fun init(key: Key, params: AlgorithmParameterSpec) {
    throw UnsupportedOperationException("AlgorithmParameterSpec is not supported")
  }

  /**
   * Initializes this [Mac] instance with the specified key.
   *
   * @param key the key to initialize this algorithm.
   * @throws InvalidKeyException if initialization fails because the provided key is null.
   * @throws RuntimeException if the specified key cannot be used to initialize this algorithm.
   */
  public fun init(key: Key) {
    if (key.getEncoded() == null) throw InvalidKeyException()
    this.key = key
  }

  /**
   * Updates this [Mac] instance with the specified byte.
   *
   * @param input the byte
   * @throws IllegalStateException if this MAC is not initialized.
   */
  public fun update(input: Byte) {
    checkNotNull(key)
    throw UnsupportedOperationException("updating HMAC message is not supported")
  }

  /**
   * Copies the buffer provided as input for further processing.
   *
   * @param input the buffer.
   * @throws IllegalStateException if this MAC is not initialized.
   */
  public fun update(input: ByteArray) {
    checkNotNull(key)
    if (message != null)
      throw UnsupportedOperationException("updating HMAC message is not supported")
    this.message = input
  }

  /**
   * Updates this [Mac] instance with the data from the specified input buffer from the specified
   * offset and length length.
   *
   * @param input the buffer.
   * @param offset the offset in the buffer.
   * @param len the length of the data in the buffer.
   * @throws IllegalStateException if this MAC is not initialized.
   * @throws IllegalArgumentException if the offset and length do not specified a valid chunk in
   *   input buffer.
   */
  public fun update(input: ByteArray, offset: Int, length: Int) {
    checkNotNull(key)
    throw UnsupportedOperationException("updating the message is not supported")
  }

  /**
   * Computes the digest of this MAC based on the data previously specified in [update] calls.
   *
   * <p>This [Mac] instance is reverted to its initial state and can be used to start the next MAC
   * computation with the same parameters or initialized with different parameters.
   *
   * @return the generated digest.
   * @throws IllegalStateException if this MAC is not initialized.
   */
  public fun doFinal(): ByteArray {
    checkNotNull(key)
    checkNotNull(message)
    val digest = ByteArray(getMacLength())
    return doFinal(digest, 0)
  }

  /**
   * Computes the digest of this MAC based on the data previously specified in [update] calls and
   * stores the digest in the specified output buffer at offset outOffset.
   *
   * <p>This [Mac] instance is reverted to its initial state and can be used to start the next MAC
   * computation with the same parameters or initialized with different parameters.
   *
   * @param output the output buffer
   * @param outOffset the offset in the output buffer
   * @throws ShortBufferException if the specified output buffer is either too small for the digest
   *   to be stored, the specified output buffer is null, or the specified offset is negative or
   *   past the length of the output buffer.
   * @throws IllegalStateException if this MAC is not initialized.
   */
  public fun doFinal(output: ByteArray, outOffset: Int): ByteArray {
    val message: ByteArray = checkNotNull(message) { "message must be not-null" }
    if (output.size < outOffset + getMacLength()) throw ShortBufferException()
    val encodedKey: ByteArray =
      checkNotNull(checkNotNull(key).getEncoded()) { "encoded key must be not-null" }
    CCHmac(
      algorithmId,
      encodedKey.refTo(0),
      encodedKey.size.toULong(),
      message.refTo(0),
      message.size.toULong(),
      output.refTo(outOffset)
    )
    this.reset()
    return output
  }

  /**
   * Computes the digest of this MAC based on the data previously specified on [update] calls and on
   * the final bytes specified by input (or based on those bytes only).
   *
   * <p>This [Mac] instance is reverted to its initial state and can be used to start the next MAC
   * computation with the same parameters or initialized with different parameters.
   *
   * @param input the final bytes.
   * @return the generated digest.
   * @throws IllegalStateException if this MAC is not initialized.
   */
  public fun doFinal(input: ByteArray): ByteArray {
    checkNotNull(key)
    if (message != null)
      throw UnsupportedOperationException("updating the message is not supported")
    this.message = input
    return doFinal()
  }

  /**
   * Resets this [Mac] instance to its initial state.
   *
   * <p>This [Mac] instance is reverted to its initial state and can be used to start the next MAC
   * computation with the same parameters or initialized with different parameters.
   */
  public fun reset() {
    checkNotNull(key)
    message = null
  }

  /**
   * Clones this [Mac] instance and the underlying implementation.
   *
   * @return the cloned instance.
   * @throws CloneNotSupportedException if the underlying implementation does not support cloning.
   */
  public override fun clone(): Mac = Mac(algorithm, algorithmId, macLength, key, message)
}
