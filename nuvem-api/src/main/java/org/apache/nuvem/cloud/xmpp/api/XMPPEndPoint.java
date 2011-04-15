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
 * platform like GAE, EC2 etc.
 * </p>
 * Some platforms like GAE doesn't require you to specify connection properties
 * for connecting to an XMPP Server but other platforms doesn't provide any
 * explicit APIs for XMPP and hence you should ensure that your XMPP server is
 * up and running. you should also inject the necessary connection properties to
 * the platform specific implementation classes of the interface
 * {@link XMPPEndPoint}.
 * 
 * 
 */
public interface XMPPEndPoint {

	/**
	 * Checks if the XMPPEndPoint is connected to the XMPPServer.
	 * 
	 * @return true if the endpoint is connected, false otherwise.
	 */
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
	 * @param message
	 *            - the message that needs to be delivered to a target JID/Node.
	 * @return A response object which holds the status of the delivery of
	 *         message.
	 * @throws XMPPConnectException
	 *             if an error occurs when trying to connect to the XMPP Server.
	 * @see org.apache.nuvem.cloud.xmpp.api.Message
	 */
	Status sendMessage(Message message);

	/**
	 * Sends a text message to a recipient, this is a simplified version for
	 * sending message compared to {@link #sendMessage(Message)} where you need
	 * to build the message using the {@link MessageBuilder}
	 * 
	 * @param content
	 *            - the text content to be sent to recipient.
	 * @param recipient
	 *            - the recipient
	 * @return status of the message delivery
	 */
	Status sendTextMessage(String content, String recipient);

	/**
	 * Registers the {@link MessageListener} to listen to messages received from
	 * a particular JID. On receipt of an XMPP message, the endpoint will call
	 * the {@link MessageListener#listen(Message)} method by passing the
	 * received message as argument.
	 * 
	 * 
	 * @param jid
	 *            - the JID for which a message listener is bound.
	 * @param listener
	 *            - will receive messages sent by a client/node identified by
	 *            the JID passed as the first argument.
	 * @see MessageListener#listen(Message)
	 */
	void registerListner(JID jid, MessageListener listener);

	/**
	 * Returns the message listener registered for the specific JID.
	 * 
	 * @param jid
	 *            the JID
	 * @return the registered Message Listener if one available, otherwise returns a default listener 
	 * @see MessageListener#LOGGING_LISTENER
	 */
	MessageListener getListenerFor(JID jid);

	/**
	 * Clears the listener for the specified JID, which means, the messages
	 * received from this specific JID will be ignored/lost!
	 * 
	 * @param jid
	 *            the JID for which the listener will be removed which means the
	 *            messages from the particular JID will be lost!
	 * @return true if listener removed, false otherwise.
	 */
	boolean clearListenersFor(JID jid);

	/**
	 * Clears all the listeners for all the JIDs
	 */
	void clearAllListeners();

	/**
	 * Checks if the JID passed as parameter is online or not
	 * 
	 * @param id
	 *            the JID in string format
	 * @return true if the JID is present, false otherwise.
	 * @see org.apache.nuvem.cloud.xmpp.api.JID
	 */
	boolean isPresent(String id);

	/**
	 * Invites the JID passed as argument for sending/reciving messages.
	 * 
	 * @param jid
	 *            the jid to invite for chat.
	 * @throws IllegalArgumentException
	 *             if JID is invalid.
	 */
	Status invite(String jid);

}
