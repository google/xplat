/* Licensed to the Apache Software Foundation (ASF) under one or more
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

package java.net;

import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * An opaque key-value value pair held by an HTTP client to permit a stateful session with an HTTP
 * server. This class parses cookie headers for all three commonly used HTTP cookie specifications:
 *
 * <ul>
 *   <li>The Netscape cookie spec is officially obsolete but widely used in practice. Each cookie
 *       contains one key-value pair and the following attributes: {@code Domain}, {@code Expires},
 *       {@code Path}, and {@code Secure}. The {@link #getVersion() version} of cookies in this
 *       format is {@code 0}.
 *       <p>There are no accessors for the {@code Expires} attribute. When parsed, expires
 *       attributes are assigned to the {@link #getMaxAge() Max-Age} attribute as an offset from
 *       {@link System#currentTimeMillis() now}.
 *   <li><a href="http://www.ietf.org/rfc/rfc2109.txt">RFC 2109</a> formalizes the Netscape cookie
 *       spec. It replaces the {@code Expires} timestamp with a {@code Max-Age} duration and adds
 *       {@code Comment} and {@code Version} attributes. The {@link #getVersion() version} of
 *       cookies in this format is {@code 1}.
 *   <li><a href="http://www.ietf.org/rfc/rfc2965.txt">RFC 2965</a> refines RFC 2109. It adds {@code
 *       Discard}, {@code Port}, and {@code CommentURL} attributes and renames the header from
 *       {@code Set-Cookie} to {@code Set-Cookie2}. The {@link #getVersion() version} of cookies in
 *       this format is {@code 1}.
 * </ul>
 *
 * <p>Support for the "HttpOnly" attribute specified in <a
 * href="http://tools.ietf.org/html/rfc6265">RFC 6265</a> is also included. RFC 6265 is intended to
 * obsolete RFC 2965. Support for features from RFC 2965 that have been deprecated by RFC 6265 such
 * as Cookie2, Set-Cookie2 headers and version information remain supported by this class.
 *
 * <p>This implementation silently discards unrecognized attributes.
 *
 * @since 1.6
 */
@NullMarked
public final class HttpCookie implements Cloneable {

  /**
   * Returns true if {@code host} matches the domain pattern {@code domain}.
   *
   * @param domainPattern a host name (like {@code android.com} or {@code localhost}), or a pattern
   *     to match subdomains of a domain name (like {@code .android.com}). A special case pattern is
   *     {@code .local}, which matches all hosts without a TLD (like {@code localhost}).
   * @param host the host name or IP address from an HTTP request.
   */
  public static boolean domainMatches(String domainPattern, String host) {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns true if {@code cookie} should be sent to or accepted from {@code uri} with respect to
   * the cookie's path. Cookies match by directory prefix: URI "/foo" matches cookies "/foo",
   * "/foo/" and "/foo/bar", but not "/" or "/foobar".
   */
  static boolean pathMatches(HttpCookie cookie, URI uri) {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns true if {@code cookie} should be sent to {@code uri} with respect to the cookie's
   * secure attribute. Secure cookies should not be sent in insecure (ie. non-HTTPS) requests.
   */
  static boolean secureMatches(HttpCookie cookie, URI uri) {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns true if {@code cookie} should be sent to {@code uri} with respect to the cookie's port
   * list.
   */
  static boolean portMatches(HttpCookie cookie, URI uri) {
    throw new UnsupportedOperationException();
  }

  /**
   * Constructs a cookie from a string. The string should comply with set-cookie or set-cookie2
   * header format as specified in <a href="http://www.ietf.org/rfc/rfc2965.txt">RFC 2965</a>. Since
   * set-cookies2 syntax allows more than one cookie definitions in one header, the returned object
   * is a list.
   *
   * @param header a set-cookie or set-cookie2 header.
   * @return a list of constructed cookies
   * @throws IllegalArgumentException if the string does not comply with cookie specification, or
   *     the cookie name contains illegal characters, or reserved tokens of cookie specification
   *     appears
   * @throws NullPointerException if header is null
   */
  public static List<HttpCookie> parse(String header) {
    throw new UnsupportedOperationException();
  }

  static class CookieParser {

    CookieParser(String input) {
      throw new UnsupportedOperationException();
    }

    public List<HttpCookie> parse() {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * Creates a new cookie.
   *
   * @param name a non-empty string that contains only printable ASCII, no commas or semicolons, and
   *     is not prefixed with {@code $}. May not be an HTTP attribute name.
   * @param value an opaque value from the HTTP server.
   * @throws IllegalArgumentException if {@code name} is invalid.
   */
  public HttpCookie(String name, String value) {
    throw new UnsupportedOperationException();
  }

  /** Returns the {@code Comment} attribute. */
  public String getComment() {
    throw new UnsupportedOperationException();
  }

  /** Returns the value of {@code CommentURL} attribute. */
  public String getCommentURL() {
    throw new UnsupportedOperationException();
  }

  /** Returns the {@code Discard} attribute. */
  public boolean getDiscard() {
    throw new UnsupportedOperationException();
  }

  /** Returns the {@code Domain} attribute. */
  public String getDomain() {
    throw new UnsupportedOperationException();
  }

  /** Returns the {@code Max-Age} attribute, in delta-seconds. */
  public long getMaxAge() {
    throw new UnsupportedOperationException();
  }

  /** Returns the name of this cookie. */
  public String getName() {
    throw new UnsupportedOperationException();
  }

  /** Returns the {@code Path} attribute. This cookie is visible to all subpaths. */
  public String getPath() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the {@code Port} attribute, usually containing comma-separated port numbers. A null
   * port indicates that the cookie may be sent to any port. The empty string indicates that the
   * cookie should only be sent to the port of the originating request.
   */
  public String getPortlist() {
    throw new UnsupportedOperationException();
  }

  /** Returns the {@code Secure} attribute. */
  public boolean getSecure() {
    throw new UnsupportedOperationException();
  }

  /** Returns the value of this cookie. */
  public String getValue() {
    throw new UnsupportedOperationException();
  }

  /** Returns the version of this cookie. */
  public int getVersion() {
    throw new UnsupportedOperationException();
  }

  /** Returns true if this cookie's Max-Age is 0. */
  public boolean hasExpired() {
    throw new UnsupportedOperationException();
  }

  /** Set the {@code Comment} attribute of this cookie. */
  public void setComment(String comment) {
    throw new UnsupportedOperationException();
  }

  /** Set the {@code CommentURL} attribute of this cookie. */
  public void setCommentURL(String commentURL) {
    throw new UnsupportedOperationException();
  }

  /** Set the {@code Discard} attribute of this cookie. */
  public void setDiscard(boolean discard) {
    throw new UnsupportedOperationException();
  }

  /**
   * Set the {@code Domain} attribute of this cookie. HTTP clients send cookies only to matching
   * domains.
   */
  public void setDomain(String pattern) {
    throw new UnsupportedOperationException();
  }

  /** Sets the {@code Max-Age} attribute of this cookie. */
  public void setMaxAge(long deltaSeconds) {
    throw new UnsupportedOperationException();
  }

  /**
   * Set the {@code Path} attribute of this cookie. HTTP clients send cookies to this path and its
   * subpaths.
   */
  public void setPath(String path) {
    throw new UnsupportedOperationException();
  }

  /** Set the {@code Port} attribute of this cookie. */
  public void setPortlist(String portList) {
    throw new UnsupportedOperationException();
  }

  /** Sets the {@code Secure} attribute of this cookie. */
  public void setSecure(boolean secure) {
    throw new UnsupportedOperationException();
  }

  /** Sets the opaque value of this cookie. */
  public void setValue(String value) {
    throw new UnsupportedOperationException();
  }

  /**
   * Sets the {@code Version} attribute of the cookie.
   *
   * @throws IllegalArgumentException if v is neither 0 nor 1
   */
  public void setVersion(int newVersion) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object clone() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns true if {@code object} is a cookie with the same domain, name and path. Domain and name
   * use case-insensitive comparison; path uses a case-sensitive comparison.
   */
  @Override
  public boolean equals(@Nullable Object object) {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the hash code of this HTTP cookie:
   *
   * <pre>{@code
   * name.toLowerCase(Locale.US).hashCode()
   *     + (domain == null ? 0 : domain.toLowerCase(Locale.US).hashCode())
   *     + (path == null ? 0 : path.hashCode())
   * }</pre>
   */
  @Override
  public int hashCode() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns a string representing this cookie in the format used by the {@code Cookie} header line
   * in an HTTP request as specified by RFC 2965 section 3.3.4.
   *
   * <p>The resulting string does not include a "Cookie:" prefix or any version information. The
   * returned {@code String} is not suitable for passing to {@link #parse(String)}: Several of the
   * attributes that would be needed to preserve all of the cookie's information are omitted. The
   * String is formatted for an HTTP request not an HTTP response.
   *
   * <p>The attributes included and the format depends on the cookie's {@code version}:
   *
   * <ul>
   *   <li>Version 0: Includes only the name and value. Conforms to RFC 2965 (for version 0
   *       cookies). This should also be used to conform with RFC 6265.
   *   <li>Version 1: Includes the name and value, and Path, Domain and Port attributes. Conforms to
   *       RFC 2965 (for version 1 cookies).
   * </ul>
   */
  @Override
  public String toString() {
    throw new UnsupportedOperationException();
  }
}
