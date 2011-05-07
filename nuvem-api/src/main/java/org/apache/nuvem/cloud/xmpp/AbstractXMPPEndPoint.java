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

package org.apache.nuvem.cloud.xmpp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.nuvem.cloud.xmpp.message.Message;
import org.apache.nuvem.cloud.xmpp.message.MessageBuilder;
import org.apache.nuvem.cloud.xmpp.message.MessageListener;

/**
 * Encloses the common code for various types of end points irrespective of the
 * platform.
 */
public abstract class AbstractXMPPEndPoint {
	/**
	 * Listeners for recivnig the XMPP messages from specific JIDs
	 */
	protected Map<JID, MessageListener> listeners = new ConcurrentHashMap<JID, MessageListener>();

	public boolean isConnected() {
		throw new UnsupportedOperationException("still not implemented");
	}

	public String uniqueID() {
		throw new UnsupportedOperationException("still not implemented");
	}

	public void registerListner(JID jid, MessageListener listener) {
		if (listener == null || jid == null)
			throw new IllegalArgumentException("invalid jid/listener");
		listeners.put(jid, listener);

	}

	public Status sendTextMessage(String content, String recipient) {
		Message message = new MessageBuilder().containing(content).toRecipient(
				recipient).build();
		return sendMessage(message);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean clearListenersFor(JID jid) {
		if (listeners != null) {
			return listeners.remove(jid) != null;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void clearAllListeners() {
		if (listeners != null)
			listeners.clear();
	}

	public abstract Status sendMessage(
			org.apache.nuvem.cloud.xmpp.message.Message message);

	/**
	 * 
	 * {@inheritDoc}
	 */
	public MessageListener getListenerFor(JID jid) {

		MessageListener target = null;
		if (listeners != null && (target = listeners.get(jid)) != null)
			return target;
		// default listener.
		return MessageListener.LOGGING_LISTENER;

	}
}
