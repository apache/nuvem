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

package org.apache.nuvem.xmpp.client;

import java.util.logging.Logger;

import org.apache.commons.lang.Validate;
import org.apache.nuvem.cloud.xmpp.api.JID;
import org.apache.nuvem.cloud.xmpp.api.Message;
import org.apache.nuvem.cloud.xmpp.api.Status;
import org.apache.nuvem.cloud.xmpp.api.XMPPConnector;
import org.apache.nuvem.cloud.xmpp.api.XMPPEndPoint;
import org.apache.nuvem.cloud.xmpp.common.AbstractXMPPEndPoint;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.oasisopen.sca.annotation.Init;
import org.oasisopen.sca.annotation.Reference;
import org.oasisopen.sca.annotation.Scope;
import org.oasisopen.sca.annotation.Service;

/**
 * A Default Implemention of XMPP Client for cloud platforms without explicit
 * XMPP support.
 * 
 */
@Service(XMPPEndPoint.class)
@Scope("COMPOSITE")
public class DefaultXMPPEndPoint extends AbstractXMPPEndPoint implements
		XMPPEndPoint {

	/**
	 * Logger.
	 */
	private static final Logger log = Logger.getLogger(XMPPEndPoint.class
			.getName());

	@Reference
	protected XMPPConnector<XMPPConnection> connector;

	/**
	 * The packet filter to decide what packets to recieve from the XMPP
	 * Server..
	 */
	private NuvemPacketFilter filter;

	/**
	 * The Packet listener.
	 */
	private NuvemPacketListener listener;

	/**
	 * Default constructor.
	 */
	public DefaultXMPPEndPoint() {

	}

	public DefaultXMPPEndPoint(XMPPConnector<XMPPConnection> connector) {
		Validate.notNull(connector);
		this.connector = connector;
	}

	@Init
	public void init() {
		log.info("Initializing DefaultXMPPEndPoint using connector "
				+ connector.getDescription());
		filter = new NuvemPacketFilter(this);
		listener = new NuvemPacketListener(this);
		connector.getConnection().addPacketListener(this.listener, filter);
	}

	/**
	 * {@inheritDoc}
	 */
	public Status sendMessage(Message message) {
		XMPPConnection connection = connector.getConnection();
		if (message == null || message.recipient() == null) {
			throw new IllegalArgumentException("invalid message");
		}
		Status deliveryStatus = new Status();

		org.apache.nuvem.cloud.xmpp.api.MessageListener nuvemListener = getListenerFor(message
				.recipient());

		Chat chat = connection.getChatManager().createChat(
				message.recipient().asString(),
				new SmackMessageListenerAdapter(nuvemListener));
		try {
			chat.sendMessage(message.payLoad().content());
		} catch (XMPPException e) {
			log.severe("error while sending xmpp message to "
					+ message.recipient().asString());
			deliveryStatus = SmackStatusAdapter.toStatus(e.getXMPPError());
		}
		return deliveryStatus;
	}

	/**
	 * {@inheritDoc}
	 */
	public Status invite(JID jid) {
		throw new UnsupportedOperationException("not supported yet");
	}

	/**
	 * {@inheritDoc}
	 */
	public Status invite(String jid) {
		throw new UnsupportedOperationException("not supported yet");
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isPresent(JID jid) {
		XMPPConnection connection = connector.getConnection();
		if (jid == null)
			throw new IllegalArgumentException(
					"Illegal JID passed for finding presence");

		Roster roster = connection.getRoster();
		Presence presence = roster.getPresence(jid.asString());
		if (presence != null)
			return presence.getType() == Type.available;
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isPresent(String id) {
		XMPPConnection connection = connector.getConnection();

		if (id == null)
			throw new IllegalArgumentException(
					"Illegal JID passed for finding presence");

		Roster roster = connection.getRoster();
		Presence presence = roster.getPresence(id);
		if (presence != null)
			return presence.getType() == Type.available;
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isConnected() {
		return connector.getConnection().isConnected();
	}
}
