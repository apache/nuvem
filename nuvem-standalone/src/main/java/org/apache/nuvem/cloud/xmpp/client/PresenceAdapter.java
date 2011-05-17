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

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.commons.lang.StringUtils;
import org.apache.nuvem.cloud.xmpp.JID;
import org.apache.nuvem.cloud.xmpp.presence.Presence;
import org.apache.nuvem.cloud.xmpp.presence.PresenceBuilder;
import org.apache.nuvem.cloud.xmpp.presence.Presence.Show;
import org.apache.nuvem.cloud.xmpp.presence.Presence.Type;
import org.jivesoftware.smack.packet.Presence.Mode;

/**
 * Hepls convert smack specific presence object into nuvem specific presence
 * object.
 * 
 */
public final class PresenceAdapter {

	/**
	 * A bidirectional map with key being smack's mode and value being nuvem's
	 * show values respectively.
	 */
	private static final BidiMap showConverstionMap = new DualHashBidiMap();

	/**
	 * A bidirectional map with key being smack's type and value being nuvems
	 * presence types respectively.
	 */
	private static final BidiMap typeConversionMap = new DualHashBidiMap();

	static {
		showConverstionMap.put(Mode.available, Show.AVAILABLE);
		showConverstionMap.put(Mode.away, Show.AWAY);
		showConverstionMap.put(Mode.chat, Show.CHAT);
		showConverstionMap.put(Mode.dnd, Show.DND);
		showConverstionMap.put(Mode.xa, Show.XA);

		typeConversionMap.put(
				org.jivesoftware.smack.packet.Presence.Type.available,
				Type.AVAILABLE);
		typeConversionMap.put(
				org.jivesoftware.smack.packet.Presence.Type.unavailable,
				Type.UNAVAILABLE);
		typeConversionMap.put(
				org.jivesoftware.smack.packet.Presence.Type.subscribe,
				Type.SUBSCRIBE);
		typeConversionMap.put(
				org.jivesoftware.smack.packet.Presence.Type.subscribed,
				Type.SUBSCRIBED);
		typeConversionMap.put(
				org.jivesoftware.smack.packet.Presence.Type.unsubscribe,
				Type.UNSUBSCRIBE);
		typeConversionMap.put(
				org.jivesoftware.smack.packet.Presence.Type.unsubscribed,
				Type.UNSUBSCRIBED);
		typeConversionMap.put(
				org.jivesoftware.smack.packet.Presence.Type.error, Type.ERROR);
	}

	/**
	 * Converts smack's {@link org.jivesoftware.smack.packet.Presence} to
	 * Nuvem's {@link org.apache.nuvem.cloud.xmpp.presence.Presence} object.
	 */
	public static Presence toNuvemPresence(
			org.jivesoftware.smack.packet.Presence presence) {
		String from = presence.getFrom();
		String to = presence.getTo();
		String status = presence.getStatus();
		Show show = (Show) showConverstionMap.get(presence.getMode());
		Type type = (Type) typeConversionMap.get(presence.getType());
		PresenceBuilder builder = new PresenceBuilder().withShow(show)
				.withType(type).withStatus(status);
		builder.from(new JID(from));
		if (StringUtils.isNotEmpty(to)) {
			builder.to(new JID(to));
		}
		return builder.build();
	}

	/**
	 * Converts Nuvem's {@link org.apache.nuvem.cloud.xmpp.presence.Presence} to
	 * smack's {@link org.jivesoftware.smack.packet.Presence} object.
	 * 
	 * @param presence
	 * @return
	 */
	public static org.jivesoftware.smack.packet.Presence toSmackPresence(
			Presence presence) {
		Mode mode = (Mode) showConverstionMap.getKey(presence.show());
		org.jivesoftware.smack.packet.Presence.Type type = (org.jivesoftware.smack.packet.Presence.Type) typeConversionMap
				.getKey(presence.type());
		String status = StringUtils.defaultString(presence.status());
		org.jivesoftware.smack.packet.Presence smackPresence = new org.jivesoftware.smack.packet.Presence(
				type, status, 0, mode);
		if (presence.from() != null) {
			smackPresence.setFrom(presence.from().asString());
		}
		if (presence.to() != null) {
			smackPresence.setTo(presence.to().asString());
		}
		return smackPresence;
	}
}
