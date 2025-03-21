/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package java.util.zip;

import static javaemul.internal.InternalPreconditions.checkCriticalArrayBounds;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * This class decompresses data that was compressed using the <i>DEFLATE</i> algorithm (see <a
 * href="http://www.gzip.org/algorithm.txt">specification</a>).
 *
 * <p>It is usually more convenient to use {@link InflaterInputStream}.
 *
 * <p>To decompress an in-memory {@code byte[]} to another in-memory {@code byte[]} manually:
 *
 * <pre>
 *     byte[] compressedBytes = ...
 *     int decompressedByteCount = ... // From your format's metadata.
 *     Inflater inflater = new Inflater();
 *     inflater.setInput(compressedBytes, 0, compressedBytes.length);
 *     byte[] decompressedBytes = new byte[decompressedByteCount];
 *     if (inflater.inflate(decompressedBytes) != decompressedByteCount) {
 *         throw new AssertionError();
 *     }
 *     inflater.end();
 * </pre>
 *
 * <p>In situations where you don't have all the input in one array (or have so much input that you
 * want to feed it to the inflater in chunks), it's possible to call {@link #setInput} repeatedly,
 * but you're much better off using {@link InflaterInputStream} to handle all this for you.
 *
 * <p>If you don't know how big the decompressed data will be, you can call {@link #inflate}
 * repeatedly on a temporary buffer, copying the bytes to a {@link java.io.ByteArrayOutputStream},
 * but this is probably another sign you'd be better off using {@link InflaterInputStream}.
 */
@NullMarked
public class Inflater {

  private int inLength;

  private int inRead; // Set by inflateImpl.
  private boolean finished; // Set by inflateImpl.
  private boolean needsDictionary; // Set by inflateImpl.

  private @Nullable NativeZipStream nativeZipStream = null;

  /**
   * This constructor creates an inflater that expects a header from the input stream. Use {@link
   * #Inflater(boolean)} if the input comes without a ZLIB header.
   */
  public Inflater() {
    this(false);
  }

  /**
   * This constructor allows to create an inflater that expects no header from the input stream.
   *
   * @param noHeader {@code true} indicates that no ZLIB header comes with the input.
   */
  public Inflater(boolean noHeader) {
    nativeZipStream = InflaterImpl.createStream(noHeader);
  }

  /**
   * Releases resources associated with this {@code Inflater}. Any unused input or output is
   * discarded. This method should be called explicitly in order to free native resources as soon as
   * possible. After {@code end()} is called, other methods will typically throw {@code
   * IllegalStateException}.
   */
  public synchronized void end() {
    if (nativeZipStream != null) {
      InflaterImpl.end(nativeZipStream);
      inRead = 0;
      inLength = 0;
      nativeZipStream = null;
    }
  }

  /**
   * Indicates if the {@code Inflater} has inflated the entire deflated stream. If deflated bytes
   * remain and {@link #needsInput} returns {@code true} this method will return {@code false}. This
   * method should be called after all deflated input is supplied to the {@code Inflater}.
   *
   * @return {@code true} if all input has been inflated, {@code false} otherwise.
   */
  public synchronized boolean finished() {
    return finished;
  }

  /**
   * Returns the {@link Adler32} checksum of the bytes inflated so far, or the checksum of the
   * preset dictionary if {@link #needsDictionary} returns true.
   */
  public synchronized int getAdler() {
    checkOpen();
    return InflaterImpl.getAdler(nativeZipStream);
  }

  /**
   * Returns the total number of bytes read by the {@code Inflater}. This method is the same as
   * {@link #getTotalIn} except that it returns a {@code long} value instead of an integer.
   */
  public synchronized long getBytesRead() {
    checkOpen();
    return InflaterImpl.getTotalIn(nativeZipStream);
  }

  /**
   * Returns a the total number of bytes written by this {@code Inflater}. This method is the same
   * as {@code getTotalOut} except it returns a {@code long} value instead of an integer.
   */
  public synchronized long getBytesWritten() {
    checkOpen();
    return InflaterImpl.getTotalOut(nativeZipStream);
  }

  /** Returns the number of bytes of current input remaining to be read by this inflater. */
  public synchronized int getRemaining() {
    return inLength - inRead;
  }

  /**
   * Returns the offset of the next byte to read in the underlying buffer.
   *
   * <p>For internal use only.
   */
  synchronized int getCurrentOffset() {
    return inRead;
  }

  /**
   * Returns the total number of bytes of input read by this {@code Inflater}. This method is
   * limited to 32 bits; use {@link #getBytesRead} instead.
   */
  public synchronized int getTotalIn() {
    checkOpen();
    return (int) Math.min(InflaterImpl.getTotalIn(nativeZipStream), (long) Integer.MAX_VALUE);
  }

  /**
   * Returns the total number of bytes written to the output buffer by this {@code Inflater}. The
   * method is limited to 32 bits; use {@link #getBytesWritten} instead.
   */
  public synchronized int getTotalOut() {
    checkOpen();
    return (int) Math.min(InflaterImpl.getTotalOut(nativeZipStream), (long) Integer.MAX_VALUE);
  }

  /**
   * Inflates bytes from the current input and stores them in {@code buf}.
   *
   * @param buf the buffer where decompressed data bytes are written.
   * @return the number of bytes inflated.
   * @throws DataFormatException if the underlying stream is corrupted or was not compressed using a
   *     {@code Deflater}.
   */
  public int inflate(byte[] buf) throws DataFormatException {
    return inflate(buf, 0, buf.length);
  }

  /**
   * Inflates up to {@code byteCount} bytes from the current input and stores them in {@code buf}
   * starting at {@code offset}.
   *
   * @throws DataFormatException if the underlying stream is corrupted or was not compressed using a
   *     {@code Deflater}.
   * @return the number of bytes inflated.
   */
  public synchronized int inflate(byte[] buf, int offset, int byteCount)
      throws DataFormatException {
    checkCriticalArrayBounds(offset, offset + byteCount, buf.length);

    checkOpen();

    if (needsInput()) {
      return 0;
    }

    boolean neededDict = needsDictionary;
    needsDictionary = false;
    int result =
        InflaterImpl.inflate(
            buf,
            offset,
            byteCount,
            nativeZipStream,
            /* streamEndCallback= */ () -> finished = true,
            /* bytesReadConsumer= */ bytesRead -> inRead += bytesRead,
            /* needDictCallback= */ () -> needsDictionary = true);
    if (needsDictionary && neededDict) {
      throw new DataFormatException("Needs dictionary");
    }
    return result;
  }

  /**
   * Returns true if the input bytes were compressed with a preset dictionary. This method should be
   * called if the first call to {@link #inflate} returns 0, to determine whether a dictionary is
   * required. If so, {@link #setDictionary} should be called with the appropriate dictionary before
   * calling {@code inflate} again. Use {@link #getAdler} to determine which dictionary is required.
   */
  public synchronized boolean needsDictionary() {
    return needsDictionary;
  }

  /** Returns true if {@link #setInput} must be called before inflation can continue. */
  public synchronized boolean needsInput() {
    return inRead == inLength;
  }

  /** Resets this {@code Inflater}. Should be called prior to inflating a new set of data. */
  public synchronized void reset() {
    checkOpen();
    finished = false;
    needsDictionary = false;
    inLength = inRead = 0;
    InflaterImpl.reset(nativeZipStream);
  }

  /**
   * Sets the preset dictionary to be used for inflation to {@code dictionary}. See {@link
   * #needsDictionary} for details.
   */
  public synchronized void setDictionary(byte[] dictionary) {
    setDictionary(dictionary, 0, dictionary.length);
  }

  /**
   * Sets the preset dictionary to be used for inflation to a subsequence of {@code dictionary}
   * starting at {@code offset} and continuing for {@code byteCount} bytes. See {@link
   * #needsDictionary} for details.
   */
  public synchronized void setDictionary(byte[] dictionary, int offset, int byteCount) {
    checkOpen();
    checkCriticalArrayBounds(offset, offset + byteCount, dictionary.length);
    InflaterImpl.setDictionary(dictionary, offset, byteCount, nativeZipStream);
  }

  /**
   * Sets the current input to to be decompressed. This method should only be called if {@link
   * #needsInput} returns {@code true}.
   */
  public synchronized void setInput(byte[] buf) {
    setInput(buf, 0, buf.length);
  }

  /**
   * Sets the current input to to be decompressed. This method should only be called if {@link
   * #needsInput} returns {@code true}.
   */
  public synchronized void setInput(byte[] buf, int offset, int byteCount) {
    checkOpen();
    checkCriticalArrayBounds(offset, offset + byteCount, buf.length);
    inRead = 0;
    inLength = byteCount;
    InflaterImpl.setInput(buf, offset, byteCount, nativeZipStream);
  }

  private void checkOpen() {
    if (nativeZipStream == null) {
      throw new IllegalStateException("attempt to use Inflater after calling end");
    }
  }
}
