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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;

import org.apache.nuvem.cloud.xmpp.api.Status;
import org.apache.nuvem.cloud.xmpp.api.XMPPConnector;
import org.apache.nuvem.cloud.xmpp.api.XMPPEndPoint;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.XMPPError;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DefaultXMPPEndPointTestCase {

	private static final String RECIPIENT = "recipient@host.com";

	private static final String MESSAGE = "hi";

	private XMPPConnector<XMPPConnection> connector;
	private XMPPConnection connection;
	private ChatManager chatManager;
	private Chat chat;

	private XMPPEndPoint endPoint;

	@Before
	public void init() {
		connection = createMock(XMPPConnection.class);
		chatManager = createMock(ChatManager.class);
		connector = createMock(XMPPConnector.class);
		expect(connector.connect()).andReturn(connection);
		chat = createMock(Chat.class);
		endPoint = new DefaultXMPPEndPoint(connector);
	}

	@Test(expected = IllegalStateException.class)
	public void shouldHandleConnectivityProblem() {
		expect(connection.isConnected()).andReturn(false);
		replay(connection);
		replay(connector);
		endPoint.sendTextMessage(MESSAGE, RECIPIENT);
	}

	@Test
	public void shouldBeAbleToSendMessage() throws XMPPException,
			IllegalArgumentException, IllegalAccessException {
		expect(connection.isConnected()).andReturn(true);
		expect(connection.getChatManager()).andReturn(chatManager);
		expect(
				chatManager.createChat(isA(String.class),
						isA(SmackMessageListenerAdapter.class)))
				.andReturn(chat);
		chat.sendMessage(MESSAGE);
		expectLastCall();
		replay(connection);
		replay(chatManager);
		replay(chat);
		replay(connector);
		Status status = endPoint.sendTextMessage(MESSAGE, RECIPIENT);
		Assert.assertNotNull(status);
		Assert.assertTrue(!status.hasErrors());
	}

	@Test
	public void shouldBeAbleToIdentifyErrors() throws IllegalArgumentException,
			IllegalAccessException, XMPPException {
		expect(connection.isConnected()).andReturn(true);
		expect(connection.getChatManager()).andReturn(chatManager);
		expect(
				chatManager.createChat(isA(String.class),
						isA(SmackMessageListenerAdapter.class)))
				.andReturn(chat);
		chat.sendMessage(MESSAGE);
		expectLastCall().andThrow(
				new XMPPException("error", new XMPPError(302, "error")));
		replay(connection);
		replay(chatManager);
		replay(chat);
		replay(connector);
		Status status = endPoint.sendTextMessage(MESSAGE, RECIPIENT);
		Assert.assertNotNull(status);
		Assert.assertTrue(status.hasErrors());
	}

}
