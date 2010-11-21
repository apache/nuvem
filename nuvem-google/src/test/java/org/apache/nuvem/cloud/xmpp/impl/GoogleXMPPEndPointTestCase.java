package org.apache.nuvem.cloud.xmpp.impl;

import java.lang.reflect.Field;

import junit.framework.Assert;

import org.apache.nuvem.cloud.xmpp.api.ErrorCode;
import org.apache.nuvem.cloud.xmpp.api.Message;
import org.apache.nuvem.cloud.xmpp.api.MessageBuilder;
import org.apache.nuvem.cloud.xmpp.api.MessageListener;
import org.apache.nuvem.cloud.xmpp.api.Status;
import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Presence;
import com.google.appengine.api.xmpp.SendResponse;
import com.google.appengine.api.xmpp.XMPPService;

public class GoogleXMPPEndPointTestCase {

    /**
     * Mocked service.
     */
    private XMPPService mockXMPPService;

    private GoogleXMPPEndPoint endPoint = new GoogleXMPPEndPoint();

    @Before
    public void setUp() throws IllegalArgumentException, IllegalAccessException {
        mockXMPPService = EasyMock.createMock(XMPPService.class);

        for (Field field : endPoint.getClass().getDeclaredFields()) {
            if (field.getName().equals("xmpp")) {
                field.setAccessible(true);
                field.set(endPoint, mockXMPPService);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullMessage() {
        endPoint.sendMessage(null);
    }

    @Test
    public void testWithValidMessageButToOfflineRecipient() {
        Presence presence = new Presence(false);
        JID jid = new JID("test@test.com");
        Message message =
            new MessageBuilder().containing("content").toRecipient("test@test.com").from("from@test.com").build();
        EasyMock.expect(mockXMPPService.getPresence(jidMatcher(jid))).andReturn(presence);
        EasyMock.replay(mockXMPPService);

        Status status = endPoint.sendMessage(message);
        Assert.assertTrue(status.hasErrors());
        Assert.assertTrue(ErrorCode.USER_OFFLINE == status.errors().get(0).code());

    }

    @Test
    public void testWithValidMessageAndForOnlineRecipient() {
        Presence presence = new Presence(true);
        JID jid = new JID("test@test.com");
        Message message =
            new MessageBuilder().containing("content").toRecipient("test@test.com").from("from@test.com").build();
        EasyMock.expect(mockXMPPService.getPresence(jidMatcher(jid))).andReturn(presence);
        EasyMock.expect(mockXMPPService.sendMessage(EasyMock.<com.google.appengine.api.xmpp.Message> anyObject()))
            .andReturn(new SendResponse());
        EasyMock.replay(mockXMPPService);
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
            JID actual = (JID)obj;
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
        endPoint.invite("test@test.com");
    }

    @Test
    public void testRegisterAndRetrieveValidListener() {
        MessageListener listener = new MessageListener() {

            @Override
            public void listen(Message message) {

            }
        };
        endPoint.registerListner(new org.apache.nuvem.cloud.xmpp.api.JID("test@domain.com"), listener);
        MessageListener registeredListener =
            GoogleXMPPEndPoint.getListenerFor(new org.apache.nuvem.cloud.xmpp.api.JID("test@domain.com"));
        Assert.assertNotNull(registeredListener);
    }

    @Test
    public void testListnerClearing() {
        MessageListener listener = new MessageListener() {

            @Override
            public void listen(Message message) {

            }
        };
        org.apache.nuvem.cloud.xmpp.api.JID jid = new org.apache.nuvem.cloud.xmpp.api.JID("test@domain.com");
        endPoint.registerListner(jid, listener);
        MessageListener registeredListener =
            GoogleXMPPEndPoint.getListenerFor(new org.apache.nuvem.cloud.xmpp.api.JID("test@domain.com"));
        Assert.assertNotNull(registeredListener);
        Assert.assertTrue(endPoint.clearListenersFor(jid));
        Assert.assertEquals(MessageListener.LOGGING_LISTENER, GoogleXMPPEndPoint.getListenerFor(jid));
    }
}
