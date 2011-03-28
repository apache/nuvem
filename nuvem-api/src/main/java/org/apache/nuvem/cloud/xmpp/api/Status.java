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

package org.apache.nuvem.cloud.xmpp.api;

import java.util.ArrayList;
import java.util.List;

/**
 * The status to indicate whether the message was sent to
 * the recipient successfully or was there any error during the process.
 */
public final class Status {

	/**
	 * List of errors that occurred during the XMPP interaction with the server..
	 */
    private List<Error> errors = new ArrayList<Error>();

    public Status(List<Error> errors) {
        this.errors = new ArrayList<Error>();
        this.errors.addAll(errors);
    }

    public Status() {

    }

    public Status(Error error) {
        this.errors = new ArrayList<Error>();
        this.errors.add(error);
    }

    public void addError(Error error) {
        this.errors.add(error);
    }

    /**
     * Returns true if the status implies that there was an internal error during message transmission.
     * @return
     */
    public boolean hasErrors() {
        return errors != null && errors.size() > 0;
    }

    public boolean wasSentSuccessfuly() {
        return errors == null || errors.size() == 0;
    }

    public List<Error> errors() {
        return errors;
    }
}
