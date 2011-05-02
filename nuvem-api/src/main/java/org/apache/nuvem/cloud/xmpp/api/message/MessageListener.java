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

package org.apache.nuvem.cloud.xmpp.api.message;

import org.apache.nuvem.cloud.xmpp.api.JID;
import org.apache.nuvem.cloud.xmpp.api.XMPPEndPoint;
import org.apache.nuvem.cloud.xmpp.common.LoggingMessageListener;

/**
 * Listens to XMPP messages of a specific type.
 * <p>
 * In order to receive messages from a particular JID, an implementation of
 * <code>MessageListener</code> should be registered with the
 * {@link XMPPEndPoint} using the API
 * {@link XMPPEndPoint#registerListner(JID, MessageListener)}.
 * </p>
 */
public interface MessageListener {

	/**
	 * To be used as default one.
	 */
	public static final MessageListener LOGGING_LISTENER = new LoggingMessageListener();

	/**
	 * This method will be called when a message is received by the XMPP
	 * endpoint for a specific {@link JID} this listener is registred for.
	 * 
	 * @param message
	 * @see org.apache.nuvem.cloud.xmpp.XMPPEndPoint
	 */
	void listen(Message message);

}
