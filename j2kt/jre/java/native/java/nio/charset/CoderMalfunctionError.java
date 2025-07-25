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

package java.nio.charset;

import org.jspecify.annotations.NullMarked;

/** A {@code CoderMalfunctionError} is thrown when the encoder/decoder is malfunctioning. */
@NullMarked
public class CoderMalfunctionError extends Error {

    /*
     * This constant is used during deserialization to check the version
     * which created the serialized object.
     */
    private static final long serialVersionUID = -1151412348057794301L;

    /**
     * Constructs a new {@code CoderMalfunctionError}.
     *
     * @param ex
     *            the original exception thrown by the encoder/decoder.
     */
    public CoderMalfunctionError(Exception ex) {
        super(ex);
    }
}
