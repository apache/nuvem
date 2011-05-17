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

import org.apache.commons.lang.Validate;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;

/**
 * Accepts only {@link Presence} stanza of {@link Type} which is injected as a
 * constructor argument
 * 
 */
public class PresenceFilter implements PacketFilter {

	private Type type;

	public PresenceFilter(Type type) {
		Validate.notNull(type);
		this.type = type;
	}

	/**
	 * Accepts only Presence stanza of type "subscribe".
	 */
	public boolean accept(Packet packet) {
		if (packet != null) {
			return (packet instanceof Presence)
					&& ((Presence) packet).getType() == type;
		}
		return false;
	}
}