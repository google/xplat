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

/**
 * A {@code MalformedInputException} is thrown when a malformed input is encountered, for example if
 * a byte sequence is illegal for the given charset.
 */
@NullMarked
public class MalformedInputException extends CharacterCodingException {

    /*
     * This constant is used during deserialization to check the version
     * which created the serialized object.
     */
    private static final long serialVersionUID = -3438823399834806194L;

    // the length of the malformed input
    private int inputLength;

    /**
     * Constructs a new {@code MalformedInputException}.
     *
     * @param length
     *            the length of the malformed input.
     */
    public MalformedInputException(int length) {
        this.inputLength = length;
    }

    /**
     * Gets the length of the malformed input.
     *
     * @return the length of the malformed input.
     */
    public int getInputLength() {
        return this.inputLength;
    }

    @Override
    public String getMessage() {
        return "Length: " + inputLength;
    }
}
