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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Encapsulates the complete message that needs to be delivered through the XMPP
 * channel.
 */
public final class Message implements Serializable {

    /**
	 * serial id.
	 */
	private static final long serialVersionUID = -4166419962507943317L;

	/**
     * to be used instead of null.
     */
    public static final Message EMPTY_MESSAGE = new Message(PayLoad.EMPTY, JID.UNKNOWN, JID.UNKNOWN);

    // TODO: Need to see the potential use of this headers feield and remove it
    // if not required.
    /**
     * Message headers if any.
     */
    private Map<String, String> headers = new HashMap<String, String>();

    /**
     * Represents and ecloses the actual content to be delivered over xmpp.
     */
    private PayLoad payLoad;

    /**
     * Identifies the reciepient to whom the message should be delivered.
     */
    private JID recipient;

    /**
     * Identifies the sender of this message.
     */
    private JID sender;

    /**
     * Constructor.
     * 
     * @param payLoad - the payload of this message.
     * @param recipient - the reciepient for this message.
     * @param sender - the sender of this message.
     */
    public Message(PayLoad payLoad, JID recipient, JID sender) {
        this(payLoad, recipient);
        this.sender = sender;
    }

    /**
     * Constructor.
     * 
     * @param payLoad - the payload.
     * @param recipient - the recipient.
     */
    public Message(PayLoad payLoad, JID recipient) {
        this.payLoad = payLoad;
        this.recipient = recipient;
    }

    /**
     * Adds headers to the message.
     * 
     * @param headers the headers.
     */
    public void addHeaders(Map<String, String> headers) {
        if (headers != null && headers.size() > 0)
            this.headers.putAll(headers);
    }

    /**
     * Returns the payload of this message.
     * 
     * @return the payload
     */
    public PayLoad payLoad() {
        return this.payLoad;
    }

    /**
     * Returns the recipient of this message.
     * 
     * @return the recipient.
     */
    public JID recipient() {
        return recipient;
    }

    /**
     * Returns the sender of this message.
     * 
     * @return the sender.
     */
    public JID sender() {
        return sender;
    }

    /**
     * Returns all the headers associated with this message.
     * 
     * @return headers
     */
    public Map<String, String> headers() {
        return Collections.unmodifiableMap(this.headers);
    }

    /**
     * equals.
     */
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Message))
            return false;

        final Message message = (Message)o;

        return new EqualsBuilder().append(this.payLoad, message.payLoad).append(this.recipient, message.recipient)
            .append(this.sender, message.sender).isEquals();
    }

    /**
     * Considers only the hashcode of the userid as userid is the unique id of
     * the user.
     */
    public int hashCode() {
        return new HashCodeBuilder().append(this.payLoad).append(this.recipient).append(this.sender).toHashCode();
    }

}
