/*
 * Copyright 2010 Google Inc.
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

package java.util.logging;

import java.io.Serializable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * An emulation of the java.util.logging.LogRecord class. See <a
 * href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/logging/LogRecord.html">The Java API doc
 * for details</a>
 */
@NullMarked
public class LogRecord implements Serializable {
  private Level level = Level.OFF;
  private String loggerName = "";
  private @Nullable String msg;
  private @Nullable Throwable thrown = null;
  private long millis;
  private int threadId;
  private @Nullable String sourceClassName;
  private @Nullable String sourceMethodName;
  private @Nullable Object @Nullable [] parameters;

  public LogRecord(Level level, @Nullable String msg) {
    this.level = level;
    this.msg = msg;
    millis = System.currentTimeMillis();
  }

  protected LogRecord() {
    // for serialization
  }

  public Level getLevel() {
    return level;
  }

  public @Nullable String getLoggerName() {
    return loggerName;
  }

  public @Nullable String getMessage() {
    return msg;
  }

  public @Nullable Object @Nullable [] getParameters() {
    return parameters;
  }

  public long getMillis() {
    return millis;
  }

  public int getThreadID() {
    return threadId;
  }

  public @Nullable String getSourceClassName() {
    return sourceClassName;
  }

  public @Nullable String getSourceMethodName() {
    return sourceMethodName;
  }

  public @Nullable Throwable getThrown() {
    return thrown;
  }

  public void setLevel(Level newLevel) {
    level = newLevel;
  }

  public void setLoggerName(@Nullable String newName) {
    loggerName = newName;
  }

  public void setMessage(@Nullable String newMessage) {
    msg = newMessage;
  }

  public void setMillis(long newMillis) {
    millis = newMillis;
  }

  public void setThreadID(int newThreadId) {
    threadId = newThreadId;
  }

  public void setSourceClassName(String newSourceClassName) {
    sourceClassName = newSourceClassName;
  }

  public void setSourceMethodName(String newSourceMethodName) {
    sourceMethodName = newSourceMethodName;
  }

  public void setParameters(@Nullable Object @Nullable [] parameters) {
    this.parameters = parameters;
  }

  public void setThrown(@Nullable Throwable newThrown) {
    thrown = newThrown;
  }

  /* Not Implemented */
  // public ResourceBundle getResourceBundle() {}
  // public String getResourceBundleName() {}
  // public long getSequenceNumber() {}
  // public void setResourceBundle(ResourceBundle bundle) {}
  // public void setResourceBundleName(String name) {}
  // public void setSequenceNumber(long seq) {}
}
