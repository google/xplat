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

import java.io.Serializable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A Uniform Resource Identifier that identifies an abstract or physical resource, as specified by
 * <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC 2396</a>.
 *
 * <h3>Parts of a URI</h3>
 *
 * A URI is composed of many parts. This class can both parse URI strings into parts and compose URI
 * strings from parts. For example, consider the parts of this URI: {@code
 * http://username:password@host:8080/directory/file?query#fragment}
 *
 * <table>
 * <tr><th>Component                                            </th><th>Example value                                                      </th><th>Also known as</th></tr>
 * <tr><td>{@link #getScheme() Scheme}                          </td><td>{@code http}                                                       </td><td>protocol</td></tr>
 * <tr><td>{@link #getSchemeSpecificPart() Scheme-specific part}</td><td>{@code //username:password@host:8080/directory/file?query#fragment}</td><td></td></tr>
 * <tr><td>{@link #getAuthority() Authority}                    </td><td>{@code username:password@host:8080}                                </td><td></td></tr>
 * <tr><td>{@link #getUserInfo() User Info}                     </td><td>{@code username:password}                                          </td><td></td></tr>
 * <tr><td>{@link #getHost() Host}                              </td><td>{@code host}                                                       </td><td></td></tr>
 * <tr><td>{@link #getPort() Port}                              </td><td>{@code 8080}                                                       </td><td></td></tr>
 * <tr><td>{@link #getPath() Path}                              </td><td>{@code /directory/file}                                            </td><td></td></tr>
 * <tr><td>{@link #getQuery() Query}                            </td><td>{@code query}                                                      </td><td></td></tr>
 * <tr><td>{@link #getFragment() Fragment}                      </td><td>{@code fragment}                                                   </td><td>ref</td></tr>
 * </table>
 *
 * <h3>Absolute vs. Relative URIs</h3>
 *
 * URIs are either {@link #isAbsolute() absolute or relative}.
 *
 * <ul>
 *   <li><strong>Absolute:</strong> {@code http://android.com/robots.txt}
 *   <li><strong>Relative:</strong> {@code robots.txt}
 * </ul>
 *
 * <p>Absolute URIs always have a scheme. If its scheme is supported by {@link URL}, you can use
 * {@link #toURL} to convert an absolute URI to a URL.
 *
 * <p>Relative URIs do not have a scheme and cannot be converted to URLs. If you have the absolute
 * URI that a relative URI is relative to, you can use {@link #resolve} to compute the referenced
 * absolute URI. Symmetrically, you can use {@link #relativize} to compute the relative URI from one
 * URI to another.
 *
 * <pre>{@code
 * URI absolute = new URI("http://android.com/");
 * URI relative = new URI("robots.txt");
 * URI resolved = new URI("http://android.com/robots.txt");
 *
 * // print "http://android.com/robots.txt"
 * System.out.println(absolute.resolve(relative));
 *
 * // print "robots.txt"
 * System.out.println(absolute.relativize(resolved));
 * }</pre>
 *
 * <h3>Opaque vs. Hierarchical URIs</h3>
 *
 * Absolute URIs are either {@link #isOpaque() opaque or hierarchical}. Relative URIs are always
 * hierarchical.
 *
 * <ul>
 *   <li><strong>Hierarchical:</strong> {@code http://android.com/robots.txt}
 *   <li><strong>Opaque:</strong> {@code mailto:robots@example.com}
 * </ul>
 *
 * <p>Opaque URIs have both a scheme and a scheme-specific part that does not begin with the slash
 * character: {@code /}. The contents of the scheme-specific part of an opaque URI is not parsed so
 * an opaque URI never has an authority, user info, host, port, path or query. An opaque URIs may
 * have a fragment, however. A typical opaque URI is {@code mailto:robots@example.com}.
 *
 * <table>
 * <tr><th>Component           </th><th>Example value             </th></tr>
 * <tr><td>Scheme              </td><td>{@code mailto}            </td></tr>
 * <tr><td>Scheme-specific part</td><td>{@code robots@example.com}</td></tr>
 * <tr><td>Fragment            </td><td>                          </td></tr>
 * </table>
 *
 * <p>Hierarchical URIs may have values for any URL component. They always have a non-null path,
 * though that path may be the empty string.
 *
 * <h3>Encoding and Decoding URI Components</h3>
 *
 * Each component of a URI permits a limited set of legal characters. Other characters must first be
 * <i>encoded</i> before they can be embedded in a URI. To recover the original characters from a
 * URI, they may be <i>decoded</i>. <strong>Contrary to what you might expect,</strong> this class
 * uses the term <i>raw</i> to refer to encoded strings. The non-<i>raw</i> accessors return decoded
 * strings. For example, consider how this URI is decoded: {@code
 * http://user:pa55w%3Frd@host:80/doc%7Csearch?q=green%20robots#over%206%22}
 *
 * <table>
 * <tr><th>Component           </th><th>Legal Characters                                                    </th><th>Other Constraints                                  </th><th>Raw Value                                                      </th><th>Value</th></tr>
 * <tr><td>Scheme              </td><td>{@code 0-9}, {@code a-z}, {@code A-Z}, {@code +-.}                  </td><td>First character must be in {@code a-z}, {@code A-Z}</td><td>                                                               </td><td>{@code http}</td></tr>
 * <tr><td>Scheme-specific part</td><td>{@code 0-9}, {@code a-z}, {@code A-Z}, {@code _-!.~'()*,;:$&+=?/[]@}</td><td>Non-ASCII characters okay                          </td><td>{@code //user:pa55w%3Frd@host:80/doc%7Csearch?q=green%20robots}</td><td>{@code //user:pa55w?rd@host:80/doc|search?q=green robots}</td></tr>
 * <tr><td>Authority           </td><td>{@code 0-9}, {@code a-z}, {@code A-Z}, {@code _-!.~'()*,;:$&+=@[]}  </td><td>Non-ASCII characters okay                          </td><td>{@code user:pa55w%3Frd@host:80}                                </td><td>{@code user:pa55w?rd@host:80}</td></tr>
 * <tr><td>User Info           </td><td>{@code 0-9}, {@code a-z}, {@code A-Z}, {@code _-!.~'()*,;:$&+=}     </td><td>Non-ASCII characters okay                          </td><td>{@code user:pa55w%3Frd}                                        </td><td>{@code user:pa55w?rd}</td></tr>
 * <tr><td>Host                </td><td>{@code 0-9}, {@code a-z}, {@code A-Z}, {@code -.[]}                 </td><td>Domain name, IPv4 address or [IPv6 address]        </td><td>                                                               </td><td>host</td></tr>
 * <tr><td>Port                </td><td>{@code 0-9}                                                         </td><td>                                                   </td><td>                                                               </td><td>{@code 80}</td></tr>
 * <tr><td>Path                </td><td>{@code 0-9}, {@code a-z}, {@code A-Z}, {@code _-!.~'()*,;:$&+=/@}   </td><td>Non-ASCII characters okay                          </td><td>{@code /doc%7Csearch}                                          </td><td>{@code /doc|search}</td></tr>
 * <tr><td>Query               </td><td>{@code 0-9}, {@code a-z}, {@code A-Z}, {@code _-!.~'()*,;:$&+=?/[]@}</td><td>Non-ASCII characters okay                          </td><td>{@code q=green%20robots}                                       </td><td>{@code q=green robots}</td></tr>
 * <tr><td>Fragment            </td><td>{@code 0-9}, {@code a-z}, {@code A-Z}, {@code _-!.~'()*,;:$&+=?/[]@}</td><td>Non-ASCII characters okay                          </td><td>{@code over%206%22}                                            </td><td>{@code over 6"}</td></tr>
 * </table>
 *
 * A URI's host, port and scheme are not eligible for encoding and must not contain illegal
 * characters.
 *
 * <p>To encode a URI, invoke any of the multiple-parameter constructors of this class. These
 * constructors accept your original strings and encode them into their raw form.
 *
 * <p>To decode a URI, invoke the single-string constructor, and then use the appropriate accessor
 * methods to get the decoded components.
 *
 * <p>The {@link URL} class can be used to retrieve resources by their URI.
 */
@NullMarked
public final class URI implements Comparable<URI>, Serializable {

  private URI() {}

  /**
   * Creates a new URI instance by parsing {@code spec}.
   *
   * @param spec a URI whose illegal characters have all been encoded.
   */
  public URI(String spec) throws URISyntaxException {
    throw new UnsupportedOperationException();
  }

  /**
   * Creates a new URI instance of the given unencoded component parts.
   *
   * @param scheme the URI scheme, or null for a non-absolute URI.
   */
  public URI(
      @Nullable String scheme, @Nullable String schemeSpecificPart, @Nullable String fragment)
      throws URISyntaxException {
    throw new UnsupportedOperationException();
  }

  /**
   * Creates a new URI instance of the given unencoded component parts.
   *
   * @param scheme the URI scheme, or null for a non-absolute URI.
   */
  public URI(
      @Nullable String scheme,
      @Nullable String userInfo,
      @Nullable String host,
      int port,
      @Nullable String path,
      @Nullable String query,
      @Nullable String fragment)
      throws URISyntaxException {
    throw new UnsupportedOperationException();
  }

  /**
   * Creates a new URI instance of the given unencoded component parts.
   *
   * @param scheme the URI scheme, or null for a non-absolute URI.
   */
  public URI(
      @Nullable String scheme,
      @Nullable String host,
      @Nullable String path,
      @Nullable String fragment)
      throws URISyntaxException {
    throw new UnsupportedOperationException();
  }

  /**
   * Creates a new URI instance of the given unencoded component parts.
   *
   * @param scheme the URI scheme, or null for a non-absolute URI.
   */
  public URI(
      @Nullable String scheme,
      @Nullable String authority,
      @Nullable String path,
      @Nullable String query,
      @Nullable String fragment)
      throws URISyntaxException {
    throw new UnsupportedOperationException();
  }

  /**
   * Compares this URI with the given argument {@code uri}. This method will return a negative value
   * if this URI instance is less than the given argument and a positive value if this URI instance
   * is greater than the given argument. The return value {@code 0} indicates that the two instances
   * represent the same URI. To define the order the single parts of the URI are compared with each
   * other. String components will be ordered in the natural case-sensitive way. A hierarchical URI
   * is less than an opaque URI and if one part is {@code null} the URI with the undefined part is
   * less than the other one.
   *
   * @param uri the URI this instance has to compare with.
   * @return the value representing the order of the two instances.
   */
  public int compareTo(URI uri) {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the URI formed by parsing {@code uri}. This method behaves identically to the string
   * constructor but throws a different exception on failure. The constructor fails with a checked
   * {@link URISyntaxException}; this method fails with an unchecked {@link
   * IllegalArgumentException}.
   */
  public static URI create(String uri) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean equals(@Nullable Object o) {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the scheme of this URI, or null if this URI has no scheme. This is also known as the
   * protocol.
   */
  public String getScheme() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the decoded scheme-specific part of this URI, or null if this URI has no
   * scheme-specific part.
   */
  public String getSchemeSpecificPart() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the encoded scheme-specific part of this URI, or null if this URI has no
   * scheme-specific part.
   */
  public String getRawSchemeSpecificPart() {
    throw new UnsupportedOperationException();
  }

  /** Returns the decoded authority part of this URI, or null if this URI has no authority. */
  public String getAuthority() {
    throw new UnsupportedOperationException();
  }

  /** Returns the encoded authority of this URI, or null if this URI has no authority. */
  public String getRawAuthority() {
    throw new UnsupportedOperationException();
  }

  /** Returns the decoded user info of this URI, or null if this URI has no user info. */
  public String getUserInfo() {
    throw new UnsupportedOperationException();
  }

  /** Returns the encoded user info of this URI, or null if this URI has no user info. */
  public String getRawUserInfo() {
    throw new UnsupportedOperationException();
  }

  /** Returns the host of this URI, or null if this URI has no host. */
  public String getHost() {
    throw new UnsupportedOperationException();
  }

  /** Returns the port number of this URI, or {@code -1} if this URI has no explicit port. */
  public int getPort() {
    throw new UnsupportedOperationException();
  }

  /** @hide */
  public int getEffectivePort() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the port to use for {@code scheme} connections will use when {@link #getPort} returns
   * {@code specifiedPort}.
   *
   * @hide
   */
  public static int getEffectivePort(String scheme, int specifiedPort) {
    throw new UnsupportedOperationException();
  }

  /** Returns the decoded path of this URI, or null if this URI has no path. */
  public String getPath() {
    throw new UnsupportedOperationException();
  }

  /** Returns the encoded path of this URI, or null if this URI has no path. */
  public String getRawPath() {
    throw new UnsupportedOperationException();
  }

  /** Returns the decoded query of this URI, or null if this URI has no query. */
  public String getQuery() {
    throw new UnsupportedOperationException();
  }

  /** Returns the encoded query of this URI, or null if this URI has no query. */
  public String getRawQuery() {
    throw new UnsupportedOperationException();
  }

  /** Returns the decoded fragment of this URI, or null if this URI has no fragment. */
  public String getFragment() {
    throw new UnsupportedOperationException();
  }

  /** Gets the encoded fragment of this URI, or null if this URI has no fragment. */
  public String getRawFragment() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int hashCode() {
    throw new UnsupportedOperationException();
  }

  /** Returns true if this URI is absolute, which means that a scheme is defined. */
  public boolean isAbsolute() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns true if this URI is opaque. Opaque URIs are absolute and have a scheme-specific part
   * that does not start with a slash character. All parts except scheme, scheme-specific and
   * fragment are undefined.
   */
  public boolean isOpaque() {
    throw new UnsupportedOperationException();
  }

  /**
   * Normalizes the path part of this URI.
   *
   * @return an URI object which represents this instance with a normalized path.
   */
  public URI normalize() {
    throw new UnsupportedOperationException();
  }

  /**
   * Tries to parse the authority component of this URI to divide it into the host, port, and
   * user-info. If this URI is already determined as a ServerAuthority this instance will be
   * returned without changes.
   *
   * @return this instance with the components of the parsed server authority.
   * @throws URISyntaxException if the authority part could not be parsed as a server-based
   *     authority.
   */
  public URI parseServerAuthority() throws URISyntaxException {
    throw new UnsupportedOperationException();
  }

  /**
   * Makes the given URI {@code relative} to a relative URI against the URI represented by this
   * instance.
   *
   * @param relative the URI which has to be relativized against this URI.
   * @return the relative URI.
   */
  public URI relativize(URI relative) {
    throw new UnsupportedOperationException();
  }

  /**
   * Resolves the given URI {@code relative} against the URI represented by this instance.
   *
   * @param relative the URI which has to be resolved against this URI.
   * @return the resolved URI.
   */
  public URI resolve(URI relative) {
    throw new UnsupportedOperationException();
  }

  /**
   * Creates a new URI instance by parsing the given string {@code relative} and resolves the
   * created URI against the URI represented by this instance.
   *
   * @param relative the given string to create the new URI instance which has to be resolved later
   *     on.
   * @return the created and resolved URI.
   */
  public URI resolve(String relative) {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the textual string representation of this URI instance using the US-ASCII encoding.
   *
   * @return the US-ASCII string representation of this URI.
   */
  public String toASCIIString() {
    throw new UnsupportedOperationException();
  }

  /** Returns the encoded URI. */
  @Override
  public String toString() {
    throw new UnsupportedOperationException();
  }

  /**
   * Converts this URI instance to a URL.
   *
   * @return the created URL representing the same resource as this URI.
   * @throws MalformedURLException if an error occurs while creating the URL or no protocol handler
   *     could be found.
   */
  public URL toURL() throws MalformedURLException {
    throw new UnsupportedOperationException();
  }
}
