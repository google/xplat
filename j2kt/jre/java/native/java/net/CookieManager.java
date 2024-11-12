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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * This class provides a concrete implementation of CookieHandler. It separates the storage of
 * cookies from the policy which decides to accept or deny cookies. The constructor can have two
 * arguments: a CookieStore and a CookiePolicy. The former is in charge of cookie storage and the
 * latter makes decision on acceptance/rejection.
 *
 * <p>CookieHandler is in the center of cookie management. User can make use of
 * CookieHandler.setDefault to set a CookieManager as the default one used.
 *
 * <p>CookieManager.put uses CookiePolicy.shouldAccept to decide whether to put some cookies into a
 * cookie store. Three built-in CookiePolicy is defined: ACCEPT_ALL, ACCEPT_NONE and
 * ACCEPT_ORIGINAL_SERVER. Users can also customize the policy by implementing CookiePolicy. Any
 * accepted HTTP cookie is stored in CookieStore and users can also have their own implementation.
 * Up to now, Only add(URI, HttpCookie) and get(URI) are used by CookieManager. Other methods in
 * this class may probably be used in a more complicated implementation.
 *
 * <p>There are many ways to customize user's own HTTP cookie management:
 *
 * <p>First, call CookieHandler.setDefault to set a new CookieHandler implementation. Second, call
 * CookieHandler.getDefault to use CookieManager. The CookiePolicy and CookieStore used are
 * customized. Third, use the customized CookiePolicy and the CookieStore.
 *
 * <p>This implementation conforms to <a href="http://www.ietf.org/rfc/rfc2965.txt">RFC 2965</a>
 * section 3.3.
 *
 * @since 1.6
 */
@NullMarked
public class CookieManager extends CookieHandler {

  /**
   * Constructs a new cookie manager.
   *
   * <p>The invocation of this constructor is the same as the invocation of CookieManager(null,
   * null).
   */
  public CookieManager() {
    this(null, null);
  }

  /**
   * Constructs a new cookie manager using a specified cookie store and a cookie policy.
   *
   * @param store a CookieStore to be used by cookie manager. The manager will use a default one if
   *     the arg is null.
   * @param cookiePolicy a CookiePolicy to be used by cookie manager ACCEPT_ORIGINAL_SERVER will be
   *     used if the arg is null.
   */
  public CookieManager(@Nullable CookieStore store, @Nullable CookiePolicy cookiePolicy) {
    throw new UnsupportedOperationException();
  }

  /**
   * Searches and gets all cookies in the cache by the specified uri in the request header.
   *
   * @param uri the specified uri to search for
   * @param requestHeaders a list of request headers
   * @return a map that record all such cookies, the map is unchangeable
   * @throws IOException if some error of I/O operation occurs
   */
  @Override
  public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders)
      throws IOException {
    throw new UnsupportedOperationException();
  }

  /**
   * Sets cookies according to uri and responseHeaders
   *
   * @param uri the specified uri
   * @param responseHeaders a list of request headers
   * @throws IOException if some error of I/O operation occurs
   */
  @Override
  public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {

    throw new UnsupportedOperationException();
  }

  /**
   * Returns a cookie-safe path by truncating everything after the last "/". When request path like
   * "/foo/bar.html" yields a cookie, that cookie's default path is "/foo/".
   */
  static String pathToCookiePath(String path) {
    throw new UnsupportedOperationException();
  }

  /**
   * Sets the cookie policy of this cookie manager.
   *
   * <p>ACCEPT_ORIGINAL_SERVER is the default policy for CookieManager.
   *
   * @param cookiePolicy the cookie policy. if null, the original policy will not be changed.
   */
  public void setCookiePolicy(CookiePolicy cookiePolicy) {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets current cookie store.
   *
   * @return the cookie store currently used by cookie manager.
   */
  public CookieStore getCookieStore() {
    throw new UnsupportedOperationException();
  }
}
