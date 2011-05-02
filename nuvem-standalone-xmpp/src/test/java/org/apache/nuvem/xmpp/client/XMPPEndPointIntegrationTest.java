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

import junit.framework.Assert;

import org.apache.nuvem.cloud.xmpp.api.JID;
import org.apache.nuvem.cloud.xmpp.api.XMPPEndPoint;
import org.apache.nuvem.cloud.xmpp.api.XMPPServer;
import org.apache.nuvem.cloud.xmpp.api.message.Message;
import org.apache.nuvem.cloud.xmpp.api.message.MessageListener;
import org.apache.nuvem.xmpp.server.DefaultXMPPServer;
import org.apache.tuscany.sca.node.Node;
import org.apache.tuscany.sca.node.NodeFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class XMPPEndPointIntegrationTest {

	private static XMPPServer server;
	private static Node xmppNode;
	private static XMPPEndPoint xmppEndPointSender;
	private static XMPPEndPoint xmppEndPointReciever;

	@BeforeClass
	public static void setUp() throws Exception {
		server = new DefaultXMPPServer();
		server.start();
		xmppNode = NodeFactory.getInstance().createNode(
				"testnuvemxmpp.composite");
		xmppNode.start();
		xmppEndPointSender = xmppNode.getService(XMPPEndPoint.class,
				"TestXMPPComponentSender/XMPPEndPoint");
		xmppEndPointReciever = xmppNode.getService(XMPPEndPoint.class,
				"TestXMPPComponentReciever/XMPPEndPoint");

	}

	@AfterClass
	public static void tearDown() throws Exception {
		if (xmppNode != null) {
			xmppNode.stop();
		}
		if (server != null) {
			server.stop();
		}
	}


	@Test
	public void SendAndRecieveMessage() throws InterruptedException {
		final String message = "hello xmpp";
		MessageListenerValidator validator = new MessageListenerValidator(
				message);
		xmppEndPointReciever.registerListner(new JID("nuvem@localhost"),
				validator);
		xmppEndPointSender.sendTextMessage(message, "reciever@localhost");
		Thread.sleep(3000);
		validator.checkIfValidMessageRecieved();
	}

	public class MessageListenerValidator implements MessageListener {

		private Message recievedMessage = null;

		private String expectedMessage;

		public MessageListenerValidator(String expectedMessage) {
			this.expectedMessage = expectedMessage;
		}

		@Override
		public void listen(Message message) {
			this.recievedMessage = message;
		}

		public void checkIfValidMessageRecieved() {
			Assert.assertNotNull(recievedMessage);
			Assert.assertTrue(expectedMessage.equals(recievedMessage.payLoad()
					.content()));
		}
	}

}
