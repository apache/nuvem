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

import junit.framework.Assert;

import org.apache.nuvem.cloud.xmpp.ErrorCode;
import org.apache.nuvem.cloud.xmpp.Status;
import org.apache.nuvem.cloud.xmpp.XMPPConnector;
import org.apache.nuvem.cloud.xmpp.message.Message;
import org.apache.nuvem.cloud.xmpp.message.MessageBuilder;
import org.apache.nuvem.cloud.xmpp.message.MessageListener;
import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Presence;
import com.google.appengine.api.xmpp.PresenceBuilder;
import com.google.appengine.api.xmpp.PresenceType;
import com.google.appengine.api.xmpp.SendResponse;
import com.google.appengine.api.xmpp.XMPPService;

public class GoogleXMPPEndPointTestCase {

	/**
	 * Mocked service.
	 */
	private XMPPService mockXMPPService;

	private XMPPConnector<XMPPService> mockedConnector;

	private GoogleXMPPEndPoint endPoint;

	@Before
	public void setUp() throws IllegalArgumentException, IllegalAccessException {
		mockedConnector = EasyMock.createMock(XMPPConnector.class);
		mockXMPPService = EasyMock.createMock(XMPPService.class);
		EasyMock.expect(mockedConnector.getConnection()).andReturn(
				mockXMPPService);
		endPoint = new GoogleXMPPEndPoint(mockedConnector);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithNullMessage() {
		endPoint.sendMessage(null);
	}

	@Test
	public void testWithValidMessageButToOfflineRecipient() {
		Presence presence = new PresenceBuilder().withPresenceType(
				PresenceType.UNAVAILABLE).build();
		JID jid = new JID("test@test.com");
		Message message = new MessageBuilder().containing("content")
				.toRecipient("test@test.com").from("from@test.com").build();
		EasyMock.expect(mockXMPPService.getPresence(jidMatcher(jid)))
				.andReturn(presence);
		EasyMock.replay(mockXMPPService);
		EasyMock.replay(mockedConnector);

		Status status = endPoint.sendMessage(message);
		Assert.assertTrue(status.hasErrors());
		Assert.assertTrue(ErrorCode.USER_OFFLINE == status.errors().get(0)
				.code());

	}

	@Test
	public void testWithValidMessageAndForOnlineRecipient() {
		Presence presence = new PresenceBuilder().withPresenceType(
				PresenceType.AVAILABLE).build();
		JID jid = new JID("test@test.com");
		Message message = new MessageBuilder().containing("content")
				.toRecipient("test@test.com").from("from@test.com").build();
		EasyMock.expect(mockXMPPService.getPresence(jidMatcher(jid)))
				.andReturn(presence);
		EasyMock.expect(
				mockXMPPService.sendMessage(EasyMock
						.<com.google.appengine.api.xmpp.Message> anyObject()))
				.andReturn(new SendResponse());
		EasyMock.replay(mockXMPPService);
		EasyMock.replay(mockedConnector);
		Status status = endPoint.sendMessage(message);
		Assert.assertTrue(!status.hasErrors());
	}

	private JID jidMatcher(JID jid) {
		EasyMock.reportMatcher(new GoogleJIDMatcher(jid));
		return null;
	}

	/**
	 * Using custom JID matcher as google's JID equals method is not working as
	 * expected.
	 */
	private static class GoogleJIDMatcher implements IArgumentMatcher {
		private JID expected;

		public GoogleJIDMatcher(JID expected) {
			this.expected = expected;
		}

		public boolean matches(Object obj) {
			if (!(obj instanceof JID)) {
				return false;
			}
			JID actual = (JID) obj;
			return actual.getId().equals(expected.getId());
		}

		@Override
		public void appendTo(StringBuffer buffer) {
			buffer.append("eqException(");
			buffer.append(expected.getClass().getName());
			buffer.append(" with id:");
			buffer.append(expected.getId());

		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInviteWithInvalidJID() {
		endPoint.invite("");
	}

	@Test
	public void testInviteWithValidJID() {
		JID jid = new JID("test@test.com");
		mockXMPPService.sendInvitation(jidMatcher(jid));
		EasyMock.expectLastCall();
		EasyMock.replay(mockXMPPService);
		EasyMock.replay(mockedConnector);
		endPoint.invite("test@test.com");
	}

	@Test
	public void testRegisterAndRetrieveValidListener() {
		MessageListener listener = new MessageListener() {

			@Override
			public void listen(Message message) {

			}
		};
		endPoint.registerListner(new org.apache.nuvem.cloud.xmpp.JID(
				"test@domain.com"), listener);
		MessageListener registeredListener = endPoint
				.getListenerFor(new org.apache.nuvem.cloud.xmpp.JID(
						"test@domain.com"));
		Assert.assertNotNull(registeredListener);
	}

	@Test
	public void testListnerClearing() {
		MessageListener listener = new MessageListener() {

			@Override
			public void listen(Message message) {

			}
		};
		org.apache.nuvem.cloud.xmpp.JID jid = new org.apache.nuvem.cloud.xmpp.JID(
				"test@domain.com");
		endPoint.registerListner(jid, listener);
		MessageListener registeredListener = endPoint
				.getListenerFor(new org.apache.nuvem.cloud.xmpp.JID(
						"test@domain.com"));
		Assert.assertNotNull(registeredListener);
		Assert.assertTrue(endPoint.clearListenersFor(jid));
		Assert.assertEquals(MessageListener.LOGGING_LISTENER, endPoint
				.getListenerFor(jid));
	}
}
