/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package javax.crypto;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.spec.AlgorithmParameterSpec;
import javaemul.internal.annotations.KtNative;
import org.jspecify.nullness.NullMarked;

/** This class provides the public API for <i>Message Authentication Code</i> (MAC) algorithms. */
@KtNative(name = "javax.crypto.Mac")
@NullMarked
public class Mac implements Cloneable {
  /**
   * Returns the name of the MAC algorithm.
   *
   * @return the name of the MAC algorithm.
   */
  public final native String getAlgorithm();

  /**
   * Returns the provider of this {@code Mac} instance.
   *
   * @return the provider of this {@code Mac} instance.
   */
  public final native Provider getProvider();

  /**
   * Creates a new {@code Mac} instance that provides the specified MAC algorithm.
   *
   * @param algorithm the name of the requested MAC algorithm.
   * @return the new {@code Mac} instance.
   * @throws NoSuchAlgorithmException if the specified algorithm is not available by any provider.
   * @throws NullPointerException if {@code algorithm} is {@code null} (instead of
   *     NoSuchAlgorithmException as in 1.4 release).
   */
  public static final native Mac getInstance(String algorithm) throws NoSuchAlgorithmException;

  /**
   * Creates a new {@code Mac} instance that provides the specified MAC algorithm from the specified
   * provider.
   *
   * @param algorithm the name of the requested MAC algorithm.
   * @param provider the name of the provider that is providing the algorithm.
   * @return the new {@code Mac} instance.
   * @throws NoSuchAlgorithmException if the specified algorithm is not provided by the specified
   *     provider.
   * @throws NoSuchProviderException if the specified provider is not available.
   * @throws IllegalArgumentException if the specified provider name is {@code null} or empty.
   * @throws NullPointerException if {@code algorithm} is {@code null} (instead of
   *     NoSuchAlgorithmException as in 1.4 release).
   */
  public static final native Mac getInstance(String algorithm, String provider)
      throws NoSuchAlgorithmException, NoSuchProviderException;

  /**
   * Creates a new {@code Mac} instance that provides the specified MAC algorithm from the specified
   * provider. The {@code provider} supplied does not have to be registered.
   *
   * @param algorithm the name of the requested MAC algorithm.
   * @param provider the provider that is providing the algorithm.
   * @return the new {@code Mac} instance.
   * @throws NoSuchAlgorithmException if the specified algorithm is not provided by the specified
   *     provider.
   * @throws IllegalArgumentException if {@code provider} is {@code null}.
   * @throws NullPointerException if {@code algorithm} is {@code null} (instead of
   *     NoSuchAlgorithmException as in 1.4 release).
   */
  public static final native Mac getInstance(String algorithm, Provider provider)
      throws NoSuchAlgorithmException;

  /**
   * Returns the length of this MAC (in bytes).
   *
   * @return the length of this MAC (in bytes).
   */
  public final native int getMacLength();

  /**
   * Initializes this {@code Mac} instance with the specified key and algorithm parameters.
   *
   * @param key the key to initialize this algorithm.
   * @param params the parameters for this algorithm.
   * @throws InvalidKeyException if the specified key cannot be used to initialize this algorithm,
   *     or it is null.
   * @throws InvalidAlgorithmParameterException if the specified parameters cannot be used to
   *     initialize this algorithm.
   */
  public final native void init(Key key, AlgorithmParameterSpec params)
      throws InvalidKeyException, InvalidAlgorithmParameterException;

  /**
   * Initializes this {@code Mac} instance with the specified key.
   *
   * @param key the key to initialize this algorithm.
   * @throws InvalidKeyException if initialization fails because the provided key is {@code null}.
   * @throws RuntimeException if the specified key cannot be used to initialize this algorithm.
   */
  public final native void init(Key key) throws InvalidKeyException;

  /**
   * Updates this {@code Mac} instance with the specified byte.
   *
   * @param input the byte
   * @throws IllegalStateException if this MAC is not initialized.
   */
  public final native void update(byte input) throws IllegalStateException;

  /**
   * Updates this {@code Mac} instance with the data from the specified buffer {@code input} from
   * the specified {@code offset} and length {@code len}.
   *
   * @param input the buffer.
   * @param offset the offset in the buffer.
   * @param len the length of the data in the buffer.
   * @throws IllegalStateException if this MAC is not initialized.
   * @throws IllegalArgumentException if {@code offset} and {@code len} do not specified a valid
   *     chunk in {@code input} buffer.
   */
  public final native void update(byte[] input, int offset, int len) throws IllegalStateException;

  /**
   * Copies the buffer provided as input for further processing.
   *
   * @param input the buffer.
   * @throws IllegalStateException if this MAC is not initialized.
   */
  public final native void update(byte[] input) throws IllegalStateException;

  /**
   * Updates this {@code Mac} instance with the data from the specified buffer, starting at {@link
   * ByteBuffer#position()}, including the next {@link ByteBuffer#remaining()} bytes.
   *
   * @param input the buffer.
   * @throws IllegalStateException if this MAC is not initialized.
   */
  public final native void update(ByteBuffer input);

  /**
   * Computes the digest of this MAC based on the data previously specified in {@link #update}
   * calls.
   *
   * <p>This {@code Mac} instance is reverted to its initial state and can be used to start the next
   * MAC computation with the same parameters or initialized with different parameters.
   *
   * @return the generated digest.
   * @throws IllegalStateException if this MAC is not initialized.
   */
  public final native byte[] doFinal() throws IllegalStateException;

  /**
   * Computes the digest of this MAC based on the data previously specified in {@link #update} calls
   * and stores the digest in the specified {@code output} buffer at offset {@code outOffset}.
   *
   * <p>This {@code Mac} instance is reverted to its initial state and can be used to start the next
   * MAC computation with the same parameters or initialized with different parameters.
   *
   * @param output the output buffer
   * @param outOffset the offset in the output buffer
   * @throws ShortBufferException if the specified output buffer is either too small for the digest
   *     to be stored, the specified output buffer is {@code null}, or the specified offset is
   *     negative or past the length of the output buffer.
   * @throws IllegalStateException if this MAC is not initialized.
   */
  public final native void doFinal(byte[] output, int outOffset)
      throws ShortBufferException, IllegalStateException;

  /**
   * Computes the digest of this MAC based on the data previously specified on {@link #update} calls
   * and on the final bytes specified by {@code input} (or based on those bytes only).
   *
   * <p>This {@code Mac} instance is reverted to its initial state and can be used to start the next
   * MAC computation with the same parameters or initialized with different parameters.
   *
   * @param input the final bytes.
   * @return the generated digest.
   * @throws IllegalStateException if this MAC is not initialized.
   */
  public final native byte[] doFinal(byte[] input) throws IllegalStateException;

  /**
   * Resets this {@code Mac} instance to its initial state.
   *
   * <p>This {@code Mac} instance is reverted to its initial state and can be used to start the next
   * MAC computation with the same parameters or initialized with different parameters.
   */
  public final native void reset();

  /**
   * Clones this {@code Mac} instance and the underlying implementation.
   *
   * @return the cloned instance.
   * @throws CloneNotSupportedException if the underlying implementation does not support cloning.
   */
  @Override
  public final native Object clone() throws CloneNotSupportedException;
}
