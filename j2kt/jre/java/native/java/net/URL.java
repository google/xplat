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

package java.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A Uniform Resource Locator that identifies the location of an Internet resource as specified by
 * <a href="http://www.ietf.org/rfc/rfc1738.txt">RFC 1738</a>.
 *
 * <h3>Parts of a URL</h3>
 *
 * A URL is composed of many parts. This class can both parse URL strings into parts and compose URL
 * strings from parts. For example, consider the parts of this URL: {@code
 * http://username:password@host:8080/directory/file?query#ref}:
 *
 * <table>
 * <tr><th>Component</th><th>Example value</th><th>Also known as</th></tr>
 * <tr><td>{@link #getProtocol() Protocol}</td><td>{@code http}</td><td>scheme</td></tr>
 * <tr><td>{@link #getAuthority() Authority}</td><td>{@code username:password@host:8080}</td><td></td></tr>
 * <tr><td>{@link #getUserInfo() User Info}</td><td>{@code username:password}</td><td></td></tr>
 * <tr><td>{@link #getHost() Host}</td><td>{@code host}</td><td></td></tr>
 * <tr><td>{@link #getPort() Port}</td><td>{@code 8080}</td><td></td></tr>
 * <tr><td>{@link #getFile() File}</td><td>{@code /directory/file?query}</td><td></td></tr>
 * <tr><td>{@link #getPath() Path}</td><td>{@code /directory/file}</td><td></td></tr>
 * <tr><td>{@link #getQuery() Query}</td><td>{@code query}</td><td></td></tr>
 * <tr><td>{@link #getRef() Ref}</td><td>{@code ref}</td><td>fragment</td></tr>
 * </table>
 *
 * <h3>Supported Protocols</h3>
 *
 * This class may be used to construct URLs with the following protocols:
 *
 * <ul>
 *   <li><strong>file</strong>: read files from the local filesystem.
 *   <li><strong>ftp</strong>: <a href="http://www.ietf.org/rfc/rfc959.txt">File Transfer
 *       Protocol</a>
 *   <li><strong>http</strong>: <a href="http://www.ietf.org/rfc/rfc2616.txt">Hypertext Transfer
 *       Protocol</a>
 *   <li><strong>https</strong>: <a href="http://www.ietf.org/rfc/rfc2818.txt">HTTP over TLS</a>
 *   <li><strong>jar</strong>: read {@link JarFile Jar files} from the filesystem
 * </ul>
 *
 * In general, attempts to create URLs with any other protocol will fail with a {@link
 * MalformedURLException}. Applications may install handlers for other schemes using {@link
 * #setURLStreamHandlerFactory} or with the {@code java.protocol.handler.pkgs} system property.
 *
 * <p>The {@link URI} class can be used to manipulate URLs of any protocol.
 */
@NullMarked
public final class URL implements Serializable {

  /**
   * Creates a new URL instance by parsing {@code spec}.
   *
   * @throws MalformedURLException if {@code spec} could not be parsed as a URL.
   */
  public URL(String spec) throws MalformedURLException {
    throw new UnsupportedOperationException();
  }

  /**
   * Creates a new URL by resolving {@code spec} relative to {@code context}.
   *
   * @param context the URL to which {@code spec} is relative, or null for no context in which case
   *     {@code spec} must be an absolute URL.
   * @throws MalformedURLException if {@code spec} could not be parsed as a URL or has an
   *     unsupported protocol.
   */
  public URL(URL context, String spec) throws MalformedURLException {
    throw new UnsupportedOperationException();
  }

  /**
   * Creates a new URL of the given component parts. The URL uses the protocol's default port.
   *
   * @throws MalformedURLException if the combination of all arguments do not represent a valid URL
   *     or if the protocol is invalid.
   */
  public URL(String protocol, String host, String file) throws MalformedURLException {
    throw new UnsupportedOperationException();
  }

  /**
   * Creates a new URL of the given component parts. The URL uses the protocol's default port.
   *
   * @param host the host name or IP address of the new URL.
   * @param port the port, or {@code -1} for the protocol's default port.
   * @param file the name of the resource.
   * @throws MalformedURLException if the combination of all arguments do not represent a valid URL
   *     or if the protocol is invalid.
   */
  public URL(String protocol, String host, int port, String file) throws MalformedURLException {
    throw new UnsupportedOperationException();
  }

  void fixURL(boolean fixHost) {
    throw new UnsupportedOperationException();
  }

  /**
   * Sets the properties of this URL using the provided arguments. Only a {@code URLStreamHandler}
   * can use this method to set fields of the existing URL instance. A URL is generally constant.
   */
  protected void set(String protocol, String host, int port, String file, String ref) {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns true if this URL equals {@code o}. URLs are equal if they have the same protocol, host,
   * port, file, and reference.
   *
   * <h3>Network I/O Warning</h3>
   *
   * <p>Some implementations of URL.equals() resolve host names over the network. This is
   * problematic:
   *
   * <ul>
   *   <li><strong>The network may be slow.</strong> Many classes, including core collections like
   *       {@link java.util.Map Map} and {@link java.util.Set Set} expect that {@code equals} and
   *       {@code hashCode} will return quickly. By violating this assumption, this method posed
   *       potential performance problems.
   *   <li><strong>Equal IP addresses do not imply equal content.</strong> Virtual hosting permits
   *       unrelated sites to share an IP address. This method could report two otherwise unrelated
   *       URLs to be equal because they're hosted on the same server.
   *   <li><strong>The network may not be available.</strong> Two URLs could be equal when a network
   *       is available and unequal otherwise.
   *   <li><strong>The network may change.</strong> The IP address for a given host name varies by
   *       network and over time. This is problematic for mobile devices. Two URLs could be equal on
   *       some networks and unequal on others.
   * </ul>
   *
   * <p>This problem is fixed in Android 4.0 (Ice Cream Sandwich). In that release, URLs are only
   * equal if their host names are equal (ignoring case).
   */
  @Override
  public boolean equals(@Nullable Object o) {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns true if this URL refers to the same resource as {@code otherURL}. All URL components
   * except the reference field are compared.
   */
  public boolean sameFile(URL otherURL) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int hashCode() {
    throw new UnsupportedOperationException();
  }

  /**
   * Sets the receiver's stream handler to one which is appropriate for its protocol.
   *
   * <p>Note that this will overwrite any existing stream handler with the new one. Senders must
   * check if the streamHandler is null before calling the method if they do not want this behavior
   * (a speed optimization).
   *
   * @throws MalformedURLException if no reasonable handler is available.
   */
  void setupStreamHandler() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the content of the resource which is referred by this URL. By default this returns an
   * {@code InputStream}, or null if the content type of the response is unknown.
   */
  public final Object getContent() throws IOException {
    throw new UnsupportedOperationException();
  }

  /** Equivalent to {@code openConnection().getContent(types)}. */
  @SuppressWarnings("unchecked") // Param not generic in spec
  public final Object getContent(Class[] types) throws IOException {
    throw new UnsupportedOperationException();
  }

  /** Equivalent to {@code openConnection().getInputStream(types)}. */
  public final InputStream openStream() throws IOException {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the URI equivalent to this URL.
   *
   * @throws URISyntaxException if this URL cannot be converted into a URI.
   */
  public URI toURI() throws URISyntaxException {
    throw new UnsupportedOperationException();
  }

  /**
   * Encodes this URL to the equivalent URI after escaping characters that are not permitted by URI.
   *
   * @hide
   */
  public URI toURILenient() throws URISyntaxException {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns a string containing a concise, human-readable representation of this URL. The returned
   * string is the same as the result of the method {@code toExternalForm()}.
   */
  @Override
  public String toString() {
    throw new UnsupportedOperationException();
  }

  /** Returns a string containing a concise, human-readable representation of this URL. */
  public String toExternalForm() {
    throw new UnsupportedOperationException();
  }

  /** @hide */
  public int getEffectivePort() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the protocol of this URL like "http" or "file". This is also known as the scheme. The
   * returned string is lower case.
   */
  public String getProtocol() {
    throw new UnsupportedOperationException();
  }

  /** Returns the authority part of this URL, or null if this URL has no authority. */
  public String getAuthority() {
    throw new UnsupportedOperationException();
  }

  /** Returns the user info of this URL, or null if this URL has no user info. */
  public String getUserInfo() {
    throw new UnsupportedOperationException();
  }

  /** Returns the host name or IP address of this URL. */
  public String getHost() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the port number of this URL or {@code -1} if this URL has no explicit port.
   *
   * <p>If this URL has no explicit port, connections opened using this URL will use its {@link
   * #getDefaultPort() default port}.
   */
  public int getPort() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the default port number of the protocol used by this URL. If no default port is defined
   * by the protocol or the {@code URLStreamHandler}, {@code -1} will be returned.
   *
   * @see URLStreamHandler#getDefaultPort
   */
  public int getDefaultPort() {
    throw new UnsupportedOperationException();
  }

  /** Returns the file of this URL. */
  public String getFile() {
    throw new UnsupportedOperationException();
  }

  /** Returns the path part of this URL. */
  public String getPath() {
    throw new UnsupportedOperationException();
  }

  /** Returns the query part of this URL, or null if this URL has no query. */
  public String getQuery() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the value of the reference part of this URL, or null if this URL has no reference part.
   * This is also known as the fragment.
   */
  public String getRef() {
    throw new UnsupportedOperationException();
  }

  /**
   * Sets the properties of this URL using the provided arguments. Only a {@code URLStreamHandler}
   * can use this method to set fields of the existing URL instance. A URL is generally constant.
   */
  protected void set(
      String protocol,
      String host,
      int port,
      String authority,
      String userInfo,
      String path,
      String query,
      String ref) {
    throw new UnsupportedOperationException();
  }
}
