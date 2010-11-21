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

/**
 * Represents An EndPoint for client to send and receive XMPP messages.
 * <p>
 * The implementations of <code>XMPPEndPoint</code> are specific to each cloud
 * platform.
 * </p>
 *
 */
public interface XMPPEndPoint {

    boolean isConnected();

    /**
     * Returns a unique id of this endpoint.
     *
     * @return the unique id.
     */
    String uniqueID();

    /**
     * Sends the message to a target JID enclosed inside the
     * <code>Message</code>.
     *
     * @param message - the message that needs to be delivered to a target
     *            JID/Node.
     * @return A response object which holds the status of the delivery of
     *         message.
     * @see org.apache.nuvem.cloud.xmpp.api.Message
     */
    Status sendMessage(Message message);

    /**
     * Sends a text message to a recipient, this is a simplified version for
     * sending message compared to {@link #sendMessage(Message)} where you need
     * to build the message using the {@link MessageBuilder}
     *
     * @param content - the text content to be sent to recipient.
     * @param recipient - the recipient
     * @return status of the message delivery
     */
    Status sendMessage(String content, String recipient);

    /**
     * Registers a <code>MessageListener</code> to listen to messages sent by a
     * particular JID which is also passed as argument to this API.
     * <p>
     * </p>
     *
     * @param jid - the JID for which a message listener is bound.
     * @param listener - will recieve messages sent by a client/node identified
     *            by the JID passed as the first argument.
     */
    void registerListner(JID jid, MessageListener listener);

    /**
     * Clears the listener for the specified JID.
     *
     * @param jid the JID for which the listener will be removed which means the
     *            messages from the particular JID will be lost!
     * @return true if listener removed, false otherwise.
     */
    boolean clearListenersFor(JID jid);

    /**
     * Clears all the listeners.
     */
    void clearAllListeners();

    /**
     * Finds the presence of a particular recipient.
     *
     * @param jid
     * @return
     */
    boolean isPresent(JID jid);

    /**
     * Simplified version of {@link #isPresent(JID)}
     *
     * @param id the JID in string format
     * @return true if the JID is present, false otherwise.
     */
    boolean isPresent(String id);

    /**
     * Sends an XMPP invitation to the JID passed as argument.
     *
     * @param jid the jid.
     * @throws IllegalArgumentException if JID is invalid.
     */
    Status invite(JID jid);

    /**
     * Simplified version of invitation API.
     *
     * @param jid the jid to invite for chat.
     * @throws IllegalArgumentException if JID is invalid.
     */
    Status invite(String jid);

}
