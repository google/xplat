/*
 * Copyright 2007 Google Inc.
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
package java.lang;

import com.google.j2kt.annotations.HiddenFromObjC;
import javaemul.internal.annotations.KtNative;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * See <a
 * href="https://docs.oracle.com/en/java/javase/27/docs/api/java.base/java/lang/StringIndexOutOfBoundsException.html">the
 * official Java API doc</a> for details.
 */
// Hidden from ObjC, as it shares the native Kotlin type and bridge with IndexOutOfBoundsException
// and has no dedicated ObjC class.
@HiddenFromObjC
@KtNative(
    name = "kotlin.IndexOutOfBoundsException",
    bridgeName = "java.lang.IndexOutOfBoundsException")
@NullMarked
public class StringIndexOutOfBoundsException extends RuntimeException {

  public StringIndexOutOfBoundsException() {}

  public StringIndexOutOfBoundsException(@Nullable String message) {}
}
