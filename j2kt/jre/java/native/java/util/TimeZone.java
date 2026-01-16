/*
 * Copyright 2025 Google Inc.
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
package java.util;

import com.google.j2kt.annotations.KtNative;
import java.io.Serializable;

/** Minimal j2kt emulation of TimeZone, backed by kotlinx.datetime.TimeZone. */
@KtNative
public abstract class TimeZone implements Serializable, Cloneable {

  public TimeZone() {}

  public static native void setDefault(TimeZone zone);

  public static native TimeZone getDefault();

  public native String getID();
}
