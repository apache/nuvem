/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */

package org.apache.nuvem.cloud.data;

/**
 * The Class NotFoundException.
 */
public class NotFoundException extends Exception {
    private static final long serialVersionUID = 6792367409396084646L;

    /**
     * Instantiates a new Not Found Exception.
     */
    public NotFoundException() {
    }

    /**
     * Instantiates a new Not Found Exception.
     * 
     * @param message the message
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Not Found Exception.
     * 
     * @param cause the cause
     */
    public NotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Not Found Exception.
     * 
     * @param message the message
     * @param cause the cause
     */
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
