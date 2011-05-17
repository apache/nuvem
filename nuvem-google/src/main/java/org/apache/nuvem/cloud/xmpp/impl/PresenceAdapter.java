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

package org.apache.nuvem.cloud.xmpp.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.commons.lang.StringUtils;
import org.apache.nuvem.cloud.xmpp.JID;
import org.apache.nuvem.cloud.xmpp.presence.Presence;
import org.apache.nuvem.cloud.xmpp.presence.PresenceBuilder;
import org.apache.nuvem.cloud.xmpp.presence.Presence.Show;
import org.apache.nuvem.cloud.xmpp.presence.Presence.Type;

import com.google.appengine.api.xmpp.PresenceShow;
import com.google.appengine.api.xmpp.PresenceType;

/**
 * Helps to convert the Presence object from google to nuvem and vice-versa.
 */
public final class PresenceAdapter {

	/**
	 * A bidirectional converstion map with key as google presence show and
	 * value as nuvem's show value.
	 */
	private static final BidiMap showConverstionMap = new DualHashBidiMap();

	/**
	 * A bidirectional converstion map with key as google's presence type value
	 * and value as nuvem's presence type value.F
	 */
	private static final BidiMap typeConverstionMap = new DualHashBidiMap();

	static {
		showConverstionMap.put(PresenceShow.AWAY, Show.AWAY);
		showConverstionMap.put(PresenceShow.CHAT, Show.CHAT);
		showConverstionMap.put(PresenceShow.DND, Show.DND);
		showConverstionMap.put(PresenceShow.XA, Show.XA);
		showConverstionMap.put(PresenceShow.NONE, Show.NONE);

		typeConverstionMap.put(PresenceType.AVAILABLE,
				Type.AVAILABLE);
		typeConverstionMap.put(PresenceType.PROBE, Type.PROBE);
		typeConverstionMap.put(PresenceType.UNAVAILABLE,
				Type.UNAVAILABLE);

	}

	/**
	 * 
	 Converts the google {@link com.google.appengine.api.xmpp.Presence} into
	 * nuvem's {@link org.apache.nuvem.cloud.xmpp.presence.Presence} object
	 */
	public static Presence toNuvemPresence(
			com.google.appengine.api.xmpp.Presence presence) {
		Show show = (Show) showConverstionMap.get(presence
				.getPresenceShow());
		Type type = (Type) typeConverstionMap.get(presence
				.getPresenceType());
		String status = StringUtils.defaultString(presence.getStatus());
		String stanza = StringUtils.defaultString(presence.getStanza());

		PresenceBuilder builder = new PresenceBuilder().withShow(show)
				.withType(type).withStatus(status).withStanza(stanza);
		builder.from(new JID(presence.getFromJid().getId()));
		if (presence.getToJid() != null) {
			builder.to(new JID(presence.getToJid().getId()));
		}
		return builder.build();
	}

	public static Presence.Type toNuvemPresenceType(
			com.google.appengine.api.xmpp.PresenceType type) {
		return (Presence.Type) typeConverstionMap.get(type);
	}

	public static PresenceType toGooglePresenceType(Presence.Type type) {
		return (PresenceType) typeConverstionMap.getKey(type);
	}

	public static Presence.Show toNuvemPresenceShow(
			com.google.appengine.api.xmpp.PresenceShow show) {
		return (Presence.Show) showConverstionMap.get(show);
	}

	public static PresenceShow toGooglePresenceShow(Presence.Show show) {
		return (PresenceShow) showConverstionMap.getKey(show);
	}
}
