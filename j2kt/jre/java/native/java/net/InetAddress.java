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
import java.util.Arrays;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * An Internet Protocol (IP) address. This can be either an IPv4 address or an IPv6 address, and in
 * practice you'll have an instance of either {@code Inet4Address} or {@code Inet6Address} (this
 * class cannot be instantiated directly). Most code does not need to distinguish between the two
 * families, and should use {@code InetAddress}.
 *
 * <p>An {@code InetAddress} may have a hostname (accessible via {@code getHostName}), but may not,
 * depending on how the {@code InetAddress} was created.
 *
 * <h4>IPv4 numeric address formats</h4>
 *
 * <p>The {@code getAllByName} method accepts IPv4 addresses in the "decimal-dotted-quad" form only:
 *
 * <ul>
 *   <li>{@code "1.2.3.4"} - 1.2.3.4
 * </ul>
 *
 * <h4>IPv6 numeric address formats</h4>
 *
 * <p>The {@code getAllByName} method accepts IPv6 addresses in the following forms (this text comes
 * from <a href="http://www.ietf.org/rfc/rfc2373.txt">RFC 2373</a>, which you should consult for
 * full details of IPv6 addressing):
 *
 * <ul>
 *   <li>
 *       <p>The preferred form is {@code x:x:x:x:x:x:x:x}, where the 'x's are the hexadecimal values
 *       of the eight 16-bit pieces of the address. Note that it is not necessary to write the
 *       leading zeros in an individual field, but there must be at least one numeral in every field
 *       (except for the case described in the next bullet). Examples:
 *       <pre>
 *     FEDC:BA98:7654:3210:FEDC:BA98:7654:3210
 *     1080:0:0:0:8:800:200C:417A</pre>
 *   <li>Due to some methods of allocating certain styles of IPv6 addresses, it will be common for
 *       addresses to contain long strings of zero bits. In order to make writing addresses
 *       containing zero bits easier a special syntax is available to compress the zeros. The use of
 *       "::" indicates multiple groups of 16-bits of zeros. The "::" can only appear once in an
 *       address. The "::" can also be used to compress the leading and/or trailing zeros in an
 *       address.
 *       <p>For example the following addresses:
 *       <pre>
 *     1080:0:0:0:8:800:200C:417A  a unicast address
 *     FF01:0:0:0:0:0:0:101        a multicast address
 *     0:0:0:0:0:0:0:1             the loopback address
 *     0:0:0:0:0:0:0:0             the unspecified addresses</pre>
 *       may be represented as:
 *       <pre>
 *     1080::8:800:200C:417A       a unicast address
 *     FF01::101                   a multicast address
 *     ::1                         the loopback address
 *     ::                          the unspecified addresses</pre>
 *   <li>
 *       <p>An alternative form that is sometimes more convenient when dealing with a mixed
 *       environment of IPv4 and IPv6 nodes is {@code x:x:x:x:x:x:d.d.d.d}, where the 'x's are the
 *       hexadecimal values of the six high-order 16-bit pieces of the address, and the 'd's are the
 *       decimal values of the four low-order 8-bit pieces of the address (standard IPv4
 *       representation). Examples:
 *       <pre>
 *     0:0:0:0:0:0:13.1.68.3
 *     0:0:0:0:0:FFFF:129.144.52.38</pre>
 *       or in compressed form:
 *       <pre>
 *     ::13.1.68.3
 *     ::FFFF:129.144.52.38</pre>
 * </ul>
 *
 * <p>Scopes are given using a trailing {@code %} followed by the scope id, as in {@code
 * 1080::8:800:200C:417A%2} or {@code 1080::8:800:200C:417A%en0}. See <a
 * href="https://www.ietf.org/rfc/rfc4007.txt">RFC 4007</a> for more on IPv6's scoped address
 * architecture.
 *
 * <p>Additionally, for backwards compatibility, IPv6 addresses may be surrounded by square
 * brackets.
 *
 * <h4>DNS caching</h4>
 *
 * <p>In Android 4.0 (Ice Cream Sandwich) and earlier, DNS caching was performed both by InetAddress
 * and by the C library, which meant that DNS TTLs could not be honored correctly. In later
 * releases, caching is done solely by the C library and DNS TTLs are honored.
 *
 * @see Inet4Address
 * @see Inet6Address
 */
@NullMarked
public class InetAddress implements Serializable {
  private int family;

  byte[] ipaddress;

  @Nullable String hostName;

  /**
   * Constructs an {@code InetAddress}.
   *
   * <p>Note: this constructor is for subclasses only.
   */
  InetAddress(int family, byte[] ipaddress, @Nullable String hostName) {
    this.family = family;
    this.ipaddress = ipaddress;
    this.hostName = hostName;
  }

  /**
   * Compares this {@code InetAddress} instance against the specified address in {@code obj}. Two
   * addresses are equal if their address byte arrays have the same length and if the bytes in the
   * arrays are equal.
   *
   * @param obj the object to be tested for equality.
   * @return {@code true} if both objects are equal, {@code false} otherwise.
   */
  @Override
  public boolean equals(@Nullable Object obj) {
    if (!(obj instanceof InetAddress)) {
      return false;
    }
    return Arrays.equals(this.ipaddress, ((InetAddress) obj).ipaddress);
  }

  /**
   * Returns the IP address represented by this {@code InetAddress} instance as a byte array. The
   * elements are in network order (the highest order address byte is in the zeroth element).
   *
   * @return the address in form of a byte array.
   */
  public byte[] getAddress() {
    return ipaddress.clone();
  }

  // /**
  //  * Gets all IP addresses associated with the given {@code host} identified by name or literal
  // IP
  //  * address. The IP address is resolved by the configured name service. If the host name is
  // empty
  //  * or {@code null} an {@code UnknownHostException} is thrown. If the host name is a literal IP
  //  * address string an array with the corresponding single {@code InetAddress} is returned.
  //  *
  //  * @param host the hostname or literal IP string to be resolved.
  //  * @return the array of addresses associated with the specified host.
  //  * @throws UnknownHostException if the address lookup fails.
  //  */
  // public static InetAddress[] getAllByName(String host) throws UnknownHostException {}

  // /**
  //  * Returns the address of a host according to the given host string name {@code host}. The host
  //  * string may be either a machine name or a dotted string IP address. If the latter, the {@code
  //  * hostName} field is determined upon demand. {@code host} can be {@code null} which means that
  // an
  //  * address of the loopback interface is returned.
  //  *
  //  * @param host the hostName to be resolved to an address or {@code null}.
  //  * @return the {@code InetAddress} instance representing the host.
  //  * @throws UnknownHostException if the address lookup fails.
  //  */
  // public static InetAddress getByName(String host) throws UnknownHostException {}

  // /** Returns the numeric representation of this IP address (such as "127.0.0.1"). */
  // public String getHostAddress() {}

  // /**
  //  * Returns the host name corresponding to this IP address. This may or may not be a
  //  * fully-qualified name. If the IP address could not be resolved, the numeric representation is
  //  * returned instead (see {@link #getHostAddress}).
  //  */
  // public String getHostName() {}

  // /** Returns the fully qualified hostname corresponding to this IP address. */
  // public String getCanonicalHostName() {}

  // /**
  //  * Returns an {@code InetAddress} for the local host if possible, or the loopback address
  //  * otherwise. This method works by getting the hostname, performing a DNS lookup, and then
  // taking
  //  * the first returned address. For devices with multiple network interfaces and/or multiple
  //  * addresses per interface, this does not necessarily return the {@code InetAddress} you want.
  //  *
  //  * <p>Multiple interface/address configurations were relatively rare when this API was
  // designed,
  //  * but multiple interfaces are the default for modern mobile devices (with separate wifi and
  // radio
  //  * interfaces), and the need to support both IPv4 and IPv6 has made multiple addresses
  //  * commonplace. New code should thus avoid this method except where it's basically being used
  // to
  //  * get a loopback address or equivalent.
  //  *
  //  * <p>There are two main ways to get a more specific answer:
  //  *
  //  * <ul>
  //  *   <li>If you have a connected socket, you should probably use {@link Socket#getLocalAddress}
  //  *       instead: that will give you the address that's actually in use for that connection.
  // (It's
  //  *       not possible to ask the question "what local address would a connection to a given
  // remote
  //  *       address use?"; you have to actually make the connection and see.)
  //  *   <li>For other use cases, see {@link NetworkInterface}, which lets you enumerate all
  // available
  //  *       network interfaces and their addresses.
  //  * </ul>
  //  *
  //  * <p>Note that if the host doesn't have a hostname set&nbsp;&ndash; as Android devices
  // typically
  //  * don't&nbsp;&ndash; this method will effectively return the loopback address, albeit by
  // getting
  //  * the name {@code localhost} and then doing a lookup to translate that to {@code 127.0.0.1}.
  //  *
  //  * @return an {@code InetAddress} representing the local host, or the loopback address.
  //  * @throws UnknownHostException if the address lookup fails.
  //  */
  // public static InetAddress getLocalHost() throws UnknownHostException {}

  /**
   * Gets the hashcode of the represented IP address.
   *
   * @return the appropriate hashcode value.
   */
  @Override
  public int hashCode() {
    return Arrays.hashCode(ipaddress);
  }

  // /**
  //  * Returns a string containing the host name (if available) and host address. For example:
  // {@code
  //  * "www.google.com/74.125.224.115"} or {@code "/127.0.0.1"}.
  //  *
  //  * <p>IPv6 addresses may additionally include an interface name or scope id. For example:
  // {@code
  //  * "www.google.com/2001:4860:4001:803::1013%eth0"} or {@code "/2001:4860:4001:803::1013%2"}.
  //  */
  // @Override
  // public String toString() {}

  /**
   * Returns true if the string is a valid numeric IPv4 or IPv6 address (such as "192.168.0.1").
   * This copes with all forms of address that Java supports, detailed in the {@link InetAddress}
   * class documentation.
   *
   * @hide used by frameworks/base to ensure that a getAllByName won't cause a DNS lookup.
   */
  public static boolean isNumeric(String address) {
    // TODO: b/377734324 - Implement
    throw new UnsupportedOperationException();
  }

  // /**
  //  * Returns an InetAddress corresponding to the given numeric address (such as {@code
  //  * "192.168.0.1"} or {@code "2001:4860:800d::68"}). This method will never do a DNS lookup.
  //  * Non-numeric addresses are errors.
  //  *
  //  * @hide used by frameworks/base's NetworkUtils.numericToInetAddress
  //  * @throws IllegalArgumentException if {@code numericAddress} is not a numeric address
  //  */
  public static InetAddress parseNumericAddress(String numericAddress) {
    // TODO: b/377734324 - Implement
    throw new UnsupportedOperationException();
  }

  // /**
  //  * Returns the IPv6 loopback address {@code ::1} or the IPv4 loopback address {@code
  // 127.0.0.1}.
  //  *
  //  * @since 1.7
  //  */
  // public static InetAddress getLoopbackAddress() {}

  /**
   * Returns whether this is the IPv6 unspecified wildcard address {@code ::} or the IPv4 "any"
   * address, {@code 0.0.0.0}.
   */
  public boolean isAnyLocalAddress() {
    return false;
  }

  /**
   * Returns whether this address is a link-local address or not.
   *
   * <p>Valid IPv6 link-local addresses have the prefix {@code fe80::/10}.
   *
   * <p><a href="http://www.ietf.org/rfc/rfc3484.txt">RFC 3484</a> "Default Address Selection for
   * Internet Protocol Version 6 (IPv6)" states that both IPv4 auto-configuration addresses (prefix
   * {@code 169.254/16}) and IPv4 loopback addresses (prefix {@code 127/8}) have link-local scope,
   * but {@link Inet4Address} only considers the auto-configuration addresses to have link-local
   * scope. That is: the IPv4 loopback address returns false.
   */
  public boolean isLinkLocalAddress() {
    return false;
  }

  /**
   * Returns whether this address is a loopback address or not.
   *
   * <p>Valid IPv4 loopback addresses have the prefix {@code 127/8}.
   *
   * <p>The only valid IPv6 loopback address is {@code ::1}.
   */
  public boolean isLoopbackAddress() {
    return false;
  }

  /**
   * Returns whether this address is a global multicast address or not.
   *
   * <p>Valid IPv6 global multicast addresses have the prefix {@code ffxe::/16}, where {@code x} is
   * a set of flags and the additional 112 bits make up the global multicast address space.
   *
   * <p>Valid IPv4 global multicast addresses are the range of addresses from {@code 224.0.1.0} to
   * {@code 238.255.255.255}.
   */
  public boolean isMCGlobal() {
    return false;
  }

  /**
   * Returns whether this address is a link-local multicast address or not.
   *
   * <p>Valid IPv6 link-local multicast addresses have the prefix {@code ffx2::/16}, where x is a
   * set of flags and the additional 112 bits make up the link-local multicast address space.
   *
   * <p>Valid IPv4 link-local multicast addresses have the prefix {@code 224.0.0/24}.
   */
  public boolean isMCLinkLocal() {
    return false;
  }

  /**
   * Returns whether this address is a node-local multicast address or not.
   *
   * <p>Valid IPv6 node-local multicast addresses have the prefix {@code ffx1::/16}, where x is a
   * set of flags and the additional 112 bits make up the link-local multicast address space.
   *
   * <p>There are no valid IPv4 node-local multicast addresses.
   */
  public boolean isMCNodeLocal() {
    return false;
  }

  /**
   * Returns whether this address is a organization-local multicast address or not.
   *
   * <p>Valid IPv6 organization-local multicast addresses have the prefix {@code ffx8::/16}, where x
   * is a set of flags and the additional 112 bits make up the link-local multicast address space.
   *
   * <p>Valid IPv4 organization-local multicast addresses have the prefix {@code 239.192/14}.
   */
  public boolean isMCOrgLocal() {
    return false;
  }

  /**
   * Returns whether this address is a site-local multicast address or not.
   *
   * <p>Valid IPv6 site-local multicast addresses have the prefix {@code ffx5::/16}, where x is a
   * set of flags and the additional 112 bits make up the link-local multicast address space.
   *
   * <p>Valid IPv4 site-local multicast addresses have the prefix {@code 239.255/16}.
   */
  public boolean isMCSiteLocal() {
    return false;
  }

  /**
   * Returns whether this address is a multicast address or not.
   *
   * <p>Valid IPv6 multicast addresses have the prefix {@code ff::/8}.
   *
   * <p>Valid IPv4 multicast addresses have the prefix {@code 224/4}.
   */
  public boolean isMulticastAddress() {
    return false;
  }

  /**
   * Returns whether this address is a site-local address or not.
   *
   * <p>For the purposes of this method, valid IPv6 site-local addresses have the deprecated prefix
   * {@code fec0::/10} from <a href="http://www.ietf.org/rfc/rfc1884.txt">RFC 1884</a>, <i>not</i>
   * the modern prefix {@code fc00::/7} from <a href="http://www.ietf.org/rfc/rfc4193.txt">RFC
   * 4193</a>.
   *
   * <p><a href="http://www.ietf.org/rfc/rfc3484.txt">RFC 3484</a> "Default Address Selection for
   * Internet Protocol Version 6 (IPv6)" states that IPv4 private addresses have the prefix {@code
   * 10/8}, {@code 172.16/12}, or {@code 192.168/16}.
   *
   * @return {@code true} if this instance represents a site-local address, {@code false} otherwise.
   */
  public boolean isSiteLocalAddress() {
    return false;
  }

  // /**
  //  * Tries to reach this {@code InetAddress}. This method first tries to use ICMP <i>(ICMP ECHO
  //  * REQUEST)</i>, falling back to a TCP connection on port 7 (Echo) of the remote host.
  //  *
  //  * @param timeout timeout in milliseconds before the test fails if no connection could be
  //  *     established.
  //  * @return {@code true} if this address is reachable, {@code false} otherwise.
  //  * @throws IOException if an error occurs during an I/O operation.
  //  * @throws IllegalArgumentException if timeout is less than zero.
  //  */
  // public boolean isReachable(int timeout) throws IOException {}

  // /**
  //  * Tries to reach this {@code InetAddress}. This method first tries to use ICMP <i>(ICMP ECHO
  //  * REQUEST)</i>, falling back to a TCP connection on port 7 (Echo) of the remote host.
  //  *
  //  * @param networkInterface the network interface on which to connection should be established.
  //  * @param ttl the maximum count of hops (time-to-live).
  //  * @param timeout timeout in milliseconds before the test fails if no connection could be
  //  *     established.
  //  * @return {@code true} if this address is reachable, {@code false} otherwise.
  //  * @throws IOException if an error occurs during an I/O operation.
  //  * @throws IllegalArgumentException if ttl or timeout is less than zero.
  //  */
  // public boolean isReachable(NetworkInterface networkInterface, final int ttl, final int timeout)
  //     throws IOException {}

  // /**
  //  * Equivalent to {@code getByAddress(null, ipAddress)}. Handy for addresses with no associated
  //  * hostname.
  //  */
  // public static InetAddress getByAddress(byte[] ipAddress) throws UnknownHostException {}

  // /**
  //  * Returns an {@code InetAddress} corresponding to the given network-order bytes {@code
  // ipAddress}
  //  * and {@code scopeId}.
  //  *
  //  * <p>For an IPv4 address, the byte array must be of length 4. For IPv6, the byte array must be
  // of
  //  * length 16. Any other length will cause an {@code UnknownHostException}.
  //  *
  //  * <p>No reverse lookup is performed. The given {@code hostName} (which may be null) is
  // associated
  //  * with the new {@code InetAddress} with no validation done.
  //  *
  //  * <p>(Note that numeric addresses such as {@code "127.0.0.1"} are names for the purposes of
  // this
  //  * API. Most callers probably want {@link #getAllByName} instead.)
  //  *
  //  * @throws UnknownHostException if {@code ipAddress} is null or the wrong length.
  //  */
  // public static InetAddress getByAddress(String hostName, byte[] ipAddress)
  //     throws UnknownHostException {}
}
