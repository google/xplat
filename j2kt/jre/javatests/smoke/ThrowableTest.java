/*
 * Copyright 2022 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package smoke;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.jspecify.annotations.Nullable;
import org.junit.Test;

public class ThrowableTest {

  @Test
  public void testBridgedException_initCause() throws Exception {
    NumberFormatException nfe = new NumberFormatException(); // Native but bridged exception
    assertNull(nfe.getCause());

    RuntimeException theCause = new RuntimeException();
    assertSame(nfe, nfe.initCause(theCause));
    assertSame(theCause, nfe.getCause());

    assertThrows(
        "Calling initCause twice should not succeed",
        IllegalStateException.class,
        () -> nfe.initCause(new RuntimeException()));
  }

  private static class TranspiledException extends Exception {

    TranspiledException() {
      super();
    }

    TranspiledException(@Nullable Throwable cause) {
      super(cause);
    }
  }

  @Test
  public void testTranspiledException_initCause() throws Exception {
    Exception exception = new TranspiledException();
    assertNull(exception.getCause());

    RuntimeException theCause = new RuntimeException();
    assertSame(exception, exception.initCause(theCause));
    assertSame(theCause, exception.getCause());

    assertThrows(
        "Calling initCause twice should not succeed",
        IllegalStateException.class,
        () -> exception.initCause(new RuntimeException()));

    Exception exceptionWithCause = new TranspiledException(/* cause= */ theCause);
    assertSame(theCause, exceptionWithCause.getCause());

    assertThrows(
        "Calling initCause should not succeed if cause was provided to constructor",
        IllegalStateException.class,
        () -> exceptionWithCause.initCause(new InterruptedException()));
  }

  @Test
  public void testAssertionError_constructors() throws Exception {
    AssertionError assertionError = new AssertionError(null);
    // assertEquals("null", assertionError.getMessage());
    assertNull(assertionError.getCause());
    assertionError.initCause(new RuntimeException()); // Would fail if ctor param was used as cause
    assertTrue(assertionError.getCause() instanceof RuntimeException);

    assertionError = new AssertionError("message");
    assertEquals("message", assertionError.getMessage());

    assertionError = new AssertionError(42);
    assertEquals("42", assertionError.getMessage());

    assertionError =
        new AssertionError(
            new Object() {
              @Override
              public String toString() {
                return "toString result";
              }
            });
    assertEquals("toString result", assertionError.getMessage());

    Exception cause = new RuntimeException();
    assertionError = new AssertionError(cause);
    assertSame(cause, assertionError.getCause());
  }

  @Test
  public void testPrintStackTrace() throws Exception {
    StringWriter stringWriter = new StringWriter();
    Throwable throwable = new RuntimeException("exception message 123");
    throwable.printStackTrace(new PrintWriter(stringWriter));
    String stackTrace = stringWriter.toString();

    assertTrue(stackTrace.contains("RuntimeException"));
    assertTrue(stackTrace.contains("exception message 123"));
    assertTrue(stackTrace.contains("testPrintStackTrace"));
  }

  @Test
  public void testStackTraceAccess() throws Exception {
    Throwable throwable = new RuntimeException("exception message 123");
    StackTraceElement[] stackTrace = throwable.getStackTrace();
    throwable.setStackTrace(stackTrace);
  }
}
