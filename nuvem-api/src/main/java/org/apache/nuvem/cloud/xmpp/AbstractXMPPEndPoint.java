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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
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
	protected List<MessageListener> genericListeners = Collections.synchronizedList(new ArrayList<MessageListener>());

	public boolean isConnected() {
		throw new UnsupportedOperationException("still not implemented");
	}

	public String uniqueID() {
		throw new UnsupportedOperationException("still not implemented");
	}

	public void registerListner(JID jid, MessageListener listener) {
		if (listener == null || jid == null)
			throw new IllegalArgumentException("invalid jid/listener");
		
		//avoid resource identifier for listeners
		listeners.put(new JID(StringUtils.substringBefore(jid.asString(), "/")), listener);

	}

	/**
	 * {@inheritDoc}
	 */
	public void registerListener(MessageListener listener) {
		if (listener == null)
			throw new IllegalArgumentException("invalid listener");
		genericListeners.add(listener);
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
		if (listeners != null
				&& (target = listeners.get(new JID(StringUtils.substringBefore(
						jid.asString(), "/")))) != null)
			return target;
		// default listener.
		return MessageListener.LOGGING_LISTENER;

	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<MessageListener> getListeners() {
		if (this.genericListeners == null || this.genericListeners.size() == 0) {
			return Collections.EMPTY_LIST;
		}
		//returning a copy to maintain thread safety.
		List<MessageListener> listeners = new ArrayList<MessageListener>();
		listeners.addAll(genericListeners);
		return listeners;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void broadCastToListeners(Message message) {
		List<MessageListener> listeners = new ArrayList<MessageListener>();
		listeners.add(getListenerFor(message.sender()));
		listeners.addAll(getListeners());
		for (MessageListener listener : listeners) {
			listener.listen(message);
		}
	}
}
