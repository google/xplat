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

package java.security;

import org.jspecify.annotations.NullMarked;

/**
 * {@code Provider} is the abstract superclass for all security providers in the Java security
 * infrastructure.
 *
 * <p>This is a much scaled down version of the Provider that only really a named data type clase
 * useful currently for Mac.
 */
@NullMarked
public abstract class Provider {
  private static final long serialVersionUID = -4298000515446427739L;

  private final String name;

  private final double version;

  private final String info;

  /**
   * Constructs a new instance of {@code Provider} with its name, version and description.
   *
   * @param name the name of the provider.
   * @param version the version of the provider.
   * @param info a description of the provider.
   */
  protected Provider(String name, double version, String info) {
    this.name = name;
    this.version = version;
    this.info = info;
  }

  /**
   * Returns the name of this provider.
   *
   * @return the name of this provider.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the version number for the services being provided.
   *
   * @return the version number for the services being provided.
   */
  public double getVersion() {
    return version;
  }

  /**
   * Returns a description of the services being provided.
   *
   * @return a description of the services being provided.
   */
  public String getInfo() {
    return info;
  }
}
