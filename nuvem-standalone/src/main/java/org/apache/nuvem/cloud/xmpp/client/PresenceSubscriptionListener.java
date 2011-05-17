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

package org.apache.nuvem.cloud.xmpp.client;

import org.apache.nuvem.cloud.xmpp.presence.PresenceListener;
import org.apache.nuvem.cloud.xmpp.presence.PresenceManager;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

/**
 * Listens to all only Presence stanza of type subscribe.
 * 
 * @see Presence.Type;
 * 
 */
public class PresenceSubscriptionListener implements PacketListener {

	/**
	 * The presence manager.
	 */
	private PresenceManager presenceManager;

	/**
	 * Constructor which takes a reference to the {@link PresenceManager}
	 * through which this listener can notify all listeners registered to
	 * recieve Presence messages.
	 * 
	 * @param endPoint
	 */
	public PresenceSubscriptionListener(PresenceManager presenceManager) {
		this.presenceManager = presenceManager;
	}

	@Override
	public void processPacket(Packet packet) {
		if (packet instanceof Presence) {
			for (PresenceListener listener : presenceManager.listeners()) {
				listener.listen(PresenceAdapter
						.toNuvemPresence((Presence) packet));
			}
		}
	}
}