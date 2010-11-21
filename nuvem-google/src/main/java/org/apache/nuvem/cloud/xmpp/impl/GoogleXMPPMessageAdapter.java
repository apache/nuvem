package org.apache.nuvem.cloud.xmpp.impl;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;

/**
 * Transforms messages of nuvem format to google's format.
 *
 */
public class GoogleXMPPMessageAdapter {

    public static Message toGoogleMessage(org.apache.nuvem.cloud.xmpp.api.Message nuvemMessage) {
        JID jid = new JID(nuvemMessage.recipient().asString());
        Message msg = new MessageBuilder().
            withRecipientJids(jid).
            withBody(nuvemMessage.payLoad().content())
            .build();
        return msg;
    }

    public static org.apache.nuvem.cloud.xmpp.api.Message toNuvemMessage(Message googleMessage) {
        String sender = googleMessage.getFromJid().getId();
        org.apache.nuvem.cloud.xmpp.api.Message nuvemMessage =
            new org.apache.nuvem.cloud.xmpp.api.MessageBuilder().
                                                from(sender).
                                                containing(googleMessage.getBody())
                                                .build();
        return nuvemMessage;
    }
}
