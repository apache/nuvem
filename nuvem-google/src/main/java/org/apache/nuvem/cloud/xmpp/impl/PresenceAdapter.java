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

	private static final Map<PresenceShow, Presence.Show> showConverstionMap = new HashMap<PresenceShow, Show>();
	private static final Map<PresenceType, Presence.Type> typeConversionMap = new HashMap<PresenceType, Type>();

	static {
		showConverstionMap.put(PresenceShow.AWAY, Show.AWAY);
		showConverstionMap.put(PresenceShow.CHAT, Show.CHAT);
		showConverstionMap.put(PresenceShow.DND, Show.DND);
		showConverstionMap.put(PresenceShow.XA, Show.XA);
		showConverstionMap.put(PresenceShow.NONE, Show.NONE);

		typeConversionMap.put(PresenceType.AVAILABLE, Type.AVAILABLE);
		typeConversionMap.put(PresenceType.PROBE, Type.PROBE);
		typeConversionMap.put(PresenceType.UNAVAILABLE, Type.UNAVAILABLE);
	}

	/**
	 * 
	 Converts the google {@link com.google.appengine.api.xmpp.Presence} into
	 * nuvem's {@link org.apache.nuvem.cloud.xmpp.presence.Presence} object
	 */
	public static Presence toNuvemPresence(
			com.google.appengine.api.xmpp.Presence presence) {
		Show show = showConverstionMap.get(presence.getPresenceShow());
		Type type = typeConversionMap.get(presence.getPresenceType());
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
}
