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

package org.apache.nuvem.cloud.xmpp.message;

import java.util.Locale;


/**
 * The Container to hold the actual data and all related attributes about the data which are required for XMPP Protocol.
 */
public final class PayLoad {

    /**
     * To be used instead of null.
     */
    public static final PayLoad EMPTY = new PayLoad("Empty Content");

    /**
     * TODO:should be configurable.
     */
    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    /**
     * The actual content.
     */
    private String content;

    /**
     * Identifies the type of the content.
     */
    private ContentType type;

    private Locale locale;

    public PayLoad(String content) {
        this(content, DEFAULT_LOCALE);
    }

    public PayLoad(String content, Locale locale) {
        this.content = content;
        if (locale != null)
            this.locale = locale;
    }

    public Locale locale() {
        return locale;
    }

    /**
     * Sets the content type for the payload.
     * 
     * @param type the content type.
     */
    public void setContentType(ContentType type) {
        this.type = type;
    }

    public String content() {
        return content;
    }

    /**
     * Returns the content type present in the payload.
     * 
     * @return the content type.
     */
    public ContentType contentType() {
        return type;
    }

    /**
     * Checks only for the user ID as it is supposed to be a unique identifier
     * for the user.
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PayLoad)) {
            return false;
        }

        final PayLoad payLoad = (PayLoad)o;

        return content.equals(payLoad.content);
    }

    /**
     * Considers only the hashcode of the userid as userid is the unique id of
     * the user.
     */
    public int hashCode() {
        return content.hashCode();
    }
}
