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

import junit.framework.Assert;

import org.apache.nuvem.cloud.xmpp.JID;
import org.apache.nuvem.cloud.xmpp.XMPPEndPoint;
import org.apache.nuvem.cloud.xmpp.XMPPServer;
import org.apache.nuvem.cloud.xmpp.presence.Presence;
import org.apache.nuvem.cloud.xmpp.presence.PresenceBuilder;
import org.apache.nuvem.cloud.xmpp.presence.PresenceListener;
import org.apache.nuvem.cloud.xmpp.presence.PresenceManager;
import org.apache.nuvem.cloud.xmpp.server.DefaultXMPPServer;
import org.apache.tuscany.sca.node.Node;
import org.apache.tuscany.sca.node.NodeFactory;
import org.junit.BeforeClass;
import org.junit.Test;

public class XMPPPresenceIntegrationTest {
	private static XMPPServer server;
	private static Node xmppNode;
	private static PresenceManager presenceManagerA;
	private static PresenceManager presenceManagerB;

	@BeforeClass
	public static void setUp() throws Exception {
		server = new DefaultXMPPServer();
		server.start();
		xmppNode = NodeFactory.getInstance().createNode(
				"testnuvemxmpp.composite");
		xmppNode.start();
		presenceManagerA = xmppNode.getService(XMPPEndPoint.class,
				"TestXMPPComponentSender/XMPPEndPoint").presenceManager();
		presenceManagerB = xmppNode.getService(XMPPEndPoint.class,
				"TestXMPPComponentReciever/XMPPEndPoint").presenceManager();
	}

	@Test
	public void ShouldSendAndRecievePresence() throws InterruptedException {
		PresenceListenerImpl listener = new PresenceListenerImpl(new JID(
				"nuvem@localhost"), Presence.Type.SUBSCRIBE);
		presenceManagerB.registerListener(listener);
		Presence presence = new PresenceBuilder().from(
				new JID("nuvem@localhost")).to(new JID("reciever@localhost"))
				.withType(Presence.Type.SUBSCRIBE).build();
		presenceManagerA.sendPresence(presence);
		Thread.sleep(2000);
		listener.assertValidPresenceRecieved();

	}

	private static class PresenceListenerImpl implements PresenceListener {

		private JID expectedFromJID;
		private Presence.Type expectedType;

		private JID actualFromJID;
		private Presence.Type actualType;

		public PresenceListenerImpl(JID expectedfrom, Presence.Type expectedType) {
			this.expectedFromJID = expectedfrom;
			this.expectedType = expectedType;
		}

		@Override
		public void listen(Presence presence) {
			actualFromJID = presence.from();
			actualType = presence.type();
		}

		public void assertValidPresenceRecieved() {
			Assert.assertEquals(expectedFromJID, actualFromJID);
			Assert.assertEquals(expectedType, actualType);
		}

	}
}
