/*
 * Copyright 2020 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
module java.base {
  exports java.io;
  exports java.lang.annotation;
  exports java.lang.invoke;
  exports java.lang.ref;
  exports java.lang.reflect;
  exports java.lang;
  exports java.math;
  exports java.net;
  exports java.nio;
  exports java.nio.charset;
  exports java.nio.file;
  exports java.security;
  exports java.sql;
  exports java.text;
  exports java.util;
  exports java.util.concurrent;
  exports java.util.concurrent.atomic;
  exports java.util.concurrent.locks;
  exports java.util.function;
  exports java.util.logging;
  exports java.util.regex;
  exports java.util.stream;
  exports java.util.zip;
  exports javax.annotation.processing;
  exports javax.crypto;
  exports javax.crypto.spec;
  // TODO(goktug): Provide a public API for array stamping and stop exporting this.
  exports javaemul.internal;
  exports javaemul.lang;
}
